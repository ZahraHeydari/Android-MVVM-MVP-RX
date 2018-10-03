package com.zest.android.category;

import android.annotation.SuppressLint;

import com.zest.android.data.Category;
import com.zest.android.data.source.CategoryRepository;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from {@link CategoryFragment}, retrieves the data and updates the
 * UI as required.
 *
 * Created by ZARA on 8/10/2018.
 */
public class CategoryPresenter implements CategoryContract.UserActionsListener {

    private static final String TAG = CategoryPresenter.class.getSimpleName();
    private final CategoryContract.View mCategoryView;
    private final CategoryRepository mCategoryRepository;


    @SuppressLint("RestrictedApi")
    public CategoryPresenter(@NotNull CategoryContract.View categoryView,
                             @NotNull CategoryRepository categoryRepository) {
        this.mCategoryView = checkNotNull(categoryView, "CategoryView can not be null!");
        this.mCategoryRepository = checkNotNull(categoryRepository, "CategoryRepository can not be null!");
        mCategoryView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void loadCategories() {
        mCategoryView.showProgressBar(true);
        mCategoryRepository.loadRootCategories(new CategoriesCallbackImp());
    }

    public class CategoriesCallbackImp {

        public void loadData(List<Category> categories) {
            mCategoryView.showProgressBar(false);
            mCategoryView.setResult(categories);
        }
    }
}
