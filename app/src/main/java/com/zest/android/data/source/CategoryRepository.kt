package com.zest.android.data.source

import android.util.Log
import com.zest.android.category.CategoryPresenter
import com.zest.android.data.CategoryResponse
import com.zest.android.data.source.local.DatabaseManager
import com.zest.android.data.source.remote.APIServices
import com.zest.android.data.source.remote.ServiceGenerator
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
class CategoryRepository {


    private val TAG = CategoryRepository::class.java.name
    private var mApiServices: APIServices? = null
    private val mDatabaseManager: DatabaseManager by lazy { DatabaseManager.getInstance() }

    init {
        mApiServices = ServiceGenerator.createService(APIServices::class.java)
    }

    /**
     * To load all [Category]s
     *
     * @param categoriesCallback
     * @return
     */
    fun loadRootCategories(categoriesCallback: CategoryPresenter.OnCategoriesCallback) {
        mApiServices?.getCategories()?.enqueue(OnCategoryResponseCallback(categoriesCallback))
    }


    private inner class OnCategoryResponseCallback(private val categoriesCallback:
                                                   CategoryPresenter.OnCategoriesCallback) : Callback<CategoryResponse> {

        override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response.raw() + "]")
            if (response.isSuccessful) {
                Log.i(TAG, "onResponseBody: " + response.body())
                val categoryResponse = response.body()
                categoryResponse?.categories?.let { nonNullCategoriesData ->
                    if (nonNullCategoriesData.isNotEmpty()) {
                        categoriesCallback.loadData(nonNullCategoriesData)
                    }
                }

            } else {
                Log.e(TAG, "onResponseError: " + response.errorBody())
            }
        }

        override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
            t.printStackTrace()
        }
    }


}