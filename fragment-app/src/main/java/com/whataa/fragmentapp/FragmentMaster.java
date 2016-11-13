package com.whataa.fragmentapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Summer on 2016/11/13.
 */

public class FragmentMaster {

    private int containerViewId;
    private FragmentManager manager;
    private HashMap<String, Bundle> installClasses = new HashMap<>();

    private FragmentMaster() {

    }

    public void showOrload(Class<? extends Fragment> fClass) {
        showOrload(fClass, 0);
    }

    public void showOrload(Class<? extends Fragment> fClass, int position) {
        String tag = tagOfCache(fClass, position);
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            try {
                fragment = fClass.newInstance();
                Bundle bundle = installClasses.get(tag);
                if (bundle == null) {
                    throw new RuntimeException(fClass + " should pre-install in Builder#build.");
                }
                fragment.setArguments(bundle);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        FragmentTransaction transaction = manager.beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(containerViewId, fragment, tag);
        }
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment item : fragments) {
                if (item == fragment && item.isHidden()) {
                    if (item.isHidden()) {
                        transaction.show(item);
                    }
                }
                if (item != fragment && !item.isHidden()) {
                    transaction.hide(item);
                }
            }
        }
        if (!transaction.isEmpty()) {
            transaction.commit();
//            manager.executePendingTransactions();
        }
    }

    public FragmentMaster loadAll() {
        if (!installClasses.isEmpty()) {
            Iterator<Map.Entry<String, Bundle>> iterator = installClasses.entrySet().iterator();
            FragmentTransaction transaction = manager.beginTransaction();
            while (iterator.hasNext()) {
                Map.Entry<String, Bundle> entry = iterator.next();
                String tag = entry.getKey();
                Fragment fragment = manager.findFragmentByTag(tag);
                if (fragment == null) {
                    try {
                        fragment = (Fragment) Class.forName(parseClassFromtag(tag)).newInstance();
                        fragment.setArguments(entry.getValue());
                        transaction.add(containerViewId, fragment, tag);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                if (!fragment.isHidden()) {
                    transaction.hide(fragment);
                }
            }
            if (!transaction.isEmpty()) {
                transaction.commit();
//                manager.executePendingTransactions();
            }
        }
        return this;
    }

    static class Builder {
        FragmentMaster master;

        public Builder() {
            master = new FragmentMaster();
        }

        public Builder containerViewId(int containerViewId) {
            master.containerViewId = containerViewId;
            return this;
        }

        public Builder fragmentManager(FragmentManager fm) {
            master.manager = fm;
            return this;
        }

        public Builder install(Class<? extends Fragment> fClass, Bundle bundle, int position) {
            master.installClasses.put(master.tagOfCache(fClass, position), bundle);
            return this;
        }

        public Builder install(Class<? extends Fragment> fClass, Bundle bundle) {
            install(fClass, bundle, 0);
            return this;
        }

        public Builder install(Class<? extends Fragment> fClass) {
            install(fClass, new Bundle());
            return this;
        }

        public FragmentMaster build() {
            return master;
        }
    }

    private String tagOfCache(Class<? extends Fragment> fClass, int position) {
        return "[" + fClass.getName() + "-" + position + "]" + " is cached in " + containerViewId;
    }

    private String parseClassFromtag(String tag) {
        String str1 = tag.substring(tag.indexOf("[") + 1, tag.indexOf("]"));
        String result = str1.split("-")[0];
        return result;
    }

}
