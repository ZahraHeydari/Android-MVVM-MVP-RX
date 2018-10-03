package com.zest.android.category;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.zest.android.data.Category;
import com.zest.android.data.source.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends BaseObservable {

    private final ObservableField<Category> model = new ObservableField<>();
    private ObservableBoolean isLoading = new ObservableBoolean(false);
    private CategoryRepository categoryRepository;
    private OnCategoryFragmentInteractionListener listener;


    public CategoryViewModel(Category category, OnCategoryFragmentInteractionListener listener) {
        this.listener = listener;
        this.model.set(category);
    }

    public CategoryViewModel(CategoryRepository categoryRepository, CategoryFragment listener) {
        this.categoryRepository = categoryRepository;
        this.listener = listener;
    }

    @Bindable
    public String getTitle() {
        return model.get().getTitle();
    }

    @Bindable
    public String getImage() {
        return model.get().getImage();
    }

    @Bindable
    public boolean getLoading() {
        return this.isLoading.get();
    }

    public void setLoading(boolean isLoaded) {
        this.isLoading.set(isLoaded);
        notifyChange();
    }

    public void onCategoryClick() {
        listener.showSubCategories(model.get());
    }

    public void loadCategories() {
        setLoading(true);
        categoryRepository.loadRootCategories(new CategoriesCallbackImp());
    }

    /**
     * To make an interaction between {@link CategoryViewModel} and {@link CategoryRepository}
     */
    public interface OnCategoriesCallback {

        void loadData(List<Category> categories);
    }

    public class CategoriesCallbackImp implements OnCategoriesCallback {

        public void loadData(List<Category> categories) {
            setLoading(false);
            listener.setResult(categories);
        }
    }
}
