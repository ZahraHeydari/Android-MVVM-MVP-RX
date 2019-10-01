package com.zest.android.splash

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.zest.android.R
import com.zest.android.databinding.ActivitySplashBinding
import com.zest.android.home.HomeActivity

/**
 * To display the splash screen
 *
 * @author ZARA
 */
class SplashActivity : AppCompatActivity() {


    private val SPLASH_DISPLAY_LENGTH: Long = 1500
    private var mHandler: Handler? = null
    private var activitySplashBinding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        activitySplashBinding?.splashImageView?.animation = zoomIn
        activitySplashBinding?.splashImageView?.startAnimation(zoomIn)

        mHandler = Handler(Handler.Callback {
            HomeActivity.start(this@SplashActivity)
            finish()
            false
        })

    }

    override fun onStart() {
        super.onStart()
        mHandler?.sendEmptyMessageDelayed(1, SPLASH_DISPLAY_LENGTH)
    }

}
