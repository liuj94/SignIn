package com.example.signin.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义圆环进度条
 * @author yubo<br/>
 * 2015年1月23日
 */
public class RoundProgressView extends View {
    private int centerX;//圆环的中心X坐标
    private int centerY;//圆环的中心Y坐标
    private int radius;//圆环的半径
    private Paint paint;//圆环的画笔
    private Paint progressPaint;//圆环中进度的画笔
    private int progress;//当前进度
    private int maxProgress = 100;//最大进度，默认为100

    public RoundProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundProgressView(Context context) {
        super(context);
        init();
    }

    /**创建RoundProgressView时通过该方法初始化*/
    private void init(){
        paint = new Paint();
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#E7E7E7"));//灰色的圆环

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.parseColor("#1082F5"));//蓝色的进度
        progressPaint.setStrokeWidth((float) 30.0);
        progressPaint.setStyle(Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//让画笔画出的线条是圆的
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //计算圆环的圆心坐标
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        //计算圆环的半径，这里取View的长宽中较小值的1/2
        radius = (centerX > centerY ? centerY : centerX) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画出灰色的圆环
        canvas.drawCircle(centerX, centerY, radius, paint);
        //构造圆环的外围矩形
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        //计算显示的进度
        int progressAngle = (progress * 360) / maxProgress;
        //画出进度，注意这里的270表示从圆环的最顶部开始画，如果270替换为0，则是从圆环的右端开始画
        canvas.drawArc(rectF, 270, progressAngle, false, progressPaint);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

}
