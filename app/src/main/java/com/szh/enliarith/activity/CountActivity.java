package com.szh.enliarith.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.View;

import com.szh.enliarith.R;
import com.szh.enliarith.adapter.CountRecyclerViewAdapter;
import com.szh.enliarith.listener.OnMinusSwitchCheckedChangeListener;
import com.szh.enliarith.listener.OnMinusSwitchTouchListener;
import com.szh.enliarith.listener.OnPagerSeekBarChangeListener;
import com.szh.enliarith.listener.OnPlusSwitchCheckedChangeListener;
import com.szh.enliarith.listener.OnPlusSwitchTouchListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SIZE = 100;
    private static final long DELAYED_MILLIS = 1000;
    private List<Integer> list = new ArrayList<>();

    private View backView;
    private AppCompatSeekBar seekBar;
    private RecyclerView recyclerView;
    private SwitchCompat minusSwitch;
    private SwitchCompat plusSwitch;
    private View minusTextView;
    private View plusTextView;

    private boolean minusFlag;
    private boolean plusFlag;

    private Handler handler = new Handler();

    private Runnable minusRunnable = new Runnable() {
        @Override
        public void run() {
            int progress = seekBar.getProgress();
            if (progress != 0 && minusFlag) {
                seekBar.setProgress(--progress);
                handler.postDelayed(minusRunnable, DELAYED_MILLIS);
            } else {
                handler.removeCallbacks(minusRunnable);
                minusFlag = false;
            }
        }
    };

    private Runnable plusRunnable = new Runnable() {
        @Override
        public void run() {
            int progress = seekBar.getProgress();
            if (progress != 99 && plusFlag) {
                seekBar.setProgress(++progress);
                handler.postDelayed(plusRunnable, DELAYED_MILLIS);
            } else {
                handler.removeCallbacks(plusRunnable);
                plusFlag = false;
            }
        }
    };
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        initView();
        initEvents();
    }

    private void initView() {
        view = findViewById(R.id.count_container);
        InputStream inputStream = getResources().openRawResource(R.raw.count_bg);
        view.setBackgroundDrawable(new BitmapDrawable(getResources(), inputStream));
        backView = findViewById(R.id.back);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seekbar);
        recyclerView = (RecyclerView) findViewById(R.id.num_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        for (int i = 0; i < SIZE; i++) {
            list.add(i + 1);
        }
        recyclerView.setAdapter(new CountRecyclerViewAdapter(this, list));
        minusSwitch = (SwitchCompat) findViewById(R.id.minus_switch);
        plusSwitch = (SwitchCompat) findViewById(R.id.plus_switch);
        minusTextView = findViewById(R.id.minus_textview);
        plusTextView = findViewById(R.id.plus_textview);
    }

    private void initEvents() {
        backView.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new OnPagerSeekBarChangeListener(recyclerView));
        minusSwitch.setOnCheckedChangeListener(new OnMinusSwitchCheckedChangeListener(seekBar));
        plusSwitch.setOnCheckedChangeListener(new OnPlusSwitchCheckedChangeListener(seekBar));
        minusSwitch.setOnTouchListener(new OnMinusSwitchTouchListener(this));
        plusSwitch.setOnTouchListener(new OnPlusSwitchTouchListener(this));
        minusTextView.setOnClickListener(this);
        plusTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
                break;
            case R.id.minus_textview:
                if (plusFlag) {
                    plusFlag = false;
                    handler.removeCallbacks(plusRunnable);
                }
                if (minusFlag) {
                    handler.removeCallbacks(minusRunnable);
                } else {
                    handler.postDelayed(minusRunnable, DELAYED_MILLIS);
                }
                minusFlag = !minusFlag;
                break;
            case R.id.plus_textview:
                if (minusFlag) {
                    minusFlag = false;
                    handler.removeCallbacks(minusRunnable);
                }
                if (plusFlag) {
                    handler.removeCallbacks(plusRunnable);
                } else {
                    handler.postDelayed(plusRunnable, DELAYED_MILLIS);
                }
                plusFlag = !plusFlag;
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        minusFlag = false;
        plusFlag = false;
        handler.removeCallbacks(minusRunnable);
        handler.removeCallbacks(plusRunnable);
        view.setBackgroundDrawable(null);
    }
}
