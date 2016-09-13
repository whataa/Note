package whataa.github.com.compat_app;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;

import whataa.github.com.matrixer.ConsortLayout;

public class ConsortActivity extends AppCompatActivity {
    private static final String TAG = ConsortLayout.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consort);
        ConsortLayout consortLayout = (ConsortLayout) findViewById(R.id.consortlayout);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        consortLayout.setHelper(new ConsortLayout.ConsortHelper() {
            @Override
            public boolean shouldOpen() {
                return scrollView.getScrollY() == 0;
            }

            @Override
            public void onScrolling(float offset) {
                Log.e(TAG, "onScrolling: "+offset);
            }
        });
    }
}
