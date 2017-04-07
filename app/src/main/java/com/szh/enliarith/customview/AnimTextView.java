package com.szh.enliarith.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by szh on 2017/4/3.
 */
public class AnimTextView extends TextView implements Cloneable {
    public AnimTextView(Context context) {
        super(context);
    }

    public AnimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
