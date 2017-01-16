package io.whataa.fragmentapp.backstackdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Administrator on 2016/12/28.
 */

public class Stack03Fragment extends BaseStackFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, Stack04Fragment.class);
    }
}
