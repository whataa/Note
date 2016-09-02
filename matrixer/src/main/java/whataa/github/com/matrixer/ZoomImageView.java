package whataa.github.com.matrixer;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import static whataa.github.com.matrixer.Utils.L;


public class ZoomImageView extends ImageView {
    private static final String TAG = ZoomImageView.class.getSimpleName();
    /**
     * 缩放、平移、旋转计算器
     */
    private ValueAnimator scaleAnimator;
    private ValueAnimator transAnimator;
    private ValueAnimator rotateAnimator;

    private ScaleGestureDetector mScaleDetector;
    private VelocityTracker mVelocityTracker;
    private Matrix mImageMatrix = new Matrix();
    private int mLastAngle = 0;

    /**
     * 最大最小缩放比
     */
    private static final float SCALE_MAX = 6f;
    private static final float SCALE_MIN = 0.6f;
    /**
     * 可以Fling的最小临界速度
     */
    private float mMinFlingVelocity;
    /**
     * VelocityTracker最大临界速度，px/s
     */
    private static final long VELO_MAX = 20000L;

    /**
     * 视图中心坐标
     */
    private int mPivotX, mPivotY;
    /**
     * 当前追踪的手指的坐标
     */
    private float mLastX, mLastY;
    /**
     * 自动缩放、旋转的焦点坐标
     */
    private float mFocusX, mFocusY;

