package com.example.view_app.chart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.view_app.R;

/**
 * Created by Administrator on 2017/2/27.
 */

public class ChartDemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartdemo);
    }

    public void btnClick(View view) {
        TestView testView = (TestView) findViewById(R.id.chartview);
        testView.startAnim(5000);
    }
}
