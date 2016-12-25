package io.whataa.fragmentapp.pagedemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Summer on 2016/12/25.
 */

public class PageDemoAdapter extends FragmentPagerAdapter {
    public PageDemoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PageDemoFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
