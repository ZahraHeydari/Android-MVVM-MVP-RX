package com.zest.android.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.adroitandroid.chipcloud.ChipCloud
import com.adroitandroid.chipcloud.ChipListener
import com.adroitandroid.chipcloud.FlowLayout
import com.squareup.picasso.Picasso
import com.zest.android.LifecycleLoggingActivity
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.DetailRepository
import com.zest.android.search.SearchActivity
import com.zest.android.util.FontCache
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.detail_instructions_layout.*
import kotlinx.android.synthetic.main.detail_tag_layout.*
import org.jetbrains.annotations.NotNull
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @Author ZARA.
 */
class DetailActivity : LifecycleLoggingActivity(), DetailContract.View {

    private var mRecipe: Recipe? = null
    private var mPresenter: DetailContract.UserActionsListener? = null
    private var mIsFavorite: Boolean = false


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initCollapsingToolbarLayout()

        setSupportActionBar(detail_toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        detail_toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))

        DetailPresenter(this, DetailRepository())

        if (intent != null && intent.extras != null && intent.extras.containsKey(Recipe::class.java.name)) {
            mRecipe = intent.extras.getParcelable(Recipe::class.java.name) as Recipe
            loadInitialValues(mRecipe!!)
        }
    }

    /**
     * To set title typeface & title text color & ....
     */
    private fun initCollapsingToolbarLayout() {
        detail_collapsing_toolbar_layout.setExpandedTitleTypeface(
                FontCache.getTypeface(this, "AtlantaBook.ttf"))
        detail_collapsing_toolbar_layout.setCollapsedTitleTypeface(
                FontCache.getTypeface(this, "AtlantaBook.ttf"))
        detail_collapsing_toolbar_layout.setCollapsedTitleTextColor(
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


    override fun onStart() {
        super.onStart()
        mPresenter?.start()
        detail_fab.setOnClickListener(OnFABClickListener())
    }


    /**
     * To load initial values of [Recipe]
     * like title, image, tags, instructions,....
     *
     * @param recipe
     */
    private fun loadInitialValues(recipe: Recipe) {

        try {
            Picasso.with(this)
                    .load(recipe.image)
                    .into(detail_toolbar_image_view)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        detail_collapsing_toolbar_layout.setTitle(recipe.title)
        detail_instructions_text_view.setText(recipe.instructions)

        checkRecipeIsFavorite(recipe)

        val tags = mPresenter?.loadTags(recipe)
        if (tags != null && tags.size != 0) {
            detail_tag_container.visible()
            ChipCloud.Configure()
                    .chipCloud(detail_tag_chip_cloud)
                    .labels(tags)
                    .mode(ChipCloud.Mode.SINGLE)
                    .allCaps(false)
                    .gravity(FlowLayout.Gravity.CENTER)
                    .chipListener(OnChipListener(tags))
                    .build()
        }
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }


    /**
     * To check [Recipe] is favorite or not?!
     *
     * @param recipe
     */
    private fun checkRecipeIsFavorite(recipe: Recipe) {
        if (mPresenter?.loadFavorite(recipe) != null) {
            mIsFavorite = true
            detail_fab.setImageResource(R.drawable.ic_star_full_vector)
        } else {
            mIsFavorite = false
            detail_fab.setImageResource(R.drawable.ic_star_empty_white_vector)
        }
    }


    override fun setPresenter(@NotNull presenter: DetailContract.UserActionsListener) {
        mPresenter = checkNotNull(presenter)
    }

    override fun navigateToSearchWithTag(tag: String) {
        SearchActivity.startWithText(this@DetailActivity, tag)
    }


    private inner class OnFABClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            if (mRecipe == null) return
            val message: String
            if (mIsFavorite) {
                mIsFavorite = false
                detail_fab.setImageResource(R.drawable.ic_star_empty_white_vector)
                message = getString(R.string.deleted_this_recipe_from_your_favorite_list)
                mPresenter?.removeFromFavorite(mRecipe!!)
            } else {
                mIsFavorite = true
                detail_fab.setImageResource(R.drawable.ic_star_full_vector)
                message = getString(R.string.added_this_recipe_to_your_favorite_list)
                mPresenter?.insertToFavorite(mRecipe!!)
            }
            Snackbar.make(detail_collapsing_toolbar_layout, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private inner class OnChipListener(private val tags: Array<String>) : ChipListener {

        override fun chipSelected(i: Int) {
            mPresenter?.searchByTag(tags[i])
        }

        override fun chipDeselected(i: Int) {
            Log.d(TAG, "chipDeselected() called with: i = [$i]")
        }
    }

    companion object {

        private val TAG = DetailActivity::class.java.simpleName

        fun start(context: Context, recipe: Recipe) {
            val starter = Intent(context, DetailActivity::class.java)
            val bundle = Bundle()
            starter.putExtra(Recipe::class.java.name, recipe)
            starter.putExtras(bundle)
            context.startActivity(starter)
        }

    }
}