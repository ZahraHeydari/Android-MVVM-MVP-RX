package com.zest.android.util;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * This provides methods to help Activities load their UI.
 *
 * Created by ZARA on 8/10/2018.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    @SuppressLint("RestrictedApi")
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    /**
     * The {@code fragment} is replaced with the container view with id {@code frameId}. The operation
     * is performed by the {@code fragmentManager}.
     *
     * @param fragmentManager
     * @param fragment
     * @param tag
     * @param frameId
     */
    @SuppressLint("RestrictedApi")
    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment,
                                                 String tag,
                                                 int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        fragmentManager.beginTransaction()
                .replace(frameId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }
}
