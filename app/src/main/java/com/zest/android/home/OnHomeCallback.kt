package com.zest.android.home

import com.zest.android.data.Category
import com.zest.android.data.Recipe


/**
 * To make interaction between [HomeActivity] and its child
 *
 * Created by ZARA on 09/25/2018.
 */
interface OnHomeCallback {

    /**
     * To hide fab in [HomeActivity]
     */
    fun hideFab()

    /**
     * To show fab in [HomeActivity]
     */
    fun showFab()

    /**
     * To go to [com.zest.android.detail.DetailActivity] by [Recipe]
     *
     * @param recipe
     */
    fun gotoDetailPage(recipe: Recipe)

    /**
     * To show a list of category sub by title of [Category]
     * in [com.zest.android.search.SearchActivity]
     *
     * @param categoryTitle
     */
    fun showSubCategoriesByCategoryTitle(categoryTitle: String)
}