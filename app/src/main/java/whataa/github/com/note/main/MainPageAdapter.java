package whataa.github.com.note.main;


import android.app.Fragment;
import android.app.FragmentManager;

import whataa.github.com.note.article.MainArticleFragment;
import whataa.github.com.note.widget.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public MainPageAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        MainArticleFragment articleFragment = (MainArticleFragment) fm.findFragmentByTag("");
        if (articleFragment == null) {
            articleFragment = new MainArticleFragment();
        }
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
