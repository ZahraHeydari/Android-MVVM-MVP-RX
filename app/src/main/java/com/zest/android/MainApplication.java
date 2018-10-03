package com.zest.android;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * @Author ZARA.
 */
public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        Fabric.with(this, new Crashlytics());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AtlantaBook.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * this method called when phone {@link Configuration} changed like
     * Locale,Rotation,LayoutDirection
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
