package com.zest.android.favorite;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.databinding.HolderFavoriteBinding;
import com.zest.android.home.RecipeViewModel;
import com.zest.android.util.DataBindingViewHolder;

import java.util.List;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to adapt
 * Favorite{@link Recipe} items into {@link RecyclerView} via {@link FavoriteViewHolder}
 * <p>
 * Created by ZARA on 8/10/2018.
 */
class FavoriteAdapter extends RecyclerView.Adapter {


    private final OnFavoriteFragmentInteractionListener listener;
    private final List<Recipe> recipes;

    public FavoriteAdapter(OnFavoriteFragmentInteractionListener listener, List<Recipe> recipes) {
        this.listener = listener;
        this.recipes = recipes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderFavoriteBinding holderFavoriteBinding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()), R.layout.holder_favorite, parent, false);
        return new FavoriteViewHolder(holderFavoriteBinding);
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

    public void addData(List<Recipe> recipes) {
        this.recipes.clear();
        if (recipes != null && !recipes.isEmpty()) {
            this.recipes.addAll(recipes);
        }
        notifyDataSetChanged();
    }

    /**
     * The holder of favorite
     */
    public class FavoriteViewHolder extends DataBindingViewHolder<Recipe> {

        public FavoriteViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void onBind(Recipe recipe) {
            (((HolderFavoriteBinding) this.getDataBinding()))
                    .setRecipeViewModel(new RecipeViewModel(recipe, listener));
        }
    }
}
