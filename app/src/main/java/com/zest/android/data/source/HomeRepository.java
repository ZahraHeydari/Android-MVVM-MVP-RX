package com.zest.android.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zest.android.data.RecipeResponse;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.local.DatabaseManager;
import com.zest.android.data.source.remote.APIServices;
import com.zest.android.data.source.remote.ServiceGenerator;
import com.zest.android.home.HomePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 *
 * @Author ZARA.
 */
public class HomeRepository {

    private static final String TAG = HomeRepository.class.getSimpleName();
    private final Context context;
    private final APIServices mApiServices;
    private DatabaseManager mDatabaseManager;


    public HomeRepository(Context context) {
        this.context = context;
        mDatabaseManager = DatabaseManager.newInstance(context);
        mApiServices = ServiceGenerator.createService(APIServices.class);
    }


    /**
     * To load all {@link Recipe}s
     *
     * @param recipesCallbackImp
     */
    public void loadRecipeList(final HomePresenter.RecipesCallbackImp recipesCallbackImp) {
        mApiServices.getRecipes().enqueue(new RecipesResponseCallback(recipesCallbackImp));
    }

    /**
     * To load {@link Recipe}
     *
     * @param recipe
     * @return
     */
    public Recipe getFavoriteByRecipeId(Recipe recipe) {
        Log.d(TAG, "getFavoriteByRecipeId() called with: recipe = [" + recipe + "]");
        return mDatabaseManager.loadRecipe(recipe);
    }

    public void deleteFavorite(Recipe recipe) {
        Log.d(TAG, "deleteFavorite() called with: recipe = [" + recipe + "]");
        mDatabaseManager.deleteRecipe(recipe);
    }

    public void insertFavorite(Recipe recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [" + recipe + "]");
        mDatabaseManager.insertOrReplaceRecipe(recipe);
    }

    private class RecipesResponseCallback implements Callback<RecipeResponse> {

        private final HomePresenter.RecipesCallbackImp recipesCallbackImp;

        public RecipesResponseCallback(HomePresenter.RecipesCallbackImp recipesCallbackImp) {
            this.recipesCallbackImp = recipesCallbackImp;
        }

        @Override
        public void onResponse(@NonNull Call<RecipeResponse> call, @NonNull Response<RecipeResponse> response) {
            Log.d(TAG, "getRecipes onResponse() called with: call = [" + call
                    + "], response = [" + response.raw() + "]");
            if (response.isSuccessful()) {
                Log.i(TAG, "getRecipes onResponseBody: " + response.body());
                final RecipeResponse meals = response.body();
                if (meals != null && meals.getRecipes() != null && !meals.getRecipes().isEmpty()) {
                    recipesCallbackImp.onLoadList(meals.getRecipes());
                }
            } else {
                Log.e(TAG, "getRecipes onResponseError ");
            }
        }

        @Override
        public void onFailure(@NonNull Call<RecipeResponse> call, @NonNull Throwable t) {
            t.printStackTrace();
        }
    }
}
