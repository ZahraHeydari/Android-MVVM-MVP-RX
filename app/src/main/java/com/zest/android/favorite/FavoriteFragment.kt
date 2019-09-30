package com.zest.android.favorite

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
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

    private val TAG = FavoriteFragment::class.java.name
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
            throw ClassCastException("$context must implement OnFavoriteFragmentInteractionListener!")
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
        mPresenter?.start()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_favorite, container, false)

        root.empty_text_view.setText(R.string.you_have_not_any_favorite)
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
        mPresenter = presenter
    }

    /**
     * To check the display of empty view
     */
    private fun checkEmptyView() {
        val recipes = mPresenter?.loadFavorites()
        root.favorite_empty_view.visibility = if (recipes?.isEmpty() == true) View.VISIBLE else View.GONE
    }


    override fun deleteFavorite(recipe: Recipe) {
        mPresenter?.deleteFavoriteRecipe(recipe)
        val recipes = mPresenter?.loadFavorites()
        mAdapter?.updateData(recipes)
        checkEmptyView()
        Snackbar.make(root, getString(R.string.deleted_this_recipe_from_your_favorite_list),
                Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }


    override fun gotoDetailPage(recipe: Recipe) {
        mCallback?.gotoDetailPage(recipe)
    }

    companion object {

        val FRAGMENT_NAME = FavoriteFragment::class.java.simpleName

        fun newInstance() =
                FavoriteFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

}
