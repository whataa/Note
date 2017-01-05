package io.whataa.fragmentapp.backstackdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whataa.fragmentapp.R;

import java.util.List;

import io.whataa.fragmentapp.BaseFragment;

/**
 * Created by Administrator on 2017/1/5.
 */

public class BaseStackFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_stack, container, false);
    }

    protected void initView(View view, final Class<? extends BaseStackFragment> from, final Class<? extends BaseStackFragment> to) {
        if (from == null) {
            Toast.makeText(getActivity(), "error! no from.", Toast.LENGTH_SHORT).show();
            return;
        }
        view.findViewById(R.id.fragment_stack_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (to == null) {
                    Toast.makeText(getActivity(), "can not next.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Fragment fromF = null, toF = null;
                    List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                    if (fragments != null) {
                        for (Fragment f : fragments) {
                            if (f != null) {
                                if (f.getTag().equals(from.getName())) {
                                    Log.e(TAG, "get fromF by using cache");
                                    fromF = f;
                                }
                                if (f.getTag().equals(to.getName())) {
                                    Log.e(TAG, "get toF by using cache");
                                    toF = f;
                                }
                            }
                        }
                    }
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (fromF != null) {
                        if (!fromF.isHidden()) {
                            Log.e(TAG, "fromF is showing, just hide it");
                            transaction.hide(fromF);
                        }
                    }
                    if (toF == null) {
                        Log.e(TAG, "no cache for toF, new one and add");
                        toF = to.newInstance();
                    }
                    if (!toF.isAdded()) {
                        Log.e(TAG, "toF is not added, so do it");
                        transaction.replace(R.id.act_stack_container, toF, to.getName());
                    }
                    if (toF.isHidden()) {
                        Log.e(TAG, "toF is not showing, so do it");
                        transaction.show(toF);
                    }
                    transaction
                            .addToBackStack(from.getSimpleName() + " to " + to.getSimpleName())
                            .commit();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        view.findViewById(R.id.fragment_stack_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack(
                        Stack01Fragment.class.getSimpleName() + " to " + Stack02Fragment.class.getSimpleName(), 0);
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(from.getSimpleName());
    }
}
