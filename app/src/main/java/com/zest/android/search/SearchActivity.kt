package com.zest.android.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.RecipeRepository
import com.zest.android.databinding.ActivitySearchBinding
import com.zest.android.detail.DetailActivity
import com.zest.android.home.RecipeViewModel
import org.parceler.Parcels
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @Author ZARA.
 */
class SearchActivity : LifecycleLoggingActivity(), OnSearchCallback {

    private var mQuery: String? = null
    private var mSearchView: SearchView? = null
    private var mAdapter: SearchAdapter? = null
    private var mMenuSearchItem: MenuItem? = null
    private var searchQuery: String? = null
    private var activitySearchBinding: ActivitySearchBinding? = null
    private var recipeViewModel: RecipeViewModel? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        recipeViewModel = RecipeViewModel(RecipeRepository(), this)
        activitySearchBinding!!.recipeViewModel = recipeViewModel
        activitySearchBinding!!.executePendingBindings()

        setSupportActionBar(activitySearchBinding!!.searchToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        (activitySearchBinding!!.searchEmptyView.findViewById<View>(R.id.empty_text_view) as TextView).text = getString(R.string.no_result)

        mAdapter = SearchAdapter(this)
        activitySearchBinding!!.searchRecyclerView.adapter = mAdapter
        if (intent != null && Action_SEARCH_TAG == intent.action) {
            if (intent != null && intent.extras != null && intent.extras.containsKey(String::class.java.name)) {
                searchQuery = intent.extras.getString(String::class.java.name)
                recipeViewModel!!.searchQuery(searchQuery!!)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search, menu)
        mSearchView = MenuItemCompat.getActionView(menu.findItem(R.id.search)) as SearchView
        mMenuSearchItem = menu.findItem(R.id.search)
        mMenuSearchItem!!.expandActionView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSearchView!!.foregroundGravity = GravityCompat.RELATIVE_LAYOUT_DIRECTION
        }
        mSearchView!!.setHorizontalGravity(Gravity.END)
        mSearchView!!.queryHint = getString(R.string.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (mSearchView != null) {
            mSearchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        mSearchView!!.setOnQueryTextListener(OnSearchQueryTextListener())
        mSearchView!!.setOnCloseListener(OnSearchCloseListener())

        if (intent != null && Action_SEARCH_TAG == intent.action) {
            //when received with searchQuery
            mSearchView!!.setQuery(searchQuery, false)
        }
        mSearchView!!.isIconified = false
        return true
    }

    override fun onBackPressed() {
        if (mSearchView != null) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun gotoDetailPage(recipe: Recipe) {
        DetailActivity.start(this, recipe)
    }

    override fun showEmptyView(visibility: Boolean) {
        activitySearchBinding!!.searchEmptyView.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    override fun noResult() {
        mAdapter!!.removePreviousData()
    }

    override fun setResult(recipes: List<Recipe>) {
        mAdapter!!.addData(recipes)
    }

    private inner class OnSearchCloseListener : SearchView.OnCloseListener {
        override fun onClose(): Boolean {
            finish()
            return false
        }
    }

    private inner class OnSearchQueryTextListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            mQuery = query
            recipeViewModel!!.searchQuery(mQuery!!)
            MenuItemCompat.expandActionView(mMenuSearchItem!!)
            return false
        }

        override fun onQueryTextChange(s: String): Boolean {
            return false
        }
    }

    companion object {

        private val TAG = SearchActivity::class.java.simpleName
        private val Action_SEARCH_TAG = "com.zest.android.ACTION_SEARCH_TAG"

        /**
         * get new Intent to Start Activity
         *
         * @param context
         * @return
         */
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }

        /**
         * Get new Intent to start activity with text
         *
         * @param context
         * @param text
         * @return
         */
        fun startWithText(context: Context, text: String) {
            val starter = Intent(context, SearchActivity::class.java)
            val bundle = Bundle()
            starter.putExtra(String::class.java.name, text)
            starter.putExtras(bundle)
            starter.action = Action_SEARCH_TAG
            context.startActivity(starter)
        }
    }

}
