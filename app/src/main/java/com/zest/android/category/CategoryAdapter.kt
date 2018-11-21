package com.zest.android.category

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.category.CategoryAdapter.CategoryViewHolder
import com.zest.android.data.Category
import com.zest.android.databinding.HolderCategoryBinding
import com.zest.android.util.DataBindingViewHolder
import java.util.*

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Category] items into [RecyclerView] via [CategoryViewHolder]
 *
 * Created by ZARA on 09/30/2018.
 */
class CategoryAdapter(private val listener: OnCategoryFragmentInteractionListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categories: MutableList<Category>

    init {
        this.categories = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding = DataBindingUtil.inflate<HolderCategoryBinding>(LayoutInflater.from(parent.context),
                R.layout.holder_category, parent, false)
        return CategoryViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CategoryViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Category {
        return categories[position]
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun addData(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        this.notifyDataSetChanged()
    }

    /**
     * The holder of [Category]
     */
    private inner class CategoryViewHolder(viewDataBinding: ViewDataBinding) : DataBindingViewHolder<Category>(viewDataBinding) {

        override fun onBind(t: Category) {
            (this.dataBinding as HolderCategoryBinding).setCategoryViewModel(CategoryViewModel(t, listener))
        }
    }

    companion object {

        private val TAG = CategoryAdapter::class.java.simpleName
    }
}