package com.zest.android.search;

import com.zest.android.BasePresenter;
import com.zest.android.BaseView;
import com.zest.android.data.Recipe;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 * <p>
 * Created by ZARA on 8/18/2018.
 */
public interface SearchContract {


    interface View extends BaseView<UserActionsListener> {


        /**
         * to go to {@link com.zest.android.detail.DetailActivity}
         *
         * @param recipe
         */
        void gotoDetailPage(Recipe recipe);

        /**
         * To set search result in {@link SearchActivity}
         *
         * @param recipes
         */
        void setResult(List<Recipe> recipes);

        /**
         * To startWithFavorite loading progress bar in {@link SearchActivity}
         *
         * @param visibility
         */
        void showProgressBar(boolean visibility);

        /**
         * To show {#EmptyView} when there is no data
         */
        void showEmptyView();

    }


    interface UserActionsListener extends BasePresenter {

        /**
         * To get all {@link Recipe}s by search query
         *
         * @param query
         * @return
         */
        void searchQuery(String query);

    }
}
