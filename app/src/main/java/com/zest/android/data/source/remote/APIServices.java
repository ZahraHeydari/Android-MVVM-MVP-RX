package com.zest.android.data.source.remote;

import com.zest.android.data.CategoryResponse;
import com.zest.android.data.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * All Api services must specific in this interface
 *
 * @Author ZARA.
 */
public interface APIServices {


    /**
     * get list of latest recipes
     */
    @GET("latest.php")
    Call<RecipeResponse> getRecipes();


    /**
     * To get {@link com.zest.android.data.Recipe} detail by id
     *
     * @param recipeId
     * @return
     */
    @GET("lookup.php")
    Call<RecipeResponse> getRecipeDetailById(@Query("i") String recipeId);

    /**
     * To search
     *
     * @param query
     * @return
     */
    @GET("search.php")
    Call<RecipeResponse> search(@Query("s") String query);


    /**
     * To get List all meal categories
     *
     * @return
     */
    @GET("categories.php")
    Call<CategoryResponse> getCategories();

}

