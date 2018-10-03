package com.zest.android.list;

import com.zest.android.data.Recipe;

public interface OnListCallback {

    /**
     * To update the action bar title of {@link ListActivity}
     *
     * @param resId
     */
    void updateActionBarTitle(int resId);

    /**
     * To go to {@link com.zest.android.detail.DetailActivity} by {@link Recipe}
     * from {@link com.zest.android.favorite.FavoriteFragment}
     *
     * @param recipe
     */
    void gotoDetailPage(Recipe recipe);
}
