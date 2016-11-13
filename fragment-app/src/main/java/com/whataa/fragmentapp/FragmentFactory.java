package com.whataa.fragmentapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Summer on 2016/11/13.
 */

public class FragmentFactory {

    int containerViewId;

    public FragmentFactory(int containerViewId) {
        this.containerViewId = containerViewId;
    }

    public Builder createBuilder(FragmentManager fm) {
        return new Builder(fm, containerViewId);
    }

    public static class Builder {
        FragmentManager fm;
        int containerViewId;
        HashMap<Class, BaseFragment> cache = new HashMap<>();

        public Builder(FragmentManager fm, int containerViewId) {
            this.fm = fm;
            this.containerViewId = containerViewId;
        }

        public Builder install(Class<? extends BaseFragment>[] classes) {
            cache.clear();
            for (int i = 0; i < classes.length; i++) {
                String tag = tagOfCache(containerViewId, classes[i]);
                BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(tag);
                if (fragment == null) {
                    try {
                        fragment = classes[i].newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                cache.put(classes[i], fragment);
            }
            return this;
        }

        public Builder setArguments(Class<? extends BaseFragment> fClass, Bundle bundle) {
            if (cache.isEmpty()) throw new RuntimeException("should call install() firstly.");
            BaseFragment fragment = cache.get(fClass);
            if (fragment != null) {
                fragment.setArguments(bundle);
            }
            return this;
        }

        public void build() {
            FragmentTransaction transaction = fm.beginTransaction();
            for (Class key : cache.keySet()) {
                BaseFragment fragment = cache.get(key);
                if (!fragment.isAdded()) {
                    transaction.add(containerViewId, fragment, tagOfCache(containerViewId, key));
                }
            }
            if (!transaction.isEmpty()) {
                transaction.commit();
            }
            cache.clear();
        }



    }

    public static String tagOfCache(int containerId, Class fClass) {
        return fClass + " fragment is cached in" + containerId;

    }

    public void show(FragmentManager fm, Class<? extends BaseFragment> fClass) {
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment cur = fm.findFragmentByTag(tagOfCache(containerViewId, fClass));
        if (cur == null) {

        }
        List<Fragment> fragments = fm.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment == cur) {
                if (fragment.isHidden()) {
                    transaction.show(fragment);
                }
            } else {
                if (!fragment.isHidden()) {
                    transaction.hide(fragment);
                }
            }
        }
        transaction.commit();
    }

}
