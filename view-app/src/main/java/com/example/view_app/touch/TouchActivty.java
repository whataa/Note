package com.example.view_app.touch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import com.example.view_app.R;

/**
 * Created by Summer on 2017/3/25.
 */

public class TouchActivty extends Activity {

    private static final String TAG = TouchActivty.class.getSimpleName();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
    }
}
