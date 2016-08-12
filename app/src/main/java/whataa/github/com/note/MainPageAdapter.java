package whataa.github.com.note;


import android.app.Fragment;
import android.app.FragmentManager;

import whataa.github.com.note.widget.FragmentPagerAdapter;

public class MainPageAdapter extends FragmentPagerAdapter {
    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
