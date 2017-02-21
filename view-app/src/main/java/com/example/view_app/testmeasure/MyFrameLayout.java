package com.example.view_app.testmeasure;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MyFrameLayout extends FrameLayout {
    private static final String TAG = MyFrameLayout.class.getSimpleName();

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasureBefore: widthMeasureSpec{"+getSpecName(widthMeasureSpec)+"}  heightMeasureSpec{"+getSpecName(heightMeasureSpec)+"}");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasureAfter: measurWidth{"+getMeasuredWidth()+"}  measureHeight{"+getMeasuredHeight()+"}");
    }

    String getSpecName(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int value = MeasureSpec.getSize(measureSpec);
        String modeStr = null;
        String valueStr = null;
        if (mode == MeasureSpec.AT_MOST) {
            modeStr =  "[AT_MOST]";
        } else if (mode == MeasureSpec.EXACTLY) {
            modeStr =  "[EXACTLY]";
        } else {
            modeStr =  "[UNSPECIFIED]";
        }
        if (value == LayoutParams.MATCH_PARENT) {
            valueStr = "MATCH_PARENT";
        } else if (value == LayoutParams.WRAP_CONTENT) {
            valueStr = "WRAP_CONTENT";
        } else {
            valueStr = "" + value;
        }
        return modeStr + " == " + valueStr;
    }
}
