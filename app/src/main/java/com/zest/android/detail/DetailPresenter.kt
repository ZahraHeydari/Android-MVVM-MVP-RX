package com.zest.android.detail

import android.text.TextUtils
import android.util.Log
import com.zest.android.data.Recipe
import com.zest.android.data.source.DetailRepository


/**
 * Listens to user actions from [DetailActivity], retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 09/25/2018.
 */
class DetailPresenter(private val detailView: DetailContract.View,
                      private val detailRepository: DetailRepository) : DetailContract.UserActionsListener {

    init {
        detailView.setPresenter(this)
    }

    override fun start() {
    }

    override fun updateRecipe(recipe: Recipe) {
        Log.d(TAG, "deleteFavorite() called with: recipe = [$recipe]")
        detailRepository.updateRecipe(recipe)
    }

    override fun insertToFavorite(recipe: Recipe) {
        Log.d(TAG, "insertToFavorite() called with: recipe = [$recipe]")
        detailRepository.insertFavorite(recipe)
    }

    override fun removeFromFavorite(recipe: Recipe) {
        Log.d(TAG, "removeFromFavorite() called with: recipe = [$recipe]")
        detailRepository.removeFavorite(recipe)
    }

    override fun loadFavorite(recipe: Recipe): Recipe? {
        Log.d(TAG, "loadFavorite() called with: recipe = [$recipe]")
        return detailRepository.getFavoriteByRecipeId(recipe)
    }

    override fun loadTags(recipe: Recipe): Array<String>? {
        Log.d(TAG, "loadTags() called with: recipe = [$recipe]")
        val tags = recipe.tag
        if (TextUtils.isEmpty(tags)) return null
        val splitTags = tags?.split(",".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()
        return splitTags
    }

    override fun searchByTag(tag: String) {
        Log.d(TAG, "searchByTag() called with: tag = [$tag]")
        detailView.navigateToSearchWithTag(tag)
    }

    companion object {
        private val TAG = DetailPresenter::class.java.simpleName
    }
}