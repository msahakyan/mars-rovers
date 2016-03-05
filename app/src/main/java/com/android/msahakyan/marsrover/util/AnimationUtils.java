package com.android.msahakyan.marsrover.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.view.View;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class AnimationUtils {

    public static final long ANIMATION_DURATION = 400;

    private static final int MAX_LEVEL = 10000;
    private static final int MID_MAX_LEVEL = MAX_LEVEL / 2;

    public static Animator rotateDrawable(final RotateDrawable drawable, final boolean rewind) {

        ValueAnimator animator = ValueAnimator.ofFloat(0.f, 1.f);
        animator.addUpdateListener(animation -> {
            int level = (int) (MID_MAX_LEVEL * animation.getAnimatedFraction() + (rewind ? MID_MAX_LEVEL : 0));
            drawable.setLevel(level);
        });
        animator.setDuration(ANIMATION_DURATION);
        return animator;
    }

    /**
     * Turns on hardware layer on animation start. Needed for smoother animations.
     */
    public static class LayerAnimationAdapter extends AnimatorListenerAdapter {

        private View mTargetView;
        private int mViewLayerType;

        public LayerAnimationAdapter(View targetView) {
            mTargetView = targetView;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mViewLayerType = mTargetView.getLayerType();
            mTargetView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mTargetView.setLayerType(mViewLayerType, null);
        }
    }
}
