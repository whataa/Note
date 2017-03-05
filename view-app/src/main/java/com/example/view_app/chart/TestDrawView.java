package com.example.view_app.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.view_app.R;

/**
 * Created by Summer on 2017/3/5.
 */

public class TestDrawView extends View {

    private static final String TAG = TestDrawView.class.getSimpleName();

    public TestDrawView(Context context) {
        this(context, null);
    }

    public TestDrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paintCanvas = new Paint();
        paintCanvas.setColor(Color.GRAY);
        paintRed = new Paint();
        paintRed.setColor(Color.RED);
        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintText = new Paint();
        paintText.setTextSize(40);
        paintText.setColor(Color.DKGRAY);
        matrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = simple; // 宽高各自直接缩小1倍
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sticker2, options);
        srcRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Log.d(TAG, "TestDrawView: " + srcRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultW = bitmap.getWidth()*simple + drawablePadding + Math.round(paintText.measureText(tips) + 0.5f)
                + getPaddingLeft() + getPaddingRight();
        int defaultH = Math.max(bitmap.getHeight()*simple, getTextHeight(80))
                + getPaddingTop() + getPaddingBottom();
        int curWidth = resolveSizeAndState(defaultW, widthMeasureSpec, 0);
        int curHeight = resolveSizeAndState(defaultH, heightMeasureSpec, 0);
        setMeasuredDimension(curWidth, curHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasRect = new RectF(getPaddingLeft(), getPaddingTop(),
                getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        desRect = new RectF(getPaddingLeft(), getPaddingTop(),
                getPaddingLeft() + (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / simple,
                getPaddingTop() + (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / simple);

        textRect = new RectF(desRect.right, desRect.top, canvasRect.right, desRect.bottom);
    }

    private int simple = 2;
    private Bitmap bitmap;
    private Matrix matrix;
    private RectF canvasRect, srcRect, desRect, textRect;
    private Paint paintCanvas, paintRed, paintGreen, paintText;
    private String tips = "Tips";
    private int drawablePadding = 48;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ////辅助框和线//////////////////////////////////////////////////////////////////////////////////
        canvas.drawRect(canvasRect, paintCanvas);
        canvas.drawRect(desRect, paintText);
        canvas.drawLine(0, getMeasuredHeight()/2, getMeasuredWidth(), getMeasuredHeight()/2, paintRed);
        canvas.drawLine(getMeasuredWidth()/2, 0, getMeasuredWidth()/2, getMeasuredHeight(), paintRed);
        canvas.drawLine(canvasRect.left, canvasRect.centerY(),
                canvasRect.right, canvasRect.centerY(), paintGreen);
        canvas.drawLine(canvasRect.centerX(), canvasRect.top,
                canvasRect.centerX(), canvasRect.bottom, paintGreen);
        ////////////////////////////////////////////////////////////////////////////////////////////


        if (bitmap != null && !bitmap.isRecycled()) {
            matrix.setRectToRect(srcRect, desRect, Matrix.ScaleToFit.END);
            canvas.drawBitmap(bitmap, matrix, null);
        }
        canvas.drawText(tips, textRect.left + drawablePadding, calcTextSuitBaseY(textRect), paintText);
    }

    private float calcTextSuitBaseY(RectF rectF) {
        return rectF.top + rectF.height() / 2 -
                (paintText.getFontMetrics().ascent + paintText.getFontMetrics().descent) / 2;
    }

    public int getTextHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }
}
