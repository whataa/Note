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

public class TouchGroup extends FrameLayout {
    public TouchGroup(@NonNull Context context) {
        this(context,null);
    }

    public TouchGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final String TAG = TouchGroup.class.getSimpleName();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.w(TAG, "dispatchTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.w(TAG, "onInterceptTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w(TAG, "onTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.onTouchEvent(event);
    }
}