    /**
     * 是否已初始化大小和位置
     */
    private boolean isNoReset;


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
        mMinFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();
        mScaleDetector = new ScaleGestureDetector(context, mScaleListener);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isNoReset) {
            isNoReset = false;
            cropToCenter();
            translateToCenter();
        }
    }



    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        isNoReset = true;
    }

    private Paint paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(mFocusX, mFocusY,36f, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 事件将以ID最小的作为追踪手指，特别是手指数量发生变化时的情况需要注意。
         * 否则会因为lastX、lastY引发开始MOVE时图片瞬间跳动的问题。
         */
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "MotionEvent.ACTION_DOWN:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()));
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            // ID从0开始自增，若0没有则补全0，以此类推。
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG, "MotionEvent.ACTION_POINTER_DOWN:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()));
                mLastX = event.getX(0);
                mLastY = event.getY(0);
                break;
            // 在POINTER_UP后的事件将ID最小的作为追踪手指，但是当前事件下，手指数依然为UP前的个数，所以不能直接getX(0)
            case MotionEvent.ACTION_POINTER_UP:
                int minID = event.getPointerId(0);
                int inX = 0, inY = 0;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    inX += event.getX(i);
                    inY += event.getY(i);
                    if (event.getPointerId(i) <= minID) {
                        minID = event.getPointerId(i);
                    }
                }
                if (event.getPointerId(event.getActionIndex()) == minID) {
                    minID = event.getPointerId(event.getActionIndex()+1);
                }
                Log.e(TAG, "MotionEvent.ACTION_POINTER_UP:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex())+" " + minID);
                mLastX = event.getX(event.findPointerIndex(minID));
                mLastY = event.getY(event.findPointerIndex(minID));
                mFocusX = inX / event.getPointerCount();
                mFocusY = inY / event.getPointerCount();

                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                Log.e(TAG, "MotionEvent.ACTION_MOVE:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()));
                // 当处于缩放模式时（多手指），由于未调用平移来更新lastX，lastY记录，所以当退出缩放模式开始平移时（多手指变单手指），会出现跳动问题。
                if (mScaleDetector.isInProgress()) {
                    mLastX = event.getX(0);
                    mLastY = event.getY(0);
                } else {
                    doTranslate(event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                // not break but continue to ACTION_UP.

            case MotionEvent.ACTION_UP:
                Log.e(TAG, "MotionEvent.ACTION_UP:" + event.getActionIndex() + " " + event.getPointerId(event.getActionIndex()));
                float curScale = getCurrentScale();
                if (curScale > SCALE_MAX) {
                    autoScale(curScale, SCALE_MAX, mFocusX, mFocusY);
                } else if (curScale < SCALE_MIN) {
                    autoScale(curScale, SCALE_MIN, mFocusX, mFocusY);
                }
                mVelocityTracker.computeCurrentVelocity(1000, VELO_MAX);
                float xVel = mVelocityTracker.getXVelocity();
                float yVel = mVelocityTracker.getYVelocity();
                Log.e(TAG, "Velocity:"+mMinFlingVelocity+" "+xVel+" "+yVel);
                if (xVel >= mMinFlingVelocity || yVel >= mMinFlingVelocity) {
                    autoTranlate(event.getX(), event.getY(), xVel / 100f, xVel / 100f);
                }
                break;
        }
        obtainVelocityTracker(event);
        // mScaleDetector需要接收完整的手势事件DOWN/MOVE/UP，否则流程异常。
        mScaleDetector.onTouchEvent(event);
        return true;
    }

    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.e(TAG, "onScaleBegin:" + detector.getScaleFactor());
            // 必须返回true，才可以进入onScale
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.e(TAG, "onScale:" + detector.getScaleFactor());
            float scaleFactor = detector.getScaleFactor();
            mImageMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            setImageMatrix(mImageMatrix);

            // 返回true每次进入缩放模式时重置scaleFactor，否则累加
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            // 仅当参与缩放的所有手指都UP后
            super.onScaleEnd(detector);
        }
    };

    private float getCurrentScale() {
        float values[] = new float[9];
        mImageMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }




    /**
     * should be called after onMeasure.
     */
    private void translateToCenter() {
        if (getDrawable() == null) return;

        float baseScase = getCurrentScale();
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
            mFocusX = mPivotX = w / 2;
            mFocusY = mPivotY = h / 2;
        }
    }




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
        // 度° = atan(斜率k)
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





    private boolean doTranslate(MotionEvent event) {
        if ((event.getAction() & event.getActionMasked()) == MotionEvent.ACTION_MOVE) {
            int dx = (int) (event.getX() - mLastX);
            int dy = (int) (event.getY() - mLastY);
            mLastX = event.getX();
            mLastY = event.getY();
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





    private void autoScale(float startScale, float endScale, final float pX, final float pY) {
        if (scaleAnimator!=null && scaleAnimator.isRunning()) {
            scaleAnimator.cancel();
            scaleAnimator.removeAllUpdateListeners();
        }
        scaleAnimator = ValueAnimator.ofFloat(startScale, endScale);
        scaleAnimator.setInterpolator(new DecelerateInterpolator());
        scaleAnimator.setDuration(600);//default is 300
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = (float) valueAnimator.getAnimatedValue();
                scale = scale / getCurrentScale();
                mImageMatrix.postScale(scale, scale, pX, pY);
                setImageMatrix(mImageMatrix);
                invalidate();
            }
        });
        scaleAnimator.start();
    }

    private void autoTranlate(final float startX, final float startY, float endX, float endY, long duration) {
        if (transAnimator!=null && transAnimator.isRunning()) {
            transAnimator.cancel();
            transAnimator.removeAllUpdateListeners();
        }
        transAnimator = ValueAnimator.ofObject(new CompatPointFEvaluator(),new PointF(startX,startY), new PointF(endX, endY));
        transAnimator.setInterpolator(new DecelerateInterpolator());
        transAnimator.setDuration(duration);//default is 300
        transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float lastX = startX, lastY = startY;
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                PointF middleValue = (PointF) valueAnimator.getAnimatedValue();
//                mImageMatrix.postTranslate(middleValue.x - lastX, middleValue.y - lastY);
//                lastX = middleValue.x;
//                lastY = middleValue.y;
//                Log.e(TAG, "onAnimationUpdate "+lastX+" "+lastY);
//                setImageMatrix(mImageMatrix);
            }
        });
        transAnimator.start();
    }

    private void autoTranlate(final float startX, final float startY, float xVelocity, float yVelocity) {
        float endX = startX + 0.6f * xVelocity;
        float endY = startY + 0.6f * yVelocity;
        autoTranlate(startX, startY, endX, endY, 600);
    }

    private void autoRotate(final float startAngle, float endAngle) {
        if (rotateAnimator!=null && rotateAnimator.isRunning()) {
            rotateAnimator.cancel();
            rotateAnimator.removeAllUpdateListeners();
        }
        rotateAnimator = ValueAnimator.ofFloat(startAngle, endAngle);
        rotateAnimator.setInterpolator(new DecelerateInterpolator());
        rotateAnimator.setDuration(600);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float lastAngle = startAngle;
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float angle = (float) valueAnimator.getAnimatedValue();
                mImageMatrix.postRotate(angle - lastAngle, mFocusX, mFocusY);
                lastAngle = angle;
                setImageMatrix(mImageMatrix);
            }
        });
        rotateAnimator.start();
    }

    static class CompatPointFEvaluator implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            PointF middleValue = new PointF();
            middleValue.x = (endValue.x - startValue.x) * fraction;
            middleValue.y = (endValue.y - startValue.y) * fraction;
            return middleValue;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // should remove all runnables
        if (scaleAnimator != null) {
            scaleAnimator.cancel();
            scaleAnimator.removeAllUpdateListeners();
        }
        releaseVelocityTracker();
        super.onDetachedFromWindow();
    }
    private void obtainVelocityTracker( MotionEvent event) {
        if(null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    private void releaseVelocityTracker() {
        if(null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}