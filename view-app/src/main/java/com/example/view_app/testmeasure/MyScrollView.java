package com.example.view_app.testmeasure;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

import static com.example.view_app.testmeasure.Util.getSpecName;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MyScrollView extends ScrollView {
    private static final String TAG = MyScrollView.class.getSimpleName();

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasureBefore: widthMeasureSpec{"+getSpecName(widthMeasureSpec)+"}  heightMeasureSpec{"+getSpecName(heightMeasureSpec)+"}");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasureAfter: measurWidth{"+getMeasuredWidth()+"}  measureHeight{"+getMeasuredHeight()+"}");
    }
}
