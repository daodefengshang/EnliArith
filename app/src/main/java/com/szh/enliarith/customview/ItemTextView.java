package com.szh.enliarith.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by szh on 2017/4/2.
 */
public class ItemTextView extends TextView implements Cloneable {
    public ItemTextView(Context context) {
        super(context);
    }

    public ItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ItemTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
