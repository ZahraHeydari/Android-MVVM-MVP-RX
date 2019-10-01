package com.zest.android.util

import android.databinding.BindingAdapter
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.zest.android.R
import com.zest.android.home.RecipeViewModel


object DataBindingUtils {

    private val TAG = DataBindingUtils::class.java.simpleName


    @BindingAdapter("onLoadImage")
    @JvmStatic
    fun onLoadImage(imageView: ImageView, url: String) {
        Picasso.with(imageView.context)
                .load(url)
                .placeholder(R.color.whiteSmoke)
                .into(imageView)
    }

    @BindingAdapter("setFloatingActionButtonSrc")
    @JvmStatic
    fun setFloatingActionButtonSrc(fab: FloatingActionButton, recipe: RecipeViewModel) {
        Log.d(TAG, "setFloatingActionButtonSrc (line 28): ${recipe.getIsDetailFavorite()}")
        if (recipe.getIsDetailFavorite()) fab.setImageResource(R.drawable.ic_star_full_vector)
        else fab.setImageResource(R.drawable.ic_star_empty_white_vector)
    }
}