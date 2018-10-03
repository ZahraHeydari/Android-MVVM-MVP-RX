package com.zest.android.home

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.RecipeRepository
import com.zest.android.databinding.HolderRecipeBinding
import com.zest.android.util.DataBindingViewHolder
import java.util.*

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Recipe] items into [RecyclerView] via [RecipeViewHolder]
 *
 *
 * Created by ZARA on 08/06/2018.
 */
internal class RecipesAdapter(private val listener: OnHomeFragmentInteractionListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val recipes: MutableList<Recipe>?

    init {
        this.recipes = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderRecipeBinding = DataBindingUtil.inflate<HolderRecipeBinding>(LayoutInflater.from(parent.context),
                R.layout.holder_recipe, parent, false)
        return RecipeViewHolder(holderRecipeBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecipeViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Recipe {
        return recipes!![position]
    }

    override fun getItemCount(): Int {
        return recipes?.size ?: 0
    }

    fun addData(recipes: List<Recipe>) {
        this.recipes!!.clear()
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }

    /**
     * Holder of [Recipe]
     */
    inner class RecipeViewHolder(dataBinding: ViewDataBinding) : DataBindingViewHolder<Recipe>(dataBinding) {

        override fun onBind(recipe: Recipe) {
            (this.dataBinding as HolderRecipeBinding).recipeViewModel = RecipeViewModel(recipe,
                    RecipeRepository(), listener)
        }
    }

    companion object {

        private val TAG = RecipesAdapter::class.java.simpleName
    }
}
