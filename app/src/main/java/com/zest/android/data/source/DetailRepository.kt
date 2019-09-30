package com.zest.android.data.source

import android.util.Log
import com.zest.android.data.Recipe
import com.zest.android.data.source.local.DatabaseManager
import com.zest.android.data.source.remote.APIServices
import com.zest.android.data.source.remote.ServiceGenerator

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 * @Author ZARA.
 */
class DetailRepository{

    private val TAG = DetailRepository::class.java.name
    private val mApiServices: APIServices
    private val mDatabaseManager: DatabaseManager by lazy { DatabaseManager.getInstance() }

    init {
        mApiServices = ServiceGenerator.createService(APIServices::class.java)
    }

    fun updateRecipe(recipe: Recipe) {
        mDatabaseManager.recipeDao.update(recipe)
    }

    fun insertFavorite(recipe: Recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [$recipe]")
        mDatabaseManager.recipeDao.insert(recipe)
    }

    fun removeFavorite(recipe: Recipe) {
        Log.d(TAG, "removeFavorite() called with: recipe = [$recipe]")
        mDatabaseManager.recipeDao.delete(recipe)
    }

    fun getFavoriteByRecipeId(recipe: Recipe): Recipe? {
        return mDatabaseManager.recipeDao.loadOneByRecipeId(recipe.recipeId)
    }
}