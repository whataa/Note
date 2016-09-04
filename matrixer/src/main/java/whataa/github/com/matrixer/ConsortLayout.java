package whataa.github.com.matrixer;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ConsortLayout extends ViewGroup {

    public static final String TAG = ConsortLayout.class.getSimpleName();

    private Scroller mScroller;
    private View headerView;
    private View contentView;
    private int mScrollRange;
    private int mCurrTop;
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
        Log.i(TAG,"onMeasure");
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG,"onSizeChanged");
        if (w != oldw || h != oldh) {
            mScrollRange = headerView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG,"onLayout");
        headerView.layout(l, mCurrTop, r, mCurrTop + headerView.getMeasuredHeight());
        contentView.layout(l, t, r, b);
        // init the position of contentView's content.
        contentView.setPadding(0, headerView.getBottom(), 0, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG,"onInterceptTouchEvent");
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"onInterceptTouchEvent | ACTION_DOWN");
                lastX = ev.getX();
                lastY = ev.getY();
                if (isScrolling()) {
                    Log.i(TAG,"onInterceptTouchEvent | ACTION_DOWN: "+isScrolling());
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"onInterceptTouchEvent | ACTION_MOVE "
                        + lastY +" "
                        +ev.getY()+" "
                        +isHeaderHide()+" "
                        +isContentViewTop()+" "
                        +mTouchSlop);
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
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"onTouchEvent | ACTION_DOWN");
                if (isScrolling()) {
                    mScroller.abortAnimation();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"onTouchEvent | ACTION_MOVE");
                computeCurrVal((int) (ev.getY() - lastY));
                lastY = ev.getY();
                headerView.requestLayout();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                determineTargetLoc(0,0,0,0);
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
    private void computeCurrVal(int dy) {
        mCurrTop = Math.max(Math.min(mCurrTop +dy, getPaddingTop()), -mScrollRange);
        // TODO 待解决：导致mCurrOffset无法等于1
//        if (Math.abs(mCurrTop - mScrollRange) < 3) {
//            mCurrTop = -mScrollRange;
//        }
        mCurrOffset = Math.abs((float) mCurrTop / (float) mScrollRange);
        Log.i(TAG,"computeCurrVal "+dy+" -"+mScrollRange+" "+ mCurrTop +" "+ mCurrOffset);
    }
    private void determineTargetLoc(int currX, int currY, float xVel, float yVel) {
        // depend on speed & location.
        lastY = headerView.getBottom();
        if (mCurrOffset >= 0.3f) {
            mScroller.startScroll(headerView.getLeft(), headerView.getBottom(), 0, -headerView.getBottom());
        } else {
            mScroller.startScroll(headerView.getLeft(), headerView.getBottom(), 0, mScrollRange-headerView.getBottom());
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            computeCurrVal((int) (mScroller.getCurrY() - lastY));
            headerView.requestLayout();
            lastY = mScroller.getCurrY();
            postInvalidate();
        } else {

        }
    }
}
