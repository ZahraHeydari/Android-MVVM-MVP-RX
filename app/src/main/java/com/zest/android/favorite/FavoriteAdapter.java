package com.zest.android.favorite;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
 * Favorite{@link Recipe} items into {@link RecyclerView} via {@link FavoriteViewHolder}
 * <p>
 * Created by ZARA on 8/10/2018.
 */
class FavoriteAdapter extends RecyclerView.Adapter {

    private final FavoriteContract.View favoriteView;
    private final List<Recipe> recipes;

    public FavoriteAdapter(FavoriteContract.View favoriteView, List<Recipe> recipes) {
        this.favoriteView = favoriteView;
        this.recipes = recipes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View favoriteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_favorite, parent, false);
        return new FavoriteViewHolder(favoriteView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FavoriteViewHolder) holder).onBind(getItem(position));

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
     * the holder of favorite
     */
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.favorite_title_text_view)
        TextView mTitleTextView;
        @BindView(R.id.favorite_icon_image_view)
        ImageView mFavoriteImageView;
        @BindView(R.id.favorite_image_view)
        ImageView mImageView;

        public FavoriteViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void onBind(final Recipe recipe) {
            itemView.setOnClickListener(new OnItemViewClickListener(recipe));
            mFavoriteImageView.setOnClickListener(new OnFavoriteClickListener(recipe));

            mTitleTextView.setText(recipe.getTitle());
            try {
                Picasso.with(itemView.getContext())
                        .load(recipe.getImage())
                        .into(mImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * To cancel delete favorite recipe
         */
        private class OnNegativeClickListener implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();// User cancelled the dialog
            }
        }

        /**
         * To delete favorite recipe
         */
        private class OnPositiveButtonClickListener implements DialogInterface.OnClickListener {
            private final Recipe recipe;

            OnPositiveButtonClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();//Cancel the dialog
                favoriteView.deleteFavorite(recipe);
            }
        }


        private class OnFavoriteClickListener implements View.OnClickListener {
            private final Recipe recipe;

            public OnFavoriteClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle(itemView.getContext().getString(R.string.delete))
                        .setMessage(itemView.getContext().getString
                                (R.string.are_you_sure_want_to_delete_this_item_from_favorite_list))
                        .setCancelable(false)
                        .setPositiveButton(itemView.getContext().getString(R.string.yes),
                                new OnPositiveButtonClickListener(recipe))
                        .setNegativeButton(itemView.getContext().getString(R.string.no),
                                new OnNegativeClickListener());
                builder.create();
                builder.show();
            }
        }


        private class OnItemViewClickListener implements View.OnClickListener {
            private final Recipe recipe;

            public OnItemViewClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            @Override
            public void onClick(View view) {
                favoriteView.gotoDetailPage(recipe);
            }
        }
    }
}
