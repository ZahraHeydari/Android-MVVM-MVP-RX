package com.zest.android.favorite

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.databinding.HolderFavoriteBinding
import com.zest.android.favorite.FavoriteAdapter.FavoriteViewHolder
import com.zest.android.home.RecipeViewModel
import com.zest.android.util.DataBindingViewHolder

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * Favorite[Recipe] items into [RecyclerView] via [FavoriteViewHolder]
 *
 * Created by ZARA on 8/10/2018.
 */
internal class FavoriteAdapter(private val listener: OnFavoriteFragmentInteractionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recipes: MutableList<Recipe> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderFavoriteBinding =
                DataBindingUtil.inflate<HolderFavoriteBinding>(LayoutInflater.from(parent.context),
                        R.layout.holder_favorite, parent, false)
        return FavoriteViewHolder(holderFavoriteBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FavoriteViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Recipe {
        return recipes[position]
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun addData(recipes: List<Recipe>?) {
        this.recipes.clear()
        if (recipes != null && !recipes.isEmpty()) {
            this.recipes.addAll(recipes)
        }
        notifyDataSetChanged()
    }

    /**
     * The holder of favorite
     */
    inner class FavoriteViewHolder(dataBinding: ViewDataBinding) : DataBindingViewHolder<Recipe>(dataBinding) {

        override fun onBind(t: Recipe) {
            (this.dataBinding as HolderFavoriteBinding).recipeViewModel = RecipeViewModel(t, listener)
        }
    }
}
