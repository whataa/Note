package io.whataa.fragmentapp.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whataa.fragmentapp.R;


public class BaseFragment extends Fragment {


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach" + " " + this.hashCode());
    }


    public final String TAG = getClass().getSimpleName();
    protected final String getTAG() {
        return TAG;
    }
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
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated" + " " + this.hashCode());
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
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: " + isVisibleToUser + " " + this.hashCode());
    }
}
