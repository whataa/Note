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
package io.github.whataa.picer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import io.github.whataa.finepic.R;
import io.github.whataa.picer.Utils;

/**
 * a layout contains a pinner.
 *
 * @author whataa.github.io
 */
public class DragPinnerLayout extends ViewGroup {

    /**
     * reserved height.
     */
    public static int BAR_HEIGHT = 56;
    private static float SEN_OFFSET = 0.2f;

    private ViewDragHelper dragHelper;
    private View pinnerView;
    private View contentView;

    /**
     * only the pinnerview can be dragged.
     */
    private int mDragRange;
    /**
     * the current Y value of pinnerView.
     */
    private int currentTop;
    /**
     * 0~1
     */
    private float mDragOffset;

    private float initX;
    private float initY;

    public DragPinnerLayout(Context context) {
        this(context, null);
    }

    public DragPinnerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.pinnerBar);
        BAR_HEIGHT = ta.getDimensionPixelSize(R.styleable.pinnerBar_height, Utils.dip2px(context, BAR_HEIGHT));
        ta.recycle();
        dragHelper = ViewDragHelper.create(this, 1f, new DragCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2)
            throw new RuntimeException("DragPinnerLayout should contains two children: the pinnerView & the contentView");
        pinnerView = getChildAt(0);
        contentView = getChildAt(1);
        // in order for it to be dragged
        pinnerView.bringToFront();
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            if (isTriggedByPinner) {
                if (mCallback != null) {
                    if (mDragOffset == 0) {
                        mCallback.onPinnerShow(callbackParam);
                    } else if(mDragOffset == 1) {
                        mCallback.onPinnerHide(callbackParam);
                    }
                }
                isTriggedByPinner = false;
                callbackParam = null;
            }
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
            initX = x;
            initY = y;
            // if the finger touches down at pinnerview's bar, intercept it for touchEvent.
            interceptTap = isBarHit((int) x, (int) y);
        }

        return dragHelper.shouldInterceptTouchEvent(ev) || interceptTap;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        boolean isBarHit = isBarHit((int) x, (int) y);
        // process the TouchEvent only when the bar or the visible content being touched.
        if (isBarHit || isContentVisibleHit((int) x, (int) y)) {
            dragHelper.processTouchEvent(ev);// 这里有问题，待处理
        }
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                initX = x;
                initY = y;
                break;
            // switch show/hide state.
            case MotionEvent.ACTION_UP:
                final float dx = x - initX;
                final float dy = y - initY;
                final int slop = dragHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isBarHit) {
                    if (mDragOffset == 0) {
                        hide();
                    } else {
                        show();
                    }
                }
                break;
        }

        return true;
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
        mDragRange = pinnerView.getHeight() - BAR_HEIGHT;
        int pinnerViewBottom = currentTop + pinnerView.getMeasuredHeight();
        pinnerView.layout(l, currentTop, r, pinnerViewBottom);
        contentView.layout(l, t, r, b);
        // init the position of contentView's content.
        contentView.setPadding(0, pinnerViewBottom, 0, 0);
    }

    // get the current Y value of pinnerview.
    public int getPinnerBottomY() {
        return currentTop + pinnerView.getMeasuredHeight();
    }

    public void slideTo(float value) {
        float offset = (pinnerView.getMeasuredHeight() - value) / mDragRange;
        if (offset >= 0 && offset <= 1) {
            final int topBound = getPaddingTop();
            currentTop = (int) (topBound + value - pinnerView.getMeasuredHeight());
            mDragOffset = Math.abs((float) currentTop / mDragRange);
            if (mCallback != null) {mCallback.onPinnerScroll(mDragOffset, currentTop);}
            pinnerView.requestLayout();// this.requestLayout() is not smooth.
        }
    }

    public void determineToState() {
        if (mDragOffset == 1f || mDragOffset == 0f) return;
        if (mDragOffset > SEN_OFFSET) {
            hide();
        } else {
            show();
        }
    }

    public void hide(Object param) {
        callbackParam = param;
        isTriggedByPinner = true;
        if (mDragOffset == 1) {
            if (mCallback != null) {
                mCallback.onPinnerHide(param);
                callbackParam = null;
                isTriggedByPinner = false;
            }
            return;
        }
        smoothSlideTo(1f);
    }
    public void hide() {
        hide(null);
    }

    public void show(Object param) {
        callbackParam = param;
        isTriggedByPinner = true;
        if (mDragOffset == 0) {
            if (mCallback != null) {
                mCallback.onPinnerShow(param);
                callbackParam = null;
                isTriggedByPinner = false;
            }
            return;
        }
        smoothSlideTo(0f);
    }
    public void show() {
        show(null);
    }

    boolean smoothSlideTo(float slideOffset) {
        final int topBound = getPaddingTop();
        int y = (int) (topBound + (-slideOffset * mDragRange));

        if (dragHelper.smoothSlideViewTo(pinnerView, pinnerView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private boolean isBarHit(int x, int y) {
        int[] viewLocation = new int[2];
        pinnerView.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0]+BAR_HEIGHT &&
                screenX < viewLocation[0] + pinnerView.getWidth()-BAR_HEIGHT &&
                screenY >= (viewLocation[1] + pinnerView.getMeasuredHeight() - BAR_HEIGHT) &&
                screenY < viewLocation[1] + pinnerView.getHeight();
    }

    private boolean isContentVisibleHit(int x, int y) {
        int[] viewLocation = new int[2];
        contentView.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] &&
                screenX < viewLocation[0] + contentView.getWidth() &&
                screenY > (viewLocation[1] + contentView.getPaddingTop()) &&
                screenY < viewLocation[1] + contentView.getHeight();
    }

    class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == pinnerView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            currentTop = top;
            mDragOffset = Math.abs((float) top / mDragRange);
            if (mCallback!=null) {
                mCallback.onPinnerScroll(mDragOffset, currentTop);
            }
            // contentView.requestLayout() is not smooth when the layout comes complex.
            contentView.setPadding(0, currentTop + pinnerView.getMeasuredHeight(), 0, 0);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            // determine which side to go.
            if (yvel < 0 || (yvel == 0 && mDragOffset > SEN_OFFSET)) {
                top -= mDragRange;
            }
            isTriggedByPinner = true;
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

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new StateSave(superState, currentTop, mDragOffset);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof StateSave)) {
            super.onRestoreInstanceState(state);
            return;
        }
        StateSave ss = (StateSave) state;
        super.onRestoreInstanceState(ss.getSuperState());
        currentTop = ss.getTop();
        mDragOffset = ss.getOffset();
        requestLayout();
        if (mCallback != null) {
            mCallback.onPinnerScroll(mDragOffset, currentTop);
        }
    }

    static class StateSave extends BaseSavedState {
        int top;

        public float getOffset() {
            return offset;
        }

        public void setOffset(float offset) {
            this.offset = offset;
        }

        float offset;

        public StateSave(Parcelable superState, int top, float offset) {
            super(superState);
            this.top = top;
            this.offset = offset;
        }

        public StateSave(Parcel source) {
            super(source);
            top = source.readInt();
            offset = source.readFloat();
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(top);
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

    // SlideStateCallback maybe need to pass some params.
    private Object callbackParam;
    // excluding impact of the outside, such as the GridView.
    private boolean isTriggedByPinner;
    private SlideStateCallback mCallback;

    public void setCallback(SlideStateCallback callback) {
        this.mCallback = callback;
    }

    public interface SlideStateCallback {
        /**
         * @param param u can pass some param when start to drag, and use them inside of the method.
         */
        void onPinnerHide(Object param);

        /**
         * @param offset show is 0, hide is 1.
         * @param pinnerTop the current top position of pinnerview, equal with using pinnerview.getTop().
         */
        void onPinnerScroll(float offset, int pinnerTop);

        /**
         * @param param same as onPinnerHide
         */
        void onPinnerShow(Object param);
    }
}
