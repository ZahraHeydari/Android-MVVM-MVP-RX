package com.zest.android.home;

import com.zest.android.BasePresenter;
import com.zest.android.BaseView;
import com.zest.android.data.Recipe;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 * <p>
 * Created by ZARA on 08/06/2018.
 */
public interface HomeContract {

    /**
     * the view of {@link HomeActivity}
     */
    interface View extends BaseView<UserActionsListener> {

        /**
         * to show the message of favorite
         *
         * @param message
         */
        void showMessage(int message);

        /**
         * to go to {@link com.zest.android.detail.DetailActivity}
         *
         * @param recipe
         */
        void gotoDetailPage(Recipe recipe);

        /**
         * To load all {@link Recipe}s
         *
         * @param recipes
         */
        void loadRecipes(List<Recipe> recipes);

        /**
         * To load favorite {@link Recipe} from DB
         *
         * @param recipe
         * @return
         */
        Recipe loadFavorite(Recipe recipe);

        /**
         * To remove favorite recipe from db by {@link Recipe#recipeId}
         *
         * @param recipe
         */
        void removeFavorite(Recipe recipe);

        /**
         * To insert favorite recipe in db
         *
         * @param recipe
         */
        void insertFavorite(Recipe recipe);

        /**
         * To startWithFavorite loading progress bar in {@link HomeFragment}
         *
         * @param visibility
         */
        void showProgressBar(boolean visibility);

        /**
         * To show/hide {#EmptyView} by visibility
         *
         * @param visibility
         */
        void showEmptyView(boolean visibility);
    }

    /**
     * to handle all user actions that is related to {@link HomeActivity}
     */
    interface UserActionsListener extends BasePresenter {

        /**
         * to get a list of {@link Recipe}
         *
         * @return
         */
        void getRecipes();

        /**
         * To load favorite by {@link Recipe#recipeId} from DB
         *
         * @param recipe
         * @return
         */
        Recipe loadFavoriteByRecipeId(Recipe recipe);

        /**
         * To delete favorite by {@link Recipe#recipeId} from DB
         *
         * @param recipe
         */
        void deleteFavoriteByRecipeId(Recipe recipe);

        /**
         * To insert favorite {@link Recipe} in DB
         *
         * @param recipe
         */
        void insertFavoriteRecipe(Recipe recipe);
    }
}
