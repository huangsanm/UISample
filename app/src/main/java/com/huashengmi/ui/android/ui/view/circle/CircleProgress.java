package com.huashengmi.ui.android.ui.view.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huangsm on 2014/10/10 0010.
 * Email:huangsanm@foxmail.com
 */
public class CircleProgress extends View {


    private CircleAttribute mCircleAttribute;
    private int mMaxProgress = 100;
    private int mProgress = 60;
    private float mAngle;
    private float mDrawAngle;

    public CircleProgress(Context context) {
        super(context);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup(){
        mCircleAttribute = new CircleAttribute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String textStr = ((int) ((80 * 1f / 100) * 100)) + "%";
        Rect textBounds = new Rect();
        mCircleAttribute.mTextPaint.getTextBounds(textStr, 0, textStr.length(), textBounds);
        //int textHeight = textBounds.bottom - textBounds.top;

        canvas.drawArc(mCircleAttribute.mOutSideRectF, 0.0F, 360.0F, true, mCircleAttribute.mOutSidePaint);
        canvas.drawArc(mCircleAttribute.mOutSideRectF, 90, mDrawAngle, true, mCircleAttribute.mSubPaint);
        canvas.drawArc(mCircleAttribute.mInSideRectF, 0.0F, 360.0F, true, mCircleAttribute.mInSidePaint);
        //canvas.drawText(textStr, mCircleAttribute.mOutSideRectF.centerX() - textBounds.width() / 2, mCircleAttribute.mOutSideRectF.centerY() + textHeight/2, mCircleAttribute.mTextPaint);
    }

    public void setTotalProgress(int totalProgress, int progress){
        mMaxProgress = totalProgress;
        mProgress = progress;
        mAngle = 360 * mProgress / mMaxProgress;
        invalidate();
    }

    public void startDraw(){
        mDrawAngle = 1;
        mHandler.sendEmptyMessage(0);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(mDrawAngle <= mAngle){
                        mDrawAngle += 1.5;
                        mHandler.sendEmptyMessage(0);
                    }
                    break;
            }
            invalidate();
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = View.MeasureSpec.getSize(widthMeasureSpec);
        View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSize(i, widthMeasureSpec), resolveSize(i, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleAttribute.bindSize(w, h);
    }

    class CircleAttribute {

        private RectF mOutSideRectF;
        private Paint mOutSidePaint;
        private int mPaintWidth = 0;
        private int mOutSideColor = Color.parseColor("#000000");
        public Paint mSubPaint;

        private RectF mInSideRectF;
        public int mSidePaintInterval = 50;
        private Paint mInSidePaint;

        private Paint mTextPaint;

        public CircleAttribute(){
            mOutSideRectF = new RectF();

            mOutSidePaint = new Paint();
            mOutSidePaint.setAntiAlias(true);
            mOutSidePaint.setStyle(Paint.Style.FILL);
            mOutSidePaint.setStrokeWidth(mPaintWidth);
            mOutSidePaint.setColor(mOutSideColor);

            mSubPaint = new Paint();
            mSubPaint.setAntiAlias(true);
            mSubPaint.setStyle(Paint.Style.FILL);
            mSubPaint.setStrokeWidth(mPaintWidth);
            mSubPaint.setColor(Color.parseColor("#3f9eee"));

            mInSideRectF = new RectF();

            mInSidePaint = new Paint();
            mInSidePaint.setAntiAlias(true);
            mInSidePaint.setStyle(Paint.Style.FILL);
            mInSidePaint.setStrokeWidth(this.mPaintWidth);
            mInSidePaint.setColor(Color.WHITE);

            mTextPaint = new Paint();
            mTextPaint.setColor(Color.parseColor("#F5F5F5"));
            mTextPaint.setTextSize(55);

        }

        public void bindSize(int width, int height) {
            mInSideRectF.set(mPaintWidth / 2 + mSidePaintInterval, mPaintWidth / 2 + mSidePaintInterval, width - mPaintWidth / 2 - mSidePaintInterval, height - mPaintWidth / 2 - mSidePaintInterval);
            int left = getPaddingLeft();
            int right = getPaddingRight();
            int top = getPaddingTop();
            int bottom = getPaddingBottom();
            mOutSideRectF.set(left + mPaintWidth / 2, top + mPaintWidth / 2, width - right - mPaintWidth / 2, height - bottom - mPaintWidth / 2);
        }
    }


}
