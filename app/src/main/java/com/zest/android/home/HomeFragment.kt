package com.zest.android.home

import android.content.Context
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.RecipeRepository
import com.zest.android.databinding.FragmentHomeBinding
import com.zest.android.util.NetworkStateReceiver
import java.util.*

/**
 * Display a grid of [Recipe]s. User can choose to view each recipe.
 *
 * Created by ZARA on 08/10/2018.
 */
class HomeFragment : Fragment(), NetworkStateReceiver.OnNetworkStateReceiverListener, OnHomeFragmentInteractionListener {


    private val TAG = HomeFragment::class.java.name
    private val mRecipes = ArrayList<Recipe>()
    private var mAdapter: RecipesAdapter? = null
    private var mCallback: OnHomeCallback? = null
    private var mNetworkReceiver: NetworkStateReceiver? = null
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var recipeViewModel: RecipeViewModel
    private var mEmptyTextView: TextView? = null


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
        mAdapter = RecipesAdapter(this)
        mNetworkReceiver = NetworkStateReceiver()
        mNetworkReceiver?.addListener(this)
        context?.registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun scrollUp() {
        fragmentHomeBinding.homeRecyclerView.post {
            fragmentHomeBinding.homeRecyclerView.smoothScrollToPosition(0)// Call smooth scroll
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        recipeViewModel = RecipeViewModel(RecipeRepository(), this)
        fragmentHomeBinding.recipeViewModel = recipeViewModel
        fragmentHomeBinding.executePendingBindings()
        mEmptyTextView = fragmentHomeBinding.homeEmptyView.findViewById(R.id.empty_text_view)
        mEmptyTextView?.text = getString(R.string.there_is_no_recipe)
        mCallback?.showFab(true)
        fragmentHomeBinding.homeRecyclerView.adapter = mAdapter
        recipeViewModel.getRecipes()

        return fragmentHomeBinding.root
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
        mAdapter = null
    }

    override fun showMessage(message: Int) {
        Snackbar.make(fragmentHomeBinding.root, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    override fun gotoDetailPage(recipe: Recipe) {
        mCallback?.gotoDetailPage(recipe)
    }

    override fun loadRecipes(recipes: List<Recipe>) {
        Log.d(TAG, "loadRecipes() called with: mRecipes = [$recipes]")
        mAdapter?.addData(recipes)
    }

    override fun loadFavorite(recipe: Recipe): Recipe?{
        return recipeViewModel.loadFavoriteByRecipeId(recipe)
    }

    override fun removeFavorite(recipe: Recipe) {
        Log.d(TAG, "removeFavorite() called with: recipe = [$recipe]")
        recipeViewModel.deleteFavoriteByRecipeId(recipe)
    }

    override fun insertFavorite(recipe: Recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [$recipe]")
        recipeViewModel.insertFavoriteRecipe(recipe)
    }

    override fun networkAvailable() {
        Log.d(TAG, "networkAvailable() called")
        recipeViewModel.getRecipes()
    }

    override fun networkUnavailable() {
        Log.d(TAG, "networkUnavailable() called")
        if (mRecipes.isEmpty()) {
            showEmptyView(true)
        }
        mEmptyTextView?.setText(R.string.check_internet_and_try_again_please)
    }

    override fun showEmptyView(visibility: Boolean) {
        fragmentHomeBinding.homeEmptyView.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mNetworkReceiver?.removeListener(this)
        unregisterNetworkChanges()
    }

    private fun unregisterNetworkChanges() {
        Log.d(TAG, "unregisterNetworkChanges() called")
        context?.unregisterReceiver(mNetworkReceiver)

    }

    companion object {

        val FRAGMENT_NAME = HomeFragment::class.java.simpleName


        fun newInstance() =  HomeFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}
