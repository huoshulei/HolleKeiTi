package edu.hsl.hollekeiti.phone.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import edu.hsl.hollekeiti.R;

/**
 * Created by Administrator on 2016/05/06.
 */
public class ClearArcView extends View {
    public static final int INT = 90;
    private Paint mPaint = new Paint();
    private RectF oval;
    private int   sweepAngle = 0;
    private int   state      = 0;// 0: 回退 1:前进
    private int   arcColor   = 0xffff8c00;
    private int[] back       = {-6, -6, -10, -10, -10, -12};
    private int backidx;
    private int[] goon = {12, 12, 12, 12, 10, 10, 10, 8, 8, 8, 6};
    private int goonIdx;
    private boolean isRuning = false;

    public ClearArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAngle(360);
        arcColor = context.getResources().getColor(R.color.clear_arc_color);
    }

    public void setAngle(int angle) {
        sweepAngle = angle;
        postInvalidate();
        isRuning = false;
    }

    /**
     * 设置圆形大小
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewWidth  = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        oval = new RectF(0, 0, viewWidth, viewHeight);
        setMeasuredDimension(viewWidth, viewHeight);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setAngleWithAnim(final int angle) {
        if (isRuning) {
            return;
        }
        isRuning = true;
        state = 0;
        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                switch (state) {
                    case 0:
                        sweepAngle += back[backidx++];
                        if (backidx >= back.length) {
                            backidx = back.length - 1;
                        }
                        postInvalidate();
                        if (sweepAngle <= 0) {
                            sweepAngle = 0;
                            state=1;
                            backidx=0;
                        }
                        break;
                    case 1:
                        sweepAngle+=goon[goonIdx++];
                        if (goonIdx>=goon.length){
                            goonIdx=goon.length-1;
                        }
                        postInvalidate();
                        if (sweepAngle>=angle){
                            sweepAngle=angle;
                            timer.cancel();
                            goonIdx=0;
                            isRuning=false;
                        }
                        break;
                }
            }
        };
        timer.schedule(timerTask, 24, 24);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(arcColor);
        mPaint.setAntiAlias(true);
        canvas.drawArc(oval, INT,sweepAngle,true,mPaint);
    }
}
