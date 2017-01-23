package io.whataa.fragmentapp.menudemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.whataa.fragmentapp.R;

import io.whataa.fragmentapp.common.BaseFragment;

/**
 * Created by Administrator on 2017/1/22.
 */

public class MenuFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_menudemo,container,false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(getTAG(), "onCreateOptionsMenu: " + menu);
        menu.clear();
        inflater.inflate(R.menu.act_menudemo_option2, menu);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(getTAG(), "onPrepareOptionsMenu: " + menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(), "onOptionsItemSelected option click:" + item, Toast.LENGTH_SHORT).show();
        if (item.getItemId() == R.id.action_two) {
            setHasOptionsMenu(false);
        } else if (item.getItemId() == R.id.action_one) {
            setMenuVisibility(false);
        }
        return true;
    }

}
