/*
 * Copyright 2016 whataa.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package whataa.github.com.matrixer;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * a layout can hide / show the headerView just like the Coordinator Layout with CollapsingToolbarLayout.<p/>
 * created by yanglinjiang on 2016/9/13
 */
public class ConsortLayout extends ViewGroup {

    public static final String TAG = ConsortLayout.class.getSimpleName();
    public static final int SCROLL_TIME = 500;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private View headerView;
    private View contentView;
    private int mTouchSlop;
    private int mMaxTop;

    // open is 1.0f, close is 0.0f.
    private float currOffset = 1f;

    private float currTop, lastTop, lastX, lastY;
    private boolean isInLayout;
    // in case of the childview not consuming touch-event.
    private boolean isIntercept;


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
        mScroller = new Scroller(context, new DecelerateInterpolator());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2)
            throw new RuntimeException(TAG + "should contains two children: the headerView & the contentView");
        headerView = getChildAt(0);
        contentView = getChildAt(1);
        headerView.bringToFront();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);

        LayoutParam lpHead = (LayoutParam) headerView.getLayoutParams();
        LayoutParam lpContent = (LayoutParam) contentView.getLayoutParams();

        int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lpContent.leftMargin - lpContent.rightMargin
                        - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lpContent.topMargin - lpContent.bottomMargin
                        - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        int headerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                lpHead.leftMargin + lpHead.rightMargin + getPaddingLeft() + getPaddingRight(),
                lpHead.width);
        int headerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lpHead.topMargin + lpHead.bottomMargin + getPaddingTop() + getPaddingBottom(),
                lpHead.height);
        headerView.measure(headerWidthSpec, headerHeightSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            LayoutParam lpHead = (LayoutParam) headerView.getLayoutParams();
            mMaxTop = -(lpHead.topMargin + headerView.getMeasuredHeight());
            // init params
            applyParamAndDispatch(currOffset);
            lastTop = currTop = !isHeaderHide() ? 0 : mMaxTop;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isInLayout = true;
        LayoutParam lpHead = (LayoutParam) headerView.getLayoutParams();
        LayoutParam lpContent = (LayoutParam) contentView.getLayoutParams();

        int headerWidth = headerView.getMeasuredWidth();
        int headerHeight = headerView.getMeasuredHeight();
        // current top
        int headerTop = -headerHeight + (int) (headerHeight * lpHead.onScreen);
        float newOffset = (float) (headerHeight + headerTop) / headerHeight;

        headerView.layout(getPaddingLeft() + lpHead.leftMargin,
                getPaddingTop() + headerTop + lpHead.topMargin,
                getPaddingLeft() + lpHead.leftMargin + headerWidth,
                getPaddingTop() + headerTop + headerHeight);
        if (newOffset != lpContent.onScreen) {
            applyParamAndDispatch(newOffset);
        }
        int contentTop = lpContent.topMargin + headerView.getBottom() + lpHead.bottomMargin;
        contentView.layout(getPaddingLeft() + lpContent.leftMargin,
                contentTop,
                getPaddingLeft() + lpContent.leftMargin + contentView.getMeasuredWidth(),
                contentTop + contentView.getMeasuredHeight());

        isInLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!isInLayout) {
            super.requestLayout();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrolling()) {
            return true;
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float diffX = ev.getX() - lastX;
                float diffY = ev.getY() - lastY;
                if (Math.abs(diffX) > Math.abs(diffY) || Math.abs(diffY) < mTouchSlop) {
                    break;
                }
                if (diffY < 0) {
                    if (!isHeaderHide()) {
                        lastY = ev.getY();
                        isIntercept = true;
                        return true;
                    }
                } else if (diffY > 0) {
                    if (isHeaderHide() && isContentViewTop()) {
                        lastY = ev.getY();
                        isIntercept = true;
                        return true;
                    }
                }
                break;
        }
        isIntercept = false;
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isIntercept || isScrolling()) {
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                lastY = ev.getY(0);
                break;
            case MotionEvent.ACTION_MOVE:
                computeAndoffset(currTop + ev.getY() - lastY);
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                determineTarget();
                break;
        }
        return true;
    }

    /**
     * obtain the correct value from the tracking finger.
     *
     * @param ev
     */
    private void onPointerUp(MotionEvent ev) {
        int minID = ev.getPointerId(0);
        for (int i = 0; i < ev.getPointerCount(); i++) {
            if (ev.getPointerId(i) <= minID) {
                minID = ev.getPointerId(i);
            }
        }
        if (ev.getPointerId(ev.getActionIndex()) == minID) {
            minID = ev.getPointerId(ev.getActionIndex() + 1);
        }
        lastY = ev.getY(ev.findPointerIndex(minID));
    }

    public boolean isHeaderHide() {
        return currOffset == 0f;
    }

    private boolean isContentViewTop() {
        return mHelper != null && mHelper.shouldOpen();
    }

    private boolean isScrolling() {
        return !mScroller.isFinished();
    }

    private void computeAndoffset(float currY) {
        currTop = Math.max(Math.min(currY, 0), mMaxTop);
        currOffset = 1 - Math.abs(currTop / mMaxTop);
        applyParamAndDispatch(currOffset);
        int dy = (int) (currTop - lastTop);
        headerView.offsetTopAndBottom(dy);
        contentView.offsetTopAndBottom(dy);
        lastTop = currTop;
    }

    private void determineTarget() {
        if (currOffset == 1f || currOffset == 0f) {
            return;
        }
        float dy;
        if (mVelocityTracker.getYVelocity() < 0) {
            dy = mMaxTop - currTop;
        } else {
            dy = 0f - currTop;
        }
        mScroller.startScroll(headerView.getLeft(), (int) currTop, 0, (int) dy, SCROLL_TIME);
        invalidate();
    }

    void applyParamAndDispatch(float slideOffset) {
        LayoutParam lp = (LayoutParam) headerView.getLayoutParams();
        if (slideOffset == lp.onScreen) {
            return;
        }
        lp.onScreen = slideOffset;
        if (mHelper != null) {
            mHelper.onScrolling(lp.onScreen);
        }
    }

    public void open() {
        if (isScrolling() || currOffset == 1f) return;
        mScroller.startScroll(headerView.getLeft(), (int) currTop, 0, (int) -currTop, SCROLL_TIME);
        invalidate();
    }

    public void close() {
        if (isScrolling() || currOffset == 0f) return;
        mScroller.startScroll(headerView.getLeft(), (int) currTop, 0, (int) (mMaxTop - currTop), SCROLL_TIME);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            computeAndoffset(mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    //----------------------------------------------------------------------------------------------

    public interface ConsortHelper {
        /**
         * At the right time to return true to make the program automatically open. <br/>
         * such as the time when adapterView's scrollY is 0.<p/>
         * eg: return scrollView.getScrollY() == 0;
         *
         * @return
         */
        boolean shouldOpen();

        /**
         * the value changes between 0f and 1f by scrolling.
         *
         * @param offset
         */
        void onScrolling(float offset);
    }

    private ConsortHelper mHelper;

    public void setHelper(ConsortHelper helper) {
        mHelper = helper;
    }


    //----------------------------------------------------------------------------------------------
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParam(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p instanceof LayoutParam
                ? new LayoutParam((LayoutParam) p)
                : p instanceof ViewGroup.MarginLayoutParams
                ? new LayoutParam((MarginLayoutParams) p)
                : new LayoutParam(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParam && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParam(getContext(), attrs);
    }

    public static class LayoutParam extends MarginLayoutParams {

        public float onScreen;

        public LayoutParam(LayoutParams source) {
            super(source);
        }

        public LayoutParam(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParam(int width, int height) {
            super(width, height);
        }

        public LayoutParam(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new StateSave(superState, currOffset);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof StateSave)) {
            super.onRestoreInstanceState(state);
            return;
        }
        StateSave ss = (StateSave) state;
        super.onRestoreInstanceState(ss.getSuperState());
        // we needn't do anything beacuse of the time this method being called.
        currOffset = ss.getOffset();
    }

    static class StateSave extends BaseSavedState {
        float offset;

        StateSave(Parcelable superState, float currOffset) {
            super(superState);
            offset = currOffset;
        }

        public float getOffset() {
            return offset;
        }

        StateSave(Parcel source) {
            super(source);
            offset = source.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(offset);
        }

        public static final Parcelable.Creator<StateSave> CREATOR = new Creator<StateSave>() {
            @Override
            public StateSave createFromParcel(Parcel parcel) {
                return new StateSave(parcel);
            }

            @Override
            public StateSave[] newArray(int i) {
                return new StateSave[0];
            }
        };
    }
}
