package com.zest.android.category

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.zest.android.R
import com.zest.android.data.Category

/**
 * [android.support.v7.widget.RecyclerView.Adapter] to adapt
 * [Category] items into [RecyclerView] via [CategoryViewHolder]
 *
 * Created by ZARA on 09/24/2018.
 */
class CategoryAdapter(private val categoryView: CategoryContract.View,
                      private val categories: List<Category>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_category, parent, false))
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CategoryViewHolder).onBind(getItem(position))
    }

    fun getItem(position: Int): Category {
        return categories[position]
    }

    inner class CategoryViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        internal var mTextView: TextView = view.findViewById(R.id.category_text_view)
        internal var mImageView: ImageView = view.findViewById(R.id.category_image_view)

        fun onBind(category: Category) {
            mTextView.setText(category.title)
            try {
                Picasso.with(itemView.context)
                        .load(category.image)
                        .into(mImageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            itemView.setOnClickListener {
                categoryView.showSubCategories(category)
            }


        }

    }

}