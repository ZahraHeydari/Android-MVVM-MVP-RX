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
     * to update the title of {@link com.zest.android.list.ListActivity}
     *
     * @param resId
     */
    void updateToolbarTitle(int resId);

    /**
     * to go to {@link com.zest.android.detail.DetailActivity}
     *
     * @param recipe
     */
    void gotoDetailPage(Recipe recipe);
}
