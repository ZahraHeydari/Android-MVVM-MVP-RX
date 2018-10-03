package com.zest.android.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zest.android.R;
import com.zest.android.data.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to adapt
 * {@link Category} items into {@link RecyclerView} via {@link CategoryViewHolder}
 *
 * Created by ZARA on 8/10/2018.
 */
public class CategoryAdapter extends RecyclerView.Adapter {

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private final CategoryContract.View mCategoryView;
    private final List<Category> mCategories;


    public CategoryAdapter(CategoryContract.View categoryView, List<Category> categories) {
        this.mCategoryView = categoryView;
        this.mCategories = categories;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CategoryViewHolder) holder).onBind(getItem(position));
    }

    private Category getItem(int position) {
        return mCategories.get(position);
    }

    @Override
    public int getItemCount() {
        if (mCategories == null) return 0;
        return mCategories.size();
    }

    /**
     * holder of {@link Category}
     */
    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_text_view)
        TextView mTextView;
        @BindView(R.id.category_image_view)
        ImageView mImageView;

        public CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * to bind {@link Category}
         *
         * @param category
         */
        public void onBind(final Category category) {
            itemView.setOnClickListener(new OnRootCategoryClickListener(category));

            mTextView.setText(category.getTitle());
            try {
                Picasso.with(itemView.getContext())
                        .load(category.getImage())
                        .into(mImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * when {@link Category} clicked
         */
        private class OnRootCategoryClickListener implements View.OnClickListener {

            private final Category category;

            public OnRootCategoryClickListener(Category category) {
                this.category = category;
            }

            @Override
            public void onClick(View view) {
                mCategoryView.showSubCategories(category);
            }
        }
    }
}
