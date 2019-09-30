package com.zest.android.data.source

import android.util.Log
import com.zest.android.data.Recipe
import com.zest.android.data.source.local.DatabaseManager

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 *
 * @Author ZARA.
 */
class FavoriteRepository{

    private val TAG = FavoriteRepository::class.java.name
    private val mDatabaseManager: DatabaseManager by lazy { DatabaseManager.getInstance() }

    /**
     * To load all [Recipe]s with isFavorite=true
     *
     * @return
     */
    fun loadAllFavorites(): List<Recipe>? {
        return mDatabaseManager.recipeDao.loadAll()
    }

    /**
     * To delete [Recipe]
     *
     * @param recipe
     */
    fun deleteFavoriteRecipe(recipe: Recipe) {
        Log.d(TAG, "deleteFavoriteRecipe() called with: recipe = [$recipe]")
        mDatabaseManager.recipeDao.delete(recipe)
    }
}
