package io.whataa.fragmentapp.pagedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whataa.fragmentapp.R;

import java.text.MessageFormat;

import io.whataa.fragmentapp.common.BaseFragment;

/**
 * Created by Summer on 2016/12/25.
 */

public class PageDemoFragment extends BaseFragment {

    private int curTab;

    public static PageDemoFragment newInstance(int curTab) {
        PageDemoFragment fragment = new PageDemoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("DATA", curTab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curTab = getArguments().getInt("DATA");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_pagedemo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.pagedemo_fragment_txt);
        textView.setText(MessageFormat.format("this is page {0}", curTab));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curTab == 2) {
                    canTwoOwnMenu = !canTwoOwnMenu;
                    setMenuVisibility(canTwoOwnMenu);
                }
            }
        });
    }

    private boolean canTwoOwnMenu;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (curTab != 2) {
            setMenuVisibility(isVisibleToUser);
        } else {
            setMenuVisibility(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(getTAG(), "onCreateOptionsMenu: " + menu);
        menu.clear();
        switch (curTab) {
            case 0:
                inflater.inflate(R.menu.act_pager_0, menu);
                break;
            case 1:
                inflater.inflate(R.menu.act_pager_1, menu);
                break;
            case 2:
                if (canTwoOwnMenu) {
                    inflater.inflate(R.menu.act_pager_2, menu);
                }
                break;
            case 3:
                inflater.inflate(R.menu.act_pager_3, menu);
                break;
        }

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(getTAG(), "onPrepareOptionsMenu: " + menu);
    }
}
