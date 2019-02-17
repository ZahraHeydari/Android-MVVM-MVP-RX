package com.zest.android.category

import com.zest.android.data.Category
import com.zest.android.data.source.CategoryRepository

/**
 * Listens to user actions from [CategoryFragment],
 * retrieves the data and updates the UI as required.
 *
 * Created by ZARA on 09/24/2018.
 */
class CategoryPresenter(private val categoryView: CategoryContract.View,
                        private val categoryRepository: CategoryRepository) : CategoryContract.UserActionsListener {

    init {
        categoryView.setPresenter(this)
    }

    override fun start() {

    }

    override fun loadCategories() {
        categoryView.showProgressBar(true)
        categoryRepository.loadRootCategories(CategoriesCallbackImp())
    }

    inner class CategoriesCallbackImp : OnCategoriesCallback {

        override fun loadData(categories: List<Category>) {

            categoryView.showProgressBar(false)
            categoryView.setResult(categories)
        }
    }

    interface OnCategoriesCallback {

        fun loadData(categories: List<Category>)
    }
}