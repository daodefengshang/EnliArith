package com.szh.enliarith.activity;

import android.animation.Animator;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;
import com.szh.enliarith.R;
import com.szh.enliarith.customview.AnimImageView;
import com.szh.enliarith.utils.DensityUtil;
import com.szh.enliarith.utils.ToastUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MinusSmallActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {

    private EditText minus1EditText;
    private EditText minus2EditText;
    private TextView leftTextView;

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
            minus1EditTextX = minus1EditText.getX();
            minus1EditTextWidth = minus1EditText.getWidth();
            minus2EditTextX = minus2EditText.getX();
            minus2EditTextWidth = minus2EditText.getWidth();
            leftTextViewX = leftTextView.getX();
            leftTextViewWidth = leftTextView.getWidth();
        }
    };

    private View view;
    private RelativeLayout animView;
    private View animBase;
    private int animViewHeight;
    private int animBaseHeight;
    private int everyHeight;
    private float minus1EditTextX;
    private int minus1EditTextWidth;
    private float minus2EditTextX;
    private int minus2EditTextWidth;
    private float leftTextViewX;
    private int leftTextViewWidth;

    private AnimImageView animImageView;
    private int once = 0;
    private int tmpOnce = 0;
    private List<ImageView> firstList = new ArrayList<>();
    private List<ImageView> secondList = new ArrayList<>();
    private int minus1Integer;
    private int minus2Integer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_minus_small);
    }

    @Override
    protected void initView() {
        super.initView();
        view = findViewById(R.id.minus_small_container);
        InputStream inputStream = getResources().openRawResource(R.raw.minus_small_bg);
        view.setBackgroundDrawable(new BitmapDrawable(getResources(), inputStream));
        minus1EditText = (EditText) findViewById(R.id.minus1);
        minus2EditText = (EditText) findViewById(R.id.minus2);
        leftTextView = (TextView) findViewById(R.id.left);
        animView = (RelativeLayout) findViewById(R.id.anim_view);
        animBase = findViewById(R.id.anim_base);
        animView.post(animRegionRunnable);
        animBase.post(animBaseRunnable);
        animImageView = (AnimImageView) LayoutInflater.from(this).inflate(R.layout.anim_imageview, null);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        view.setOnTouchListener(this);
        animBase.setOnTouchListener(this);
        minus1EditText.setOnFocusChangeListener(this);
        minus2EditText.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            leftTextView.setText("");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.minus_small_container:
                minus1EditText.clearFocus();
                minus2EditText.clearFocus();
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
        int minus1Num = random.nextInt(19) + 1;
        minus1EditText.setText(String.valueOf(minus1Num));
        minus2EditText.setText(String.valueOf(random.nextInt(minus1Num) + 1));
        leftTextView.setText("");
    }

    @Override
    protected void onNextClick() {
        String minus1String = minus1EditText.getText().toString();
        if (TextUtils.isEmpty(minus1String)) {
            minus1String = "0";
        }
        int minus1Int = Integer.parseInt(minus1String);
        if (!(minus1Int > 0 && minus1Int < 21)) {
            minus1EditText.setText("");
            minus1EditText.requestFocus();
            toggleSoftInput();
            ToastUtil.toast(this, R.string.minus1_error);
            return;
        } else {
            minus1EditText.setText(String.valueOf(minus1Int));
        }
        String minus2String = minus2EditText.getText().toString();
        if (TextUtils.isEmpty(minus2String)) {
            minus2String = "0";
        }
        int minus2Int = Integer.parseInt(minus2String);
        if (!(minus2Int > 0 && minus2Int <= minus1Int)) {
            minus2EditText.setText("");
            minus2EditText.requestFocus();
            toggleSoftInput();
            ToastUtil.toast(this, String.format(Locale.CHINESE, "请输入1～%d的整数", minus1Int));
            return;
        } else {
            minus2EditText.setText(String.valueOf(minus2Int));
        }
        runAnimator();
    }

    private void runAnimator() {
        tmpOnce = 0;
        minus1EditText.clearFocus();
        minus2EditText.clearFocus();
        minus1EditText.setEnabled(false);
        minus2EditText.setEnabled(false);
        setNextClickable(false);
        setRefreshClickable(false);
        animBase.setEnabled(false);
        switch (once) {
            case 0:
                leftTextView.setText("");
                minus1Integer = Integer.parseInt(minus1EditText.getText().toString());
                animImageView.setImageResource(R.drawable.ic_blue_object);
                firstAnimator();
                break;
            case 1:
                minus2Integer = Integer.parseInt(minus2EditText.getText().toString());
                animImageView.setImageResource(R.drawable.ic_minus_left_forward);
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
                leftTextView.setText("");
                once = 0;
                minus1EditText.setEnabled(true);
                minus2EditText.setEnabled(true);
                setNextClickable(true);
                setRefreshClickable(true);
                animBase.setEnabled(true);
                return;
        }
        once++;
    }

    private void firstAnimator() {
        try {
            AnimImageView clone = (AnimImageView) animImageView.clone();
            animView.addView(clone);
            firstList.add(clone);
            clone.setTranslationX(minus1EditTextX + minus1EditTextWidth / 2 - DensityUtil.dip2px(this, 12));
            clone.setTranslationY(animBaseHeight / 2);
            clone.animate()
                    .translationY(animBaseHeight + tmpOnce * everyHeight)
                    .setDuration(250 + tmpOnce * 10)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (++tmpOnce < minus1Integer) {
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
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void secondAnimator() {
        try {
            AnimImageView clone = (AnimImageView) animImageView.clone();
            animView.addView(clone);
            secondList.add(clone);
            clone.setTranslationX(minus2EditTextX + minus2EditTextWidth / 2 - DensityUtil.dip2px(this, 12));
            clone.setTranslationY(animBaseHeight / 2);
            clone.animate()
                    .translationY(animBaseHeight + tmpOnce * everyHeight)
                    .setDuration(250 + tmpOnce * 10)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (++tmpOnce < minus2Integer) {
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
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void thirdAnimator() {
        try {
            final ImageView clone = firstList.get(tmpOnce);
            float translationX;
            long duration;
            if (tmpOnce < secondList.size()) {
                translationX = minus2EditTextX + minus2EditTextWidth / 2 - DensityUtil.dip2px(this, 12);
                duration = 150;
            } else {
                translationX = leftTextViewX + leftTextViewWidth / 2 - DensityUtil.dip2px(this, 12);
                duration = 250;
            }
            clone.animate()
                    .translationX(translationX)
                    .setDuration(duration)
                    .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (tmpOnce < secondList.size()) {
                        firstList.get(tmpOnce).setVisibility(View.INVISIBLE);
                        secondList.get(tmpOnce).setVisibility(View.INVISIBLE);
                        ParticleSystem ps1 = new ParticleSystem(MinusSmallActivity.this, 40, R.drawable.ic_blue_object, 200);
                        ps1.setScaleRange(DensityUtil.px2dip(getApplicationContext(), 0.3f), DensityUtil.px2dip(getApplicationContext(), 1.5f));
                        ps1.setSpeedRange(DensityUtil.px2dip(getApplicationContext(), 0.05f), DensityUtil.px2dip(getApplicationContext(), 0.36f));
                        ps1.setRotationSpeedRange(90, 180);
                        ps1.setFadeOut(100, new AccelerateInterpolator());
                        ps1.oneShot(clone, 40);
                        ParticleSystem ps2 = new ParticleSystem(MinusSmallActivity.this, 60, R.drawable.ic_minus_left_forward, 200);
                        ps2.setScaleRange(DensityUtil.px2dip(getApplicationContext(), 0.3f), DensityUtil.px2dip(getApplicationContext(), 1.5f));
                        ps2.setSpeedRange(DensityUtil.px2dip(getApplicationContext(), 0.05f), DensityUtil.px2dip(getApplicationContext(), 0.48f));
                        ps2.setRotationSpeedRange(90, 180);
                        ps2.setFadeOut(100, new AccelerateInterpolator());
                        ps2.oneShot(clone, 40);
                    }
                    if (++tmpOnce < firstList.size()) {
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
            final ImageView clone = firstList.get(tmpOnce + secondList.size());
            clone.animate()
                    .translationY(animBaseHeight / 2)
                    .setDuration(250 + (tmpOnce + secondList.size()) * 10)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animView.removeView(clone);
                            leftTextView.setText(String.valueOf(tmpOnce + 1));
                            if (++tmpOnce + secondList.size() < firstList.size()) {
                                fourthAnimator();
                            } else {
                                for (int i = 0; i < secondList.size(); i++) {
                                    animView.removeView(firstList.get(i));
                                    animView.removeView(secondList.get(i));
                                }
                                firstList.clear();
                                secondList.clear();
                                setNextClickable(true);
                                setRefreshClickable(true);
                                animBase.setEnabled(true);
                                minus1EditText.setEnabled(true);
                                minus2EditText.setEnabled(true);
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
            leftTextView.setText(String.valueOf(0));
            for (int i = 0; i < secondList.size(); i++) {
                animView.removeView(firstList.get(i));
                animView.removeView(secondList.get(i));
            }
            firstList.clear();
            secondList.clear();
            setNextClickable(true);
            setRefreshClickable(true);
            animBase.setEnabled(true);
            minus1EditText.setEnabled(true);
            minus2EditText.setEnabled(true);
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
        minus1EditText.setText("");
        minus1EditText.clearFocus();
        minus1EditText.clearComposingText();
        minus2EditText.setText("");
        minus2EditText.clearFocus();
        minus2EditText.clearComposingText();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        super.onFling(e1, e2, velocityX, velocityY);
        minus1EditText.setText("");
        minus2EditText.setText("");
        leftTextView.setText("");
        return false;
    }
}
