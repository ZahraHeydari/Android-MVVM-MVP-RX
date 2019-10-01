package com.zest.android.search

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.databinding.HolderSearchBinding
import com.zest.android.home.RecipeViewModel
import com.zest.android.util.DataBindingViewHolder
import java.util.*

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Recipe] items into [RecyclerView] via [SearchViewHolder]
 *
 * Created by ZARA on 8/18/2018.
 */
class SearchAdapter(private val callback: OnSearchCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = SearchAdapter::class.java.name
    private val recipes: MutableList<Recipe> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderSearchBinding = DataBindingUtil
                .inflate<HolderSearchBinding>(LayoutInflater.from(parent.context),
                        R.layout.holder_search, parent, false)
        return SearchViewHolder(holderSearchBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Recipe {
        return recipes[position]
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun addData(recipes: List<Recipe>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }

    fun removePreviousData() {
        this.recipes.clear()
        notifyDataSetChanged()
    }

    /**
     * holder of search item
     */
    inner class SearchViewHolder(dataBinding: ViewDataBinding) : DataBindingViewHolder<Recipe>(dataBinding) {

        override fun onBind(recipe: Recipe) {
            (this.dataBinding as HolderSearchBinding).recipeViewModel = RecipeViewModel(recipe, callback)
        }
    }
}
