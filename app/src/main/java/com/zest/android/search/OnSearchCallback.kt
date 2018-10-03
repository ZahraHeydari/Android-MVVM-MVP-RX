package com.zest.android.search

import com.zest.android.data.Recipe

interface OnSearchCallback {

    /**
     * To set search result [Recipe]s in [SearchActivity]
     *
     * @param recipes
     */
    fun setResult(recipes: List<Recipe>)

    /**
     * To show the detail of search item[Recipe] in [com.zest.android.detail.DetailActivity]
     *
     * @param recipe
     */
    fun gotoDetailPage(recipe: Recipe)

    /**
     * To show/hide the EmptyView of [SearchActivity] by visibility
     *
     * @param visibility
     */
    fun showEmptyView(visibility: Boolean)

    /**
     * When the result of search is NULL!
     */
    fun noResult()
}
