package com.whataa.fragmentapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ContainerActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    FragmentMaster master;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Bundle bundle = new Bundle();
        bundle.putString("DATA", "page1");
        Bundle bundle1 = new Bundle();
        bundle1.putString("DATA", "page2");

        master = new FragmentMaster.Builder()
                .containerViewId(R.id.main_container)
                .fragmentManager(getSupportFragmentManager())
                .install(FragmentRadio01.class, bundle)
                .install(FragmentRadio01.class, bundle1, 1)
                .build().loadAll();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_button_group);
        radioGroup.setOnCheckedChangeListener(this);
//        RadioButton radioButton = (RadioButton) findViewById(R.id.main_button_0);
//        radioButton.setChecked(true);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_button_0:
                master.showOrload(FragmentRadio01.class);
                break;
            case R.id.main_button_1:
                master.showOrload(FragmentRadio01.class, 1);
        }
    }
}
