package com.example.view_app.testmeasure;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import static com.example.view_app.testmeasure.Util.getSpecName;

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

}
