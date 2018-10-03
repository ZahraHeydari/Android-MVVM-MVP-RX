package com.zest.android.data.source

import android.util.Log
import com.zest.android.data.RecipeResponse
import com.zest.android.data.source.local.DatabaseManager
import com.zest.android.data.source.remote.APIServices
import com.zest.android.data.source.remote.ServiceGenerator
import com.zest.android.search.SearchPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 *
 * @Author ZARA.
 */
class SearchRepository {

    private val mApiServices: APIServices
    private val mDatabaseManager: DatabaseManager by lazy { DatabaseManager.getInstance() }

    init {
        mApiServices = ServiceGenerator.createService(APIServices::class.java)
    }

    fun getAllRecipesByQuery(query: String,
                             searchCallback: SearchPresenter.OnSearchCallback) {
        mApiServices.search(query).enqueue(SearchResponseCallback(searchCallback))
    }


    private inner class SearchResponseCallback(private val searchCallback: SearchPresenter.OnSearchCallback)
        : Callback<RecipeResponse> {

        override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response.raw() + "]")
            if (response.isSuccessful()) {
                Log.i(TAG, "onResponseBody: " + response.body()!!)
                val meals = response.body()
                if (meals != null && meals.recipes != null && !meals.recipes.isEmpty()) {
                    searchCallback.loadData(meals.recipes)
                } else {
                    searchCallback.showEmptyView()
                }
            } else {
                Log.e(TAG, "onResponseError: " + response.errorBody()!!)
            }
        }

        override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
            t.printStackTrace()
        }
    }

    companion object {

        private val TAG = SearchRepository::class.java.simpleName
    }
}
