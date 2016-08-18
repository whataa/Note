package io.github.whataa.note.article;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.whataa.picer.PicerActivity;
import whataa.github.com.note.R;

public class MainArticleFragment extends Fragment {

    @BindView(R.id.article_btn1)
    public Button button;
    private Unbinder unbinder;

    public static MainArticleFragment newInstance() {
        MainArticleFragment fragment = new MainArticleFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState!=null) {
            Log.i(MainArticleFragment.class.getName(),"onCreateView with savedInstanceState");
            for (String key :savedInstanceState.keySet()) {
                Log.i(MainArticleFragment.class.getName(),"key="+key+" value="+savedInstanceState.get(key));
            }
        } else {
            Log.e(MainArticleFragment.class.getName(),"onCreateView");
        }
        View v = inflater.inflate(R.layout.fragment_article,container,false);
        unbinder = ButterKnife.bind(this, v);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PicerActivity.class));
            }
        });
        return v;
    }
    @Override public void onDestroyView() {
        super.onDestroyView();
        Log.e(MainArticleFragment.class.getName(),"onDestroyView");
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(MainArticleFragment.class.getName(),"onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(MainArticleFragment.class.getName(),"onActivityCreated");
        if (savedInstanceState!=null) {
            Log.i(MainArticleFragment.class.getName(),"onActivityCreated with savedInstanceState");
            for (String key :savedInstanceState.keySet()) {
                Log.i(MainArticleFragment.class.getName(),"key="+key+" value="+savedInstanceState.get(key));
            }
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e(MainArticleFragment.class.getName(),"onViewStateRestored");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(MainArticleFragment.class.getName(),"onCreate");
        if (savedInstanceState!=null) {
            Log.i(MainArticleFragment.class.getName(),"onCreate with savedInstanceState");
            for (String key :savedInstanceState.keySet()) {
                Log.i(MainArticleFragment.class.getName(),"key="+key+" value="+savedInstanceState.get(key));
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e(MainArticleFragment.class.getName(),"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(MainArticleFragment.class.getName(),"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(MainArticleFragment.class.getName(),"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(MainArticleFragment.class.getName(),"onStop");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(MainArticleFragment.class.getName(),"onDestroy");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(MainArticleFragment.class.getName(),"onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(MainArticleFragment.class.getName(),"onDetach");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("passData",passData);
        Log.e(MainArticleFragment.class.getName(),"onSaveInstanceState");
    }
}
