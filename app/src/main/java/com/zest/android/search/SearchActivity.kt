package com.zest.android.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.SearchRepository
import com.zest.android.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import kotlinx.android.synthetic.main.empty_view.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

/**
 * @Author ZARA.
 */
class SearchActivity : LifecycleLoggingActivity(), SearchContract.View {


    private var mPresenter: SearchContract.UserActionsListener? = null
    private var mQuery: String? = null
    private var mSearchView: SearchView? = null
    private val mRecipes = ArrayList<Recipe>()
    private var mAdapter: SearchAdapter? = null
    private var mMenuSearchItem: MenuItem? = null
    private var text: String? = null


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        empty_text_view.setText(R.string.no_result)

        SearchPresenter(this, SearchRepository())

        mAdapter = SearchAdapter(this, mRecipes)
        search_recycler_view.setAdapter(mAdapter)

        if (intent != null && Action_SEARCH_TAG.equals(intent.action)) {
            if (intent != null && intent.extras != null && intent.extras.containsKey(String::class.java.name)) {
                text = intent.extras.getString(String::class.java.name)
                mPresenter?.searchQuery(text!!)
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
        mMenuSearchItem?.expandActionView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSearchView?.setForegroundGravity(GravityCompat.RELATIVE_LAYOUT_DIRECTION)
        }
        mSearchView?.setHorizontalGravity(Gravity.END)
        mSearchView?.setQueryHint(getString(R.string.action_search))
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (mSearchView != null) {
            mSearchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        mSearchView?.setOnQueryTextListener(OnSearchQueryTextListener())
        mSearchView?.setOnCloseListener(OnSearchCloseListener())

        if (intent != null && Action_SEARCH_TAG.equals(intent.action)) {
            //when received with text
            mSearchView?.setQuery(text, false)
        }
        mSearchView?.setIconified(false)
        return true
    }


    override fun onBackPressed() {
        if (mSearchView != null) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mPresenter != null) {
            mPresenter?.start()
        }
    }

    override fun setPresenter(presenter: SearchContract.UserActionsListener) {
        mPresenter = checkNotNull(presenter)
    }


    override fun gotoDetailPage(recipe: Recipe) {
        DetailActivity.start(this, recipe)
    }

    override fun setResult(recipes: List<Recipe>) {
        mRecipes.clear()
        mRecipes.addAll(recipes)
        mAdapter?.notifyDataSetChanged()
        showEmptyView()
    }

    override fun showEmptyView() {
        search_empty_view.setVisibility(if (mRecipes.isEmpty()) View.VISIBLE else View.GONE)
    }

    override fun showProgressBar(visibility: Boolean) {
        search_progress_bar.setVisibility(if (visibility) View.VISIBLE else View.GONE)
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
            mPresenter?.searchQuery(mQuery!!)
            MenuItemCompat.expandActionView(mMenuSearchItem)
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
         * Get a new Intent to Start Activity
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