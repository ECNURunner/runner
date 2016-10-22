package com.zjut.runner.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Phuylai on 2016/10/23.
 */

public class CircleView extends View {

    private int backGroundColor;
    private Paint paint = new Paint();

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context) {
        super(context);
    }

    public void setCircleColor(int color) {
        if(backGroundColor == color){
            return;
        }
        backGroundColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setColor(backGroundColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2,
                paint);
    }
}

