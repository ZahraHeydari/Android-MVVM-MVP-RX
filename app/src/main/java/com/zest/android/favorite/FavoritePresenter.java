package com.zest.android.favorite;

import android.annotation.SuppressLint;

import com.zest.android.data.Recipe;
import com.zest.android.data.source.FavoriteRepository;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from {@link FavoriteFragment}, retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 8/14/2018.
 */
public class FavoritePresenter implements FavoriteContract.UserActionsListener {


    private static final String TAG = FavoritePresenter.class.getSimpleName();
    private FavoriteContract.View mFavoriteView;
    private FavoriteRepository mFavoriteRepository;

    @SuppressLint("RestrictedApi")
    public FavoritePresenter(@NotNull FavoriteContract.View favoriteView,
                             @NotNull FavoriteRepository favoriteRepository) {
        this.mFavoriteView = checkNotNull(favoriteView, "mFavoriteView cannot be null!");
        this.mFavoriteRepository = checkNotNull(favoriteRepository, "mFavoriteRepository cannot be null!");
        mFavoriteView.setPresenter(this);
    }


    @Override
    public void start() { }

    @Override
    public List<Recipe> loadFavorites() {
        return mFavoriteRepository.loadAllFavorites();
    }


    @Override
    public void deleteFavoriteRecipe(Recipe recipe) {
        mFavoriteRepository.deleteFavoriteRecipe(recipe);
    }
}
