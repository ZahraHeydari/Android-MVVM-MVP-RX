package com.zest.android.favorite

import com.zest.android.data.Recipe

/**
 * To make an interaction between [FavoriteFragment] and [com.zest.android.list.ListActivity]
 *
 * Created by ZARA on 09/25/2018.
 */
interface OnFavoriteFragmentInteractionListener {

    /**
     * To update the title of [com.zest.android.list.ListActivity]
     *
     * @param resId
     */
    fun updateToolbarTitle(resId: Int)

    /**
     * To go to [com.zest.android.detail.DetailActivity]
     *
     * @param recipe
     */
    fun gotoDetailPage(recipe: Recipe)
}
