package io.github.whataa.picer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.whataa.finepic.R;

public class PicerFragment extends Fragment {
    private static final String PARAM_SIZE = "param_size";
    public static String tagOfCache() {
        return PicerFragment.class.getSimpleName();
    }

    public static PicerFragment newInstance(FragmentManager fm, int size) {
        PicerFragment fragment = (PicerFragment) fm.findFragmentByTag(tagOfCache());
        if (fragment == null) {
            fragment = new PicerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(PARAM_SIZE,size);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picer, container, false);
//        getActivity().getContentResolver().query()
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
        }
//        getLoaderManager().initLoader(0,null,new LoaderPicCallback());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


//    private static class LoaderPicCallback implements LoaderManager.LoaderCallbacks<Cursor> {
//        private final String[] IMAGE_PROJECTION = {
//                MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.DATE_ADDED,
//                MediaStore.Images.Media.MIME_TYPE,
//                MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media._ID};
//
//        @Override
//        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//            CursorLoader cursorLoader = null;
//            if (id == LOADER_ALL) {
//                cursorLoader = new CursorLoader(getActivity(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
//                        IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
//                        new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
//            } else if (id == LOADER_CATEGORY) {
//                cursorLoader = new CursorLoader(getActivity(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
//                        IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
//                        null, IMAGE_PROJECTION[2] + " DESC");
//            }
//            return cursorLoader;
//        }
//
//        private boolean fileExist(String path) {
//            return !TextUtils.isEmpty(path) && new File(path).exists();
//        }
//
//        @Override
//        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//            if (data != null) {
//                if (data.getCount() > 0) {
//                    List<Image> images = new ArrayList<>();
//                    data.moveToFirst();
//                    do {
//                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
//                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
//                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
//                        if (!fileExist(path)) {
//                            continue;
//                        }
//                        Image image = null;
//                        if (!TextUtils.isEmpty(name)) {
//                            image = new Image(path, name, dateTime);
//                            images.add(image);
//                        }
//                        if (!hasFolderGened) {
//                            // get all folder data
//                            File folderFile = new File(path).getParentFile();
//                            if (folderFile != null && folderFile.exists()) {
//                                String fp = folderFile.getAbsolutePath();
//                                Folder f = getFolderByPath(fp);
//                                if (f == null) {
//                                    Folder folder = new Folder();
//                                    folder.name = folderFile.getName();
//                                    folder.path = fp;
//                                    folder.cover = image;
//                                    List<Image> imageList = new ArrayList<>();
//                                    imageList.add(image);
//                                    folder.images = imageList;
//                                    mResultFolder.add(folder);
//                                } else {
//                                    f.images.add(image);
//                                }
//                            }
//                        }
//
//                    } while (data.moveToNext());
//
//                    mImageAdapter.setData(images);
//                    if (resultList != null && resultList.size() > 0) {
//                        mImageAdapter.setDefaultSelected(resultList);
//                    }
//                    if (!hasFolderGened) {
//                        mFolderAdapter.setData(mResultFolder);
//                        hasFolderGened = true;
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onLoaderReset(Loader<Cursor> loader) {
//
//        }
//
//        private Folder getFolderByPath(String path) {
//            if (mResultFolder != null) {
//                for (Folder folder : mResultFolder) {
//                    if (TextUtils.equals(folder.path, path)) {
//                        return folder;
//                    }
//                }
//            }
//            return null;
//        }
//    }


}
