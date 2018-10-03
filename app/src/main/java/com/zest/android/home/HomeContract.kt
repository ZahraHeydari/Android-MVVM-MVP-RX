package com.zest.android.home

import com.zest.android.BasePresenter
import com.zest.android.BaseView
import com.zest.android.data.Recipe

/**
 * This specifies the contract between the view and the presenter.
 * <p>
 * Created by ZARA on 09/25/2018.
 */
interface HomeContract {

    /**
     * the view of [HomeActivity]
     */
    interface View : BaseView<UserActionsListener> {

        /**
         * to show the message of favorite
         *
         * @param message
         */
        fun showMessage(message: Int)

        /**
         * to go to [com.zest.android.detail.DetailActivity]
         *
         * @param recipe
         */
        fun gotoDetailPage(recipe: Recipe)

        /**
         * To load all [Recipe]s
         *
         * @param recipes
         */
        fun loadRecipes(recipes: List<Recipe>)

        /**
         * To load favorite [Recipe] from DB
         *
         * @param recipe
         * @return
         */
        fun loadFavorite(recipe: Recipe): Recipe?

        /**
         * To remove favorite recipe from db by [Recipe.recipeId]
         *
         * @param recipe
         */
        fun removeFavorite(recipe: Recipe)

        /**
         * To insert favorite recipe in db
         *
         * @param recipe
         */
        fun insertFavorite(recipe: Recipe)

        /**
         * To startWithFavorite loading progress bar in [HomeFragment]
         *
         * @param visibility
         */
        fun showProgressBar(visibility: Boolean)

        /**
         * To show/hide {#EmptyView} by visibility
         *
         * @param visibility
         */
        fun showEmptyView(visibility: Boolean)
    }

    /**
     * To handle all user actions that is related to [HomeActivity]
     */
    interface UserActionsListener : BasePresenter {

        /**
         * to get a list of [Recipe]
         *
         * @return
         */
        fun getRecipes()

        /**
         * To load favorite by [Recipe.recipeId] from DB
         *
         * @param recipe
         * @return
         */
        fun loadFavoriteByRecipeId(recipe: Recipe): Recipe?

        /**
         * To delete favorite by [Recipe.recipeId] from DB
         *
         * @param recipe
         */
        fun deleteFavoriteByRecipeId(recipe: Recipe)

        /**
         * To insert favorite [Recipe] in DB
         *
         * @param recipe
         */
        fun insertFavoriteRecipe(recipe: Recipe)
    }

}