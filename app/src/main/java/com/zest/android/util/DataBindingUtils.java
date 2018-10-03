package com.zest.android.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zest.android.R;

public class DataBindingUtils {

    private static final String TAG = DataBindingUtils.class.getSimpleName();

    @BindingAdapter(value = "onLoadImage")
    public static void onLoadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.color.whiteSmoke)
                .into(imageView);
    }


    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

}