package io.whataa.fragmentapp;

import android.content.Context;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach" + " " + this.hashCode());
    }


    public static final String TAG = FragmentRadio01.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + savedInstanceState + " " + this.hashCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart" + " " + this.hashCode());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume" + " " + this.hashCode());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState" + " " + this.hashCode());
    }

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated" + " " + this.hashCode());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause" + " " + this.hashCode());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop" + " " + this.hashCode());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView" + " " + this.hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy" + " " + this.hashCode());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach" + " " + this.hashCode());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged" + hidden + " " + this.hashCode());
    }
}
