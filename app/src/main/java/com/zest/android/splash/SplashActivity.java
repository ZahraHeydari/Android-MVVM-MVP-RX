package com.zest.android.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zest.android.R;
import com.zest.android.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * To display the splash screen
 *
 * @author ZARA
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 1500;
    @BindView(R.id.splash_image_view)
    ImageView mImageView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        mImageView.setAnimation(zoomIn);
        mImageView.startAnimation(zoomIn);

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
