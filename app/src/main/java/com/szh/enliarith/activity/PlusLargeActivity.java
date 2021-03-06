package com.szh.enliarith.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szh.enliarith.R;
import com.szh.enliarith.animation.FlipCardAnimation;
import com.szh.enliarith.utils.DensityUtil;
import com.szh.enliarith.utils.InflateUtil;
import com.szh.enliarith.utils.ToastUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PlusLargeActivity extends BaseActivity
        implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener, IOption {

    private static final int TEXTSIZE = 30;

    private EditText plus1EditText;
    private EditText plus2EditText;
    private TextView sumTextView;
    private FrameLayout sumFrame;
    private float sumFrameX;
    private EditText result;

    private Runnable animRegionRunnable = new Runnable() {
        @Override
        public void run() {
            animViewHeight = animView.getHeight();
            animBaseWidth = animBase.getWidth();
            animBaseHeight = animBase.getHeight();
            spaceViewWidth = spaceView.getWidth();
        }
    };

    private Runnable animBaseRunnable = new Runnable() {

        @Override
        public void run() {
            plus1EditTextX = plus1EditText.getX();
            plus1EditTextWidth = plus1EditText.getWidth();
            plus2EditTextX = plus2EditText.getX();
            plus2EditTextWidth = plus2EditText.getWidth();
            sumFrameX = sumFrame.getX();
            sumTextViewX = sumTextView.getX();
            sumTextViewWidth = sumTextView.getWidth();
        }
    };

    private View view;
    private RelativeLayout animView;
    private View animBase;
    private View spaceView;
    private int animViewHeight;
    private int animBaseWidth;
    private int animBaseHeight;
    private int spaceViewWidth;
    private float plus1EditTextX;
    private int plus1EditTextWidth;
    private float plus2EditTextX;
    private int plus2EditTextWidth;
    private float sumTextViewX;
    private int sumTextViewWidth;

    private int once = 0;
    private List<TextView> firstList = new ArrayList<>();
    private List<TextView> secondList = new ArrayList<>();
    private List<TextView> thirdList = new ArrayList<>();
    private List<ImageView> fourthList = new ArrayList<>();
    private List<TextView> fifthList = new ArrayList<>();//和
    private List<TextView> sixthList = new ArrayList<>();//进1
    private TextView optionTextView;

    private FlipCardAnimation flipCardAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_plus_large);
    }

    @Override
    protected void initView() {
        super.initView();
        view = findViewById(R.id.plus_large_container);
        InputStream inputStream = getResources().openRawResource(R.raw.plus_large_bg);
        view.setBackgroundDrawable(new BitmapDrawable(getResources(), inputStream));
        plus1EditText = (EditText) findViewById(R.id.plus1);
        plus2EditText = (EditText) findViewById(R.id.plus2);
        sumTextView = (TextView) findViewById(R.id.sum);
        sumFrame = (FrameLayout) findViewById(R.id.sum_frame);
        result = (EditText) findViewById(R.id.result);
        animView = (RelativeLayout) findViewById(R.id.anim_view);
        animBase = findViewById(R.id.anim_base);
        spaceView = findViewById(R.id.space_view);
        optionTextView = (TextView) findViewById(R.id.option);
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
            result.setText("");
            sumTextView.setText("");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.plus_large_container:
                plus1EditText.clearFocus();
                plus2EditText.clearFocus();
                result.clearFocus();
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
        int plus1Num = random.nextInt(9998) + 1;
        plus1EditText.setText(String.valueOf(plus1Num));
        plus2EditText.setText(String.valueOf(random.nextInt(9999 - plus1Num) + 1));
        result.setText("");
        sumTextView.setText("");
    }

    @Override
    protected void onNextClick() {
        String plus1String = plus1EditText.getText().toString();
        if (TextUtils.isEmpty(plus1String)) {
            plus1String = "0";
        }
        int plus1Int = Integer.parseInt(plus1String);
        if (!(plus1Int > 0 && plus1Int < 9999)) {
            plus1EditText.setText("");
            plus1EditText.requestFocus();
            toggleSoftInput();toggleSoftInput();
            ToastUtil.toast(this, R.string.large_plus1_error);
            return;
        } else {
            plus1EditText.setText(String.valueOf(plus1Int));
        }
        String plus2String = plus2EditText.getText().toString();
        if (TextUtils.isEmpty(plus2String)) {
            plus2String = "0";
        }
        int plus2Int = Integer.parseInt(plus2String);
        if (!(plus2Int > 0 && plus2Int < 10000 - plus1Int)) {
            plus2EditText.setText("");
            plus2EditText.requestFocus();
            toggleSoftInput();
            ToastUtil.toast(this, String.format(Locale.CHINESE, "请输入1～%d的整数", 9999 - plus1Int));
            return;
        } else {
            plus2EditText.setText(String.valueOf(plus2Int));
        }
        runAnimator();
    }

    private void runAnimator() {
        plus1EditText.clearFocus();
        plus2EditText.clearFocus();
        result.clearFocus();
        plus1EditText.setEnabled(false);
        plus2EditText.setEnabled(false);
        result.setEnabled(false);
        setNextClickable(false);
        setRefreshClickable(false);
        animBase.setEnabled(false);
        switch (once) {
            case 0:
                sumTextView.setText("");
                firstAnimator();
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                secondAnimator();
                break;
            case 5:
                thirdAnimator();
                break;
            default:
                once = 0;
                plus1EditText.setEnabled(true);
                plus2EditText.setEnabled(true);
                result.setEnabled(true);
                setNextClickable(true);
                setRefreshClickable(true);
                animBase.setEnabled(true);
        }
    }

    private void firstAnimator() {
        sixthList.add(null);
        try {
            Editable text = plus1EditText.getText();
            int length = text.length();
            for (int i = length - 1; i >= 0; i--) {
                TextView animTextView = InflateUtil.getShadowTextView(this);
                animTextView.setText(String.valueOf(text.charAt(i)));
                animView.addView(animTextView);
                firstList.add(animTextView);
                animTextView.setTranslationX(plus1EditTextX + plus1EditTextWidth / 2 - DensityUtil.sp2px(this, TEXTSIZE - 20));
                animTextView.setTranslationY(animBaseHeight / 2);
                final int finalI = i;
                animTextView.animate()
                        .translationX(sumFrameX + sumTextViewX - (length - i - 1) * (animBaseWidth * 1.0f - sumTextViewWidth * 2 - spaceViewWidth * 4) / 4)
                        .translationY(animBaseHeight + DensityUtil.sp2px(this, TEXTSIZE + 20))
                        .setDuration(300)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (finalI <= 0) {
                                    firstNextFirstAnimator();
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firstNextFirstAnimator() {
        try {
            final int[] m = {0, 0};
            Editable text = plus2EditText.getText();
            int length = text.length();
            for (int i = length - 1; i >= 0; i--) {
                TextView animTextView = InflateUtil.getShadowTextView(this);
                animTextView.setText(String.valueOf(text.charAt(i)));
                animView.addView(animTextView);
                secondList.add(animTextView);
                animTextView.setTranslationX(plus2EditTextX + plus2EditTextWidth / 2 - DensityUtil.sp2px(this, TEXTSIZE - 20));
                animTextView.setTranslationY(animBaseHeight / 2);
                animTextView.animate()
                        .translationX(sumFrameX + sumTextViewX - (length - i - 1) * (animBaseWidth * 1.0f - sumTextViewWidth * 2 - spaceViewWidth * 4) / 4)
                        .translationY(animBaseHeight + DensityUtil.sp2px(this, TEXTSIZE * 2 + 40))
                        .setDuration(300)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                m[0]++;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                m[1]++;
                                if (m[0] == m[1]) {
                                    firstNextSecondAnimator();
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firstNextSecondAnimator() {
        try {
            TextView animTextView = InflateUtil.getShadowTextView(this);
            animTextView.setText("+");
            animView.addView(animTextView);
            thirdList.add(animTextView);
            animTextView.setTranslationX((plus1EditTextX + plus1EditTextWidth + plus2EditTextX) / 2 - DensityUtil.sp2px(this, TEXTSIZE - 22));
            animTextView.setTranslationY(animBaseHeight / 2);
            animTextView.animate()
                    .translationX(Math.min(firstList.get(firstList.size() - 1).getX(), secondList.get(secondList.size() - 1).getX())
                            - (animBaseWidth * 1.0f - sumTextViewWidth * 2 - spaceViewWidth * 4) / 4)
                    .translationY(animBaseHeight + DensityUtil.sp2px(this, TEXTSIZE * 2 + 40))
                    .setDuration(300)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            firstNextThirdAnimator();
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

    private void firstNextThirdAnimator() {
        try {
            ImageView imageView = InflateUtil.getShadowImageView(this);
            animView.addView(imageView);
            fourthList.add(imageView);
            imageView.setTranslationX((thirdList.get(0).getX() + secondList.get(0).getX() + DensityUtil.sp2px(this, TEXTSIZE - 18)) / 2);
            imageView.setTranslationY(animBaseHeight + DensityUtil.sp2px(this, TEXTSIZE * 3 + 80));
            imageView.animate()
                    .scaleX((secondList.get(0).getX() - thirdList.get(0).getX() + DensityUtil.sp2px(this, TEXTSIZE)) / DensityUtil.dip2px(this,2))
                    .setDuration(300)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            once++;
                            setNextClickable(true);
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
        final int[] m = {0, 0};
        boolean flag = false;
        ObjectAnimator animator = new ObjectAnimator().setDuration(150);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setRepeatCount(2);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setPropertyName("alpha");
        animator.setFloatValues(1.0f, 0.01f, 1.0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                m[0]++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                m[1]++;
                if (m[0] == m[1]) {
                    secondNextFirstAnimator();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (secondList.size() > once - 1) {
            ObjectAnimator animator1 = animator.clone();
            animator1.setTarget(secondList.get(once - 1));
            animator1.start();
            flag = true;
        }
        if (sixthList.size() > once - 1 && sixthList.get(once - 1) != null) {
            ObjectAnimator animator2 = animator.clone();
            animator2.setTarget(sixthList.get(once - 1));
            animator2.start();
            flag = true;
        }
        if (firstList.size() > once - 1) {
            animator.setTarget(firstList.get(once - 1));
            animator.start();
            flag = true;
        }
        if (!flag) {
            setNextClickable(true);
            once = 5;
        }
    }

    private void secondNextFirstAnimator() {
        try {
            int firstNum = 0;
            int secondNum = 0;
            int thirdNum = 0;
            if (firstList.size() > once - 1) {
                firstNum = Integer.parseInt(firstList.get(once - 1).getText().toString());
            }
            if (secondList.size() > once - 1) {
                secondNum = Integer.parseInt(secondList.get(once - 1).getText().toString());
            }
            if (sixthList.size() > once - 1 && sixthList.get(once - 1) != null) {
                thirdNum = Integer.parseInt(sixthList.get(once - 1).getText().toString());
            }
            int sumNum = firstNum + secondNum + thirdNum;
            showOption(firstNum, secondNum, thirdNum);
            final TextView animTextView = InflateUtil.getShadowTextView(this);
            TextView animTextViewTmp = null;
            if (sumNum >= 0 && sumNum < 10) {
                animTextView.setText(String.valueOf(sumNum));
            } else {
                animTextView.setText(String.valueOf(sumNum % 10));
                animTextViewTmp = InflateUtil.getShadowTextView(this);
                animTextViewTmp.setText(String.valueOf(sumNum / 10));
            }
            fifthList.add(animTextView);
            sixthList.add(animTextViewTmp);
            animTextView.setScaleX(0.01f);
            animTextView.setScaleY(0.01f);
            animTextView.setAlpha(0.01f);
            animTextView.setVisibility(View.INVISIBLE);
            animView.addView(animTextView);
            animTextView.setTranslationX(sumFrameX + sumTextViewX - (once - 1) * (animBaseWidth * 1.0f - sumTextViewWidth * 2 - spaceViewWidth * 4) / 4);
            animTextView.setTranslationY(animBaseHeight + DensityUtil.sp2px(this, TEXTSIZE * 4 + 70));
            if (animTextViewTmp != null) {
                animTextViewTmp.setScaleX(0.01f);
                animTextViewTmp.setScaleY(0.01f);
                animTextViewTmp.setAlpha(0.01f);
                animTextViewTmp.setVisibility(View.INVISIBLE);
                animView.addView(animTextViewTmp);
                animTextViewTmp.setTranslationX(sumFrameX + sumTextViewX - (once - 1) * (animBaseWidth * 1.0f - sumTextViewWidth * 2 - spaceViewWidth * 2) / 4
                        - DensityUtil.sp2px(this, TEXTSIZE / 2));
                animTextViewTmp.setTranslationY(animBaseHeight + DensityUtil.sp2px(this, TEXTSIZE * 4 + 70));
                final TextView finalTextViewTmp = animTextViewTmp;
                animTextViewTmp.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f)
                        .setInterpolator(new OvershootInterpolator())
                        .setDuration(200)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                finalTextViewTmp.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                secondNextSecondAnimator();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
            }
            animTextView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            animTextView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (sixthList.get(once) == null) {
                                once++;
                                if (once > Math.max(firstList.size(), secondList.size())) {
                                    once = 5;
                                }
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

    private void secondNextSecondAnimator() {
        sixthList.get(once).animate().scaleX(0.6f).scaleY(0.6f)
                .translationX(sumFrameX + sumTextViewX - once * (animBaseWidth * 1.0f - sumTextViewWidth * 2 - spaceViewWidth * 4) / 4
                        + DensityUtil.sp2px(this, TEXTSIZE - 22))
                .translationY(animBaseHeight + DensityUtil.sp2px(this, TEXTSIZE * 3 + 50))
                .setDuration(200)
                .setStartDelay(150)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        once++;
                        if (once > Math.max(firstList.size(), secondList.size()) + 1) {
                            once = 5;
                        }
                        setNextClickable(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    private void thirdAnimator() {
        final int firstNum = Integer.parseInt(plus1EditText.getText().toString());
        final int secondNum = Integer.parseInt(plus2EditText.getText().toString());
        showOption(firstNum, secondNum, 0);
        final int[] m = {0, 0};
        for (final TextView animTextView : fifthList) {
            animTextView.animate().alpha(0.1f)
                    .translationX(sumFrameX + sumTextViewX + sumTextViewWidth / 2 - DensityUtil.sp2px(this, TEXTSIZE - 20))
                    .translationY(animBaseHeight / 2)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            m[0]++;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animView.removeView(animTextView);
                            m[1]++;
                            if (m[0] == m[1]) {
                                sumTextView.setText(String.valueOf(firstNum + secondNum));
                                for (TextView animTextViewTmp : sixthList) {
                                    if (animTextViewTmp != null) {
                                        animView.removeView(animTextViewTmp);
                                    }
                                }
                                fifthList.clear();
                                sixthList.clear();
                                thirdNextFirstAnimator();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
        }
    }

    private void thirdNextFirstAnimator() {
        fourthList.get(0).animate().scaleX(1.0f).setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.removeView(fourthList.get(0));
                        fourthList.clear();
                        thirdNextSecondAnimator();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    private void thirdNextSecondAnimator() {
        thirdList.get(0).animate()
                .translationX((plus1EditTextX + plus1EditTextWidth + plus2EditTextX) / 2 - DensityUtil.sp2px(this, TEXTSIZE - 22))
                .translationY(animBaseHeight / 2)
                .alpha(0.1f)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.removeView(thirdList.get(0));
                        thirdList.clear();
                        thirdNextThirdAnimator();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    private void thirdNextThirdAnimator() {
        final int[] m = {0, 0};
        for (final TextView animTextView : secondList) {
            animTextView.animate().alpha(0.1f)
                    .translationX(plus2EditTextX + plus2EditTextWidth / 2 - DensityUtil.sp2px(this, TEXTSIZE - 20))
                    .translationY(animBaseHeight / 2)
                    .setDuration(300)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            m[0]++;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animView.removeView(animTextView);
                            m[1]++;
                            if (m[0] == m[1]) {
                                thirdNextFourthAnimator();
                                secondList.clear();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
        }
    }

    private void thirdNextFourthAnimator() {
        final int[] m = {0, 0};
        for (final TextView animTextView : firstList) {
            animTextView.animate().alpha(0.1f)
                    .translationX(plus1EditTextX + plus1EditTextWidth / 2 - DensityUtil.sp2px(this, TEXTSIZE - 20))
                    .translationY(animBaseHeight / 2)
                    .setDuration(300)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            m[0]++;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animView.removeView(animTextView);
                            m[1]++;
                            if (m[0] == m[1]) {
                                showResult();
                                firstList.clear();
                                once = 0;
                                setNextClickable(true);
                                setRefreshClickable(true);
                                plus1EditText.setEnabled(true);
                                plus2EditText.setEnabled(true);
                                result.setEnabled(true);
                                animBase.setEnabled(true);
                                dismissOption();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
        }
    }

    private void showResult() {
        Editable resultText = result.getText();
        if (TextUtils.isEmpty(resultText)) {
            return;
        }
        if (Integer.parseInt(resultText.toString()) == Integer.parseInt(sumTextView.getText().toString())) {
            showCorrectView();
        } else {
            showErrorView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animView.removeCallbacks(animRegionRunnable);
        animBase.removeCallbacks(animBaseRunnable);
        firstList.clear();
        secondList.clear();
        thirdList.clear();
        fourthList.clear();
        fifthList.clear();
        sixthList.clear();
        ToastUtil.setToastNull();
        view.setBackgroundDrawable(null);
        plus1EditText.setText("");
        plus1EditText.clearFocus();
        plus1EditText.clearComposingText();
        plus2EditText.setText("");
        plus2EditText.clearFocus();
        plus2EditText.clearComposingText();
        result.setText("");
        result.clearFocus();
        result.clearComposingText();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        plus1EditText.setText("");
        plus2EditText.setText("");
        result.setText("");
        sumTextView.setText("");
        return false;
    }

    @Override
    public void showOption(int firstNum, int secondNum, int thirdNum) {
        final String format;
        if (thirdNum == 0) {
            format = String.format(Locale.CHINESE, "%d + %d = %d", firstNum, secondNum, firstNum + secondNum);
        } else {
            format = String.format(Locale.CHINESE, "%d + %d + %d = %d", firstNum, secondNum, thirdNum, firstNum + secondNum + thirdNum);
        }
        if (optionTextView.getVisibility() == View.INVISIBLE) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(450);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    optionTextView.setVisibility(View.VISIBLE);
                    optionTextView.setText(format);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            optionTextView.startAnimation(alphaAnimation);
        } else {
            FlipCardAnimation.startAnimation(flipCardAnimation, optionTextView, -180, format, 450);
        }
    }

    @Override
    public void dismissOption() {
        if (optionTextView.getVisibility() == View.VISIBLE) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(400);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    optionTextView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            optionTextView.startAnimation(alphaAnimation);
        }
    }
}
