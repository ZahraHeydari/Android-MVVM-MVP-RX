package com.zest.android.detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.zest.android.LifecycleLoggingActivity;
import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.DetailRepository;
import com.zest.android.databinding.ActivityDetailBinding;
import com.zest.android.home.RecipeViewModel;
import com.zest.android.search.SearchActivity;
import com.zest.android.util.FontCache;

import org.parceler.Parcels;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @Author ZARA.
 */
public class DetailActivity extends LifecycleLoggingActivity implements OnDetailCallback {


    private static final String TAG = DetailActivity.class.getSimpleName();
    private Recipe mRecipe;
    private ActivityDetailBinding activityDetailBinding;
    private RecipeViewModel recipeViewModel;


    public static void start(Context context, Recipe recipe) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(Recipe.class.getName(), Parcels.wrap(recipe));
        context.startActivity(starter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mRecipe = Parcels.unwrap(getIntent().getExtras().getParcelable(Recipe.class.getName()));
        }
        recipeViewModel = new RecipeViewModel(mRecipe, new DetailRepository(this), this);
        activityDetailBinding.setRecipeViewModel(recipeViewModel);
        activityDetailBinding.executePendingBindings();
        initCollapsingToolbarLayout();
        setSupportActionBar(activityDetailBinding.detailToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        activityDetailBinding.detailToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        final String[] tags = recipeViewModel.loadTags(mRecipe);
        if (tags != null && tags.length != 0) {
            new ChipCloud.Configure()
                    .chipCloud(activityDetailBinding.detailTagChipCloud)
                    .labels(tags)
                    .mode(ChipCloud.Mode.SINGLE)
                    .allCaps(false)
                    .gravity(ChipCloud.Gravity.CENTER)
                    .chipListener(new OnChipListener(tags))
                    .build();
        }
    }

    /**
     * To set title typeface & title text color & ....
     */
    private void initCollapsingToolbarLayout() {
        activityDetailBinding.detailCollapsingToolbarLayout.setExpandedTitleTypeface(
                FontCache.getTypeface(this, "AtlantaBook.ttf"));
        activityDetailBinding.detailCollapsingToolbarLayout.setCollapsedTitleTypeface(
                FontCache.getTypeface(this, "AtlantaBook.ttf"));
        activityDetailBinding.detailCollapsingToolbarLayout.setCollapsedTitleTextColor(
                ContextCompat.getColor(this, R.color.colorAccent));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFavoriteIcon(int drawableRes) {
        activityDetailBinding.detailFab.setImageResource(drawableRes);
    }

    @Override
    public void showMessage(int stringRes) {
        Snackbar.make(activityDetailBinding.detailCollapsingToolbarLayout
                , getString(stringRes), Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private class OnChipListener implements ChipListener {

        private final String[] tags;

        public OnChipListener(String[] tags) {
            this.tags = tags;
        }

        @Override
        public void chipSelected(int i) {
            Log.d(TAG, "chipSelected() called with: i = [" + i + "]+ Tag: +" + tags[i] + ")");
            SearchActivity.startWithText(DetailActivity.this, tags[i]);
        }

        @Override
        public void chipDeselected(int i) {
            Log.d(TAG, "chipDeselected() called with: i = [" + i + "]");
        }
    }
}
