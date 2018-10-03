package com.zest.android.category

import com.zest.android.data.Category

/**
 * To make an interaction between {@link CategoryFragment} and {@link CategoryViewModel}
 *
 * Created by ZARA on 09/30/2018.
 */
interface OnCategoryFragmentInteractionListener {

    fun showSubCategories(category: Category)

    fun setResult(categories: List<Category>)
}