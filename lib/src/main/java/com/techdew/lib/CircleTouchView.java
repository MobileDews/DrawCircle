package com.techdew.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by techdew-007 on 19/12/16.
 */

public class CircleTouchView extends View {
    private static final double MOVE_SENSITIVITY = 1.25;
    private Paint circlePaint;
    private boolean isPinchMode;
    private int lastCircleX;
    private int lastCircleY;
    public Circle circle;
    private boolean isDoneResizing = true;
    public Float radius;
    private Paint boarberPaint;
    private Paint shadowpaint;
    int circle_color;
    int stroke_color;
    OnCircleTouchListener onCircleTouchListener;

    public CircleTouchView(Context context) {
        super(context);
        setCirclePaint(Color.parseColor("#ffffff"));
    }

    public CircleTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCustomWidget);
        circle_color = a.getResourceId(R.styleable.MyCustomWidget_circle_color, -1);
        stroke_color= a.getResourceId(R.styleable.MyCustomWidget_stroke_color, -1);

        setCirclePaint(ContextCompat.getColor(context,circle_color));
    }

    public CircleTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCustomWidget);
        circle_color = a.getResourceId(R.styleable.MyCustomWidget_circle_color, -1);
        stroke_color= a.getResourceId(R.styleable.MyCustomWidget_stroke_color, -1);


        setCirclePaint(ContextCompat.getColor(context,circle_color));

    }

    private void setCirclePaint(int color) {
        circle = new Circle();
        circlePaint = new Paint();
        circlePaint.setColor(color);
        boarberPaint = new Paint();
        boarberPaint.setColor(ContextCompat.getColor(getContext(), stroke_color));
        boarberPaint.setStrokeWidth(5);
        boarberPaint.setAlpha(255);
        boarberPaint.setAntiAlias(true);
        boarberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        boarberPaint.setPathEffect(new DashPathEffect(new float[]{5,5},50));

        shadowpaint = new Paint();
        shadowpaint.setColor(ContextCompat.getColor(getContext(), circle_color));
        shadowpaint.setStrokeWidth(20);
        shadowpaint.setAlpha(35);
        shadowpaint.setAntiAlias(true);
        shadowpaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(circle.centerX, circle.centerY, circle.radius, boarberPaint);
        canvas.drawCircle(circle.centerX, circle.centerY, circle.radius, shadowpaint);
        canvas.drawCircle(circle.centerX, circle.centerY, circle.radius, circlePaint);

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        int historySize;
        double lastDistance;
        double oneBeforeLastDistance;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastCircleX = circle.centerX;
                lastCircleY = circle.centerY;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                isPinchMode = true;
                isDoneResizing = false;
                break;

            case MotionEvent.ACTION_MOVE:
                circle.centerX = lastCircleX;
                circle.centerY = lastCircleY;
                ;

                if (getTouchedCircle((int) event.getX(), (int) event.getY()) && !isPinchMode && isDoneResizing) {

                    historySize = event.getHistorySize();
                    if (historySize > 0) {

                        oneBeforeLastDistance = Math.sqrt((event.getX() - event.getHistoricalX(0, historySize - 1)) *
                                (event.getX() - event.getHistoricalX(0, historySize - 1)) +
                                (event.getY() - event.getHistoricalY(0, historySize - 1)) *
                                        (event.getY() - event.getHistoricalY(0, historySize - 1)));


                        if (oneBeforeLastDistance > MOVE_SENSITIVITY) {
                            circle.centerX = (int) event.getX();
                            circle.centerY = (int) event.getY();
                            lastCircleX = circle.centerX;
                            lastCircleY = circle.centerY;

                        }
                    }
                }
                if (isPinchMode) {
                    circle.centerX = lastCircleX;
                    circle.centerY = lastCircleY;

                    historySize = event.getHistorySize();
                    if (historySize > 0) {

                        lastDistance = Math.sqrt((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1)) +
                                (event.getY(0) - event.getY(1)) * (event.getY(0) - event.getY(1)));

                        oneBeforeLastDistance = Math.sqrt((event.getHistoricalX(0, historySize - 1) - event.getHistoricalX(1, historySize - 1)) *
                                (event.getHistoricalX(0, historySize - 1) - event.getHistoricalX(1, historySize - 1)) +
                                (event.getHistoricalY(0, historySize - 1) - event.getHistoricalY(1, historySize - 1)) *
                                        (event.getHistoricalY(0, historySize - 1) - event.getHistoricalY(1, historySize - 1)));


                        if (lastDistance < oneBeforeLastDistance) {
                            circle.radius -= Math.abs(lastDistance - oneBeforeLastDistance);
                        } else {
                            circle.radius += Math.abs(lastDistance - oneBeforeLastDistance);
                        }
                    }
                }
                lastCircleX = circle.centerX;
                lastCircleY = circle.centerY;
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                circle.centerX = lastCircleX;
                circle.centerY = lastCircleY;
                isPinchMode = false;
                break;

            case MotionEvent.ACTION_UP:
                circle.centerX = lastCircleX;
                circle.centerY = lastCircleY;
                isPinchMode = false;
                isDoneResizing = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_HOVER_MOVE:
                break;


            default:
                super.onTouchEvent(event);
                break;

        }

        setValues(circle.radius);

        return true;
    }

    private Boolean getTouchedCircle(final int xTouch, final int yTouch) {
        if ((circle.centerX - xTouch) * (circle.centerX - xTouch) +
                (circle.centerY - yTouch) * (circle.centerY - yTouch) <= circle.radius * circle.radius) {
            return true;
        } else {
            return false;
        }

    }

    public void setValues(float radius) {
        if (onCircleTouchListener != null) {
            onCircleTouchListener.circleValue(radius);
        }

    }


    public void setEventListener(OnCircleTouchListener onCircleTouchListener) {
        this.onCircleTouchListener = onCircleTouchListener;
    }


    public Float getValues() {

        return this.radius;
    }

    static class Circle {
        int radius;
        int centerX;
        int centerY;

        Circle() {
            this.radius = 150;
            this.centerX = 378;
            this.centerY = 478;
        }
    }

}