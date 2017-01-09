package io.whataa.fragmentapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whataa.fragmentapp.R;

import io.whataa.fragmentapp.common.BaseFragment;

/**
 * Created by Administrator on 2017/1/6.
 */

public class StaticLoadFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_static_load, container, false);
    }
}
