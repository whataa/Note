package io.github.whataa.picer.picer;


import android.app.Activity;
import android.app.LoaderManager;
import android.os.Bundle;

import java.util.List;

public interface PicerContract {

    interface View {
        void showHint(String msg);

        int getMaxSize();

        LoaderManager getLoaderMgr();

        Activity getAct();

        void updateAdapterView(List<Picture> pics, List<Folder> folders);

        void updateCountView(int chosenNum);

        void updateFolderName(String name);
    }

    interface Presenter {

        /**
         * you can pass an invalid value to get the current list of picture's path quikly.
         * @param path
         * @return  the current list of picture's path that has been chosen.
         */
        List<String> addOrRemoveChosen(String path);

        /**
         * you can pass an invalid value to get the current folder's path.
         * @param folderPath
         * @return the current folder's path.
         */
        String setCurrentFolder(String folderPath);

        /**
         * load data.
         */
        void load();

        /**
         * if Fragment#setRetainInstance(true) sets, no need to call this method.
         * @param savedInstanceState
         * @return
         */
        Bundle saveState(Bundle savedInstanceState);

        /**
         * if Fragment#setRetainInstance(true) sets, no need to call this method.
         * @param outState
         */
        void restoreState(Bundle outState);

        /**
         * if system's data changes, call it to reload.
         */
        void refresh();
    }
}
