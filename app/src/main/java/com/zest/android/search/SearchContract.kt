package com.zest.android.search

import com.zest.android.BasePresenter
import com.zest.android.BaseView
import com.zest.android.data.Recipe

/**
 * This specifies the contract between the view and the presenter.
 *
 *
 * Created by ZARA on 8/18/2018.
 */
interface SearchContract {


    interface View : BaseView<UserActionsListener> {


        /**
         * to go to [com.zest.android.detail.DetailActivity]
         *
         * @param recipe
         */
        fun gotoDetailPage(recipe: Recipe)

        /**
         * To set search result in [SearchActivity]
         *
         * @param recipes
         */
        fun setResult(recipes: List<Recipe>)

        /**
         * To startWithFavorite loading progress bar in [SearchActivity]
         *
         * @param visibility
         */
        fun showProgressBar(visibility: Boolean)

        /**
         * To show EmptyView in [SearchActivity] when there is no data
         */
        fun showEmptyView()

    }


    interface UserActionsListener : BasePresenter {

        /**
         * To get all [Recipe]s by search query
         *
         * @param query
         * @return
         */
        fun searchQuery(query: String)

    }
}
