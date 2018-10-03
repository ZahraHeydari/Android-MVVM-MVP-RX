package com.zest.android.detail

/**
 * To make an interaction between [DetailActivity] and [RecipeViewModel]
 * */
interface OnDetailCallback {

    fun setFavoriteIcon(drawableRes: Int)

    fun showMessage(stringRes: Int)
}
