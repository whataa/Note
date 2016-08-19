package whataa.github.com.compat_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.github.whataa.picer.PicerActivity;
import io.github.whataa.picer.PicerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PicerActivity.start(this,2);
    }

    public void btnClick(View view) {
    }
}
