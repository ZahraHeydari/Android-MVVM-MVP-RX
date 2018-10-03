package com.zest.android.util

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.zest.android.R



object DataBindingUtils {

    private val TAG = DataBindingUtils::class.java.simpleName

    @BindingAdapter(value = "onLoadImage")
    @JvmStatic
    fun onLoadImage(imageView: ImageView, url: String) {
        Picasso.with(imageView.context)
                .load(url)
                .placeholder(R.color.whiteSmoke)
                .into(imageView)
    }

}