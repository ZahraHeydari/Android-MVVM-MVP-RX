package com.zest.android.search;

import com.zest.android.data.Recipe;

import java.util.List;

public interface OnSearchCallback {

    /**
     * To set search result {@link Recipe}s in {@link SearchActivity}
     *
     * @param recipes
     */
    void setResult(List<Recipe> recipes);

    /**
     * To show the detail of search item{@link Recipe} in {@link com.zest.android.detail.DetailActivity}
     *
     * @param recipe
     */
    void gotoDetailPage(Recipe recipe);

    /**
     * To show/hide the EmptyView of {@link SearchActivity} by visibility
     *
     * @param visibility
     */
    void showEmptyView(boolean visibility);

    /**
     * When the result of search is NULL!
     */
    void noResult();
}
