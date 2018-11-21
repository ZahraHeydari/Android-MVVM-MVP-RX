package com.zest.android.util;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * To make it optimized used as {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * in DataBinding Architecture.
 * <p>
 * Created by ZARA on 09/25/2018.
 */
public abstract class DataBindingViewHolder<T> extends RecyclerView.ViewHolder {

    /**
     * To compromise in combination of the {@link ViewDataBinding}
     * and the {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * we need some abstractions and also interfaces.
     */
    private final ViewDataBinding dataBinding;

    /**
     * To make it possible to pass {@linkplain ViewDataBinding}
     *
     * @param dataBinding
     */
    public DataBindingViewHolder(ViewDataBinding dataBinding) {
        super(dataBinding.getRoot());
        this.dataBinding = dataBinding;
    }

    /**
     * When you want to reduce the redundancy you can use this constructor and it'll be calling the
     * {@link DataBindingViewHolder#DataBindingViewHolder(ViewDataBinding)} by itself as overloading
     * constructor :")
     *
     * @param inflater
     * @param layoutId
     * @param parent
     * @param attachToParent
     */
    public DataBindingViewHolder(@NonNull LayoutInflater inflater,
                                 @IdRes int layoutId,
                                 @NonNull ViewGroup parent,
                                 @NonNull boolean attachToParent) {
        this(DataBindingUtil.inflate(inflater, layoutId, parent, attachToParent));
    }

    /**
     * To call on
     * {@link android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     * and bind the data model object as parameter.
     *
     * @param t instance of {@link T} class to bind values on Views and set the unexpected behaviors
     */
    public abstract void onBind(T t);

    /**
     * To get {@link #dataBinding}
     *
     * @return
     */
    public ViewDataBinding getDataBinding() {
        return dataBinding;
    }
}
