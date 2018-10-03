package com.zest.android.home;

import com.zest.android.data.Recipe;

import java.util.List;

/**
 * To make an interaction between {@link HomeFragment} and {@link RecipeViewModel}
 */
public interface OnHomeFragmentInteractionListener {

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
     * To show/hide {#EmptyView} by visibility
     *
     * @param visibility
     */
    void showEmptyView(boolean visibility);

}
