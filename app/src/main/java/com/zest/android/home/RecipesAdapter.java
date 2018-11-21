package com.zest.android.home;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.HomeRepository;
import com.zest.android.databinding.HolderRecipeBinding;
import com.zest.android.util.DataBindingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to adapt
 * {@link Recipe} items into {@link RecyclerView} via {@link RecipeViewHolder}
 * <p>
 * Created by ZARA on 08/06/2018.
 */
class RecipesAdapter extends RecyclerView.Adapter {

    private static final String TAG = RecipesAdapter.class.getSimpleName();
    private final OnHomeFragmentInteractionListener listener;
    private List<Recipe> recipes;


    public RecipesAdapter(OnHomeFragmentInteractionListener listener) {
        this.listener = listener;
        this.recipes = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderRecipeBinding holderRecipeBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_recipe, parent, false);
        return new RecipeViewHolder(holderRecipeBinding);
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

    public void addData(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    /**
     * Holder of {@link Recipe}
     */
    public class RecipeViewHolder extends DataBindingViewHolder<Recipe> {

        public RecipeViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void onBind(Recipe recipe) {
            (((HolderRecipeBinding) this.getDataBinding())).setRecipeViewModel(new RecipeViewModel(recipe,
                    new HomeRepository(itemView.getContext()), listener));
        }
    }


}
