package com.zest.android.data.source.remote

import com.zest.android.data.CategoryResponse
import com.zest.android.data.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * All Api services must specific in this interface
 *
 * @Author ZARA.
 */
interface APIServices{


    /**
     * To get list of latest [Recipe]s
     */
    @GET("latest.php")
    fun getRecipes(): Call<RecipeResponse>


    /**
     * To get [com.zest.android.data.Recipe] detail by id
     *
     * @param recipeId
     * @return
     */
    @GET("lookup.php")
    fun getRecipeDetailById(@Query("i") recipeId: String): Call<RecipeResponse>

    /**
     * To search
     *
     * @param query
     * @return
     */
    @GET("search.php")
    fun search(@Query("s") query: String): Call<RecipeResponse>


    /**
     * To get List all meal categories
     *
     * @return
     */
    @GET("categories.php")
    fun getCategories(): Call<CategoryResponse>
}