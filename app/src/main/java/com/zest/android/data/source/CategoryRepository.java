package com.zest.android.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zest.android.category.CategoryViewModel;
import com.zest.android.data.Category;
import com.zest.android.data.CategoryResponse;
import com.zest.android.data.source.local.DatabaseManager;
import com.zest.android.data.source.remote.APIServices;
import com.zest.android.data.source.remote.ServiceGenerator;

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
public class CategoryRepository {

    private static final String TAG = CategoryRepository.class.getSimpleName();
    private final Context context;
    private final APIServices mApiServices;
    private DatabaseManager mDatabaseManager;


    public CategoryRepository(Context context) {
        this.context = context;
        mDatabaseManager = DatabaseManager.newInstance(context);
        mApiServices = ServiceGenerator.createService(APIServices.class);
    }

    /**
     * To load all {@link Category}s
     *
     * @param categoriesCallback
     * @return
     */
    public void loadRootCategories(final CategoryViewModel.OnCategoriesCallback categoriesCallback) {
        mApiServices.getCategories().enqueue(new OnCategoryResponseCallback(categoriesCallback));
    }


    private class OnCategoryResponseCallback implements Callback<CategoryResponse> {

        private final CategoryViewModel.OnCategoriesCallback categoriesCallback;

        public OnCategoryResponseCallback(CategoryViewModel.OnCategoriesCallback categoriesCallback) {
            this.categoriesCallback = categoriesCallback;
        }

        @Override
        public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
            Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response.raw() + "]");
            if (response.isSuccessful()) {
                Log.i(TAG, "onResponseBody: " + response.body());
                final CategoryResponse categoryResponse = response.body();
                if (categoryResponse != null && categoryResponse.getCategories() != null &&
                        !categoryResponse.getCategories().isEmpty()) {
                    categoriesCallback.loadData(categoryResponse.getCategories());
                }
            } else {
                Log.e(TAG, "onResponseError: " + response.errorBody());
            }
        }

        @Override
        public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
            t.printStackTrace();
        }
    }
}
