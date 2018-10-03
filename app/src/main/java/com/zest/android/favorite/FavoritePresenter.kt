package com.zest.android.favorite

import com.zest.android.data.Recipe
import com.zest.android.data.source.FavoriteRepository
import org.greenrobot.greendao.annotation.NotNull

/**
 * Listens to user actions from [FavoriteFragment], retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 09/25/2018.
 */
class FavoritePresenter(@NotNull val favoriteView: FavoriteContract.View,
                        @NotNull val favoriteRepository: FavoriteRepository) : FavoriteContract.UserActionsListener {

    init {
        favoriteView.setPresenter(this)
    }

    override fun start() {

    }

    override fun loadFavorites(): List<Recipe> {
        return favoriteRepository.loadAllFavorites()!!
    }

    override fun deleteFavoriteRecipe(recipe: Recipe) {
        favoriteRepository.deleteFavoriteRecipe(recipe)
    }

    companion object {

        private val TAG = FavoritePresenter::class.java.simpleName
    }
}
