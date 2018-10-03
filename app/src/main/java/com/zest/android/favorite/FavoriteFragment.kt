package com.zest.android.favorite

import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.RecipeRepository
import com.zest.android.databinding.FragmentFavoriteBinding
import com.zest.android.home.RecipeViewModel
import com.zest.android.list.OnListCallback

/**
 * Display a grid of Favorite [Recipe]s. User can choose to view each favorite recipe.
 *
 * Created by ZARA on 08/10/2018.
 */
class FavoriteFragment : Fragment(), OnFavoriteFragmentInteractionListener {

    private var mCallback: OnListCallback? = null
    private lateinit var fragmentFavoriteBinding: FragmentFavoriteBinding
    private var mAdapter: FavoriteAdapter? = null
    private var recipeViewModel: RecipeViewModel? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListCallback) {
            mCallback = context
        } else {
            throw ClassCastException(context.toString() + "must implement OnListCallback!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCallback!!.updateActionBarTitle(R.string.favorite)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentFavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        recipeViewModel = RecipeViewModel(RecipeRepository(), this)
        fragmentFavoriteBinding.recipeViewModel = recipeViewModel
        fragmentFavoriteBinding.executePendingBindings()
        (fragmentFavoriteBinding.favoriteEmptyView.findViewById<View>(R.id.empty_text_view) as TextView)
                .setText(R.string.you_have_not_any_favorite)
        mAdapter = FavoriteAdapter(this)
        fragmentFavoriteBinding.favoriteRecyclerView.adapter = mAdapter

        return fragmentFavoriteBinding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume (line 68): ")
        mAdapter?.addData(recipeViewModel!!.loadFavorites())
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
        mAdapter = null
    }

    override fun showDeleteFavoriteDialog(recipe: Recipe) {
        if (context == null) return
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_want_to_delete_this_item_from_favorite_list))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), OnPositiveButtonClickListener(recipe))
                .setNegativeButton(getString(R.string.no), OnNegativeClickListener())
        builder.create()
        builder.show()
    }

    /**
     * To cancel delete favorite recipe
     */
    private inner class OnNegativeClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, id: Int) {
            dialog.dismiss()// User cancelled the dialog
        }
    }

    /**
     * To Submit the delete favorite recipe
     */
    private inner class OnPositiveButtonClickListener
    internal constructor(private val recipe: Recipe) : DialogInterface.OnClickListener {

        override fun onClick(dialog: DialogInterface, id: Int) {
            dialog.dismiss()//Cancel the dialog
            recipeViewModel!!.deleteFavoriteRecipe(recipe)
            mAdapter!!.addData(recipeViewModel!!.loadFavorites())
            Snackbar.make(fragmentFavoriteBinding.root, getString(R.string.deleted_this_recipe_from_your_favorite_list),
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }

    override fun gotoDetailPage(recipe: Recipe) {
        mCallback!!.gotoDetailPage(recipe)
    }

    companion object {

        val FRAGMENT_NAME = FavoriteFragment::class.java.name
        private val TAG = FavoriteFragment::class.java.simpleName

        fun newInstance(): FavoriteFragment {
            val args = Bundle()
            val fragment = FavoriteFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
