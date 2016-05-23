package edu.hsl.hollekeiti.phone.view;

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
 * Created by Administrator on 2016/05/09.
 */
public class PiechartView extends View {
    private Paint mPaint;
    private RectF oval;//圆形大小
    private float proportionPhone = 0;//手机总空间所占比例
    private float proportionSD;//存储卡所占比例
    private float piecharAnglePhone = 0;//手机空间角度
    private float piecharAngleSD    = 0;//存储卡空间角度
    private int   phoneColor        = 0;//手机空间饼形图颜色
    private int   sdColor           = 0;//存储卡颜色
    private int   baseColor         = 0;

    public PiechartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        phoneColor = context.getResources().getColor(R.color.piechar_phone);
        sdColor = context.getResources().getColor(R.color.piechar_sdcard);
        baseColor = context.getResources().getColor(R.color.piechar_base);
    }

    /**
     * 设置手机饼形图
     * f1 手机
     * f2 SD卡
     */
    public void setPiecharProportion(float f1, float f2) {
        proportionPhone = f1;
        proportionSD = f2;
        piecharAnglePhone = f1 * 360;
        piecharAngleSD = f2 * 360;
        postInvalidate();
    }

    public void setPiecharProportionWithAnim(float f1, float f2) {
        proportionPhone = f1;
        proportionSD = f2;
        final float phoneTargetAngle  = 360 * proportionPhone;
        final float sdcardTargetAngle = 360 * proportionSD;
        final Timer timer             = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                piecharAnglePhone += 5;
                piecharAngleSD += 5;
                postInvalidate();
                if (piecharAnglePhone >= phoneTargetAngle) {
                    piecharAnglePhone = phoneTargetAngle;
                }
                if (piecharAngleSD >= sdcardTargetAngle) {
                    piecharAngleSD = sdcardTargetAngle;
                }
                if (piecharAnglePhone == phoneTargetAngle && piecharAngleSD == sdcardTargetAngle) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 26, 26);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWith   = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        oval = new RectF(0, 0, viewWith, viewHeight);
        setMeasuredDimension(viewWith, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(baseColor);
        canvas.drawArc(oval, -90, 360, true, mPaint);
        mPaint.setColor(phoneColor);
        canvas.drawArc(oval, -90, piecharAnglePhone, true, mPaint);
        mPaint.setColor(sdColor);
        canvas.drawArc(oval, -90 + piecharAnglePhone, piecharAngleSD, true, mPaint);
    }
}
