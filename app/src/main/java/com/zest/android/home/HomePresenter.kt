package com.zest.android.home

import android.util.Log
import com.zest.android.data.Recipe
import com.zest.android.data.source.HomeRepository

/**
 * Listens to user actions from [HomeFragment], retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 08/06/2018.
 */
class HomePresenter(val homeView: HomeContract.View,
                    val homeRepository: HomeRepository) : HomeContract.UserActionsListener {

    init {
        homeView.setPresenter(this)
    }

    override fun start() {
        homeView.showEmptyView(false)
        getRecipes()
    }

    override fun getRecipes() {
        homeView.showProgressBar(true)
        homeView.showEmptyView(false)
        homeRepository.loadRecipeList(RecipesCallbackImp())
    }

    override fun loadFavoriteByRecipeId(recipe: Recipe): Recipe? {
        Log.d(TAG, "loadFavoriteByRecipeId() called with: recipe = [$recipe]")
        return homeRepository.getFavoriteByRecipeId(recipe)
    }

    override fun deleteFavoriteByRecipeId(recipe: Recipe) {
        Log.d(TAG, "deleteFavoriteByRecipeId() called with: recipe = [$recipe]")
        homeRepository.deleteFavorite(recipe)
    }

    override fun insertFavoriteRecipe(recipe: Recipe) {
        Log.d(TAG, "insertFavoriteRecipe() called with: recipe = [$recipe]")
        homeRepository.insertFavorite(recipe)
    }


    internal interface OnRecipesCallback {

        fun onLoadList(recipes: List<Recipe>)
    }

    inner class RecipesCallbackImp : OnRecipesCallback {

        override fun onLoadList(recipes: List<Recipe>) {
            homeView.showProgressBar(false)
            homeView.loadRecipes(recipes)
        }
    }

    companion object {

        private val TAG = HomePresenter::class.java.simpleName
    }
}
