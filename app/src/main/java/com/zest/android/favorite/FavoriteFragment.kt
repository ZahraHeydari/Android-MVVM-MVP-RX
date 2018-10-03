package com.zest.android.favorite

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.FavoriteRepository
import kotlinx.android.synthetic.main.empty_view.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.util.*

/**
 * Display a grid of Favorite [Recipe]s. User can choose to view each favorite recipe.
 *
 * Created by ZARA on 09/24/2018.
 */
class FavoriteFragment : Fragment(), FavoriteContract.View {


    private lateinit var root: View
    private var mCallback: OnFavoriteFragmentInteractionListener? = null
    private var mPresenter: FavoriteContract.UserActionsListener? = null
    private var mAdapter: FavoriteAdapter? = null
    private var mRecipes: MutableList<Recipe> = ArrayList()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFavoriteFragmentInteractionListener) {
            mCallback = context
        } else {
            throw ClassCastException(context.toString() + "must implement OnFavoriteFragmentInteractionListener!")
        }
        FavoritePresenter(this, FavoriteRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCallback?.updateToolbarTitle(R.string.favorite)
        mRecipes = mPresenter?.loadFavorites() as MutableList<Recipe>
    }

    override fun onStart() {
        super.onStart()
        if (mPresenter != null) {
            mPresenter?.start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_favorite, container, false)

        root.empty_text_view.setText(R.string.you_have_not_any_favorite)
        root.favorite_recycler_view.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        mAdapter = FavoriteAdapter(this, mRecipes)
        root.favorite_recycler_view.adapter = mAdapter
        checkEmptyView()

        return root
    }


    override fun onDetach() {
        super.onDetach()
        mCallback = null
        mAdapter = null
    }

    override fun setPresenter(presenter: FavoriteContract.UserActionsListener) {
        mPresenter = checkNotNull(presenter)
    }

    /**
     * To check the display of empty view
     */
    fun checkEmptyView() {
        val recipes = mPresenter?.loadFavorites()
        root.favorite_empty_view.visibility = if (recipes!!.isEmpty()) View.VISIBLE else View.GONE
    }


    override fun deleteFavorite(recipe: Recipe) {
        mPresenter?.deleteFavoriteRecipe(recipe)
        mRecipes.clear()
        val recipes = mPresenter!!.loadFavorites()
        if (!recipes.isEmpty()) {
            mRecipes.addAll(recipes)
        }
        mAdapter?.notifyDataSetChanged()
        checkEmptyView()
        Snackbar.make(root, getString(R.string.deleted_this_recipe_from_your_favorite_list),
                Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }


    override fun gotoDetailPage(recipe: Recipe) {
        mCallback?.gotoDetailPage(recipe)
    }

    companion object {

        val FRAGMENT_NAME = FavoriteFragment::class.java.name
        private val TAG = FavoriteFragment::class.java.simpleName
        private val SPAN_COUNT = 2

        fun newInstance(): FavoriteFragment {
            val args = Bundle()
            val fragment = FavoriteFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
