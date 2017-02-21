package com.example.view_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.view_app.layoutmode.LayoutModeActivity;
import com.example.view_app.testmeasure.MeasureActivity;

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
}
