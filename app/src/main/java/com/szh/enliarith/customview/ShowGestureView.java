package com.szh.enliarith.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.szh.enliarith.utils.DensityUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by szh on 2017/4/6.
 */
public class ShowGestureView extends View {

    private boolean flag;
    private List<Float> list = new ArrayList<>();
    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            if (list.size() >= 2) {
                list.remove(list.size() - 1);
                list.remove(list.size() - 1);
                invalidate();
            }
            if (!flag) {
                postDelayed(removeRunnable, 40);
            }
        }
    };
    private Paint paint;
    private int statusBarHeight;

    public ShowGestureView(Context context) {
        this(context, null);
    }

    public ShowGestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShowGestureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void destroyRunnable() {
        this.flag = true;
        this.removeCallbacks(removeRunnable);
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        statusBarHeight = getStatusBarHeight();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 3f));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setDither(true);
        post(removeRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = list.size();
        if (size >= 4) {
            for (int i = 0; i < size - 2 && i < 15; i += 2) {
                paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 3f - i * 1.0f / 7));
                canvas.drawLine(list.get(i), list.get(i + 1), list.get(i + 2), list.get(i + 3), paint);
            }
        }
    }

    public void showGesture(MotionEvent event) {
        list.add(0, event.getRawX());
        list.add(1, event.getRawY() - statusBarHeight);
        if (list.size() == 20) {
            list.remove(19);
            list.remove(18);
        }
        invalidate();
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
