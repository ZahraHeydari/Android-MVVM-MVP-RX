package com.zest.android.category;

import com.zest.android.data.Category;

import java.util.List;

/**
 * To make an interaction between {@link CategoryFragment} and {@link CategoryViewModel}
 * <p>
 * Created by ZARA on 09/30/2018.
 */
interface OnCategoryFragmentInteractionListener {

    void showSubCategories(Category category);

    void setResult(List<Category> categories);
}
