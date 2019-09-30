package com.zest.android.favorite

import com.zest.android.BasePresenter
import com.zest.android.BaseView
import com.zest.android.data.Recipe

/**
 * This specifies the contract between the view and the presenter.
 *
 *
 * Created by ZARA on 8/10/2018.
 */
interface FavoriteContract {


    /**
     * The view of [FavoriteFragment]
     */
    interface View : BaseView<UserActionsListener> {


        /**
         * To update [Recipe]
         *
         * @param recipe
         */
        fun deleteFavorite(recipe: Recipe)


        /**
         * To go to [com.zest.android.detail.DetailActivity]
         *
         * @param recipe
         */
        fun gotoDetailPage(recipe: Recipe)

    }


    /**
     * To handle all interactions that is related to [FavoriteFragment]
     */
    interface UserActionsListener : BasePresenter {

        /**
         * To load all favorites
         */
        fun loadFavorites(): List<Recipe>?


        /**
         * To update [Recipe]
         *
         * @param recipe
         */
        fun deleteFavoriteRecipe(recipe: Recipe)

    }
}