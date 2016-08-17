package io.github.whataa.note.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import whataa.github.com.note.R;
import io.github.whataa.note.widget.pager.CycleViewPager;
import io.github.whataa.note.widget.pager.SimpleIndicator;

public class MainActivity extends Activity {

    @BindView(R.id.wrapper)
    public LinearLayout wrapper;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.simple_indicator)
    public SimpleIndicator indicator;
    @BindView(R.id.viewpager)
    public CycleViewPager viewPager;
    private MainPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(MainActivity.class.getName(),"onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);



        viewPager.setAdapter(adapter = new MainPageAdapter(getFragmentManager(), viewPager.getId()));
        viewPager.addOnPageChangeListener(new CycleViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected",""+position+" cur="+viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled","position="+position+" "+positionOffset+" "+positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("onStateChanged","state="+state);
            }
        });
        indicator.setUpWith(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(MainActivity.class.getName(),"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(MainActivity.class.getName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(MainActivity.class.getName(),"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(MainActivity.class.getName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(MainActivity.class.getName(),"onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(MainActivity.class.getName(),"onRestart");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e(MainActivity.class.getName(),"onAttachFragment");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(MainActivity.class.getName(),"onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(MainActivity.class.getName(),"onRestoreInstanceState");
    }
}
