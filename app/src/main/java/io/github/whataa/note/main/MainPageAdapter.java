package io.github.whataa.note.main;


import android.app.Fragment;
import android.app.FragmentManager;

import io.github.whataa.note.article.MainArticleFragment;
import io.github.whataa.note.goal.MainGoalFragment;
import io.github.whataa.note.pager.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {

    private MainArticleFragment articleFragment;
    private MainGoalFragment goalFragment;

    public MainArticleFragment getArticleFragment() {
        return articleFragment;
    }

    public MainGoalFragment getGoalFragment() {
        return goalFragment;
    }

    public MainPageAdapter(FragmentManager fm, int pagerId) {
        super(fm);
        articleFragment = (MainArticleFragment) fm.findFragmentByTag(makeFragmentName(pagerId, 0));
        if (articleFragment == null) articleFragment = MainArticleFragment.newInstance();

        goalFragment = (MainGoalFragment) fm.findFragmentByTag(makeFragmentName(pagerId,1));
        if (goalFragment == null) goalFragment = MainGoalFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        return position==0?articleFragment:goalFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    public static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
