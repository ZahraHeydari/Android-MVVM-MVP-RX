package com.zest.android.search;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.databinding.HolderSearchBinding;
import com.zest.android.home.RecipeViewModel;
import com.zest.android.util.DataBindingViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to adapt
 * {@link Recipe} items into {@link RecyclerView} via {@link SearchViewHolder}
 *
 * Created by ZARA on 8/18/2018.
 */
public class SearchAdapter extends RecyclerView.Adapter {


    private static final String TAG = SearchAdapter.class.getSimpleName();
    private final List<Recipe> recipes;
    private final OnSearchCallback callback;


    public SearchAdapter(OnSearchCallback callback) {
        this.callback = callback;
        this.recipes = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderSearchBinding holderSearchBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.holder_search, parent, false);
        return new SearchViewHolder(holderSearchBinding);
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
        return recipes.size();
    }

    public void addData(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void removePreviousData() {
        this.recipes.clear();
        notifyDataSetChanged();
    }

    /**
     * holder of search item
     */
    public class SearchViewHolder extends DataBindingViewHolder<Recipe> {

        public SearchViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void onBind(Recipe recipe) {
            ((HolderSearchBinding) this.getDataBinding())
                    .setRecipeViewModel(new RecipeViewModel(recipe, callback));
        }
    }
}
