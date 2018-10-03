package com.zest.android.favorite;

import com.zest.android.BasePresenter;
import com.zest.android.BaseView;
import com.zest.android.data.Recipe;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 * <p>
 * Created by ZARA on 8/10/2018.
 */
public interface FavoriteContract {


    /**
     * the view of {@link FavoriteFragment}
     */
    interface View extends BaseView<UserActionsListener> {


        /**
         * to update {@link Recipe}
         *
         * @param recipe
         */
        void deleteFavorite(Recipe recipe);


        /**
         * to go to {@link com.zest.android.detail.DetailActivity}
         *
         * @param recipe
         */
        void gotoDetailPage(Recipe recipe);

    }


    /**
     * to handle all interactions that is related to {@link FavoriteFragment}
     */
    interface UserActionsListener extends BasePresenter {

        /**
         * to load all favorites
         */
        List<Recipe> loadFavorites();


        /**
         * to update {@link Recipe}
         *
         * @param recipe
         */
        void deleteFavoriteRecipe(Recipe recipe);

    }
}
