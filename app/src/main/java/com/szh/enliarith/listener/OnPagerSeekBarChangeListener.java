package com.szh.enliarith.listener;

import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;

/**
 * Created by szh on 2017/4/2.
 */
public class OnPagerSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private RecyclerView recyclerView;

    public OnPagerSeekBarChangeListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            recyclerView.smoothScrollToPosition(progress);
        } else {
            recyclerView.scrollToPosition(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
