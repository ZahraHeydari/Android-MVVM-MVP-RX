package com.zest.android.util

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * To make it optimized used as [android.support.v7.widget.RecyclerView.ViewHolder]
 * in DataBinding Architecture.
 *
 *
 * Created by ZARA on 09/25/2018.
 */
abstract class DataBindingViewHolder<T>
/**
 * To make it possible to pass [ViewDataBinding]
 *
 * @param dataBinding
 */
(
        /**
         * To compromise in combination of the [ViewDataBinding]
         * and the [android.support.v7.widget.RecyclerView.ViewHolder]
         * we need some abstractions and also interfaces.
         */
        /**
         * To get [.dataBinding]
         *
         * @return
         */
        val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {

    /**
     * When you want to reduce the redundancy you can use this constructor and it'll be calling the
     * [DataBindingViewHolder.DataBindingViewHolder] by itself as overloading
     * constructor :")
     *
     * @param inflater
     * @param layoutId
     * @param parent
     * @param attachToParent
     */
    constructor(inflater: LayoutInflater,
                @IdRes layoutId: Int,
                parent: ViewGroup,
                attachToParent: Boolean) : this(DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, attachToParent)) {
    }

    /**
     * To call on
     * [android.support.v7.widget.RecyclerView.Adapter.onBindViewHolder]
     * and bind the data model object as parameter.
     *
     * @param t instance of [T] class to bind values on Views and set the unexpected behaviors
     */
    abstract fun onBind(t: T)
}