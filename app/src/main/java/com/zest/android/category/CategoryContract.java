package com.zest.android.category;

import com.zest.android.BasePresenter;
import com.zest.android.BaseView;
import com.zest.android.data.Category;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 *
 * Created by ZARA on 8/10/2018.
 */
public interface CategoryContract {

    /**
     * the view of {@link CategoryFragment}
     */
    interface View extends BaseView<CategoryContract.UserActionsListener> {

        /**
         * to go to {@link }
         *
         * @param category
         */
        void showSubCategories(Category category);

        /**
         * To set result of response(data) in {@link CategoryFragment}
         *
         * @param categories
         */
        void setResult(List<Category> categories);

        /**
         * To show progress bar in {@link CategoryFragment} before loading data
         *
         * @param visibility
         */
        void showProgressBar(boolean visibility);
    }

    /**
     * To handle all user actions that are related to {@link CategoryFragment}
     */
    interface UserActionsListener extends BasePresenter {


        /**
         * to load all {@link Category}s
         *
         * @return
         */
        void loadCategories();

    }
}
