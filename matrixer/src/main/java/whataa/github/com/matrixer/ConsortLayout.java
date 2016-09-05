package whataa.github.com.matrixer;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ConsortLayout extends ViewGroup {

    public static final String TAG = ConsortLayout.class.getSimpleName();

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private View headerView;
    private View contentView;
    private int mScrollRange;
    private float mCurrTop;
    private float mCurrOffset;
    private int mTouchSlop;

    private float lastX, lastY;

    public ConsortLayout(Context context) {
        this(context, null);
    }

    public ConsortLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConsortLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2)
            throw new RuntimeException("DragPinnerLayout should contains two children: the pinnerView & the contentView");
        headerView = getChildAt(0);
        contentView = getChildAt(1);
        // in order for it to be dragged
        headerView.bringToFront();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mScrollRange = headerView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        headerView.layout(l, (int) mCurrTop, r, (int) (mCurrTop + headerView.getMeasuredHeight()));
        contentView.layout(l, t, r, b);
        // init the position of contentView's content.
        contentView.setPadding(0, headerView.getBottom(), 0, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent");
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent | ACTION_DOWN");
                lastX = ev.getX();
                lastY = ev.getY();
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(ev);
//                if (isScrolling()) {
//                    Log.i(TAG,"onInterceptTouchEvent | ACTION_DOWN: "+isScrolling());
//                    return true;
//                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onInterceptTouchEvent | ACTION_MOVE "
                        + lastY + " "
                        + ev.getY() + " "
                        + isHeaderHide() + " "
                        + isContentViewTop() + " "
                        + mTouchSlop);
                float diffX = ev.getX() - lastX;
                float diffY = ev.getY() - lastY;
                if (Math.abs(diffX) > Math.abs(diffY) || Math.abs(diffY) < mTouchSlop) {
                    break;
                }
                if (diffY < 0) {
                    if (!isHeaderHide()) {
                        lastY = ev.getY();
                        return true;
                    }
                } else if (diffY > 0) {
                    if (isHeaderHide() && isContentViewTop()) {
                        lastY = ev.getY();
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if (isScrolling()) {
//            return true;
//        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent | ACTION_DOWN");
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent | ACTION_MOVE");
                computeCurrVal(mCurrTop + ev.getY() - lastY);
                headerView.requestLayout();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                determineTarget(ev);
                break;
        }
        return true;
    }

    public boolean isHeaderHide() {
        return mCurrOffset == 1f;
    }

    private boolean isContentViewTop() {
        return contentView.getScrollY() == 0;
    }

    private boolean isScrolling() {
        return !mScroller.isFinished();
    }

    private void computeCurrVal(float currY) {
        mCurrTop = Math.max(Math.min(currY, getPaddingTop()), -mScrollRange);
        mCurrOffset = Math.abs(mCurrTop / mScrollRange);
        Log.i(TAG, "computeCurrVal " + currY + " -" + mScrollRange + " " + mCurrTop + " " + mCurrOffset);
    }

    private void determineTarget(MotionEvent ev) {
        float dy = 0;
        if (mVelocityTracker.getYVelocity() < 0) {
            dy = -mScrollRange - mCurrTop;
        } else {
            dy = getPaddingTop() - mCurrTop;
        }
        mScroller.startScroll(headerView.getLeft(), (int) mCurrTop, 0, (int) dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            computeCurrVal(mScroller.getCurrY());
            headerView.requestLayout();
            invalidate();
        }
    }
}
