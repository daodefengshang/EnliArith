package com.szh.enliarith.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Property;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.szh.enliarith.R;
import com.szh.enliarith.utils.ChildViewHelper;
import com.szh.enliarith.utils.DensityUtil;
import com.szh.enliarith.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by szh on 2017/4/26.
 */
public class AbacusActivity extends AppCompatActivity {

    private final Handler mHideHandler = new Handler();

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            mHideHandler.postDelayed(mBorderAndSetBeadRunnable, 200);
        }
    };

    private final Runnable mBorderAndSetBeadRunnable = new Runnable() {
        @Override
        public void run() {
            getBorderAndSetBead(view.getWidth(), view.getHeight());
        }
    };

    private void getBorderAndSetBead(int width, int height) {
        float bitmapWidth, bitmapHeight;
        if (width * 364 < height * 756) {
            bitmapWidth = width;
            bitmapHeight = width * 364f / 756;
        } else {
            bitmapWidth = height * 756f / 364;
            bitmapHeight = height;
        }
        float beadWidth = bitmapWidth * 50 / 756;
        beadHeight = beadWidth * 41 / 79;
        yBorders[0] = (height - bitmapHeight) / 2 + bitmapHeight * 17 / 364 - 1;
        yBorders[1] = (height - bitmapHeight) / 2 + bitmapHeight * 118 / 364 - (beadHeight - 1);
        yBorders[2] = (height - bitmapHeight) / 2 + bitmapHeight * 138 / 364 - 1;
        yBorders[3] = (height - bitmapHeight) / 2 + bitmapHeight * 344 / 364 - (beadHeight - 1);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int)(beadWidth + 0.5f), (int)(beadHeight + 0.5f));
        for (int i = 0; i < 13 * 7; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.bead);
            beadView.addView(imageView);
            imageView.setTranslationX((i / 7 * 53.75f + 57) * bitmapWidth / 756 - beadWidth / 2 + (width - bitmapWidth) / 2);
            if (i % 7 < 2) {
                imageView.setTranslationY(yBorders[0] + (i % 7) * (beadHeight - 1));
            } else {
                imageView.setTranslationY(yBorders[3] - (6 - i % 7) * (beadHeight - 1));
            }
        }
    }

    private float[] yBorders = new float[4];
    private float beadHeight;
    private View view;
    private FrameLayout beadView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abacus);
        view = findViewById(R.id.image_view);
        beadView = (FrameLayout) findViewById(R.id.frame_bead);
        hide();
        gestureDetector = new GestureDetector(this, new OnGestureListener());
        beadView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void hide() {
        mHideHandler.post(mHidePart2Runnable);
    }



    private class OnGestureListener extends GestureDetector.SimpleOnGestureListener {

        private int childViewIndex;
        private float translationY;

        @Override
        public boolean onDown(MotionEvent e) {
            childViewIndex = ChildViewHelper.findChildViewUnder(beadView, e.getX(), e.getY());
            if (childViewIndex != -1) {
                View view = beadView.getChildAt(childViewIndex);
                translationY =  view.getTranslationY();
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (childViewIndex != -1) {
                try {
                    View view = beadView.getChildAt(childViewIndex);
                    float v = translationY + e2.getY() - e1.getY();
                    switch (childViewIndex % 7) {
                        case 0:
                            if (v < yBorders[0]) {
                                v = yBorders[0];
                            } else if (v > yBorders[1] - (beadHeight - 1)) {
                                v = yBorders[1] - (beadHeight - 1);
                            }
                            view.setTranslationY(v);
                            if (e2.getY() - e1.getY() > 0) {
                                toDown(1);
                            }
                            break;
                        case 1:
                            if (v < yBorders[0] + (beadHeight - 1)) {
                                v = yBorders[0] + (beadHeight - 1);
                            } else if (v > yBorders[1]) {
                                v = yBorders[1];
                            }
                            view.setTranslationY(v);
                            if (e2.getY() - e1.getY() < 0) {
                                toUp(1);
                            }
                            break;
                        case 2:
                            if (v < yBorders[2]) {
                                v = yBorders[2];
                            } else if (v > yBorders[3] - 4 * (beadHeight - 1)) {
                                v = yBorders[3] - 4 * (beadHeight - 1);
                            }
                            view.setTranslationY(v);
                            if (e2.getY() - e1.getY() > 0) {
                                toDown(4);
                            }
                            break;
                        case 3:
                            if (v < yBorders[2] + (beadHeight - 1)) {
                                v = yBorders[2] + (beadHeight - 1);
                            } else if (v > yBorders[3] - 3 * (beadHeight - 1)) {
                                v = yBorders[3] - 3 * (beadHeight - 1);
                            }
                            view.setTranslationY(v);
                            if (e2.getY() - e1.getY() > 0) {
                                toDown(3);
                            } else {
                                toUp(1);
                            }
                            break;
                        case 4:
                            if (v < yBorders[2] + 2 * (beadHeight - 1)) {
                                v = yBorders[2] + 2 * (beadHeight - 1);
                            } else if (v > yBorders[3] - 2 * (beadHeight - 1)) {
                                v = yBorders[3] - 2 * (beadHeight - 1);
                            }
                            view.setTranslationY(v);
                            if (e2.getY() - e1.getY() > 0) {
                                toDown(2);
                            } else {
                                toUp(2);
                            }
                            break;
                        case 5:
                            if (v < yBorders[2] + 3 * (beadHeight - 1)) {
                                v = yBorders[2] + 3 * (beadHeight - 1);
                            } else if (v > yBorders[3] - (beadHeight - 1)) {
                                v = yBorders[3] - (beadHeight - 1);
                            }
                            view.setTranslationY(v);
                            if (e2.getY() - e1.getY() > 0) {
                                toDown(1);
                            } else {
                                toUp(3);
                            }
                            break;
                        case 6:
                            if (v < yBorders[2] + 4 * (beadHeight - 1)) {
                                v = yBorders[2] + 4 * (beadHeight - 1);
                            } else if (v > yBorders[3]) {
                                v = yBorders[3];
                            }
                            view.setTranslationY(v);
                            if (e2.getY() - e1.getY() < 0) {
                                toUp(4);
                            }
                            break;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (childViewIndex != -1 || DensityUtil.px2dip(AbacusActivity.this, Math.abs(velocityY)) > 1000000) {
                View view = beadView.getChildAt(childViewIndex);
                try {
                    switch (childViewIndex % 7) {
                        case 0:
                            if (velocityY < 0) {
                                view.setTranslationY(yBorders[0]);
                            } else {
                                view.setTranslationY(yBorders[1] - (beadHeight - 1));
                                toDown(1);
                            }
                            break;
                        case 1:
                            if (velocityY < 0) {
                                view.setTranslationY(yBorders[0] + (beadHeight - 1));
                                toUp(1);
                            } else {
                                view.setTranslationY(yBorders[1]);
                            }
                            break;
                        case 2:
                            if (velocityY < 0) {
                                view.setTranslationY(yBorders[2]);
                            } else {
                                view.setTranslationY(yBorders[3] - 4 * (beadHeight - 1));
                                toDown(4);
                            }
                            break;
                        case 3:
                            if (velocityY < 0) {
                                view.setTranslationY(yBorders[2] + (beadHeight - 1));
                                toUp(1);
                            } else {
                                view.setTranslationY(yBorders[3] - 3 * (beadHeight - 1));
                                toDown(3);
                            }
                            break;
                        case 4:
                            if (velocityY < 0) {
                                view.setTranslationY(yBorders[2] + 2 * (beadHeight - 1));
                                toUp(2);
                            } else {
                                view.setTranslationY(yBorders[3] - 2 * (beadHeight - 1));
                                toDown(2);
                            }
                            break;
                        case 5:
                            if (velocityY < 0) {
                                view.setTranslationY(yBorders[2] + 3 * (beadHeight - 1));
                                toUp(3);
                            } else {
                                view.setTranslationY(yBorders[3] - (beadHeight - 1));
                                toDown(1);
                            }
                            break;
                        case 6:
                            if (velocityY < 0) {
                                view.setTranslationY(yBorders[2] + 4 * (beadHeight - 1));
                                toUp(4);
                            } else {
                                view.setTranslationY(yBorders[3]);
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        private void toDown(int n) {
            for (int i = 0; i < n; i++) {
                View view1 = beadView.getChildAt(childViewIndex + i + 1);
                View view0 = beadView.getChildAt(childViewIndex + i);
                if (view0 != null && view1 != null
                        && view1.getTranslationY() - view0.getTranslationY() < (beadHeight - 1)) {
                    view1.setTranslationY(view0.getTranslationY() + (beadHeight - 1));
                }
            }
        }

        private void toUp(int n) {
            for (int i = 0; i < n; i++) {
                View view0 = beadView.getChildAt(childViewIndex - i);
                View view1 = beadView.getChildAt(childViewIndex - i - 1);
                if (view0 != null && view1 != null
                        && view0.getTranslationY() - view1.getTranslationY() < (beadHeight - 1)) {
                    view1.setTranslationY(view0.getTranslationY() - (beadHeight - 1));
                }
            }
        }
    }
}
