package io.github.whataa.picer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.github.whataa.finepic.R;
import io.github.whataa.picer.executor.PicLoader;
import io.github.whataa.picer.picer.PicerFragment;

public class PicerActivity extends AppCompatActivity implements EventCallback {

    private static final String PARAM_SIZE = "param_size";
    public static final int REQUEST_CODE = 0x09;
    public static final String REQUEST_PARAM = "request_param";

    public static void start(Activity activity, int maxSize) {
        Intent intent = new Intent(activity, PicerActivity.class);
        intent.putExtra(PARAM_SIZE, maxSize < 1 ? 1 : maxSize);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PicLoader.initial(this);
        setContentView(R.layout.activity_picer);
        if (savedInstanceState != null) {
        }
        int size = getIntent().getIntExtra(PARAM_SIZE, 0);
        if (size < 1)
            throw new UnsupportedOperationException("must start PicerActivity by invoking PicerActivity#start");

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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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

    @Override
    public void onEvent(int type, List<String> pics) {
        Log.d("PicerActivity#onEvent", type+" "+pics);
        switch (type) {
            case EVENT_COMPLETE:
                ArrayList<String> datas = new ArrayList<>();
                datas.addAll(pics);
                setResult(RESULT_OK, new Intent().putStringArrayListExtra(REQUEST_PARAM,datas));
                onBackPressed();
                break;
            case EVENT_CANCEL:
                onBackPressed();
                break;
            case EVENT_CAMERA:
                break;
        }
    }

}
