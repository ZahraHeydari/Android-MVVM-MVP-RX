package com.zest.android.search

import com.zest.android.data.Recipe
import com.zest.android.data.source.SearchRepository

/**
 * Listens to user actions from the UI [SearchActivity], retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 09/25/2018.
 */
class SearchPresenter(private val searchView: SearchContract.View,
                      private val searchRepository: SearchRepository) : SearchContract.UserActionsListener {

    init {
        searchView.setPresenter(this)
    }

    override fun start() {}

    override fun searchQuery(query: String) {
        searchRepository.getAllRecipesByQuery(query, SearchCallbackImp())
    }

    inner class SearchCallbackImp : OnSearchCallback {

        override fun loadData(recipes: List<Recipe>) {
            searchView.showProgressBar(false)
            searchView.showEmptyView(false)
            searchView.setResult(recipes)
        }

        override fun showEmptyView() {
            searchView.showProgressBar(false)
            searchView.showEmptyView(true)
            searchView.noData()
        }
    }

    interface OnSearchCallback {

        fun loadData(recipes: List<Recipe>)

        fun showEmptyView()
    }

    companion object {

        private val TAG = SearchPresenter::class.java.simpleName
    }
}
