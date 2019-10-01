package com.zest.android.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import com.adroitandroid.chipcloud.ChipCloud
import com.adroitandroid.chipcloud.ChipListener
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.RecipeRepository
import com.zest.android.databinding.ActivityDetailBinding
import com.zest.android.home.RecipeViewModel
import com.zest.android.search.SearchActivity
import com.zest.android.util.FontCache
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @Author ZARA.
 */
class DetailActivity : LifecycleLoggingActivity(), OnDetailCallback {

    private val TAG = DetailActivity::class.java.name
    private var mRecipe: Recipe? = null
    private lateinit var activityDetailBinding: ActivityDetailBinding
    lateinit var recipeViewModel: RecipeViewModel

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        if (intent?.extras?.containsKey(Recipe::class.java.name) == true) {
            mRecipe = intent?.extras?.getParcelable(Recipe::class.java.name)
        }

        recipeViewModel = RecipeViewModel(mRecipe, RecipeRepository(), this)
        activityDetailBinding.recipeViewModel = recipeViewModel
        activityDetailBinding.executePendingBindings()

        initCollapsingToolbarLayout()
        setSupportActionBar(activityDetailBinding.detailToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityDetailBinding.detailToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))

        mRecipe?.let { nonNullRecipe ->
            val tags = recipeViewModel.loadTags(nonNullRecipe)
            if (tags != null && tags.isNotEmpty()) {
                ChipCloud.Configure()
                        .chipCloud(activityDetailBinding.detailTagChipCloud)
                        .labels(tags)
                        .mode(ChipCloud.Mode.SINGLE)
                        .allCaps(false)
                        // .gravity(ChipCloud.Gravity.CENTER)
                        .chipListener(OnChipListener(tags))
                        .build()
            }
        }

    }

    /**
     * To set title typeface & title text color & ....
     */
    private fun initCollapsingToolbarLayout() {
        activityDetailBinding.detailCollapsingToolbarLayout.setExpandedTitleTypeface(
                FontCache.getTypeface(this, "AtlantaBook.ttf"))
        activityDetailBinding.detailCollapsingToolbarLayout.setCollapsedTitleTypeface(
                FontCache.getTypeface(this, "AtlantaBook.ttf"))
        activityDetailBinding.detailCollapsingToolbarLayout.setCollapsedTitleTextColor(
                ContextCompat.getColor(this, R.color.colorAccent))
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

    override fun showMessage(stringRes: Int) {
        Snackbar.make(activityDetailBinding.detailCollapsingToolbarLayout, getString(stringRes),
                Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    private inner class OnChipListener(private val tags: Array<String>) : ChipListener {

        override fun chipSelected(i: Int) {
            Log.d(TAG, "chipSelected() called with: i = [" + i + "]+ Tag: +" + tags[i] + ")")
            SearchActivity.startWithText(this@DetailActivity, tags[i])
        }

        override fun chipDeselected(i: Int) {
            Log.d(TAG, "chipDeselected() called with: i = [$i]")
        }
    }

    companion object {

        fun start(context: Context, recipe: Recipe) {
            val starter = Intent(context, DetailActivity::class.java).apply {
                this.putExtra(Recipe::class.java.name, recipe)
            }
            context.startActivity(starter)
        }
    }
}
