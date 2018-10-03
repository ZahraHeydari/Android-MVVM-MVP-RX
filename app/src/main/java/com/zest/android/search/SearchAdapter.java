package com.zest.android.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zest.android.R;
import com.zest.android.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to adapt
 * {@link Recipe} items into {@link RecyclerView} via {@link SearchViewHolder}
 *
 * Created by ZARA on 8/18/2018.
 */
public class SearchAdapter extends RecyclerView.Adapter {


    private static final String TAG = SearchAdapter.class.getSimpleName();
    private final List<Recipe> recipes;
    private final SearchContract.View searchView;

    public SearchAdapter(SearchContract.View searchView, List<Recipe> recipes) {
        this.recipes = recipes;
        this.searchView = searchView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SearchViewHolder) holder).onBind(getItem(position));
    }

    private Recipe getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    /**
     * holder of search item
     */
    public class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.search_image_view)
        ImageView mImageView;
        @BindView(R.id.search_text_view)
        TextView mTextView;

        public SearchViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void onBind(Recipe recipe) {
            itemView.setOnClickListener(new OnItemViewClickListener(recipe));

            mTextView.setText(recipe.getTitle());

            try {
                Picasso.with(itemView.getContext())
                        .load(recipe.getImage())
                        .into(mImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private class OnItemViewClickListener implements View.OnClickListener {
            private final Recipe recipe;

            public OnItemViewClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            @Override
            public void onClick(View view) {
                searchView.gotoDetailPage(recipe);
            }
        }
    }
}
