package whataa.github.com.compat_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.whataa.picer.PicerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void btnClick1(View view) {
        PicerActivity.start(this,6);
    }

    public void btnClick2(View view) {
        startActivity(new Intent(this, ConsortActivity.class));
    }

    public void btnClick3(View view) {
        startActivity(new Intent(this, TestMeasureActivity.class));
    }

    public void btnClick4(View view) {
        startActivity(new Intent(this, PinAncherActivity.class));
    }
}
