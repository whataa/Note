package com.example.view_app.chart;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;


public class TestView extends View {
    // 最大值，纵坐标
    private final float maxValue = 100f;
    // 测试数据
//    private float[] testDatas = { 55f, 38f, 100f, 44f, 31f, 22f, 9f, 19f, 50f, 78f, 62f, 51f, 45f, 66f, 79f, 50f, 33f,
//            24f, 26f, 100f };
    private float[] testDatas = { 60f, 30f, 57f, 41f, 88f, 70f, 100f };
    //private float[] testDatas = { 60f, 55f};

    // 点记录
    private List<Point> datas;
    // 路径
    private Path clicPath;
    // 渐变填充
    private Paint mPaint;
    // 辅助参考点
    private Paint originPaint;
    // 辅助性画笔
    private Paint controllPaintA;
    private Paint controllPaintB;
    private Path linePath;

    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private float[] mPrePosition = new float[2];
    LinearGradient mGradient;
    int realWidth;
    int realHeight;
    int offSet;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        clicPath = new Path();
        linePath = new Path();
        datas = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        originPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        originPaint.setColor(0xff000000);
        //mPaint.setStyle(Paint.Style.STROKE);
        controllPaintA = new Paint(Paint.ANTI_ALIAS_FLAG);
        controllPaintA.setStyle(Paint.Style.STROKE);
        controllPaintA.setStrokeWidth(5);
        controllPaintA.setColor(0xffff0000);

        controllPaintB = new Paint(Paint.ANTI_ALIAS_FLAG);
        controllPaintB.setStyle(Paint.Style.STROKE);
        controllPaintB.setColor(0xff00ff00);
    }

    // 右移1位，表示除以2
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int curWidth = resolveSizeAndState((int) (2f * maxValue), widthMeasureSpec, 0);
        int curHeight = resolveSizeAndState((int) (1f * maxValue), heightMeasureSpec, 0);
        setMeasuredDimension(curWidth, curHeight);

        realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        realHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        offSet = realWidth / (testDatas.length - 1);
        if (datas.size() > 0) {
            datas.clear();
        }
        for (int i = 0; i < testDatas.length; i++) {
            float ratio = testDatas[i] / maxValue;
            Point point = new Point(i * offSet + getPaddingLeft(), (int) (realHeight * (1 - ratio)));
            datas.add(point);
        }
//        if (mGradient == null) {
            mGradient = new LinearGradient(getMeasuredWidth() >> 1, getMeasuredHeight() >> 1, getMeasuredWidth() >> 1,
                    getMeasuredHeight(), Color.parseColor("#e0cab3"), Color.parseColor("#ffffff"),
                    Shader.TileMode.CLAMP);
            mPaint.setShader(mGradient);
//        }
    }

    private boolean animaFirst = true;
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("onDraw: ", "onDraw");
        clicPath.reset();
        super.onDraw(canvas);
        for (int i = 0; i < datas.size() - 1; i++) {
            Point startPoint = datas.get(i);
            Point endPoint = datas.get(i + 1);
            if (i == 0) clicPath.moveTo(startPoint.x, startPoint.y);

            int controllA_X = (startPoint.x + endPoint.x) >> 1;
            int controllA_Y = startPoint.y;
            int controllB_X = (startPoint.x + endPoint.x) >> 1;
            int controllB_Y = endPoint.y;
            clicPath.cubicTo(controllA_X, controllA_Y, controllB_X, controllB_Y, endPoint.x, endPoint.y);
            // 辅助点和线===============================================================
//            canvas.drawCircle(controllA_X,controllA_Y,5,controllPaintA);
//            canvas.drawCircle(controllB_X,controllB_Y,5,controllPaintB);
//
//            canvas.drawCircle(startPoint.x,startPoint.y,5,originPaint);
//
//            canvas.drawLine(startPoint.x,startPoint.y,controllA_X,controllA_Y,originPaint);
//            canvas.drawLine(endPoint.x,endPoint.y,controllB_X,controllB_Y,originPaint);
            //=========================================================================

        }
        // 封闭路径================================================================
        clicPath.lineTo(datas.get(datas.size() - 1).x, realHeight);
        clicPath.lineTo(datas.get(0).x, realHeight);
        clicPath.lineTo(datas.get(0).x, datas.get(0).y);
        //=========================================================================

        canvas.drawPath(clicPath, mPaint);

        if (animaFirst) {
            linePath.moveTo(datas.get(0).x, datas.get(0).y);
            mCurrentPosition[0] = datas.get(0).x;
            mCurrentPosition[1] = datas.get(0).y;
            animaFirst = false;
        }
        else {
            linePath.lineTo(mCurrentPosition[0], mCurrentPosition[1]);
        }
        canvas.drawPath(linePath, controllPaintA);
    }
    // TODO: http://www.jianshu.com/p/c0d7ad796cee
    private ValueAnimator valueAnimator;
    public void startAnima(long duration) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            return;
        }
        mPathMeasure = new PathMeasure(clicPath, true);

        final List<PointF> points = new ArrayList<>();
        for (Point p : datas) {
            points.add(new PointF(p.x,p.y));
        }

        valueAnimator = ValueAnimator.ofObject(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float time, PointF start, PointF end) {
                float timeLeft = 1.0f - time;
                float x = timeLeft * timeLeft * timeLeft * (start.x) + 3 * timeLeft * timeLeft * time *
                        (points.get(0).x) + 3 * timeLeft * time *
                        time * (points.get(points.size()-1).x) + time * time * time * (end.x);
                float y = timeLeft * timeLeft * timeLeft * (start.y) + 3 * timeLeft * timeLeft * time *
                        (points.get(0).y) + 3 * timeLeft * time *
                        time * (points.get(points.size()-1).y) + time * time * time * (end.y);
                return new PointF(x,y);
            }
        }, points);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF value = (PointF) animation.getAnimatedValue();
                mCurrentPosition[0] = value.x;
                mCurrentPosition[1] = value.y;
                invalidate();
                if (value.equals(points.get(points.size()-1))) {
                    valueAnimator.cancel();
                    animaFirst = true;
                }
            }
        });
        valueAnimator.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        startAnima(2000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        datas.clear();
        clicPath = null;
        controllPaintA = null;
        controllPaintB = null;
        mPathMeasure = null;
    }
}