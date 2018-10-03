package com.zest.android.detail

import com.zest.android.BasePresenter
import com.zest.android.BaseView
import com.zest.android.data.Recipe

/**
 * This specifies the contract between the view and the presenter.
 *
 * Created by ZARA on 09/25/2018.
 */
interface DetailContract {

    interface View : BaseView<UserActionsListener> {

        /**
         * To go to [com.zest.android.search.SearchActivity] with tag
         *
         * @param tag
         */
        fun navigateToSearchWithTag(tag: String)
    }

    interface UserActionsListener : BasePresenter {

        /**
         * To update [Recipe]
         *
         * @param recipe
         */
        fun updateRecipe(recipe: Recipe)

        /**
         * To insert recipe as a favorite in DB
         *
         * @param recipe
         */
        fun insertToFavorite(recipe: Recipe)

        /**
         * To remove favorite recipe from DB
         *
         * @param recipe
         */
        fun removeFromFavorite(recipe: Recipe)

        /**
         * To load favorite recipe from DB
         *
         * @param recipe
         * @return
         */
        fun loadFavorite(recipe: Recipe): Recipe?

        /**
         * To load String array of [Recipe.tag]s
         *
         * @param recipe
         * @return
         */
        fun loadTags(recipe: Recipe): Array<String>?

        /**
         * To search tag in [com.zest.android.search.SearchActivity]
         *
         * @param tag
         */
        fun searchByTag(tag: String)
    }

}