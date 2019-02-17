package com.zest.android.favorite

import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.favorite.FavoriteAdapter.FavoriteViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.holder_favorite.*

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * Favorite[Recipe] items into [RecyclerView] via [FavoriteViewHolder]
 *
 *
 * Created by ZARA on 09/25/2018.
 */
internal class FavoriteAdapter(private val favoriteView: FavoriteContract.View,
                               private val recipes: List<Recipe>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_favorite, parent, false))
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

    /**
     * The holder of favorite
     */
    inner class FavoriteViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {


        fun onBind(recipe: Recipe) {
            favorite_title_text_view.setText(recipe.title)
            try {
                Picasso.with(itemView.context)
                        .load(recipe.image)
                        .into(favorite_image_view)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            containerView.setOnClickListener{
                favoriteView.gotoDetailPage(recipe)
            }
            favorite_icon_image_view.setOnClickListener(OnFavoriteClickListener(recipe))
        }


        /**
         * To cancel delete favorite recipe
         */
        private inner class OnNegativeClickListener : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id: Int) {
                dialog.dismiss()// User cancelled the dialog
            }
        }

        /**
         * To delete favorite recipe
         */
        private inner class OnPositiveButtonClickListener
        internal constructor(private val recipe: Recipe) : DialogInterface.OnClickListener {

            override fun onClick(dialog: DialogInterface, id: Int) {
                dialog.dismiss()//Cancel the dialog
                favoriteView.deleteFavorite(recipe)
            }
        }


        private inner class OnFavoriteClickListener(private val recipe: Recipe) : View.OnClickListener {

            override fun onClick(view: View) {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle(itemView.context.getString(R.string.delete))
                        .setMessage(itemView.context.getString(
                                R.string.are_you_sure_want_to_delete_this_item_from_favorite_list))
                        .setCancelable(false)
                        .setPositiveButton(itemView.context.getString(R.string.yes),
                                OnPositiveButtonClickListener(recipe))
                        .setNegativeButton(itemView.context.getString(R.string.no),
                                OnNegativeClickListener())
                builder.create()
                builder.show()
            }
        }
    }
}
