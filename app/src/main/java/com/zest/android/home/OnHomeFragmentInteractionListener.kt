package com.zest.android.home

import com.zest.android.data.Recipe

/**
 * To make an interaction between [HomeFragment] and [RecipeViewModel]
 */
interface OnHomeFragmentInteractionListener {

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
    fun loadFavorite(recipe: Recipe): Recipe

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
     * To show/hide {#EmptyView} by visibility
     *
     * @param visibility
     */
    fun showEmptyView(visibility: Boolean)

}
