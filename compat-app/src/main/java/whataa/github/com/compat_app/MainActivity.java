package whataa.github.com.compat_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.whataa.picer.PicerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PicerActivity.start(this,6);
    }

    public void btnClick(View view) {
    }
}
