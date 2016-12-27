package io.whataa.fragmentapp.pagedemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.whataa.fragmentapp.R;

/**
 * Created by Summer on 2016/12/25.
 */

public class PageDemoActivity extends AppCompatActivity {
    PageDemoAdapter adapter;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagedemo);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.act_pagedemo_bn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_one:
                        // Switch to page one
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_two:
                        // Switch to page two
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_three:
                        // Switch to page three
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_four:
                        // Switch to page three
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        viewPager = (ViewPager) findViewById(R.id.act_pagedemo_vp);
        viewPager.setAdapter(adapter = new PageDemoAdapter(getSupportFragmentManager()));
    }

}
