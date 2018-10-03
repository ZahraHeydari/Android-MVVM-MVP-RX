package com.zest.android.favorite;

import com.zest.android.data.Recipe;

/**
 * To make an interaction between {@link FavoriteFragment} and
 * {@link com.zest.android.list.ListActivity}
 *
 * Created by ZARA on 8/10/2018.
 */
public interface OnFavoriteFragmentInteractionListener {

    /**
     * to go to {@link com.zest.android.detail.DetailActivity}
     *
     * @param recipe
     */
    void gotoDetailPage(Recipe recipe);


    /**
     * to update {@link Recipe}
     *
     * @param recipe
     */
    void showDeleteFavoriteDialog(Recipe recipe);

}
