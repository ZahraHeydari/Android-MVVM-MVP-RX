package com.zest.android.data.source;

import android.content.Context;
import android.util.Log;

import com.zest.android.data.Recipe;
import com.zest.android.data.source.local.DatabaseManager;
import com.zest.android.data.source.remote.APIServices;
import com.zest.android.data.source.remote.ServiceGenerator;

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 * @Author ZARA.
 */
public class DetailRepository {

    private static final String TAG = DetailRepository.class.getSimpleName();
    private final Context context;
    private final APIServices mApiServices;
    private DatabaseManager mDatabaseManager;

    public DetailRepository(Context context) {
        this.context = context;
        mDatabaseManager = DatabaseManager.newInstance(context);
        mApiServices = ServiceGenerator.createService(APIServices.class);
    }


    public void updateRecipe(Recipe recipe) {
        mDatabaseManager.insertOrReplaceRecipe(recipe);
    }

    public void insertFavorite(Recipe recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [" + recipe + "]");
        mDatabaseManager.insertOrReplaceRecipe(recipe);
    }

    public void removeFavorite(Recipe recipe) {
        Log.d(TAG, "removeFavorite() called with: recipe = [" + recipe + "]");
        mDatabaseManager.deleteRecipe(recipe);
    }

    public Recipe getFavoriteByRecipeId(Recipe recipe) {
        return mDatabaseManager.loadRecipe(recipe);
    }
}
