package com.example.view_app.touch;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Summer on 2017/3/25.
 */

public class TouchGroupWrapper extends FrameLayout {
    public TouchGroupWrapper(@NonNull Context context) {
        this(context,null);
    }

    public TouchGroupWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchGroupWrapper(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final String TAG = TouchGroupWrapper.class.getSimpleName();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "dispatchTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d(TAG, "onInterceptTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.onTouchEvent(event);
    }
}
