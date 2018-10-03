package com.zest.android.util

import android.content.Context
import android.graphics.Typeface
import java.util.HashMap

/**
 * to Create cache for fonts from assets and use in the customized views
 * NOTICE: don't use this below code to make and set Typeface
 * Typeface.createFromAsset(context.getAssets(),FONTS_PATH + "IRAN_Sans.ttf");
 * because it may raise [OutOfMemoryError].
 *
 * Created by ZARA on 09/25/2018.
 */
object FontCache {

    private val fontCache = HashMap<String, Typeface>()

    /**
     * to store and fetch [Typeface], create [HashMap] that contains
     * fontName keys and [Typeface] values and have ability fetch from stored items
     * instead of make an new instance for each fontName.
     *
     * @param context
     * @param fontName
     * @return
     */
    fun getTypeface(context: Context, fontName: String): Typeface? {
        var fontName = fontName
        fontName = "fonts/$fontName"
        var typeface: Typeface? = fontCache[fontName]

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.assets, fontName)
            } catch (e: Exception) {
                return null
            }

            fontCache[fontName] = typeface
        }
        return typeface
    }
}