package com.zest.android.home;

import com.zest.android.data.Category;
import com.zest.android.data.Recipe;

/**
 * To make interaction between {@link HomeActivity} and its child
 *
 * Created by ZARA on 08/06/2018.
 */
public interface OnHomeCallback {

    /**
     * To show/hide fab in {@link HomeFragment}
     *
     * @param visibility
     */
    void showFab(boolean visibility);

    /**
     * to go to {@link com.zest.android.detail.DetailActivity} by {@link Recipe}
     *
     * @param recipe
     */
    void gotoDetailPage(Recipe recipe);

    /**
     * To show a list of the category subs by {@link Category}
     * in {@link com.zest.android.search.SearchActivity}
     *
     * @param category
     */
    void showSubCategoriesByCategoryTitle(Category category);

}
