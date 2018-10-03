package com.zest.android.data.source

import android.util.Log
import com.zest.android.data.Recipe
import com.zest.android.data.RecipeResponse
import com.zest.android.data.source.local.DatabaseManager
import com.zest.android.data.source.remote.APIServices
import com.zest.android.data.source.remote.ServiceGenerator
import com.zest.android.home.HomePresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 *
 * @Author ZARA.
 */
class HomeRepository {

    private val mApiServices: APIServices
    private val mDatabaseManager: DatabaseManager by lazy { DatabaseManager.getInstance() }

    init {
        mApiServices = ServiceGenerator.createService(APIServices::class.java)
    }

    /**
     * To load all [Recipe]s
     *
     * @param recipesCallbackImp
     */
    fun loadRecipeList(recipesCallbackImp: HomePresenter.RecipesCallbackImp) {
        mApiServices.getRecipes().enqueue(RecipesResponseCallback(recipesCallbackImp))
    }

    /**
     * To load favorite[Recipe] by id
     *
     * @param recipe
     * @return
     */
    fun getFavoriteByRecipeId(recipe: Recipe): Recipe? {
        Log.d(TAG, "getFavoriteByRecipeId() called with: recipe = [$recipe]")
        return mDatabaseManager.recipeDao.loadOneByRecipeId(recipe.recipeId)
    }

    fun deleteFavorite(recipe: Recipe) {
        Log.d(TAG, "deleteFavorite() called with: recipe = [$recipe]")
        mDatabaseManager.recipeDao.delete(recipe)
    }

    fun insertFavorite(recipe: Recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [$recipe]")
        mDatabaseManager.recipeDao.insert(recipe)
    }

    private inner class RecipesResponseCallback(private val recipesCallbackImp: HomePresenter.RecipesCallbackImp)
        : Callback<RecipeResponse> {

        override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
            Log.d(TAG, "getRecipes onResponse() called with: call = [" + call
                    + "], response = [" + response.raw() + "]")
            if (response.isSuccessful()) {
                Log.i(TAG, "getRecipes onResponseBody: " + response.body()!!)
                val meals = response.body()
                if (meals != null && !meals.recipes.isEmpty()) {
                    recipesCallbackImp.onLoadList(meals.recipes)
                }
            } else {
                Log.e(TAG, "getRecipes onResponseError ")
            }
        }

        override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
            t.printStackTrace()
        }
    }

    companion object {

        private val TAG = HomeRepository::class.java.simpleName
    }
}
