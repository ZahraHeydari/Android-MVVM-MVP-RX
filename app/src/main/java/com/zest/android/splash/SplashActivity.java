package com.zest.android.splash;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zest.android.R;
import com.zest.android.databinding.ActivitySplashBinding;
import com.zest.android.home.HomeActivity;

/**
 * To display the splash screen
 *
 * @author ZARA
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 1500;
    private Handler mHandler;
    private ActivitySplashBinding activitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        activitySplashBinding.splashImageView.setAnimation(zoomIn);
        activitySplashBinding.splashImageView.startAnimation(zoomIn);

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                HomeActivity.start(SplashActivity.this);
                finish();
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.sendEmptyMessageDelayed(1, SPLASH_DISPLAY_LENGTH);
    }

}
