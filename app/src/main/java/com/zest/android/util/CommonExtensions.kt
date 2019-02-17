package com.zest.android.util

import android.support.design.widget.Snackbar
import android.view.View

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
}