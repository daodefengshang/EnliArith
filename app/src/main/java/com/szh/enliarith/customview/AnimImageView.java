package com.szh.enliarith.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by szh on 2017/4/3.
 */
public class AnimImageView extends ImageView implements Cloneable {
    public AnimImageView(Context context) {
        super(context);
    }

    public AnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
