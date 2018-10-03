package com.zest.android.category

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.data.Category
import com.zest.android.data.Recipe
import com.zest.android.data.source.CategoryRepository
import com.zest.android.home.OnHomeCallback
import kotlinx.android.synthetic.main.fragment_category.view.*
import java.util.*

/**
 * Display a grid of [Category]s. User can choose to view each category subs.
 *
 * Created by ZARA on 09/24/2018.
 */
class CategoryFragment : Fragment(), CategoryContract.View {

    private lateinit var root: View
    private var mPresenter: CategoryContract.UserActionsListener? = null
    private var mCallback: OnHomeCallback? = null
    private var mAdapter: CategoryAdapter? = null
    private val mCategories = ArrayList<Category>()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnHomeCallback) {
            mCallback = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnHomeCallback!")
        }
        mCallback?.hideFab()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CategoryPresenter(this, CategoryRepository())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_category, container, false)

        mAdapter = CategoryAdapter(this, mCategories)
        root.category_recycler_view.setAdapter(mAdapter)
        mPresenter?.loadCategories()

        return root
    }

    fun scrollUp() {
        root.category_recycler_view.post({
            root.category_recycler_view.smoothScrollToPosition(0)// Call smooth scroll
        })
    }

    override fun onStart() {
        super.onStart()
        if (mPresenter != null) {
            mPresenter?.start()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mAdapter = null
        mCallback = null
    }


    override fun setPresenter(presenter: CategoryContract.UserActionsListener) {
        mPresenter = checkNotNull(presenter)
    }

    override fun showSubCategories(category: Category) {
        mCallback?.showSubCategoriesByCategoryTitle(category.title)
    }

    override fun setResult(categories: List<Category>) {
        mCategories.addAll(categories)
        mAdapter?.notifyDataSetChanged()
    }

    override fun showProgressBar(visibility: Boolean) {
        root.category_progress_bar.setVisibility(if (visibility) View.VISIBLE else View.GONE)
    }

    companion object {

        val FRAGMENT_NAME = CategoryFragment::class.java.simpleName
        val TAG = CategoryFragment::class.java.simpleName

        fun newInstance(): CategoryFragment {
            val fragment = CategoryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}