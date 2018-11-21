package com.zest.android.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zest.android.data.DaoMaster;
import com.zest.android.data.DaoSession;
import com.zest.android.data.Recipe;
import com.zest.android.data.RecipeDao;

import java.util.List;


/**
 * all {@link android.app.Activity} and {@link android.support.v4.app.Fragment}
 * must use this class
 * to handle or making queries into databases tables
 *
 * @Author ZARA
 */
public class DatabaseManager {

    private static final String DB_NAME = "Zest-DB";
    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static DatabaseManager mInstance;
    private final Context context;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase mDatabase;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;


    /**
     * Constructs a new DatabaseManager with the specified argument
     *
     * @param context
     */
    private DatabaseManager(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(this.context, DB_NAME, null);
    }

    /**
     * make new instance when you want and check if there persistence use it without create new one
     * NOTICE: to prevent duplication in instances use this method instead of using constructor
     *
     * @return
     */
    public static DatabaseManager newInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseManager(context);
        }
        return mInstance;
    }

    /**
     * when you want read data for database use it
     */
    private void openReadableDb() {
        mDatabase = mHelper.getReadableDatabase();
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();
    }


    public void closeConnections() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
        // close the isInternetConnected
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
        }

        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }

        if (mInstance != null) {
            mInstance = null;
        }
    }

    public void dropDatabase() {
        openWritableDb();
    }

    /**
     * to add database with writable permission
     */
    private void openWritableDb() {
        mDatabase = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();
    }


    /**
     * To update {@link Recipe}
     *
     * @param recipe
     */
    public void insertOrReplaceRecipe(Recipe recipe) {
        Log.d(TAG, "insertRecipe() called with: recipe = [" + recipe + "]");
        try {
            openWritableDb();
            mDaoSession.getRecipeDao().insertOrReplace(recipe);
            mDaoSession.clear();
        } catch (Exception e) {
            mDaoSession.clear();
            e.printStackTrace();
        }
    }


    /**
     * To delete favorite recipe by {@link Recipe#recipeId}
     *
     * @param recipe
     */
    public void deleteRecipe(Recipe recipe) {
        Log.d(TAG, "deleteRecipe() called with: recipe = [" + recipe + "]");
        try {
            openWritableDb();
            final Recipe unique = mDaoSession.getRecipeDao().queryBuilder()
                    .where(RecipeDao.Properties.RecipeId.eq(recipe.getRecipeId())).unique();
            mDaoSession.getRecipeDao().delete(unique);
            mDaoSession.clear();
        } catch (Exception e) {
            mDaoSession.clear();
            e.printStackTrace();
        }
    }

    /**
     * To load favorite recipe by {@link Recipe#recipeId}
     *
     * @param recipe
     * @return
     */
    public Recipe loadRecipe(Recipe recipe) {
        Log.d(TAG, "getFavoriteByRecipeId() called with: recipe = [" + recipe + "]");
        if (recipe == null) return null;
        try {
            openReadableDb();
            final Recipe unique = mDaoSession.getRecipeDao().queryBuilder()
                    .where(RecipeDao.Properties.RecipeId.eq(recipe.getRecipeId())).unique();
            mDaoSession.clear();
            return unique;
        } catch (Exception e) {
            mDaoSession.clear();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * To load all favorite {@link Recipe}s
     *
     * @return
     */
    public List<Recipe> loadAllFavoriteRecipes() {
        openReadableDb();
        try {
            final List<Recipe> favorites = mDaoSession.getRecipeDao().queryBuilder().list();
            mDaoSession.clear();
            if (!favorites.isEmpty()) {
                return favorites;
            }
        } catch (Exception e) {
            mDaoSession.clear();
            e.printStackTrace();
        }
        return null;
    }
}
