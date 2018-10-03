package com.zest.android.search;

import android.annotation.SuppressLint;

import com.zest.android.data.Recipe;
import com.zest.android.data.source.SearchRepository;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI {@link SearchActivity}, retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 8/18/2018.
 */
public class SearchPresenter implements SearchContract.UserActionsListener {


    private static final String TAG = SearchPresenter.class.getSimpleName();
    private final SearchContract.View mSearchView;
    private final SearchRepository mSearchRepository;

    @SuppressLint("RestrictedApi")
    public SearchPresenter(@NotNull SearchContract.View searchView,
                           @NotNull SearchRepository searchRepository) {
        this.mSearchView = checkNotNull(searchView, "searchView cannot be null!");
        this.mSearchRepository = checkNotNull(searchRepository, "searchRepository cannot be null!");
        mSearchView.setPresenter(this);
    }


    @Override
    public void start() {
    }

    @Override
    public void searchQuery(String query) {
        mSearchView.showProgressBar(true);
        mSearchRepository.getAllRecipesByQuery(query, new SearchCallbackImp());
    }

    public class SearchCallbackImp {

        public void loadData(List<Recipe> recipes) {
            mSearchView.showProgressBar(false);
            mSearchView.setResult(recipes);
        }

        public void showEmptyView() {
            mSearchView.showProgressBar(false);
            mSearchView.showEmptyView();
        }
    }
}
