package com.szh.enliarith.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by szh on 2017/4/2.
 */
public class RecyclerFrameLayout extends FrameLayout {
    public RecyclerFrameLayout(Context context) {
        super(context);
    }

    public RecyclerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecyclerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
