package whataa.github.com.matrixer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Summer on 2016/10/7.
 */

public class TestMeasureView extends View {

    private static final String TAG = TestMeasureView.class.getSimpleName();

    public TestMeasureView(Context context) {
        this(context, null);
    }

    public TestMeasureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestMeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 自定义View的onMeasure只需注意layout_width或layout_height为WRAP_CONTENT一种情况，给出一个默认大小，例如TextView等
        setMeasuredDimension(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST ? 300 : MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST ? 300 : MeasureSpec.getSize(heightMeasureSpec));
    }
}
