package com.szh.enliarith.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.szh.enliarith.utils.DensityUtil;

/**
 * Created by szh on 2017/4/2.
 */
public class OnMinusSwitchTouchListener implements View.OnTouchListener {

    private Context context;
    private float minusX;

    public OnMinusSwitchTouchListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                minusX = event.getX();
                return false;
            case MotionEvent.ACTION_UP:
                boolean flag = minusX - event.getX() < DensityUtil.dip2px(context, 15);
                if (flag) {
                    ((CompoundButton) v).setChecked(true);
                }
                return flag;
        }
        return false;
    }
}
