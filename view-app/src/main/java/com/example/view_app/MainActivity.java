package com.example.view_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.view_app.chart.ChartDemoActivity;
import com.example.view_app.layoutmode.LayoutModeActivity;
import com.example.view_app.testmeasure.MeasureActivity;
import com.example.view_app.touch.TouchActivty;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn1Click(View view) {
        startActivity(new Intent(this, LayoutModeActivity.class));
    }

    public void btn2Click(View view) {
        startActivity(new Intent(this, MeasureActivity.class));
    }

    public void btn3Click(View view) {
        startActivity(new Intent(this, ChartDemoActivity.class));
    }

    public void btn4Click(View view) {
        startActivity(new Intent(this, TouchActivty.class));
    }
}
