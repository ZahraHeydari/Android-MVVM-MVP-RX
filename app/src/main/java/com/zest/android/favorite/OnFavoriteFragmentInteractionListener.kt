package com.zest.android.favorite

import com.zest.android.data.Recipe

/**
 * To make an interaction between [FavoriteFragment] and
 * [com.zest.android.list.ListActivity]
 *
 * Created by ZARA on 8/10/2018.
 */
interface OnFavoriteFragmentInteractionListener {

    /**
     * to go to [com.zest.android.detail.DetailActivity]
     *
     * @param recipe
     */
    fun gotoDetailPage(recipe: Recipe)


    /**
     * to update [Recipe]
     *
     * @param recipe
     */
    fun showDeleteFavoriteDialog(recipe: Recipe)

}
