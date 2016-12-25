package io.whataa.fragmentapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whataa.fragmentapp.R;

/**
 * Created by Summer on 2016/11/13.
 */

public class FragmentRadio01 extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView" + " " + this.hashCode());
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated" + " " + this.hashCode());
        TextView tv = (TextView) view.findViewById(R.id.fragment_base_txt);
        String msg = "default";
        if (getArguments() != null && getArguments().getString("DATA") != null) {
            msg = getArguments().getString("DATA");
        }
        tv.setText(msg + " " + this.toString() + (savedInstanceState != null ? savedInstanceState : null));
    }

}
