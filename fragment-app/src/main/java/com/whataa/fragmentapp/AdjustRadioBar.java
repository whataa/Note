package com.whataa.fragmentapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Summer on 2016/11/15.
 */

public class AdjustRadioBar extends RadioGroup {
    public AdjustRadioBar(Context context) {
        this(context, null);
    }

    public AdjustRadioBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        removeAllViews();
        setOrientation(HORIZONTAL);
        setPadding(0,0,0,0);
    }

    public void adjust(String[] names) {
        removeAllViews();
        for (String name : names) {
            RadioButton radioButton = new RadioButton(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            radioButton.setButtonDrawable(null);
            radioButton.setText(name);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextSize(16);
            radioButton.setTextColor(getContext().getResources().getColor(R.color.state_check_txt));
            radioButton.setBackgroundResource(R.drawable.selector_bg_radio);
            addView(radioButton, params);
        }
    }
}
