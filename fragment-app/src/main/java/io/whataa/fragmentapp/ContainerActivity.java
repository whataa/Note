package io.whataa.fragmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.whataa.fragmentapp.R;

import io.whataa.fragmentapp.backstackdemo.StackActivity;
import io.whataa.fragmentapp.common.BaseActivity;
import io.whataa.fragmentapp.common.BundleBuilder;
import io.whataa.fragmentapp.common.FragmentMaster;
import io.whataa.fragmentapp.pagedemo.PageDemoActivity;

public class ContainerActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    FragmentMaster master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

//        FragmentManager.enableDebugLogging(true);
        master = new FragmentMaster.Builder()
                .containerViewId(R.id.main_container)
                .fragmentManager(getSupportFragmentManager())
                .config(FragmentRadio01.class, 0, new BundleBuilder().putString("DATA", "page1").build())
                .config(FragmentRadio01.class, 1, new BundleBuilder().putString("DATA", "page2").build())
                .config(FragmentRadio01.class, 2, new BundleBuilder().putString("DATA", "page3").build())
                .build().loadAllFragments();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_button_group);
        radioGroup.setOnCheckedChangeListener(this);

    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_button_0:
                master.showOrLoad(0);
                break;
            case R.id.main_button_1:
                master.showOrLoad(1);
                break;
            case R.id.main_button_2:
                master.showOrLoad(2);
                break;
        }
    }



    public void mainBtn1Click(View view) {
        startActivity(new Intent(this, StackActivity.class));
    }

    public void mainBtn2Click(View view) {
        startActivity(new Intent(this, PageDemoActivity.class));
    }

    public void mainBtn3Click(View view) {
    }

    public void mainBtn4Click(View view) {
    }
}
