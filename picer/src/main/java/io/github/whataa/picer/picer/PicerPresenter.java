package io.github.whataa.picer.picer;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.whataa.picer.EventCallback;
import io.github.whataa.picer.Utils;

public class PicerPresenter implements PicerContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = PicerPresenter.class.getSimpleName();
    private static final String STATE_FOLDER = "state_folder";
    private static final String STATE_CHOSEN = "state_chosen";
    private PicerContract.View mView;
    private List<String> chosenPics = new ArrayList<>();
    private String currentFolder;// don't forget to initial it.

    private List<Picture> picDatas = new ArrayList<>();
    private List<Folder> folderDatas = new ArrayList<>();

    PicerPresenter(PicerContract.View view) {
        mView = view;
    }

    @Override
    public List<String> addOrRemoveChosen(String path) {
        if (!TextUtils.isEmpty(path) && !picDatas.isEmpty() && !folderDatas.isEmpty()) {
            if (chosenPics.contains(path)) {
                chosenPics.remove(path);
            } else {
                if (chosenPics.size() >= mView.getMaxSize()) {
                    return chosenPics;
                }
                chosenPics.add(path);
            }
            mView.updateCountView(chosenPics.size());
            for (int i = 0; i < picDatas.size(); i++) {
                if (chosenPics.contains(picDatas.get(i).getPath())) {
                    picDatas.get(i).setChosen(true);
                } else {
                    picDatas.get(i).setChosen(false);
                }
            }
            setCurrentFolder(currentFolder);
        }
        return chosenPics;
    }

    @Override
    public String setCurrentFolder(String folderPath) {
        Log.d(TAG, "setCurrentFolder:" + currentFolder);
        if (TextUtils.isEmpty(currentFolder) || TextUtils.isEmpty(folderPath))
            throw new IllegalArgumentException("currentFolder can not be null");

        currentFolder = folderPath;
        for (int i = folderDatas.size() - 1; i >= 0; i--) {
            if (TextUtils.equals(folderDatas.get(i).getPath(), folderPath)) {
                folderDatas.get(i).setCurrent(true);
                mView.updateFolderName(folderDatas.get(i).getName());
            } else {
                folderDatas.get(i).setCurrent(false);
            }
        }

        if (currentFolder.equals(Folder.PATH_OF_ALL)) {
            mView.updateAdapterView(picDatas, folderDatas);
            return currentFolder;
        }
        List<Picture> folderPics = new ArrayList<>();
        if (!picDatas.isEmpty()) {
            for (Picture pic : picDatas) {
                if (pic.getFolderPath().equals(currentFolder)) {
                    folderPics.add(pic);
                }
            }
        }

        mView.updateAdapterView(folderPics, folderDatas);
        return currentFolder;
    }

    @Override
    public void onCompleteBack(EventCallback callback) {
        if (chosenPics.isEmpty()) {
            mView.showHint("请选择图片");
            return;
        } else if (chosenPics.size() < mView.getMaxSize()) {
            mView.showHint("请选择" + mView.getMaxSize() + "张图片");
            return;
        }
        if (callback != null) {
            callback.onEvent(EventCallback.EVENT_COMPLETE, chosenPics);
        }
    }

    @Override
    public Bundle saveState(Bundle outState) {
        Log.d(TAG, "saveState:" + currentFolder);
        outState.putStringArrayList(STATE_CHOSEN, (ArrayList<String>) chosenPics);
        outState.putString(STATE_FOLDER, currentFolder);
        return outState;
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        Log.d(TAG, "restoreState:" + currentFolder);
        if (savedInstanceState != null) {
            if (currentFolder == null) {
                currentFolder = savedInstanceState.getString(STATE_FOLDER);
            }
            if (chosenPics.isEmpty()) {
                List<String> datas = savedInstanceState.getStringArrayList(STATE_CHOSEN);
                if (datas != null) {
                    chosenPics.addAll(datas);
                }
            }
        }
    }

    @Override
    public void load() {
        Log.d(TAG, "load:" + currentFolder);
        // if the last loader exsits and has data, initLoader will reused it by immidiately calling onLoadFinished.
        // TODO 连续调用该方法，测试在loader存在但还未得到数据的情况
        mView.getLoaderMgr().initLoader(-1, null, this);
    }

    @Override
    public void refresh() {
        currentFolder = null;
        // TODO 连续调用
        mView.getLoaderMgr().restartLoader(-1, null, this);
    }


    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mView.getAct(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished:" + currentFolder);
        List<Picture> pics = new ArrayList<>();
        List<Folder> folders = new ArrayList<>();
        // set the default folder
        if (currentFolder == null) currentFolder = Folder.PATH_OF_ALL;

        if (data != null) {
            if (data.getCount() > 0) {
                data.moveToFirst();
                do {
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    String type = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                    String size = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                    if (!Utils.fileExist(path)) {
                        continue;
                    }
                    Picture pic = new Picture();
                    pic.setPath(path);
                    pic.setName(TextUtils.isEmpty(name) ? "" : name);
                    pic.setAddTime(dateTime);
                    pic.setType(type);
                    pic.setSize(size);
                    pic.setChosen(isChosen(path));

                    File folderFile = new File(path).getParentFile();
                    if (folderFile != null && folderFile.exists()) {
                        String fp = folderFile.getAbsolutePath();
                        pic.setFolderPath(fp);
                        int index = getFolderIndexByPath(folders, fp);
                        if (index == -1) {
                            Folder folder = new Folder();
                            folder.setName(folderFile.getName());
                            folder.setPath(fp);
                            folder.setCurrent(fp.equals(currentFolder));
                            folders.add(folder);
                        }
                    }
                    pics.add(pic);
                } while (data.moveToNext());

            }
        }
        // add a folder called "all"
        Folder folder = new Folder();
        folder.setName(Folder.NAME_OF_ALL);
        folder.setPath(Folder.PATH_OF_ALL);
        folder.setCurrent(folders.isEmpty() || (Folder.PATH_OF_ALL.equals(currentFolder)));
        folders.add(0, folder);

        picDatas.clear();
        picDatas.addAll(pics);
        folderDatas.clear();
        folderDatas.addAll(folders);
        setCurrentFolder(currentFolder);
        mView.updateCountView(chosenPics.size());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private int getFolderIndexByPath(List<Folder> folders, String path) {
        if (folders != null && !folders.isEmpty()) {
            for (int i = folders.size() - 1; i >= 0; i--) {
                if (TextUtils.equals(folders.get(i).getPath(), path)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isChosen(String path) {
        return chosenPics != null && !chosenPics.isEmpty() && chosenPics.contains(path);
    }
}
