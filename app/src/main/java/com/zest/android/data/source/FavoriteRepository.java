package com.zest.android.data.source;

import android.content.Context;
import android.util.Log;

import com.zest.android.data.Recipe;
import com.zest.android.data.source.local.DatabaseManager;

import java.util.List;

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 *
 * @Author ZARA.
 */
public class FavoriteRepository {

    private static final String TAG = FavoriteRepository.class.getSimpleName();
    private final Context context;
    private DatabaseManager mDatabaseManager;


    public FavoriteRepository(Context context) {
        this.context = context;
        mDatabaseManager = DatabaseManager.newInstance(context);
    }


    /**
     * to load all {@link Recipe}s with isFavorite=true
     *
     * @return
     */
    public List<Recipe> loadAllFavorites() {
        return mDatabaseManager.loadAllFavoriteRecipes();
    }


    /**
     * to update {@link Recipe}
     *
     * @param recipe
     */
    public void deleteFavoriteRecipe(Recipe recipe) {
        Log.d(TAG, "deleteFavoriteRecipe() called with: recipe = [" + recipe + "]");
        mDatabaseManager.deleteRecipe(recipe);
    }
}
