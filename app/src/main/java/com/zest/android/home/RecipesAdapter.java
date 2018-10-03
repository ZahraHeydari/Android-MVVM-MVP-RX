package com.zest.android.home;

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
 * {@link Recipe} items into {@link RecyclerView} via {@link RecipeViewHolder}
 * <p>
 * Created by ZARA on 08/06/2018.
 */
class RecipesAdapter extends RecyclerView.Adapter {

    private static final String TAG = RecipesAdapter.class.getSimpleName();
    private final HomeContract.View homeView;
    private final List<Recipe> recipes;


    public RecipesAdapter(HomeContract.View homeView, List<Recipe> recipes) {
        this.homeView = homeView;
        this.recipes = recipes;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecipeViewHolder) holder).onBind(getItem(position));
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
     * Holder of {@link Recipe}
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_title_text_view)
        TextView mTitleTextView;
        @BindView(R.id.recipe_image_view)
        ImageView mImageView;
        @BindView(R.id.recipe_favorite_image_view)
        ImageView mFavoriteImageView;


        public RecipeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void onBind(final Recipe recipe) {
            itemView.setOnClickListener(new OnItemViewClickListener(recipe));
            mFavoriteImageView.setOnClickListener(new OnFavoriteClickListener(recipe));
            checkFavorite(recipe);

            try {
                Picasso.with(mImageView.getContext())
                        .load(recipe.getImage())
                        .into(mImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mTitleTextView.setText(recipe.getTitle());
        }

        /**
         * To check the recipe is favorite or not?!
         *
         * @param recipe
         */
        private void checkFavorite(Recipe recipe) {
            if (homeView.loadFavorite(recipe) != null) {
                mFavoriteImageView.setImageResource(R.drawable.ic_star_full_vector);
            } else {
                mFavoriteImageView.setImageResource(R.drawable.ic_star_empty_color_text_secondary_vector);
            }
        }



        private class OnFavoriteClickListener implements View.OnClickListener {
            private final Recipe recipe;

            public OnFavoriteClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            @Override
            public void onClick(View view) {

                if (homeView.loadFavorite(recipe) != null) {
                    mFavoriteImageView.setImageResource(R.drawable.ic_star_empty_color_text_secondary_vector);
                    homeView.showMessage(R.string.deleted_this_recipe_from_your_favorite_list);
                    homeView.removeFavorite(recipe);
                } else {
                    mFavoriteImageView.setImageResource(R.drawable.ic_star_full_vector);
                    homeView.showMessage(R.string.added_this_recipe_to_your_favorite_list);
                    homeView.insertFavorite(recipe);
                }
            }
        }


        private class OnItemViewClickListener implements View.OnClickListener {
            private final Recipe recipe;

            public OnItemViewClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            @Override
            public void onClick(View view) {
                homeView.gotoDetailPage(recipe);
            }
        }
    }


}
