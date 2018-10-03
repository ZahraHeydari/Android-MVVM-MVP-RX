package com.zest.android.detail;

import com.zest.android.BasePresenter;
import com.zest.android.BaseView;
import com.zest.android.data.Recipe;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 *
 * Created by ZARA on 8/10/2018.
 */
public interface DetailContract {

    interface View extends BaseView<UserActionsListener> {

        /**
         * To go to {@link com.zest.android.search.SearchActivity} with tag
         *
         * @param tag
         */
        void navigateToSearchWithTag(String tag);
    }

    interface UserActionsListener extends BasePresenter {


        /**
         * to update {@link Recipe}
         *
         * @param recipe
         */
        void updateRecipe(Recipe recipe);

        /**
         * To insert recipe as a favorite in DB
         *
         * @param recipe
         */
        void insertToFavorite(Recipe recipe);

        /**
         * To remove favorite recipe from DB
         *
         * @param recipe
         */
        void removeFromFavorite(Recipe recipe);

        /**
         * To load favorite recipe from DB
         *
         * @param recipe
         * @return
         */
        Recipe loadFavorite(Recipe recipe);

        /**
         * To load String array of {@link Recipe#tag}s
         *
         * @param recipe
         * @return
         */
        String[] loadTags(Recipe recipe);


        /**
         * To load a list of {@link Recipe#tag}s
         *
         * @param recipe
         * @return
         */
        List<String> loadTagTitles(Recipe recipe);

        /**
         * To search tag in {@link com.zest.android.search.SearchActivity}
         *
         * @param tag
         */
        void searchByTag(String tag);
    }
}
