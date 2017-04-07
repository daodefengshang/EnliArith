package com.szh.enliarith.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.szh.enliarith.utils.DensityUtil;

/**
 * Created by szh on 2017/4/2.
 */
public class OnPlusSwitchTouchListener implements View.OnTouchListener {

    private Context context;
    private float plusX;

    public OnPlusSwitchTouchListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                plusX = event.getX();
                return false;
            case MotionEvent.ACTION_UP:
                boolean flag = event.getX() - plusX < DensityUtil.dip2px(context, 15);
                if (flag) {
                    ((CompoundButton) v).setChecked(false);
                }
                return flag;
        }
        return false;
    }
}
