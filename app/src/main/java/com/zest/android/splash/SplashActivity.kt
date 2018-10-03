package com.zest.android.splash

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * To display the splash screen
 *
 * @author ZARA
 */
class SplashActivity : LifecycleLoggingActivity() {

    private lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        splash_image_view.setAnimation(zoomIn)
        splash_image_view.startAnimation(zoomIn)

        mHandler = Handler(Handler.Callback {
            HomeActivity.start(this@SplashActivity)
            finish()
            false
        })
    }

    override fun onStart() {
        super.onStart()
        mHandler.sendEmptyMessageDelayed(1, SPLASH_DISPLAY_LENGTH)
    }

    companion object {
        private val SPLASH_DISPLAY_LENGTH: Long = 1500
    }

}