package com.zest.android


/**
 * This uses in all views
 *
 * @param <T>
 */
interface BaseView<T>{

    fun setPresenter(presenter: T)
}