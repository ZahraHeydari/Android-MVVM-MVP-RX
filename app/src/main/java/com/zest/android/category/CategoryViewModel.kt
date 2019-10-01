package com.zest.android.category

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.zest.android.data.Category
import com.zest.android.data.source.CategoryRepository

class CategoryViewModel : BaseObservable {

    private lateinit var categoryRepository: CategoryRepository
    private val categoryData = ObservableField<Category>()
    private val isLoading = ObservableBoolean(false)
    private val listener: OnCategoryFragmentInteractionListener

    constructor(category: Category, listener: OnCategoryFragmentInteractionListener) {
        this.listener = listener
        this.categoryData.set(category)
    }

    constructor(categoryRepository: CategoryRepository, listener: OnCategoryFragmentInteractionListener) {
        this.categoryRepository = categoryRepository
        this.listener = listener
    }

    @Bindable
    fun getTitle(): String {
        return categoryData.get()?.title ?: ""
    }

    @Bindable
    fun getImage(): String {
        return categoryData.get()?.image ?: ""
    }

    @Bindable
    fun getLoading(): Boolean {
        return this.isLoading.get()
    }

    fun setLoading(isLoaded: Boolean) {
        this.isLoading.set(isLoaded)
        notifyChange()
    }

    fun onCategoryClick() {
        categoryData.get()?.let {
            listener.showSubCategories(it)
        }
    }

    fun loadCategories() {
        setLoading(true)
        categoryRepository.loadRootCategories(CategoriesCallbackImp())
    }

    /**
     * To make an interaction between [CategoryViewModel] and [CategoryRepository]
     */
    interface OnCategoriesCallback {

        fun loadData(categories: List<Category>)
    }

    inner class CategoriesCallbackImp : OnCategoriesCallback {

        override fun loadData(categories: List<Category>) {
            setLoading(false)
            listener.setResult(categories)
        }
    }

}