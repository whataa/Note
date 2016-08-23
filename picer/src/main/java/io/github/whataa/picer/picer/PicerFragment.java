package io.github.whataa.picer.picer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.whataa.finepic.R;
import io.github.whataa.picer.executor.PicLoader;
import io.github.whataa.picer.widget.DragPinnerLayout;
import io.github.whataa.picer.widget.ObservableGridView;

public class PicerFragment extends Fragment
        implements PicerContract.View,
        AdapterView.OnItemClickListener,
        View.OnClickListener, ObservableGridView.ObservableScrollViewCallbacks {
    private static final String PARAM_SIZE = "param_size";
    private static final String TAG_LISTVIEW = "tag_listview";

    public static String tagOfCache() {
        return PicerFragment.class.getSimpleName();
    }

    public static PicerFragment newInstance(FragmentManager fm, int size) {
        PicerFragment fragment = (PicerFragment) fm.findFragmentByTag(tagOfCache());
        if (fragment == null) {
            fragment = new PicerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(PARAM_SIZE, size);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private int maxSize;
    private PicerPresenter mPresenter;

    private DragPinnerLayout dragPinnerLayout;
    private ObservableGridView gridView;
    private ImageView ivPreview;
    private TextView tvChosenNum, tvCurrentFolder;
    private PopupWindow popupWindow;
    private PictureAdapter mPicAdapter;
    private FolderAdapter mFolderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        maxSize = getArguments().getInt(PARAM_SIZE, 0);
        mPresenter = new PicerPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picer, container, false);
        dragPinnerLayout = (DragPinnerLayout) v.findViewById(R.id.picer_draglayout);
        ivPreview = (ImageView) v.findViewById(R.id.picer_preview);
        tvChosenNum = (TextView) v.findViewById(R.id.picer_chosen_num);
        tvChosenNum.setOnClickListener(this);
        tvCurrentFolder = (TextView) v.findViewById(R.id.picer_folder);
        tvCurrentFolder.setOnClickListener(this);
        gridView = (ObservableGridView) v.findViewById(R.id.picer_gridview);
        gridView.setAdapter(mPicAdapter = new PictureAdapter(maxSize));
        gridView.setOnItemClickListener(this);
        gridView.setScrollViewCallbacks(this);

        View popLayout = inflater.inflate(R.layout.layout_popupwindow, null);
        popLayout.setOnClickListener(this);
        ListView popListView = (ListView) popLayout.findViewById(R.id.popupwindow_listview);
        popListView.setTag(TAG_LISTVIEW);
        popListView.setAdapter(mFolderAdapter = new FolderAdapter());
        popListView.setOnItemClickListener(this);
        popupWindow = new PopupWindow(popLayout, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.restoreState(savedInstanceState);
        mPresenter.load();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(mPresenter.saveState(outState));
    }

    @Override
    public void showHint(String msg) {
        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public LoaderManager getLoaderMgr() {
        return getLoaderManager();
    }

    @Override
    public Activity getAct() {
        return getActivity();
    }

    @Override
    public void updateAdapterView(List<Picture> pics, List<Folder> folders) {
        mPicAdapter.notifyDataSetChanged(pics);
        mFolderAdapter.notifyDataSetChanged(folders);
    }

    @Override
    public void updateCountView(int chosenNum) {
        tvChosenNum.setText(chosenNum + " / " + maxSize);
    }

    @Override
    public void updateFolderName(String name) {
        tvCurrentFolder.setText(name);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (TAG_LISTVIEW.equals(((String) adapterView.getTag()))) {
            mPresenter.setCurrentFolder(mFolderAdapter.getCurrentFolderPath(i));
            popupWindow.dismiss();
        } else {
            String path = mPicAdapter.getItemPath(i);
            mPresenter.addOrRemoveChosen(path);
            PicLoader.instance().loadPreview(path, ivPreview);
            dragPinnerLayout.show();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.picer_chosen_num) {

        } else if (i == R.id.picer_folder) {
            int[] location = new int[2];
            tvCurrentFolder.getLocationOnScreen(location);
            popupWindow.showAtLocation(tvCurrentFolder, Gravity.NO_GRAVITY,
                    (location[0] + tvCurrentFolder.getWidth() / 2) - popupWindow.getWidth() / 2,
                    location[1] - popupWindow.getHeight());
        } else if (i == R.id.popupwindow_wrapper) {
            popupWindow.dismiss();
        }

    }


    //---------------------------the below part handles gesture between gridview & Pinner-----------
    private boolean downToDrag = false;
    private boolean canSlide = false;
    // 28f~32f is suitable.
    private static float SEN_SLIDE = 30f;

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (downToDrag) {
            float diff = Math.abs(gridView.getCurrentY() - dragPinnerLayout.getPinnerBottomY());
            if (diff <= SEN_SLIDE) {
                downToDrag = false;
                canSlide = true;
            }
        }
        if (canSlide) {
            float shouldToY = gridView.getCurrentY();
            dragPinnerLayout.slideTo(shouldToY);
        }
    }

    @Override
    public void onDownMotionEvent() {
        downToDrag = true;
    }

    @Override
    public void onUpOrCancelMotionEvent(ObservableGridView.ScrollState scrollState) {
        downToDrag = false;
        canSlide = false;
        dragPinnerLayout.determineToState();
    }
}
