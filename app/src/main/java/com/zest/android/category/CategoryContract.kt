package com.zest.android.category

import com.zest.android.BasePresenter
import com.zest.android.BaseView
import com.zest.android.data.Category


/**
 * This specifies the contract between the view and the presenter.
 *
 * Created by ZARA on 09/24/2018.
 */
interface CategoryContract {

    /**
     * The view of [CategoryFragment]
     */
    interface View : BaseView<UserActionsListener> {

        /**
         * To search sub of [Category] and show the result in [SearchActivityt]
         *
         * @param category
         */
        fun showSubCategories(category: Category)

        /**
         * To set result[category]s in [CategoryFragment]
         *
         * @param categories
         */
        fun setResult(categories: List<Category>)

        /**
         * To show progress bar in [CategoryFragment] before loading data
         *
         * @param visibility
         */
        fun showProgressBar(visibility: Boolean)
    }

    /**
     * To handle all user actions that are related to [CategoryFragment]
     */
    interface UserActionsListener : BasePresenter {


        /**
         * To load all [Category]s
         *
         * @return
         */
        fun loadCategories()

    }
}