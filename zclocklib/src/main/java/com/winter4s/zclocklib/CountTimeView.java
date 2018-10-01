package com.winter4s.zclocklib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;



public class CountTimeView extends View {

    private Paint paint_Circle;
    private Paint paint_Progress;
    private Paint paint_Time;

    private int color_Circle;
    private int color_Progress;
    private int color_Time;

    private int time;
    private int now;
    private static float progress;

    private RectF rect;

    public interface OnFinishedListener {
        void finished();
    }

    public interface OnCountListener{
        void count(int time);
    }

    private OnFinishedListener onFinishedListener;

    private OnCountListener onCountListener;


    public CountTimeView(Context context) {
        super(context);
        init();
        now=time;
    }

    public CountTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountTimeView);
        time = a.getInteger(R.styleable.CountTimeView_time, 4);
        a.recycle();
        init();
    }

    public CountTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountTimeView);
        time = a.getInteger(R.styleable.CountTimeView_time, 4);
        color_Circle=a.getColor(R.styleable.CountTimeView_color_circle,Color.parseColor("#ffffff"));
        color_Progress=a.getColor(R.styleable.CountTimeView_color_progress,Color.parseColor("#ffffff"));
        color_Time=a.getColor(R.styleable.CountTimeView_color_text,Color.parseColor("#ffffff"));
        a.recycle();
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        init();

        canvas.drawCircle(getWidth()/2,getHeight()/2,(int)getWidth()/2-8,paint_Circle);
        canvas.drawArc(rect,-90,progress,false,paint_Progress);
        canvas.drawText(secondToTime(now), getWidth() / 2, getHeight() / 2, paint_Time);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rect=new RectF(8,8,getMeasuredWidth()-8,getMeasuredHeight()-8);
    }

    private void init(){
        paint_Circle = new Paint();
        paint_Circle.setAntiAlias(true);
        paint_Circle.setColor(Color.parseColor("#88ffffff"));
        paint_Circle.setStyle(Paint.Style.STROKE);
        paint_Circle.setStrokeCap(Paint.Cap.ROUND);
        paint_Circle.setStrokeWidth(8);

        paint_Progress = new Paint();
        paint_Progress.setAntiAlias(true);
        paint_Progress.setColor(Color.parseColor("#ffdd00"));
        paint_Progress.setStyle(Paint.Style.STROKE);
        paint_Progress.setStrokeCap(Paint.Cap.ROUND);
        paint_Progress.setStrokeWidth(12);

        paint_Time = new Paint();
        paint_Time.setAntiAlias(true);
        paint_Time.setColor(Color.parseColor("#ffffff"));
        paint_Time.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_Time.setTextAlign(Paint.Align.CENTER);
        paint_Time.setStrokeCap(Paint.Cap.ROUND);
        paint_Time.setStrokeWidth(0.1f);
        paint_Time.setTextSize(80);


    }

    public void start(){
        now=time;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(time*1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                now= time-(int) (time*i/100f);
                progress=((float) 360 * ((float) i / 100f));
                //onCountListener.count((int) progress);
                invalidate();
            }
        });


        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onFinishedListener.finished();
            }
        });

        valueAnimator.start();


    }

    public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }

    public void setOnCountListener(OnCountListener onCountListener) {
        this.onCountListener = onCountListener;
    }

    public static String secondToTime(long second){
        long days = second / 86400;            //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;            //转换小时
        second = second % 3600;                //剩余秒数
        long minutes = second /60;            //转换分钟
        second = second % 60;                //剩余秒数
        if(days>0){
            return days + "天" + hours + "小时" + minutes + "分" + second + "秒";
        }else if(hours>0){
            return hours + "小时" + minutes + "分" + second + "秒";
        }else if(minutes>0) {
            if(minutes<10){
                if(second<10){
                    return "0"+minutes+":0"+second;
                }else {
                    return "0"+minutes+":"+second;
                }
            } else {
                if(second<10){
                    return minutes+":0"+second;
                }else {
                    return minutes+":"+second;
                }
            }
        }else {
            if(second<10){
                return "00:0"+second;
            }
            return "00:"+second;
        }
    }

}
