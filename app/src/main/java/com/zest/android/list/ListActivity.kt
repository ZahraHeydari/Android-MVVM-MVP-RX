package com.zest.android.list

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.databinding.ActivityListBinding
import com.zest.android.detail.DetailActivity
import com.zest.android.favorite.FavoriteFragment
import com.zest.android.util.ActivityUtils
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @Author ZARA.
 */
class ListActivity : LifecycleLoggingActivity(), OnListCallback {

    private val TAG = ListActivity::class.java.name
    lateinit var activityListBinding: ActivityListBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        setSupportActionBar(activityListBinding.listToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityListBinding.listToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))

        intent?.action?.let {
            when (it) {
                ACTION_FAVORITE -> ActivityUtils.addFragmentToActivity(
                        supportFragmentManager,
                        FavoriteFragment.newInstance(),
                        R.id.list_container)
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


    override fun updateActionBarTitle(resId: Int) {
        supportActionBar?.setTitle(resId)
    }

    override fun gotoDetailPage(recipe: Recipe) {
        DetailActivity.start(this, recipe)
    }

    companion object {

        private val ACTION_FAVORITE = "com.zest.android.ACTION_FAVORITE"

        /**
         * To start activity with favorite mode
         *
         * @param context
         * @return
         */
        fun startWithFavorite(context: Context) {
            val starter = Intent(context, ListActivity::class.java).apply {
                this.action = ACTION_FAVORITE
                this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(starter)
        }
    }

}
