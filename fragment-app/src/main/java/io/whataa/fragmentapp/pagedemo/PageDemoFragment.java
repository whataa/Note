package io.whataa.fragmentapp.pagedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
    }


}
