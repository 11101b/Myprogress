package com.example.myprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class MyProgress extends View {
    private Context context;
    private Drawable thumb;
    private Paint mPaint;
    private int myWidth;
    private int myHeight;
    private int index = 0;
    private int mSpecp = 0;
    private int X;
    private int Y;
    private int mPWidth;
    private int allWidth;
    private int myOffset;
    private int mIndex;
    private int LineHight = 30;
    private int[] fontSize = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public MyProgress(Context context) {
        this(context, null);
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgress(Context context, @Nullable AttributeSet attrs
            , int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        thumb = context.getResources().getDrawable(R.mipmap.thumb);
        mPaint = new Paint();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            myWidth = 500;
        } else {
            myWidth = width;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            myHeight = 50;
        } else {
            myHeight = height;
        }

        setParentWidth(myWidth);
        setMeasuredDimension(myWidth, myHeight);
    }

    public void setParentWidth(int width) {
        mPWidth = width;
    }

    float mLastX = 0;
    int mFontIndex = 0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        float x = event.getX();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float specX = x - mLastX;
            move(specX);
            mLastX = x;
            mFontIndex = addCenter(x);

        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setCenter(x);
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            addCenter(x);
            getFontSize(mFontIndex);

        }
        return true;
    }



    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        int hight = 8;
        int width = 4;
        for (index = 0; index < 10; index++) {
            mPaint.setColor(mIndex > index ? Color.RED : Color.BLUE);
            canvas.drawRect(mSpecp * index + myOffset, (myHeight - LineHight) / 2,
                    mSpecp * index + width + myOffset,
                    ((myHeight - LineHight) / 2 + LineHight / 2), mPaint);
            if (index != 9) {
                float length = index != 8 ? mSpecp * (index + 1) + myOffset : mSpecp * (index + 1) + myOffset + width;
                canvas.drawRect(mSpecp * index + myOffset, (myHeight - LineHight) / 2 + LineHight / 2,
                        length,
                        (myHeight - LineHight) / 2 + LineHight / 2 + hight, mPaint);
            }

        }
        thumb.setBounds(X - myHeight / 2 + 5, Y - myHeight / 2 + 5,
                X + myHeight / 2 - 5, Y + myHeight / 2 - 5);
        thumb.draw(canvas);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        allWidth = myWidth - getPaddingLeft() - getPaddingRight();

        myOffset = getPaddingLeft();
        if (myOffset <= 5) {
            myOffset = 10;
        }

        mSpecp = allWidth / (fontSize.length - 1);

        X = mSpecp * mIndex + myOffset;
        Y = (myHeight - 30) / 2 + 15;
    }


    public void setIndex(int index) {
        for (int i = 0; index < fontSize.length; index++) {
            if (index == fontSize[i]) {
                mIndex = i;
                break;
            }
        }
        X = mSpecp * mIndex + myOffset;
        invalidate();
    }



    public void setCenter(float center) {
        center = center - (mPWidth - myWidth) / 2;
        X = (int) (center);
    }

    public int addCenter(float local) {
        local = local - (mPWidth - myWidth) / 2;
        mIndex = (int) local / mSpecp;
        if (mIndex <= 0) {
            mIndex = 0;
        }
        if (mIndex >= fontSize.length-1) {
            mIndex =  fontSize.length-1;
        }
        X = mSpecp * mIndex + myOffset;
        invalidate();
        return mIndex;
    }

    public int getFontSize(int index) {
        return fontSize[index];
    }

    public int getIndex() {
        return mIndex;
    }


    public void move(float spec) {

        X += spec;
        if (X <= myOffset) {
            X = myOffset;
        }
        if (X >= myWidth - myOffset) {
            X = myWidth - myOffset;
        }
    }

}
