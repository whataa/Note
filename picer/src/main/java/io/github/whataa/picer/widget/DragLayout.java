package io.github.whataa.picer.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import io.github.whataa.picer.Utils;

public class DragLayout extends ViewGroup {
    public static final String TAG = DragLayout.class.getSimpleName();
    private ViewDragHelper dragHelper;
    private View headerView;
    private View contentView;

    public static int BAR_HEIGHT;
    /**
     * 固定为headerview的高度 - toobar，正
     */
    private int mDragRange;
    /**
     * 当前y值
     */
    private int mTop;
    /**
     * 偏移度，正
     */
    private float mDragOffset;
    private float mInitialMotionX;
    private float mInitialMotionY;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        BAR_HEIGHT = Utils.dip2px(context, 56);
        dragHelper = ViewDragHelper.create(this, 1f, new DragCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerView = getChildAt(0);
        contentView = getChildAt(1);
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        boolean interceptTap;
        if ((action != MotionEvent.ACTION_DOWN)) {
            dragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        } else {
            final float x = ev.getX();
            final float y = ev.getY();
            mInitialMotionX = x;
            mInitialMotionY = y;
            interceptTap = dragHelper.isViewUnder(headerView, (int) x, (int) y);
        }

        return dragHelper.shouldInterceptTouchEvent(ev) || interceptTap;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        dragHelper.processTouchEvent(ev);
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        boolean isHeaderViewUnder = dragHelper.isViewUnder(headerView, (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = dragHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        show();
                    } else {
                        hide();
                    }
                }
                break;
            }
        }

        return isHeaderViewUnder && isViewHit(headerView, (int) x, (int) y) || isViewHit(contentView, (int) x, (int) y);
    }

    public void show() {
        smoothSlideTo(1f);
    }
    public void hide() {
        smoothSlideTo(0f);
    }

    boolean smoothSlideTo(float slideOffset) {
        final int topBound = getPaddingTop();
        int y = (int) (topBound + (-slideOffset * mDragRange));

        if (dragHelper.smoothSlideViewTo(headerView, headerView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = headerView.getHeight() - BAR_HEIGHT;
        headerView.layout(
                0,
                mTop,
                r,
                mTop + headerView.getMeasuredHeight());// 滑动距离+自身高度

        contentView.layout(
                0,
                mTop + headerView.getMeasuredHeight(),// headerview当前bottom坐标
                r,
                b);// 始终在最下面
    }

    class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == headerView;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTop = top;
            mDragOffset = Math.abs((float) top / mDragRange);
            headerView.setAlpha(1 - mDragOffset);
//            requestLayout();
            contentView.setY(mTop + headerView.getHeight());
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            if (yvel < 0 || (yvel == 0 && mDragOffset > 0.15f)) {
                top -= mDragRange;
            }// 到两端
            dragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return Math.max(Math.min(top, getPaddingTop()), -mDragRange);
        }
    }
}
