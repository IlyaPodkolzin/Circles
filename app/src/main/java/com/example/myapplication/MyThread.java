package com.example.myapplication;

import android.animation.ArgbEvaluator;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.animation.PathInterpolator;

public class MyThread extends Thread{

    private Paint paint;
    private ArgbEvaluator argbEvaluator;
    public static final int FRACTION_TIME = 1000;
    private SurfaceHolder surfaceHolder;
    private boolean flag;
    private long startTime;
    private long buffRedrawTime;

    MyThread(SurfaceHolder h) {
        flag = false;
        surfaceHolder = h;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        argbEvaluator = new ArgbEvaluator();
    }

    public void setRunning(boolean f) {
        this.flag = f;
    }

    @Override
    public void run() {
        Canvas canvas;
        startTime = getTime();
        while (flag) {
            long currentTime = getTime();
            long elapsedTime = currentTime - buffRedrawTime;
            if (elapsedTime < 100000) {
                continue;
            }
            canvas = surfaceHolder.lockCanvas();
            drawCircles(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
            buffRedrawTime = getTime();
        }
    }

    public long getTime() {
        return System.nanoTime() / 1000;
    }

    public void drawCircles(Canvas canvas) {
        long currentTime = getTime();
        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;
        canvas.drawColor(Color.BLACK);
        float maxRadius = Math.min(canvas.getHeight(), canvas.getWidth()) / 2;
        Log.d("RRRR maxRadius", Float.toString(maxRadius));
        float fraction = (float)(currentTime % FRACTION_TIME) / FRACTION_TIME;
        Log.d("RRRR fraction", Float.toString(fraction));
        paint.setColor(Color.rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
        canvas.drawCircle(centerX, centerY, maxRadius * fraction, paint);
    }
}
