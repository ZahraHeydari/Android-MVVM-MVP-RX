package com.zest.android.home

import com.zest.android.data.Category
import com.zest.android.data.Recipe

/**
 * To make interaction between [HomeActivity] and its child
 *
 * Created by ZARA on 08/06/2018.
 */
interface OnHomeCallback {

    /**
     * To show/hide fab in [HomeFragment]
     *
     * @param visibility
     */
    fun showFab(visibility: Boolean)

    /**
     * to go to [com.zest.android.detail.DetailActivity] by [Recipe]
     *
     * @param recipe
     */
    fun gotoDetailPage(recipe: Recipe)

    /**
     * To show a list of the category subs by [Category]
     * in [com.zest.android.search.SearchActivity]
     *
     * @param category
     */
    fun showSubCategoriesByCategoryTitle(category: Category)

}
