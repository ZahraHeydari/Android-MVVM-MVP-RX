package com.zest.android.home

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.util.NetworkStateReceiver
import com.zest.android.util.showSnackBar
import kotlinx.android.synthetic.main.empty_view.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


/**
 * Display a grid of [Recipe]s. User can choose to view each recipe.
 *
 * Created by ZARA on 09/25/2018.
 */
class HomeFragment : Fragment(), HomeContract.View, NetworkStateReceiver.OnNetworkStateReceiverListener {

    private lateinit var root: View
    private var mPresenter: HomeContract.UserActionsListener? = null
    private val mRecipes = ArrayList<Recipe>()
    private var mAdapter: RecipesAdapter? = null
    private var mCallback: OnHomeCallback? = null
    private var mNetworkReceiver: NetworkStateReceiver? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnHomeCallback) {
            mCallback = context
        } else {
            throw ClassCastException(context.toString() + "must implement OnHomeCallback!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = RecipesAdapter(this, mRecipes)

        mNetworkReceiver = NetworkStateReceiver()
        mNetworkReceiver?.addListener(this)
        if (context != null) {
            context?.registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        mCallback?.showFab()
        root.home_recycler_view.setAdapter(mAdapter)
        //mPresenter?.getRecipes()
        return root
    }

    fun scrollUp() {
        root.home_recycler_view.post {
            root.home_recycler_view.smoothScrollToPosition(0)// Call smooth scroll
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart() called")
        super.onStart()
        mPresenter?.start()
    }


    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    override fun setPresenter(presenter: HomeContract.UserActionsListener) {
        mPresenter = checkNotNull(presenter)
    }

    override fun showMessage(message: Int) {
        view?.showSnackBar(message = getString(message))
    }

    override fun gotoDetailPage(recipe: Recipe) {
        mCallback?.gotoDetailPage(recipe)
    }

    override fun loadRecipes(recipes: List<Recipe>) {
        Log.d(TAG, "loadRecipes() called with: mRecipes = [$recipes]")
        mRecipes.clear()
        mRecipes.addAll(recipes)
        mAdapter?.notifyDataSetChanged()
    }

    override fun loadFavorite(recipe: Recipe): Recipe? {
        return mPresenter?.loadFavoriteByRecipeId(recipe)
    }

    override fun removeFavorite(recipe: Recipe) {
        Log.d(TAG, "removeFavorite() called with: recipe = [$recipe]")
        mPresenter?.deleteFavoriteByRecipeId(recipe)
    }

    override fun insertFavorite(recipe: Recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [$recipe]")
        mPresenter?.insertFavoriteRecipe(recipe)
    }

    override fun showProgressBar(visibility: Boolean) {
        root.home_progress_bar.setVisibility(if (visibility) View.VISIBLE else View.GONE)
    }

    override fun networkAvailable() {
        Log.d(TAG, "networkAvailable() called")
        mPresenter?.getRecipes()
    }

    override fun networkUnavailable() {
        Log.d(TAG, "networkUnavailable() called")
        if (mRecipes.isEmpty()) {
            showEmptyView(true)
        }
        showProgressBar(false)
        root.empty_text_view.setText(R.string.check_internet_and_try_again_please)
    }

    override fun showEmptyView(visibility: Boolean) {
        root.home_empty_view.setVisibility(if (visibility) View.VISIBLE else View.GONE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mNetworkReceiver?.removeListener(this)
        unregisterNetworkChanges()
    }

    protected fun unregisterNetworkChanges() {
        Log.d(TAG, "unregisterNetworkChanges() called")
        try {
            if (context != null) {
                context?.unregisterReceiver(mNetworkReceiver)
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }


    companion object {

        val FRAGMENT_NAME = HomeFragment::class.java.name
        private val TAG = HomeFragment::class.java.simpleName


        fun newInstance() = HomeFragment().apply{
            arguments = Bundle().apply {

            }
        }

    }

}