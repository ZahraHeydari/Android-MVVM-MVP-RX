package com.zest.android.category

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.data.Category
import com.zest.android.data.source.CategoryRepository
import com.zest.android.databinding.FragmentCategoryBinding
import com.zest.android.home.OnHomeCallback

/**
 * Display a grid of [Category]s. User can choose to view the subs of each category.
 *
 * Created by ZARA on 09/30/2018.
 */
class CategoryFragment : Fragment(), OnCategoryFragmentInteractionListener {


    private val TAG = CategoryFragment::class.java.name
    private var mAdapter: CategoryAdapter? = null
    private var mCallback: OnHomeCallback? = null
    private lateinit var fragmentCategoryBinding: FragmentCategoryBinding
    private var categoryViewModel: CategoryViewModel? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnHomeCallback) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement OnHomeCallback!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = CategoryViewModel(CategoryRepository(), this)
        mAdapter = CategoryAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        fragmentCategoryBinding.categoryViewModel = categoryViewModel
        fragmentCategoryBinding.executePendingBindings()
        fragmentCategoryBinding.categoryRecyclerView.adapter = mAdapter
        categoryViewModel?.loadCategories()
        return fragmentCategoryBinding.root
    }

    override fun onResume() {
        super.onResume()
        mCallback?.showFab(false)
    }

    override fun showSubCategories(category: Category) {
        mCallback?.showSubCategoriesByCategoryTitle(category)
    }

    override fun setResult(categories: List<Category>) {
        mAdapter?.addData(categories)
    }

    override fun onDetach() {
        super.onDetach()
        mAdapter = null
        mCallback = null
    }

    fun scrollUp() {
        fragmentCategoryBinding.categoryRecyclerView.post {
            fragmentCategoryBinding.categoryRecyclerView.smoothScrollToPosition(0)// Call smooth scroll
        }
    }

    companion object {

        val FRAGMENT_NAME = CategoryFragment::class.java.simpleName


        fun newInstance() = CategoryFragment().apply {
            Bundle().apply {

            }
        }
    }
}
