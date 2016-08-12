package whataa.github.com.note;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.wrapper)
    public LinearLayout wrapper;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.tablayout)
    public TabLayout tabLayout;
    @BindView(R.id.viewpager)
    public ViewPager viewPager;
    private MainPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setActionBar(toolbar);
        viewPager.setAdapter(adapter = new MainPageAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

}
