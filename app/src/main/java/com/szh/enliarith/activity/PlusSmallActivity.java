package com.szh.enliarith.activity;

import android.animation.Animator;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szh.enliarith.R;
import com.szh.enliarith.utils.DensityUtil;
import com.szh.enliarith.utils.InflateUtil;
import com.szh.enliarith.utils.ToastUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PlusSmallActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {

    private EditText plus1EditText;
    private EditText plus2EditText;
    private TextView sumTextView;

    private Runnable animRegionRunnable = new Runnable() {
        @Override
        public void run() {
            animViewHeight = animView.getHeight();
            animBaseHeight = animBase.getHeight();
            everyHeight = (animViewHeight - animBaseHeight) / 20;
        }
    };

    private Runnable animBaseRunnable = new Runnable() {

        @Override
        public void run() {
            plus1EditTextX = plus1EditText.getX();
            plus1EditTextWidth = plus1EditText.getWidth();
            plus2EditTextX = plus2EditText.getX();
            plus2EditTextWidth = plus2EditText.getWidth();
            sumTextViewX = sumTextView.getX();
            sumTextViewWidth = sumTextView.getWidth();
        }
    };

    private View view;
    private RelativeLayout animView;
    private View animBase;
    private int animViewHeight;
    private int animBaseHeight;
    private int everyHeight;
    private float plus1EditTextX;
    private int plus1EditTextWidth;
    private float plus2EditTextX;
    private int plus2EditTextWidth;
    private float sumTextViewX;
    private int sumTextViewWidth;

    private int once = 0;
    private int tmpOnce = 0;
    private List<ImageView> firstList = new ArrayList<>();
    private List<ImageView> secondList = new ArrayList<>();
    private int plus1Integer;
    private int plus2Integer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_plus_small);
    }

    @Override
    protected void initView() {
        super.initView();
        view = findViewById(R.id.plus_small_container);
        InputStream inputStream = getResources().openRawResource(R.raw.plus_small_bg);
        view.setBackgroundDrawable(new BitmapDrawable(getResources(), inputStream));
        plus1EditText = (EditText) findViewById(R.id.plus1);
        plus2EditText = (EditText) findViewById(R.id.plus2);
        sumTextView = (TextView) findViewById(R.id.sum);
        animView = (RelativeLayout) findViewById(R.id.anim_view);
        animBase = findViewById(R.id.anim_base);
        animView.post(animRegionRunnable);
        animBase.post(animBaseRunnable);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        view.setOnTouchListener(this);
        animBase.setOnTouchListener(this);
        plus1EditText.setOnFocusChangeListener(this);
        plus2EditText.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            sumTextView.setText("");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.plus_small_container:
                plus1EditText.clearFocus();
                plus2EditText.clearFocus();
                hideSoftInputFromWindow(view);
                return true;
            case R.id.anim_base:
                gestureEvent(event);
                return true;
        }
        return false;
    }

    @Override
    protected void onRefreshClick() {
        Random random = new Random();
        int plus1Num = random.nextInt(19) + 1;
        plus1EditText.setText(String.valueOf(plus1Num));
        plus2EditText.setText(String.valueOf(random.nextInt(20 - plus1Num) + 1));
        sumTextView.setText("");
    }

    @Override
    protected void onNextClick() {
        String plus1String = plus1EditText.getText().toString();
        if (TextUtils.isEmpty(plus1String)) {
            plus1String = "0";
        }
        int plus1Int = Integer.parseInt(plus1String);
        if (!(plus1Int > 0 && plus1Int < 20)) {
            plus1EditText.setText("");
            plus1EditText.requestFocus();
            toggleSoftInput();
            ToastUtil.toast(this, R.string.plus1_error);
            return;
        } else {
            plus1EditText.setText(String.valueOf(plus1Int));
        }
        String plus2String = plus2EditText.getText().toString();
        if (TextUtils.isEmpty(plus2String)) {
            plus2String = "0";
        }
        int plus2Int = Integer.parseInt(plus2String);
        if (!(plus2Int > 0 && plus2Int < 21 - plus1Int)) {
            plus2EditText.setText("");
            plus2EditText.requestFocus();
            toggleSoftInput();
            ToastUtil.toast(this, String.format(Locale.CHINESE, "请输入1～%d的整数", 20 - plus1Int));
            return;
        } else {
            plus2EditText.setText(String.valueOf(plus2Int));
        }
        runAnimator();
    }

    private void runAnimator() {
        tmpOnce = 0;
        plus1EditText.clearFocus();
        plus2EditText.clearFocus();
        plus1EditText.setEnabled(false);
        plus2EditText.setEnabled(false);
        setNextClickable(false);
        setRefreshClickable(false);
        animBase.setEnabled(false);
        switch (once) {
            case 0:
                sumTextView.setText("");
                plus1Integer = Integer.parseInt(plus1EditText.getText().toString());
                firstAnimator();
                break;
            case 1:
                plus2Integer = Integer.parseInt(plus2EditText.getText().toString());
                secondAnimator();
                break;
            case 2:
                thirdAnimator();
                break;
            case 3:
                once = 0;
                fourthAnimator();
                return;
            default:
                sumTextView.setText("");
                once = 0;
                plus1EditText.setEnabled(true);
                plus2EditText.setEnabled(true);
                setNextClickable(true);
                setRefreshClickable(true);
                animBase.setEnabled(true);
                return;
        }
        once++;
    }

    private void firstAnimator() {
        try {
            ImageView animImageView = InflateUtil.getImageView(this);
            animImageView.setImageResource(R.drawable.ic_red_object);
            animView.addView(animImageView);
            firstList.add(animImageView);
            animImageView.setTranslationX(plus1EditTextX + plus1EditTextWidth / 2 - DensityUtil.dip2px(this, 12));
            animImageView.setTranslationY(animBaseHeight / 2);
            animImageView.animate()
                    .translationY(animBaseHeight + tmpOnce * everyHeight)
                    .setDuration(250 + tmpOnce * 10)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (++tmpOnce < plus1Integer) {
                        firstAnimator();
                    } else {
                        setNextClickable(true);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void secondAnimator() {
        try {
            ImageView animImageView = InflateUtil.getImageView(this);
            animImageView.setImageResource(R.drawable.ic_blue_object);
            animView.addView(animImageView);
            secondList.add(animImageView);
            animImageView.setTranslationX(plus2EditTextX + plus2EditTextWidth / 2 - DensityUtil.dip2px(this, 12));
            animImageView.setTranslationY(animBaseHeight / 2);
            animImageView.animate()
                    .translationY(animBaseHeight + (tmpOnce + plus1Integer) * everyHeight)
                    .setDuration(250 + (tmpOnce + plus1Integer) * 10)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (++tmpOnce < plus2Integer) {
                        secondAnimator();
                    } else {
                        setNextClickable(true);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void thirdAnimator() {
        try {
            ImageView animImageView;
            long duration;
            if (tmpOnce < firstList.size()) {
                animImageView = firstList.get(tmpOnce);
                duration = 150;
            } else {
                animImageView = secondList.get(tmpOnce - firstList.size());
                duration = 250;
            }
            animImageView.animate()
                    .translationX(sumTextViewX + sumTextViewWidth / 2 - DensityUtil.dip2px(this, 12))
                    .setDuration(duration)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (++tmpOnce < firstList.size() + secondList.size()) {
                        thirdAnimator();
                    } else {
                        setNextClickable(true);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fourthAnimator() {
        try {
            final ImageView animImageView;
            if (tmpOnce < firstList.size()) {
                animImageView = firstList.get(tmpOnce);
            } else {
                animImageView = secondList.get(tmpOnce - firstList.size());
            }
            animImageView.animate()
                    .translationY(animBaseHeight / 2)
                    .setDuration(250 + tmpOnce * 10)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animView.removeView(animImageView);
                            sumTextView.setText(String.valueOf(tmpOnce + 1));
                            if (++tmpOnce < firstList.size() + secondList.size()) {
                                fourthAnimator();
                            } else {
                                firstList.clear();
                                secondList.clear();
                                setNextClickable(true);
                                setRefreshClickable(true);
                                plus1EditText.setEnabled(true);
                                plus2EditText.setEnabled(true);
                                animBase.setEnabled(true);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animView.removeCallbacks(animRegionRunnable);
        animBase.removeCallbacks(animBaseRunnable);
        firstList.clear();
        secondList.clear();
        ToastUtil.setToastNull();
        view.setBackgroundDrawable(null);
        plus1EditText.setText("");
        plus1EditText.clearFocus();
        plus1EditText.clearComposingText();
        plus2EditText.setText("");
        plus2EditText.clearFocus();
        plus2EditText.clearComposingText();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        plus1EditText.setText("");
        plus2EditText.setText("");
        sumTextView.setText("");
        return false;
    }
}
