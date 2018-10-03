package com.zest.android.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * to Create cache for fonts from assets and use in the customized views
 * NOTICE: don't use this below code to make and set Typeface
 * Typeface.createFromAsset(context.getAssets(),FONTS_PATH + "IRAN_Sans.ttf");
 * because it may raise {@link OutOfMemoryError}.
 *
 * Created by ZARA on 8/10/2018.
 */
public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    /**
     * to store and fetch {@link Typeface}, create {@link HashMap} that contains
     * fontName keys and {@link Typeface} values and have ability fetch from stored items
     * instead of make an new instance for each fontName.
     *
     * @param context
     * @param fontName
     * @return
     */
    public static Typeface getTypeface(Context context, String fontName) {
        fontName = "fonts/" + fontName;
        Typeface typeface = fontCache.get(fontName);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontName, typeface);
        }
        return typeface;
    }
}