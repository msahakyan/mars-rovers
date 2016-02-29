package com.android.msahakyan.marsrover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.android.msahakyan.marsrover.R;
import com.android.msahakyan.marsrover.service.RoverDataLoaderService;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 4000;
    private static final int ANIMATION_SET_DURATION = 6000;
    private final Handler handler = new Handler();
    private ImageView mSplashImage;

    private final Runnable startActivityRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mSplashImage = (ImageView) findViewById(R.id.splash_image);

        startRoverDataLoaderService();
    }

    private void startRoverDataLoaderService() {
        Intent intent = new Intent(this, RoverDataLoaderService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AnimationSet set = new AnimationSet(true);
        initFadeInAnimation(set);
        initFadeOutAnimation(set);
        mSplashImage.startAnimation(set);

        handler.postDelayed(startActivityRunnable, ANIMATION_SET_DURATION);
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(startActivityRunnable);
    }

    private void initFadeInAnimation(AnimationSet set) {
        Animation fadeIn = FadeIn(ANIMATION_DURATION);
        fadeIn.setStartOffset(0);
        set.addAnimation(fadeIn);
    }

    private void initFadeOutAnimation(AnimationSet set) {
        Animation fadeOut = FadeOut(ANIMATION_DURATION);
        fadeOut.setStartOffset(ANIMATION_DURATION);
        set.addAnimation(fadeOut);
    }

    private Animation FadeIn(int t) {
        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(t);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        return fadeIn;
    }

    private Animation FadeOut(int t) {
        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(t);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        return fadeOut;
    }
}
