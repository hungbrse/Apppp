package com.example.sensorgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    private int mX = 0;
    private int mY = 0;
    private int mRadius = 0;
    private Paint mBallPaint ;

    public Ball() {
       mBallPaint = new Paint();
       mBallPaint.setColor(Color.RED);
       mBallPaint.setAntiAlias(true);
    }
    public void setPosition (int x , int y) {
       this.mX = x;
        this.mY = y;

    }

    public int getRadius() {
        return mRadius;
    }

    public void setmRadius(int r) {
        mRadius = r;
    }

    public int getmX() {
        return mX;
    }

    public int getmY() {
        return mY;
    }
    public void draw(Canvas canvas ){
        canvas.drawCircle(mX,mY,mRadius,mBallPaint);

    }
    public void move(int dx, int dy) {
        mX += dx;
        mY += dy;
    }
}
