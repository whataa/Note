package com.example.view_app.touch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Summer on 2017/3/25.
 */

public class TouchView extends View {
    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setClickable(true);
    }

    private static final String TAG = TouchView.class.getSimpleName();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(TAG, "dispatchTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + MotionEvent.actionToString(event.getActionMasked()));
//        return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        } else {
            return false;
        }
//        return super.onTouchEvent(event);
    }
}
