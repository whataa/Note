package io.github.whataa.picer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.github.whataa.finepic.R;
import io.github.whataa.picer.executor.PicLoader;
import io.github.whataa.picer.picer.PicerFragment;

public class PicerActivity extends AppCompatActivity {

    private static final String PARAM_SIZE = "param_size";
    private int size;

    public static void start(Context context, int maxSize) {
        Intent intent = new Intent(context,PicerActivity.class);
        intent.putExtra(PARAM_SIZE, maxSize < 1 ? 1 : maxSize);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PicLoader.initial(this);
        setContentView(R.layout.activity_picer);
        if (savedInstanceState != null) {
        }
        size = getIntent().getIntExtra(PARAM_SIZE, 0);
        if (size < 1) throw new UnsupportedOperationException("must start PicerActivity by invoking PicerActivity#start");
        Toolbar toolbar = (Toolbar) findViewById(R.id.picer_activity_toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        PicerFragment picerFragment = PicerFragment.newInstance(getFragmentManager(), size);
        if (!picerFragment.isAdded()) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.picer_activity_container,
                            picerFragment,
                            PicerFragment.tagOfCache())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        if (PicLoader.isInitial()) PicLoader.instance().shutDown();
        super.onBackPressed();
    }
}
