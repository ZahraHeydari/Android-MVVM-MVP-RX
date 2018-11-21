package com.zest.android.category;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zest.android.R;
import com.zest.android.data.Category;
import com.zest.android.databinding.HolderCategoryBinding;
import com.zest.android.util.DataBindingViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to adapt
 * {@link Category} items into {@link RecyclerView} via {@link CategoryViewHolder}
 *
 * Created by ZARA on 09/30/2018.
 */
public class CategoryAdapter extends RecyclerView.Adapter {

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private final OnCategoryFragmentInteractionListener listener;
    private final List<Category> categories;


    public CategoryAdapter(OnCategoryFragmentInteractionListener listener) {
        this.listener = listener;
        this.categories = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HolderCategoryBinding viewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.holder_category, parent, false);
        return new CategoryViewHolder(viewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CategoryViewHolder) holder).onBind(getItem(position));
    }

    private Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addData(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        this.notifyDataSetChanged();
    }

    /**
     * holder of {@link Category}
     */
    private class CategoryViewHolder extends DataBindingViewHolder<Category> {

        public CategoryViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
        }

        @Override
        public void onBind(Category category) {
            ((HolderCategoryBinding) this.getDataBinding())
                    .setCategoryViewModel(new CategoryViewModel(category, listener));
        }
    }


}
