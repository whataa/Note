package com.whataa.fragmentapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

public class ContainerActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = ContainerActivity.class.getSimpleName();
    FragmentMaster master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        master = new FragmentMaster.Builder()
                .containerViewId(R.id.main_container)
                .fragmentManager(getSupportFragmentManager())
                .config(FragmentRadio01.class, 0, new BundleBuilder().putString("DATA", "page1").build())
                .config(FragmentRadio01.class, 1, new BundleBuilder().putString("DATA", "page2").build())
                .config(FragmentRadio01.class, 2)
                .build();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_button_group);
        radioGroup.setOnCheckedChangeListener(this);

        AdjustRadioBar bar = (AdjustRadioBar) findViewById(R.id.main_bar);
        bar.adjust(new String[]{"tab1", "tab2", "tab3", "tab4"});
    }

    @Override
    protected void onStart() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onPostResume();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_button_0:
                master.showOrload(0);
                break;
            case R.id.main_button_1:
                master.showOrload(1);
                break;
            case R.id.main_button_2:
                master.showOrload(2);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + hashCode());
        super.onRestoreInstanceState(savedInstanceState);
    }
}
