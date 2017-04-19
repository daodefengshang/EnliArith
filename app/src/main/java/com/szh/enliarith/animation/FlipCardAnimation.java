package com.szh.enliarith.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * A 3D Flip Card for Android
 */
public class FlipCardAnimation extends Animation{
    private final float mFromDegrees;

    private final float mToDegrees;

    private final float mCenterX;

    private final float mCenterY;

    private static Camera mCamera;
    //用于确定内容是否开始变化
    private boolean isContentChange = false;
    private OnContentChangeListener listener;
    public FlipCardAnimation(float fromDegrees, float toDegrees,

                             float centerX, float centerY) {

        mFromDegrees = fromDegrees;

        mToDegrees = toDegrees;

        mCenterX = centerX;

        mCenterY = centerY;
    }
    ////用于确定内容是否开始变化  在动画开始之前调用
    public void setCanContentChange(){
        this.isContentChange = false;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {

        super.initialize(width, height, parentWidth, parentHeight);
        if (mCamera == null)
        synchronized (FlipCardAnimation.class) {
            if (mCamera == null){
                mCamera = new Camera();
            }
        }
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        final float fromDegrees = mFromDegrees;

        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;

        final float centerY = mCenterY;

        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();

        if (degrees>90 || degrees<-90){
            if (!isContentChange){
                if(listener!=null){
                    listener.contentChange();
                }
                isContentChange = true;
            }

            if (degrees>0) {
                degrees = 270 + degrees - 90;
            }else if (degrees<0){
                degrees = -270+(degrees+90);
            }
        }

        camera.rotateX(degrees);

        camera.getMatrix(matrix);

        camera.restore();

        matrix.preTranslate(-centerX, -centerY);

        matrix.postTranslate(centerX, centerY);


    }
    public void setOnContentChangeListener(OnContentChangeListener listener) {
        this.listener = listener;
    }

    public interface OnContentChangeListener{
        void contentChange();
    }

    public static void startAnimation(FlipCardAnimation animation, final View view, int degree, final CharSequence text, long duration) {
        if (animation != null) {
            animation.setCanContentChange();
            view.startAnimation(animation);
        } else {
            int width = view.getWidth() / 2;
            int height = view.getHeight() / 2;
            animation = new FlipCardAnimation(0, degree, width, height);
            animation.setDuration(duration);
            animation.setFillAfter(false);
            animation.setRepeatCount(0);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    ((FlipCardAnimation)animation).setCanContentChange();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setOnContentChangeListener(new FlipCardAnimation.OnContentChangeListener() {
                @Override
                public void contentChange() {
                    if (view instanceof TextView) {
                        ((TextView) view).setText(text);
                    }
                }
            });
            view.startAnimation(animation);
        }
    }
}

