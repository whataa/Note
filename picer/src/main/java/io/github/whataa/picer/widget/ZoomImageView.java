package io.github.whataa.picer.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Scroller;


public class ZoomImageView extends ImageView {
    private static final String TAG = ZoomImageView.class.getSimpleName();
    private Scroller mScroller;
    private ScaleGestureDetector mScaleDetector;
    private Matrix mImageMatrix;
    /* Last Rotation Angle */
    private int mLastAngle = 0;
    /* Pivot Point for Transforms */
    private int mPivotX, mPivotY;

    public ZoomImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mScaleDetector = new ScaleGestureDetector(context, mScaleListener);

        setScaleType(ScaleType.MATRIX);
        mImageMatrix = new Matrix();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        scaleCropToCenter();
        translateToCenter();
    }

    /**
     * should be called after onMeasure.
     */
    private void translateToCenter() {
        if (getDrawable() == null) return;
        //Shift the image to the center of the view
        // getDrawable().getIntrinsicWidth() 获取表现出来的宽度（适配不同密度的屏幕），并不代表图片本身的宽度
        int translateX = (getMeasuredWidth() - getDrawable().getIntrinsicWidth()) / 2;
        int translateY = (getMeasuredHeight() - getDrawable().getIntrinsicHeight()) / 2;
        mImageMatrix.setTranslate(translateX, translateY);
        setImageMatrix(mImageMatrix);
    }

    private void scaleCropToCenter() {
        if (getDrawable() == null) return;
        double scaleX = getDrawable().getIntrinsicWidth() / getMeasuredWidth();
        double scaleY = getDrawable().getIntrinsicHeight() / getMeasuredHeight();
        double scale = 1f;
        if ((scaleX >= 1 && scaleY >= 1) || (scaleX < 1 && scaleY < 1)) {
            scale = scaleX <= scaleY ? 1/scaleX : 1/scaleY;
        } else if (scaleX >= 1) {
            scale = 1/scaleY;
        } else if (scaleY >= 1) {
            scale = 1/scaleX;
        }
        Log.e(TAG, "scaleCropToCenter:"+scale+" "+scaleX+" "+scaleY+" "
                +getDrawable().getIntrinsicWidth()+"/"+getMeasuredWidth()+" "
                +getDrawable().getIntrinsicHeight()+"/"+getMeasuredHeight());
//        mImageMatrix.setScale(scale,scale);
//        setImageMatrix(mImageMatrix);
    }

    /*
         * Use onSizeChanged() to calculate values based on the view's size.
         * The view has no size during init(), so we must wait for this
         * callback.
         */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            //Get the center point for future scale and rotate transforms
            mPivotX = w / 2;
            mPivotY = h / 2;
        }
    }


    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // ScaleGestureDetector calculates a scale factor based on whether
            // the fingers are moving apart or together
            float scaleFactor = detector.getScaleFactor();
            //Pass that factor to a scale for the image
            mImageMatrix.postScale(scaleFactor, scaleFactor, mPivotX, mPivotY);
            setImageMatrix(mImageMatrix);

            return true;
        }
    };

    /*
     * Operate on two-finger events to rotate the image.
     * This method calculates the change in angle between the
     * pointers and rotates the image accordingly.  As the user
     * rotates their fingers, the image will follow.
     */
    private boolean doRotationEvent(MotionEvent event) {
        //Calculate the angle between the two fingers
        float deltaX = event.getX(0) - event.getX(1);
        float deltaY = event.getY(0) - event.getY(1);
        double radians = Math.atan(deltaY / deltaX);
        //Convert to degrees
        int degrees = (int) (radians * 180 / Math.PI);

        /*
         * Must use getActionMasked() for switching to pick up pointer events.
         * These events have the pointer index encoded in them so the return
         * from getAction() won't match the exact action constant.
         */
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                //Mark the initial angle
                mLastAngle = degrees;
                break;
            case MotionEvent.ACTION_MOVE:
                // ATAN returns a converted value between -90deg and +90deg
                // which creates a point when two fingers are vertical where the
                // angle flips sign.  We handle this case by rotating a small amount
                // (5 degrees) in the direction we were traveling
                if ((degrees - mLastAngle) > 45) {
                    //Going CCW across the boundary
                    mImageMatrix.postRotate(-5, mPivotX, mPivotY);
                } else if ((degrees - mLastAngle) < -45) {
                    //Going CW across the boundary
                    mImageMatrix.postRotate(5, mPivotX, mPivotY);
                } else {
                    //Normal rotation, rotate the difference
                    mImageMatrix.postRotate(degrees - mLastAngle, mPivotX, mPivotY);
                }
                //Post the rotation to the image
                setImageMatrix(mImageMatrix);
                //Save the current angle
                mLastAngle = degrees;
                break;
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initX = lastX = event.getX();
            initY = lastY = event.getY();
            // We don't care about this event directly, but we declare
            // interest so we can get later multi-touch events.
            return true;
        }


        switch (event.getPointerCount()) {
            case 3:
                // With two fingers down, rotate the image
                // following the fingers
                return doRotationEvent(event);
            case 2:
                // With three fingers down, zoom the image
                // using the ScaleGestureDetector
                return mScaleDetector.onTouchEvent(event);
            case 1:
                return doTranslate(event);
            default:
                //Ignore this event
                return super.onTouchEvent(event);
        }
    }

    private float lastX, lastY, initX, initY;


    private boolean doTranslate(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) (event.getX() - lastX);
            int dy = (int) (event.getY() - lastY);
            lastX = event.getX();
            lastY = event.getY();
            mImageMatrix.postTranslate(dx, dy);
            setImageMatrix(mImageMatrix);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            post(new BackToInitXY(event.getX(), event.getY()));
        }
        return true;
    }

    class BackToInitXY implements Runnable {
        private int newX, newY;

        public BackToInitXY(float lastX, float lastY) {
            newX = (int) lastX;
            newY = (int) lastY;
            mScroller.abortAnimation();
            mScroller.startScroll(newX, newY, (int) initX - newX, (int) initY - newY, 800);
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {// call to compute, if has valid value.
                mImageMatrix.postTranslate(mScroller.getCurrX() - newX, mScroller.getCurrY() - newY);
                newX = mScroller.getCurrX();
                newY = mScroller.getCurrY();
                setImageMatrix(mImageMatrix);
                postDelayed(this, 16);
            }
        }
    }
}