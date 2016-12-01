package whataa.github.com.matrixer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by Summer on 2016/10/7.
 */

public class TestMeasureViewGroup extends ViewGroup {

    private static final String TAG = TestMeasureViewGroup.class.getSimpleName();

    public TestMeasureViewGroup(Context context) {
        this(context, null);
    }

    public TestMeasureViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestMeasureViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.d(TAG,MeasureSpec.toString(widthMeasureSpec)+" "+ MeasureSpec.toString(heightMeasureSpec));
//
//        Log.e(TAG,MeasureSpec.toString(getChildMeasureSpec(widthMeasureSpec,0,getChildAt(0).getLayoutParams().width))+"");
//        Log.e(TAG,MeasureSpec.toString(getChildMeasureSpec(heightMeasureSpec,0,getChildAt(0).getLayoutParams().height))+"");

//        Log.d(TAG,"before: "+getMeasuredWidth()+" "+ getMeasuredHeight()+"\n"+
//                getChildAt(0).getMeasuredWidth()+" "+getChildAt(0).getMeasuredHeight());

//        measureChild(getChildAt(0),widthMeasureSpec,  heightMeasureSpec);


//        setMeasuredDimension(getChildAt(0).getMeasuredWidth(), getChildAt(0).getMeasuredHeight());

//        Log.d(TAG,"after: "+getMeasuredWidth()+" "+ getMeasuredHeight()+"\n"+
//                getChildAt(0).getMeasuredWidth()+" "+getChildAt(0).getMeasuredHeight());

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int count = getChildCount();

        int totalW = 0, totalH = 0;

        for (int i = 0; i < count; ++i) {
            measureChild(getChildAt(i),widthMeasureSpec,  heightMeasureSpec);
            totalW += getChildAt(i).getMeasuredWidth();
            totalH += getChildAt(i).getMeasuredHeight();
        }

        setMeasuredDimension(totalW, totalH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG,"onLayout: "+l+" "+t+" "+r+" "+b);
        getChildAt(0).layout(l,t,l+getChildAt(0).getMeasuredWidth(),t+getChildAt(0).getMeasuredHeight());

        int l2 = l+getChildAt(0).getMeasuredWidth();
        int t2 = t+getChildAt(0).getMeasuredHeight();
        getChildAt(1).layout(l2,t2,l2+getChildAt(1).getMeasuredWidth(),t2+getChildAt(1).getMeasuredHeight());
    }
}
