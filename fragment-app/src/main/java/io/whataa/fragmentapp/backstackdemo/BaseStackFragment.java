package io.whataa.fragmentapp.backstackdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whataa.fragmentapp.R;

import java.util.List;

import io.whataa.fragmentapp.common.BaseFragment;

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
    protected void initView(final View view, final Class<? extends BaseStackFragment> to) {
        initView(view, true, to);
    }

    protected void initView(final View view, final boolean addToBackStack, final Class<? extends BaseStackFragment> to) {
        view.findViewById(R.id.fragment_stack_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (to == null) {
                    Toast.makeText(getActivity(), "can not next.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment toF = null;
                    List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                    if (fragments != null) {
                        for (Fragment f : fragments) {
                            if (f != null) {
                                if (f.getTag().equals(to.getName())) {
                                    Log.e(TAG, "get toF by using cache");
                                    toF = f;
                                } else {
                                    if (f.getTag().equals(BaseStackFragment.this.getClass().getName())) {
                                        Log.e(TAG, "cur is showing ? " + !f.isHidden());
                                    }
                                    if (!f.isHidden()) {
                                        Log.e(TAG, f + " in mActive is showing, just hide it");
                                        transaction.hide(f);
                                    }
                                }
                            }
                        }
                    }
                    if (toF == null) {
                        Log.e(TAG, "no cache for toF, new one and add");
                        toF = to.newInstance();
                    }
                    if (!toF.isAdded()) {
                        Log.e(TAG, "toF is not added, do it");
                        transaction.add(R.id.act_stack_container, toF, to.getName());
                    }
                    if (toF.isHidden()) {
                        Log.e(TAG, "toF is not showing, do it");
                        transaction.show(toF);
                    }
                    if (addToBackStack) {
                        transaction.addToBackStack(makeBackStackName(BaseStackFragment.this.getClass(), to));
                    }
                    transaction.commit();
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
                        makeBackStackName(Stack01Fragment.class, Stack02Fragment.class), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(getClass().getSimpleName());
    }

    private static String makeBackStackName(Class from, Class to) {
        Log.e("makeBackStackName", "from: " + from.getSimpleName() + " && to: " + to.getSimpleName());
        return from.getSimpleName() + " to " + to.getSimpleName();
//        return "makeBackStackName test name";
    }
}
