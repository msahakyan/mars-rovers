package com.android.msahakyan.marsrover.speedDial;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.util.AnimationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SpeedDialElementView extends LinearLayout {

    private static final long ANIMATION_DURATION = AnimationUtils.ANIMATION_DURATION;

    @Bind(R.id.view_action_label)
    TextView mLabelView;

    @Bind(R.id.view_action_button)
    FloatingActionButton mActionButton;

    private Interpolator mInterpolator = new AccelerateInterpolator();

    public SpeedDialElementView(Context context) {
        this(context, null);
    }

    public SpeedDialElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SpeedDialElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpeedDialElementView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);

        LayoutInflater.from(context).inflate(R.layout.view_speed_dial_element, this, true);
        ButterKnife.bind(this);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SpeedDialElementView);

        Drawable buttonIcon = styledAttributes.getDrawable(R.styleable.SpeedDialElementView_buttonIcon);
        ColorStateList buttonTint = styledAttributes.getColorStateList(R.styleable.SpeedDialElementView_buttonTintColor);
        String labelText = styledAttributes.getString(R.styleable.SpeedDialElementView_labelText);

        mLabelView.setText(labelText);
        mActionButton.setBackgroundTintList(buttonTint);
        if (buttonIcon != null) {
            mActionButton.setImageDrawable(buttonIcon);
        }

        styledAttributes.recycle();
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        super.setOnClickListener(listener);
        mActionButton.setOnClickListener(listener);
        mLabelView.setOnClickListener(listener);
    }

    public void show(int anchorY) {
        animateShow(anchorY);
    }

    public void hide(int anchorY) {
        animateHide(anchorY);
    }

    private void animateShow(int anchorY) {

        animate().cancel();

        setAlpha(0.f);
        setTranslationY(anchorY);

        animate().alpha(1.f)
            .translationY(0.f)
            .setInterpolator(mInterpolator)
            .setDuration(ANIMATION_DURATION)
            .setListener(new AnimationUtils.LayerAnimationAdapter(this));

        actionButtonAnimateShow();
    }

    private void animateHide(int anchorY) {

        animate().cancel();

        setAlpha(1.f);
        setTranslationY(0.f);

        animate().alpha(0.f)
            .translationY(anchorY)
            .setInterpolator(mInterpolator)
            .setDuration(ANIMATION_DURATION)
            .setListener(new AnimationUtils.LayerAnimationAdapter(this));

        actionButtonAnimateHide();
    }

    private void actionButtonAnimateShow() {

        mActionButton.animate().cancel();

        mActionButton.setScaleX(.5f);
        mActionButton.setScaleY(.5f);
        mActionButton.setAlpha(0.f);

        ViewPropertyAnimator animator = mActionButton.animate().scaleX(1.f).scaleY(1.f).alpha(1.f)
            .setInterpolator(mInterpolator)
            .setDuration(ANIMATION_DURATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator.setListener(new AnimationDisableElevationAdapter(mActionButton));
        }
    }

    private void actionButtonAnimateHide() {

        mActionButton.animate().cancel();

        mActionButton.setScaleX(1.f);
        mActionButton.setScaleY(1.f);
        mActionButton.setAlpha(1.f);

        // to avoid 'cut off' shadow effect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActionButton.setElevation(0.f);
        }

        mActionButton.animate().scaleX(.5f).scaleY(.5f).alpha(0.f)
            .setInterpolator(mInterpolator)
            .setDuration(ANIMATION_DURATION);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static class AnimationDisableElevationAdapter extends AnimatorListenerAdapter {

        private final View mElevatedView;
        private final float mElevation;

        public AnimationDisableElevationAdapter(View elevatedView) {
            mElevatedView = elevatedView;
            mElevation = mElevatedView.getElevation();
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mElevatedView.setElevation(0.f);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mElevatedView.setElevation(mElevation);
        }
    }
}
