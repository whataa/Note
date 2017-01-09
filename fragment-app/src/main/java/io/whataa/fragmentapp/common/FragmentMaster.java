package io.whataa.fragmentapp.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by linjiang on 2016/11/13.
 */
public class FragmentMaster {

    public static final String KEY = "fragment_master_key";
    public static final String VALUE = "fragment_master_value_is_";

    private int containerViewId;
    private FragmentManager manager;
    private HashMap<Integer, InstalledInfo> installClasses = new HashMap<>();

    private FragmentMaster() {

    }

    public void showOrLoad(int label) {
        InstalledInfo info = installClasses.get(label);
        if (info == null) {
            throw new RuntimeException("didn't find fragment in " + label + ", was it deployed in Builder?");
        }
        String tag = tagOfCache(info);
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            try {
                fragment = info.fClass.newInstance();
                Bundle bundle = info.bundle;
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
                if (item == null) continue;
                Bundle arguments = item.getArguments();
                if (arguments != null && (VALUE + containerViewId).equals(arguments.get(KEY))) {
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
        }
        if (!transaction.isEmpty()) {
            transaction.commit();
        }
    }

    public FragmentMaster loadAllFragments() {
        if (!installClasses.isEmpty()) {
            Iterator<Map.Entry<Integer, InstalledInfo>> iterator = installClasses.entrySet().iterator();
            FragmentTransaction transaction = manager.beginTransaction();
            while (iterator.hasNext()) {
                Map.Entry<Integer, InstalledInfo> entry = iterator.next();
                String tag = tagOfCache(entry.getValue());
                Fragment fragment = manager.findFragmentByTag(tag);
                if (fragment == null) {
                    try {
                        fragment = entry.getValue().fClass.newInstance();
                        if (entry.getValue().label == 0) {
                            fragment.setUserVisibleHint(true);
                        } else {
                            fragment.setUserVisibleHint(false);
                        }
                        fragment.setArguments(entry.getValue().bundle);
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
            }
        }
        return this;
    }

    public void clearAllFragments() {
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            FragmentTransaction transaction = manager.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment == null) continue;
                Bundle arguments = fragment.getArguments();
                if (arguments != null && (VALUE + containerViewId).equals(arguments.get(KEY))) {
                    transaction.remove(fragment);
                }
            }
            if (!transaction.isEmpty()) {
                transaction.commit();
            }
        }
    }

    public static class Builder {
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

        public Builder config(Class<? extends Fragment> fClass, int label, Bundle bundle) {
            if (master.containerViewId == 0 || master.manager == null) {
                throw new RuntimeException("method containerViewId() and fragmentManager() must be called.");
            }
            if (bundle == null) {
                bundle = new Bundle();
            }
            // make sure that we can separate whether the fragment contained in containerViewId.
            bundle.putString(KEY, VALUE + master.containerViewId);
            master.installClasses.put(label, new InstalledInfo(fClass, label, bundle));
            return this;
        }

        public Builder config(Class<? extends Fragment> fClass, int label) {
            config(fClass, label, null);
            return this;
        }

        public FragmentMaster build() {
            return master;
        }
    }


    private String tagOfCache(InstalledInfo info) {
        return "[" + info.fClass.getName() + "-" + info.label + "]" + " is cached in " + containerViewId;
    }

    static class InstalledInfo {
        Class<? extends Fragment> fClass;
        int label;
        Bundle bundle;

        public InstalledInfo(Class<? extends Fragment> fClass, int label, Bundle bundle) {
            this.fClass = fClass;
            this.label = label;
            this.bundle = bundle;
        }
    }
}
