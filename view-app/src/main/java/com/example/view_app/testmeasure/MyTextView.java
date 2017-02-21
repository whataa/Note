package com.example.view_app.testmeasure;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MyTextView extends TextView {
    private static final String TAG = MyTextView.class.getSimpleName();
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        if (value == FrameLayout.LayoutParams.MATCH_PARENT) {
            valueStr = "MATCH_PARENT";
        } else if (value == FrameLayout.LayoutParams.WRAP_CONTENT) {
            valueStr = "WRAP_CONTENT";
        } else {
            valueStr = "" + value;
        }
        return modeStr + " == " + valueStr;
    }
}
