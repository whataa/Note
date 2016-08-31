package whataa.github.com.matrixer;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Scroller;

import static whataa.github.com.matrixer.Utils.L;


public class ZoomImageView extends ImageView {
    private static final String TAG = ZoomImageView.class.getSimpleName();
    private Scroller mScroller;
    private Scroller translateScroller;
    private ScaleGestureDetector mScaleDetector;
    private Matrix mImageMatrix = new Matrix();
    /* Last Rotation Angle */
    private int mLastAngle = 0;
    /* Pivot Point for Transforms */
    private int mPivotX, mPivotY;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        translateScroller = new Scroller(context);
//        mScaleDetector = new ScaleGestureDetector(context, mScaleListener);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isNoReset) {
            isNoReset = false;
            insideToCenter();
            translateToCenter();
        }
    }

    private boolean isNoReset;

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        isNoReset = true;
    }

    private int firstPointerId;
    private int lastPointerId;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                firstPointerId = event.getPointerId(event.getActionIndex());
                Log.e(TAG, "MotionEvent.ACTION_DOWN:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()) + " " + event.getPointerCount());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG, "MotionEvent.ACTION_POINTER_DOWN:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()) + " " + event.getPointerCount());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "MotionEvent.ACTION_MOVE:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()) + " " + event.getPointerCount());
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "MotionEvent.ACTION_UP:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()) + " " + event.getPointerCount());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG, "MotionEvent.ACTION_POINTER_UP:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()) + " " + event.getPointerCount());
                if (firstPointerId == event.getPointerId(event.getActionIndex())) {
                    lastX = event.getX(event.getPointerCount() - 1);
                    lastY = event.getY(event.getPointerCount() - 1);
                    firstPointerId = -1;
                }
                break;
        }
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            initX = lastX = event.getX();
            initY = lastY = event.getY();
            // We don't care about this event directly, but we declare
            // interest so we can get later multi-touch events.
            return true;
        }

        return doTranslate(event);
    }


    /**
     * should be called after onMeasure.
     */
    private void translateToCenter() {
        if (getDrawable() == null) return;

        float values[] = new float[9];
        mImageMatrix.getValues(values);
        float baseScase = values[Matrix.MSCALE_X];
        int drawableW = (int) (getDrawable().getIntrinsicWidth() * baseScase);
        int drawableH = (int) (getDrawable().getIntrinsicHeight() * baseScase);
        //Shift the image to the center of the view
        // getDrawable().getIntrinsicWidth() 获取表现出来的宽度（适配不同密度的屏幕），并不代表图片本身的宽度
        int translateX = (getMeasuredWidth() - drawableW) / 2;
        int translateY = (getMeasuredHeight() - drawableH) / 2;
        mImageMatrix.postTranslate(translateX, translateY);
        setImageMatrix(mImageMatrix);

        L(TAG, mImageMatrix.toShortString());
        L(TAG, "translateToCenter:" + baseScase + " "
                + drawableW + "/" + getMeasuredWidth() + " "
                + drawableH + "/" + getMeasuredHeight());
    }

    private void cropToCenter() {
        if (getDrawable() == null) return;
        float scaleX = (float) getDrawable().getIntrinsicWidth() / (float) getMeasuredWidth();
        float scaleY = (float) getDrawable().getIntrinsicHeight() / (float) getMeasuredHeight();
        float scale = 1f;
        if ((scaleX >= 1 && scaleY >= 1) || (scaleX < 1 && scaleY < 1)) {
            scale = scaleX <= scaleY ? 1 / scaleX : 1 / scaleY;
        } else if (scaleX >= 1) {
            scale = 1 / scaleY;
        } else if (scaleY >= 1) {
            scale = 1 / scaleX;
        }
        // if not reset,post/pre will grow based on last.
        mImageMatrix.reset();
        mImageMatrix.postScale(scale, scale);
        setImageMatrix(mImageMatrix);

        L(TAG, mImageMatrix.toShortString());
        L(TAG, "scaleCropToCenter:" + scale + " " + scaleX + " " + scaleY + " "
                + getDrawable().getIntrinsicWidth() + "/" + getMeasuredWidth() + " "
                + getDrawable().getIntrinsicHeight() + "/" + getMeasuredHeight());
    }

    private void insideToCenter() {
        if (getDrawable() == null) return;
        float scaleX = (float) getDrawable().getIntrinsicWidth() / (float) getMeasuredWidth();
        float scaleY = (float) getDrawable().getIntrinsicHeight() / (float) getMeasuredHeight();
        float scale = 1f;
        if ((scaleX >= 1 && scaleY >= 1) || (scaleX < 1 && scaleY < 1)) {
            scale = scaleX >= scaleY ? 1 / scaleX : 1 / scaleY;
        } else if (scaleX >= 1) {
            scale = 1 / scaleX;
        } else if (scaleY >= 1) {
            scale = 1 / scaleY;
        }
        // if not reset,post/pre will grow based on last.
        mImageMatrix.reset();
        mImageMatrix.postScale(scale, scale);
        setImageMatrix(mImageMatrix);

        L(TAG, mImageMatrix.toShortString());
        L(TAG, "insideToCenter:" + scale + " " + scaleX + " " + scaleY + " "
                + getDrawable().getIntrinsicWidth() + "/" + getMeasuredWidth() + " "
                + getDrawable().getIntrinsicHeight() + "/" + getMeasuredHeight());
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


    private float lastX, lastY, initX, initY;


    private boolean doTranslate(MotionEvent event) {
        if ((event.getAction() & event.getActionMasked()) == MotionEvent.ACTION_MOVE) {
            int dx = (int) (event.getX() - lastX);
            int dy = (int) (event.getY() - lastY);
            lastX = event.getX();
            lastY = event.getY();
            mImageMatrix.postTranslate(dx, dy);
            setImageMatrix(mImageMatrix);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            post(new BackToInitXY(event.getX(), event.getY()));
        }
        return true;
    }

    private boolean checkIsEdge(MotionEvent event) {
        // when picasso starts loading pics, it will load a transition-pic, not a drawable.
        if (getDrawable() == null) return false;
        Rect srcRect = new Rect();
        Rect ivRect = getDrawable().getBounds();
        RectF rectTmp = new RectF(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
        mImageMatrix.mapRect(rectTmp);
        rectTmp.round(srcRect);
        L(TAG, "checkIsEdge:" + rectTmp.toShortString());
        if (srcRect.contains(ivRect)) {
            // do nothing.
            return false;
        }
        if (ivRect.contains(srcRect) // 包含在内
                || (srcRect.left >= ivRect.left && srcRect.right <= ivRect.right) // 图片左右小于控件左右
                || (srcRect.top >= ivRect.top && srcRect.bottom <= ivRect.bottom) // 图片上下小于控件上下
                // 图片与控件不相交
                || (srcRect.right <= ivRect.left || srcRect.left >= ivRect.left || srcRect.bottom <= ivRect.top || srcRect.top >= ivRect.bottom)) {
            // 平移到中间,不缩放
            return false;
        }

        if (srcRect.left >= ivRect.left && srcRect.left <= ivRect.right) {
            if (srcRect.top >= ivRect.top && srcRect.top <= ivRect.bottom) {
                // 左上角在区域内
                mImageMatrix.postTranslate(ivRect.left - srcRect.left, ivRect.top - srcRect.top);

            } else if (srcRect.bottom >= ivRect.top && srcRect.bottom <= ivRect.bottom) {
                // 左下角在区域内
                mImageMatrix.postTranslate(ivRect.left - srcRect.left, ivRect.bottom - srcRect.bottom);
            } else if (srcRect.top >= ivRect.top && srcRect.bottom <= ivRect.bottom) {
                // 左半部分在区域内
            } else {
                // 区域右半部分在图片内
            }
        } else if (srcRect.right >= ivRect.left && srcRect.right <= ivRect.right) {
            if (srcRect.top >= ivRect.top && srcRect.top <= ivRect.bottom) {
                // 右上角在区域内
                mImageMatrix.postTranslate(ivRect.right - srcRect.right, ivRect.top - srcRect.top);
            } else if (srcRect.bottom >= ivRect.top && srcRect.bottom <= ivRect.bottom) {
                // 右下角在区域内
                mImageMatrix.postTranslate(ivRect.right - srcRect.right, ivRect.bottom - srcRect.bottom);
            }
        }

        return false;
    }

    class TransRunnable implements Runnable {
        private int lastX, lastY;

        public TransRunnable(int startX, int startY, int endX, int endY) {
            lastX = startX;
            lastY = startY;
            translateScroller.abortAnimation();
            translateScroller.startScroll(startX, startY, endX - startX, endY - startY, 800);
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                mImageMatrix.postTranslate(mScroller.getCurrX() - lastX, mScroller.getCurrY() - lastY);
                lastX = mScroller.getCurrX();
                lastY = mScroller.getCurrY();
                setImageMatrix(mImageMatrix);
                postDelayed(this, 16);
            }
        }
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

    @Override
    protected void onDetachedFromWindow() {
        // should remove all runnables
        super.onDetachedFromWindow();
    }
}