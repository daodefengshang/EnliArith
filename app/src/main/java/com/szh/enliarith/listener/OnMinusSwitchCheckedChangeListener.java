package com.szh.enliarith.listener;

import android.support.v7.widget.AppCompatSeekBar;
import android.widget.CompoundButton;

/**
 * Created by szh on 2017/4/2.
 */
public class OnMinusSwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

    private AppCompatSeekBar seekBar;

    public OnMinusSwitchCheckedChangeListener(AppCompatSeekBar seekBar) {
        this.seekBar = seekBar;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            int progress = seekBar.getProgress();
            if (progress != 0) {
                seekBar.setProgress(--progress);
            }
            buttonView.setChecked(true);
        }
    }
}
