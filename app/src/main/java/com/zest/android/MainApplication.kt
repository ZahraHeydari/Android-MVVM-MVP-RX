package com.zest.android

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import kotlin.properties.Delegates


/**
 * @Author ZARA.
 */
class MainApplication : Application() {

    companion object {

        private val TAG = MainApplication::class.java.simpleName
        var instance: MainApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        Fabric.with(this, Crashlytics())

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AtlantaBook.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    /**
     * this method called when phone [Configuration] changed like
     * Locale,Rotation,LayoutDirection
     *
     * @param newConfig
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}