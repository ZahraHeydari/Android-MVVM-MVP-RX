package com.zest.android.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.category.CategoryFragment
import com.zest.android.data.Category
import com.zest.android.data.Recipe
import com.zest.android.databinding.ActivityHomeBinding
import com.zest.android.detail.DetailActivity
import com.zest.android.list.ListActivity
import com.zest.android.search.SearchActivity
import com.zest.android.util.ActivityUtils
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @Author ZARA.
 */
class HomeActivity : LifecycleLoggingActivity(), NavigationView.OnNavigationItemSelectedListener, OnHomeCallback {

    private lateinit var activityHomeBinding: ActivityHomeBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setSupportActionBar(activityHomeBinding.homeToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

        val toggle = ActionBarDrawerToggle(
                this, activityHomeBinding.drawerLayout, activityHomeBinding.homeToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        activityHomeBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        var homeFragment = supportFragmentManager.findFragmentById(R.id.home_container)
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, homeFragment, R.id.home_container)
        }
        activityHomeBinding.homeToolbarTitleImageView.setOnClickListener(OnToolbarImageClickListener())
    }

    override fun onStart() {
        super.onStart()
        activityHomeBinding.homeFab.setOnClickListener(OnFABClickListener())
        activityHomeBinding.navView.setNavigationItemSelectedListener(this)
        activityHomeBinding.navView.setCheckedItem(R.id.nav_recipes)
    }

    override fun onBackPressed() {
        if (activityHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_search) {
            SearchActivity.start(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_recipes) {
            //nothing to do
        } else if (id == R.id.nav_favorite) {
            ListActivity.startWithFavorite(this)
        }
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showFab(visibility: Boolean) {
        if (visibility) activityHomeBinding.homeFab.show() else activityHomeBinding.homeFab.hide()
    }

    override fun gotoDetailPage(recipe: Recipe) {
        DetailActivity.start(this, recipe)
    }

    override fun showSubCategoriesByCategoryTitle(category: Category) {
        SearchActivity.startWithText(this@HomeActivity, category.title)
    }

    private inner class OnFABClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            ActivityUtils.replaceFragmentInActivity(
                    supportFragmentManager,
                    CategoryFragment.newInstance(),
                    CategoryFragment.FRAGMENT_NAME,
                    R.id.home_container)
        }
    }

    private inner class OnToolbarImageClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val fragmentById = supportFragmentManager.findFragmentById(R.id.home_container)
            if (fragmentById != null && fragmentById is HomeFragment) {
                fragmentById.scrollUp()
            } else if (fragmentById is CategoryFragment) {
                fragmentById.scrollUp()
            }
        }
    }

    companion object {

        private val TAG = HomeActivity::class.java.simpleName

        fun start(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }
}
