package io.whataa.fragmentapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.whataa.fragmentapp.R;

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
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            radioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            radioButton.setText(name);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextSize(16);
            radioButton.setTextColor(getContext().getResources().getColorStateList(R.color.state_check_txt));
            radioButton.setBackgroundResource(R.drawable.selector_bg_radio);
            addView(radioButton, params);
        }
    }
}
