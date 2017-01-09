package io.whataa.fragmentapp.backstackdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.whataa.fragmentapp.R;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public class StackActivity extends AppCompatActivity {

    private static final String TAG = StackActivity.class.getName();
    {
//        FragmentManager.enableDebugLogging(true);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackCount = getSupportFragmentManager().getBackStackEntryCount();
                Toast.makeText(StackActivity.this, "onBackStackChanged:" + stackCount, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < stackCount; i++) {
                    FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
                    Log.e(TAG, entry.toString());
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (savedInstanceState != null) return;
        Fragment fragment = null;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f != null) {
                    if (f.getTag().equals(Stack01Fragment.class.getName())) {
                        Log.e(TAG, "get Stack01Fragment by using cache");
                        fragment = f;
                    }
                }
            }
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            if (!fragment.isAdded()) {
                transaction.add(R.id.act_stack_container, fragment, Stack01Fragment.class.getName());
            }
        } else {
            transaction.add(R.id.act_stack_container, new Stack01Fragment(), Stack01Fragment.class.getName());
        }

        transaction.addToBackStack(StackActivity.class.getSimpleName() + " to " + Stack01Fragment.class.getSimpleName())
                .commit();
    }
}
