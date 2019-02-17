package com.zest.android.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.home.RecipesAdapter.RecipeViewHolder

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Recipe] items into [RecyclerView] via [RecipeViewHolder]
 *
 *
 * Created by ZARA on 09/25/2018.
 */
internal class RecipesAdapter(private val homeView: HomeContract.View,
                              private val recipes: List<Recipe>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_recipe, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecipeViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Recipe {
        return recipes[position]
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    /**
     * Holder of [Recipe]
     */
    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal var mTitleTextView: TextView = view.findViewById(R.id.recipe_title_text_view)
        internal var mImageView: ImageView = view.findViewById(R.id.recipe_image_view)
        internal var mFavoriteImageView: ImageView = view.findViewById(R.id.recipe_favorite_image_view)

        fun onBind(recipe: Recipe) {
            mTitleTextView.setText(recipe.title)
            try {
                Picasso.with(mImageView.context)
                        .load(recipe.image)
                        .into(mImageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            checkFavorite(recipe)
            mFavoriteImageView.setOnClickListener(OnFavoriteClickListener(recipe))
            itemView.setOnClickListener {
                homeView.gotoDetailPage(recipe)
            }
        }

        /**
         * To check the recipe is favorite or not?!
         *
         * @param recipe
         */
        private fun checkFavorite(recipe: Recipe) {
            if (homeView.loadFavorite(recipe) != null)
                setFavoriteImage(R.drawable.ic_star_full_vector)
            else
                setFavoriteImage(R.drawable.ic_star_empty_color_text_secondary_vector)

        }

        private fun setFavoriteImage(drawableId:Int){
            mFavoriteImageView.setImageResource(drawableId)
        }

        private inner class OnFavoriteClickListener(private val recipe: Recipe) : View.OnClickListener {

            override fun onClick(view: View) {

                if (homeView.loadFavorite(recipe) != null) {
                    setFavoriteImage(R.drawable.ic_star_empty_color_text_secondary_vector)
                    homeView.showMessage(R.string.deleted_this_recipe_from_your_favorite_list)
                    homeView.removeFavorite(recipe)
                } else {
                    setFavoriteImage(R.drawable.ic_star_full_vector)
                    homeView.showMessage(R.string.added_this_recipe_to_your_favorite_list)
                    homeView.insertFavorite(recipe)
                }
            }
        }
    }

    companion object {

        private val TAG = RecipesAdapter::class.java.simpleName
    }


}
