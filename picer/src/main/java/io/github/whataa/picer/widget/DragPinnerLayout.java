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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import io.github.whataa.picer.Utils;

/**
 * a layout contains a pinner.
 * @author whataa.github.io
 */
public class DragPinnerLayout extends ViewGroup {

    /**
     * reserved height.
     */
    public static int BAR_HEIGHT = 56;
    private static float SEN_OFFSET = 0.15f;

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

    public DragPinnerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        BAR_HEIGHT = Utils.dip2px(context, BAR_HEIGHT);
        dragHelper = ViewDragHelper.create(this, 1f, new DragCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        pinnerView = getChildAt(0);
        contentView = getChildAt(1);
        // in order for it to be dragged
        pinnerView.bringToFront();
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
            initX = x;
            initY = y;
            interceptTap = dragHelper.isViewUnder(pinnerView, (int) x, (int) y);
        }

        return dragHelper.shouldInterceptTouchEvent(ev) || interceptTap;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        dragHelper.processTouchEvent(ev);
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        boolean isPinnerViewUnder = dragHelper.isViewUnder(pinnerView, (int) x, (int) y);
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
                if (dx * dx + dy * dy < slop * slop && isPinnerViewUnder) {
                    if (mDragOffset == 0) {
                        hide();
                    } else {
                        show();
                    }
                }
                break;
        }

        return isPinnerViewUnder && isViewHit(pinnerView, (int) x, (int) y) || isViewHit(contentView, (int) x, (int) y);
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
        pinnerView.layout(l, currentTop, r,
                currentTop + pinnerView.getMeasuredHeight());

        contentView.layout(l, t, r, b);
        // init the position of contentView's content.
        contentView.setPadding(0, currentTop + pinnerView.getMeasuredHeight(), 0, 0);
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
            pinnerView.setAlpha(1 - mDragOffset);
            pinnerView.requestLayout();// this.requestLayout() is not smooth.
        }
    }

    public void determineToState() {
        if (mDragOffset > SEN_OFFSET) {
            hide();
        } else {
            show();
        }
    }

    public void hide() {
        if (mDragOffset == 1) return;
        smoothSlideTo(1f);
    }

    public void show() {
        if (mDragOffset == 0) return;
        smoothSlideTo(0f);
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

    class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == pinnerView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            currentTop = top;
            mDragOffset = Math.abs((float) top / mDragRange);
            pinnerView.setAlpha(1 - mDragOffset);
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
        return new StateSave(superState, currentTop);
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
        requestLayout();
        if (currentTop != 0) pinnerView.setAlpha(0);
    }

    static class StateSave extends BaseSavedState {
        int top;

        public StateSave(Parcelable superState, int top) {
            super(superState);
            this.top = top;
        }

        public StateSave(Parcel source) {
            super(source);
            top = source.readInt();
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
