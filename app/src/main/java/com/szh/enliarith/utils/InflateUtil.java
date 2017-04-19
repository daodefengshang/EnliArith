package com.szh.enliarith.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.szh.enliarith.R;

/**
 * Created by szh on 2017/4/19.
 */
public class InflateUtil {
    public static ImageView getImageView(Context context) {
        return  (ImageView) LayoutInflater.from(context).inflate(R.layout.anim_imageview, null);
    }

    public static ImageView getShadowImageView(Context context) {
        ImageView imageView = getImageView(context);
        imageView.setImageResource(R.drawable.ic_point);
        imageView.setPadding(0, 0, 0, (int) DensityUtil.dip2px(context, 1));
        imageView.setBackgroundColor(Color.BLACK);
        return imageView;
    }

    public static TextView getTextView(Context context) {
        return  (TextView) LayoutInflater.from(context).inflate(R.layout.anim_textview, null);
    }

    public static TextView getShadowTextView(Context context) {
        TextView textView = getTextView(context);
        textView.setShadowLayer(1, 2, 2, Color.BLACK);
        return textView;
    }
}
