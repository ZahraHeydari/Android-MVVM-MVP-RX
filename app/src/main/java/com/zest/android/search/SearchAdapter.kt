package com.zest.android.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.search.SearchAdapter.SearchViewHolder

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Recipe] items into [RecyclerView] via [SearchViewHolder]
 *
 * Created by ZARA on 09/25/2018.
 */
class SearchAdapter(private val searchView: SearchContract.View,
                    private val recipes: MutableList<Recipe>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_search, parent, false))
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


    fun removePreviousData() {
        recipes.clear()
        notifyDataSetChanged()
    }

    /**
     * holder of search item
     */
    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal var mImageView: ImageView = view.findViewById(R.id.search_image_view)
        internal var mTextView: TextView = view.findViewById(R.id.search_text_view)

        fun onBind(recipe: Recipe) {
            mTextView.setText(recipe.title)
            try {
                Picasso.with(itemView.context)
                        .load(recipe.image)
                        .into(mImageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            itemView.setOnClickListener {
                searchView.gotoDetailPage(recipe)
            }

        }
    }

    companion object {

        private val TAG = SearchAdapter::class.java.simpleName
    }

}
