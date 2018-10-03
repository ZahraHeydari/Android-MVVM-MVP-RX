package com.zest.android.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zest.android.data.RecipeResponse;
import com.zest.android.data.source.local.DatabaseManager;
import com.zest.android.data.source.remote.APIServices;
import com.zest.android.data.source.remote.ServiceGenerator;
import com.zest.android.search.SearchPresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * To handle data operations. It provides a clean API so that the rest of the app can retrieve this data easily.
 * It knows where to get the data from and what API calls to make when data is updated.
 * You can consider repositories to be mediators between different data sources, such as persistent models,
 * web services, and caches.
 *
 * @Author ZARA.
 */
public class SearchRepository {

    private static final String TAG = SearchRepository.class.getSimpleName();
    private final Context context;
    private final APIServices mApiServices;
    private DatabaseManager mDatabaseManager;


    public SearchRepository(Context context) {
        this.context = context;
        mDatabaseManager = DatabaseManager.newInstance(context);
        mApiServices = ServiceGenerator.createService(APIServices.class);
    }


    public void getAllRecipesByQuery(String query,
                                     final SearchPresenter.SearchCallbackImp searchCallbackImp) {
        mApiServices.search(query).enqueue(new SearchResponseCallback(searchCallbackImp));
    }


    private class SearchResponseCallback implements Callback<RecipeResponse> {
        private final SearchPresenter.SearchCallbackImp searchCallbackImp;

        public SearchResponseCallback(SearchPresenter.SearchCallbackImp searchCallbackImp) {
            this.searchCallbackImp = searchCallbackImp;
        }

        @Override
        public void onResponse(@NonNull Call<RecipeResponse> call, @NonNull Response<RecipeResponse> response) {
            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response.raw() + "]");
            if (response.isSuccessful()) {
                Log.i(TAG, "onResponseBody: " + response.body());
                final RecipeResponse meals = response.body();
                if (meals != null && meals.getRecipes() != null && !meals.getRecipes().isEmpty()) {
                    searchCallbackImp.loadData(meals.getRecipes());
                } else {
                    searchCallbackImp.showEmptyView();
                }
            } else {
                Log.e(TAG, "onResponseError: " + response.errorBody());
            }
        }

        @Override
        public void onFailure(@NonNull Call<RecipeResponse> call, @NonNull Throwable t) {
            t.printStackTrace();
        }
    }
}
