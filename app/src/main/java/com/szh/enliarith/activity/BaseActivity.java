package com.szh.enliarith.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.szh.enliarith.R;
import com.szh.enliarith.customview.ShowGestureView;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener, View.OnFocusChangeListener, GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    private View backView;
    private View refreshView;
    private InputMethodManager imm;
    private View nextView;

    private ShowGestureView showGestureView;
    private TextView correctTextView;
    private TextView errorTextView;

    private Runnable correctRunnable = new Runnable() {
        @Override
        public void run() {
            correctTextView.setVisibility(View.INVISIBLE);
        }
    };

    private Runnable errorRunnable = new Runnable() {
        @Override
        public void run() {
            errorTextView.setVisibility(View.INVISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initView();
        initEvents();
    }

    protected abstract void setContentView();
    protected void initView() {
        backView = findViewById(R.id.back);
        refreshView = findViewById(R.id.refresh);
        nextView = findViewById(R.id.next_textview);
        gestureDetector = new GestureDetector(this, this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        correctTextView = (TextView) findViewById(R.id.correct);
        if (correctTextView != null) {
            correctTextView.setVisibility(View.INVISIBLE);
        }
        errorTextView = (TextView) findViewById(R.id.error);
        if (errorTextView != null) {
            errorTextView.setVisibility(View.INVISIBLE);
        }
        showGestureView = (ShowGestureView) findViewById(R.id.showgesture_view);
    }

    protected void showCorrectView() {
        correctTextView.setVisibility(View.VISIBLE);
        correctTextView.postDelayed(correctRunnable, 2000);
    }

    protected void showErrorView() {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.postDelayed(errorRunnable, 2000);
    }

    protected void initEvents() {
        backView.setOnClickListener(this);
        refreshView.setOnClickListener(this);
        nextView.setOnClickListener(this);
    }

    protected void gestureEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
    }

    protected void hideSoftInputFromWindow(View rootView) {
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void toggleSoftInput() {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.anim_access, R.anim.anim_return);
                break;
            case R.id.refresh:
                onRefreshClick();
                break;
            case R.id.next_textview:
                onNextClick();
                break;
        }
    }

    protected abstract void onRefreshClick();

    protected abstract void onNextClick();

    protected void setNextClickable(boolean clickable) {
        nextView.setClickable(clickable);
    }

    protected void setRefreshClickable(boolean clickable) {
        refreshView.setClickable(clickable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showGestureView != null) {
            showGestureView.destroyRunnable();
        }
        if (correctTextView != null) {
            correctTextView.removeCallbacks(correctRunnable);
        }
        if (errorTextView != null) {
            errorTextView.removeCallbacks(errorRunnable);
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
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (showGestureView != null) {
            showGestureView.showGesture(e2);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
