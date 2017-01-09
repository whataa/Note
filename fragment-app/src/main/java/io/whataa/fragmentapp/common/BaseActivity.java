package io.whataa.fragmentapp.common;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2017/1/6.
 */

public class BaseActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    protected final void logMethord(boolean isStart) {
        Log.d(TAG, "【" + Thread.currentThread().getStackTrace()[3].getMethodName() + (isStart ? "】-------->>>>start>>>>" : "--------<<<<end<<<<"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logMethord(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        logMethord(true);
        super.setContentView(layoutResID);
        logMethord(false);
    }

    @Override
    protected void onStart() {
        logMethord(true);
        super.onStart();
    }

    @Override
    protected void onRestart() {
        logMethord(true);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        logMethord(true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        logMethord(true);
        super.onPause();
    }

    @Override
    protected void onStop() {
        logMethord(true);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        logMethord(true);
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        logMethord(true);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        logMethord(true);
        super.onPostResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        logMethord(true);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        logMethord(true);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
