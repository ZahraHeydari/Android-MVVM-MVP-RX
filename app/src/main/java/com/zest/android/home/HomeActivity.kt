package com.zest.android.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.category.CategoryFragment
import com.zest.android.data.Recipe
import com.zest.android.data.source.HomeRepository
import com.zest.android.detail.DetailActivity
import com.zest.android.list.ListActivity
import com.zest.android.search.SearchActivity
import com.zest.android.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class HomeActivity : LifecycleLoggingActivity(), NavigationView.OnNavigationItemSelectedListener, OnHomeCallback {


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(home_toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        home_toolbar_title_image_view.setOnClickListener {
            val fragmentById = supportFragmentManager.findFragmentById(R.id.home_container)
            if (fragmentById != null && fragmentById is HomeFragment) {
                fragmentById.scrollUp()
            } else if (fragmentById != null && fragmentById is CategoryFragment) {
                fragmentById.scrollUp()
            }
        }

        val toggle = ActionBarDrawerToggle(this, home_drawer_layout, home_toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        home_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        var homeFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.home_container)
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, homeFragment, R.id.home_container)
        }

        HomePresenter((homeFragment as HomeContract.View), HomeRepository())

        home_fab.setOnClickListener {
            ActivityUtils.replaceFragmentInActivity(
                    supportFragmentManager,
                    CategoryFragment.newInstance(),
                    CategoryFragment.FRAGMENT_NAME,
                    R.id.home_container)
            hideFab()
        }
    }

    override fun hideFab() {
        home_fab.hide()
    }

    override fun showFab() {
        home_fab.show()
    }

    override fun gotoDetailPage(recipe: Recipe) {
        DetailActivity.start(this, recipe)
    }

    override fun showSubCategoriesByCategoryTitle(categoryTitle: String) {
        SearchActivity.startWithText(this@HomeActivity, categoryTitle)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.nav_recipes) {
            // Handle the favorite action
        } else if (id == R.id.nav_favorite) {
            ListActivity.startWithFavorite(this)
        }
        home_drawer_layout.closeDrawer(GravityCompat.START)
        return true
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


    override fun onStart() {
        super.onStart()
        home_nav_view.setNavigationItemSelectedListener(this)
        home_nav_view.setCheckedItem(R.id.nav_recipes)
    }

    override fun onBackPressed() {
        if (home_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            home_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }


}