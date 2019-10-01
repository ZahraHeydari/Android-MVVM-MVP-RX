package com.zest.android.list

import com.zest.android.data.Recipe

interface OnListCallback {

    /**
     * To update the action bar title of [ListActivity]
     *
     * @param resId
     */
    fun updateActionBarTitle(resId: Int)

    /**
     * To go to [com.zest.android.detail.DetailActivity] by [Recipe]
     * from [com.zest.android.favorite.FavoriteFragment]
     *
     * @param recipe
     */
    fun gotoDetailPage(recipe: Recipe)
}
