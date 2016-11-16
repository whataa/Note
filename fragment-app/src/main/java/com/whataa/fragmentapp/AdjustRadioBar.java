package com.whataa.fragmentapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AdjustRadioBar extends RadioGroup implements View.OnClickListener {
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
        for (int i = 0; i < names.length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            radioButton.setButtonDrawable(null);
            radioButton.setText(names[i]);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextSize(16);
            radioButton.setTextColor(getContext().getResources().getColorStateList(R.color.state_check_txt));
            radioButton.setBackgroundResource(R.drawable.selector_bg_radio);
            radioButton.setTag(R.id.key_position, i);
            radioButton.setTag(R.id.key_string, names[i]);
            radioButton.setOnClickListener(this);
            addView(radioButton, params);
        }
        checkRadio(0);
    }

    public void checkRadio(int position) {
        if (position > getChildCount() - 1 || position < 0) return;
        ((RadioButton)getChildAt(position)).setChecked(true);
        onClick(getChildAt(position));
    }


    private OnRadioCheckedListener listener;
    public void setOnRadioCheckedListener(OnRadioCheckedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null && view instanceof RadioButton) {
            listener.onRadioCheck((Integer) view.getTag(R.id.key_position), (String) view.getTag(R.id.key_string));
        }
    }

    public interface OnRadioCheckedListener {
        void onRadioCheck(int position, String name);
    }
}
