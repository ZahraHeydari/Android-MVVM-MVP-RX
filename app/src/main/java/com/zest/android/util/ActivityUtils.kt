package com.zest.android.util

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * This provides methods to help Activities load their UI.
 *
 * Created by ZARA on 09/25/2018.
 */
object ActivityUtils {

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }

    /**
     * The `fragment` is replaced with the container view with id `frameId`. The operation
     * is performed by the `fragmentManager`.
     *
     * @param fragmentManager
     * @param fragment
     * @param tag
     * @param frameId
     */
    fun replaceFragmentInActivity(fragmentManager: FragmentManager,
                                  fragment: Fragment,
                                  tag: String,
                                  frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        fragmentManager.beginTransaction()
                .replace(frameId, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }
}
