package whataa.github.com.note.main;


import android.app.Fragment;
import android.app.FragmentManager;

import whataa.github.com.note.article.MainArticleFragment;
import whataa.github.com.note.goal.MainGoalFragment;
import whataa.github.com.note.widget.pager.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {

    private MainArticleFragment articleFragment;
    private MainGoalFragment goalFragment;
    private MainGoalFragment goalFragment1;
    private MainGoalFragment goalFragment2;

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

        goalFragment1 = (MainGoalFragment) fm.findFragmentByTag(makeFragmentName(pagerId,2));
        if (goalFragment1 == null) goalFragment1 = MainGoalFragment.newInstance();

        goalFragment2 = (MainGoalFragment) fm.findFragmentByTag(makeFragmentName(pagerId,3));
        if (goalFragment2 == null) goalFragment2 = MainGoalFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        return position==0?articleFragment:position==1?goalFragment:position==2?goalFragment1:goalFragment2;
    }

    @Override
    public int getCount() {
        return 4;
    }
    public static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
