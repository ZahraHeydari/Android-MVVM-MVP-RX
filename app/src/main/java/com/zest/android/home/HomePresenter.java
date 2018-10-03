package com.zest.android.home;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zest.android.data.Recipe;
import com.zest.android.data.source.HomeRepository;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from {@link HomeFragment}, retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 08/06/2018.
 */
public class HomePresenter implements HomeContract.UserActionsListener {


    private static final String TAG = HomePresenter.class.getSimpleName();
    private HomeContract.View mHomeView;
    private HomeRepository mHomeRepository;


    @SuppressLint("RestrictedApi")
    public HomePresenter(@NonNull HomeContract.View homeView,
                         @NonNull HomeRepository homeRepository) {
        mHomeView = checkNotNull(homeView, "homeView cannot be null!");
        mHomeRepository = checkNotNull(homeRepository, "homeRepository cannot be null!");
        mHomeView.setPresenter(this);
    }

    @Override
    public void start() {
        mHomeView.showEmptyView(false);
        getRecipes();
    }

    @Override
    public void getRecipes() {
        mHomeView.showProgressBar(true);
        mHomeView.showEmptyView(false);
        mHomeRepository.loadRecipeList(new RecipesCallbackImp());
    }

    @Override
    public Recipe loadFavoriteByRecipeId(Recipe recipe) {
        Log.d(TAG, "loadFavoriteByRecipeId() called with: recipe = [" + recipe + "]");
        return mHomeRepository.getFavoriteByRecipeId(recipe);
    }

    @Override
    public void deleteFavoriteByRecipeId(Recipe recipe) {
        Log.d(TAG, "deleteFavoriteByRecipeId() called with: recipe = [" + recipe + "]");
        mHomeRepository.deleteFavorite(recipe);
    }

    @Override
    public void insertFavoriteRecipe(Recipe recipe) {
        Log.d(TAG, "insertFavoriteRecipe() called with: recipe = [" + recipe + "]");
        mHomeRepository.insertFavorite(recipe);
    }


    interface OnRecipesCallback {

        void onLoadList(List<Recipe> recipes);
    }

    public class RecipesCallbackImp implements OnRecipesCallback {

        @Override
        public void onLoadList(List<Recipe> recipes) {
            mHomeView.showProgressBar(false);
            mHomeView.loadRecipes(recipes);
        }
    }
}
