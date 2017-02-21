package com.example.view_app.testmeasure;

import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/2/21.
 */

class Util {

    static String getSpecName(int measureSpec) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int value = View.MeasureSpec.getSize(measureSpec);
        String modeStr = null;
        String valueStr = null;
        if (mode == View.MeasureSpec.AT_MOST) {
            modeStr =  "[AT_MOST]";
        } else if (mode == View.MeasureSpec.EXACTLY) {
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
