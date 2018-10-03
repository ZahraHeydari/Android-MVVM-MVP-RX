package com.zest.android.detail;

import android.text.TextUtils;
import android.util.Log;

import com.zest.android.data.Recipe;
import com.zest.android.data.source.DetailRepository;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ZARA on 9/11/2017.
 */

public class DetailPresenter implements DetailContract.UserActionsListener {

    private static final String TAG = DetailPresenter.class.getSimpleName();
    private final DetailContract.View mDetailView;
    private final DetailRepository mDetailRepository;


    public DetailPresenter(@NotNull DetailContract.View detailView,
                           @NotNull DetailRepository detailRepository) {
        this.mDetailView = detailView;
        this.mDetailRepository = detailRepository;
        mDetailView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public List<String> loadTagTitles(Recipe recipe) {
        Log.d(TAG, "loadTagTitles() called with: recipe = [" + recipe + "]");
        final String tags = recipe.getTag();
        if (TextUtils.isEmpty(tags)) return null;
        final String[] split = tags.split(",");
        return Arrays.asList(split);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        Log.d(TAG, "deleteFavorite() called with: recipe = [" + recipe + "]");
        mDetailRepository.updateRecipe(recipe);
    }

    @Override
    public void insertToFavorite(Recipe recipe) {
        Log.d(TAG, "insertToFavorite() called with: recipe = [" + recipe + "]");
        mDetailRepository.insertFavorite(recipe);
    }

    @Override
    public void removeFromFavorite(Recipe recipe) {
        Log.d(TAG, "removeFromFavorite() called with: recipe = [" + recipe + "]");
        mDetailRepository.removeFavorite(recipe);
    }

    @Override
    public Recipe loadFavorite(Recipe recipe) {
        Log.d(TAG, "loadFavorite() called with: recipe = [" + recipe + "]");
        return mDetailRepository.getFavoriteByRecipeId(recipe);
    }

    @Override
    public String[] loadTags(Recipe recipe) {
        Log.d(TAG, "loadTags() called with: recipe = [" + recipe + "]");
        final String tags = recipe.getTag();
        if (TextUtils.isEmpty(tags)) return null;
        final String[] splitTags = tags.split(",");
        return splitTags;
    }

    @Override
    public void searchByTag(String tag) {
        Log.d(TAG, "searchByTag() called with: tag = [" + tag + "]");
        mDetailView.navigateToSearchWithTag(tag);
    }
}
