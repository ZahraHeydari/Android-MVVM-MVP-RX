package com.zest.android.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.squareup.picasso.Picasso;
import com.zest.android.LifecycleLoggingActivity;
import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.DetailRepository;
import com.zest.android.search.SearchActivity;
import com.zest.android.util.FontCache;

import org.greenrobot.greendao.annotation.NotNull;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * @Author ZARA.
 */
public class DetailActivity extends LifecycleLoggingActivity implements DetailContract.View {


    private static final String TAG = DetailActivity.class.getSimpleName();
    @BindView(R.id.detail_toolbar_image_view)
    ImageView mToolbarImageView;
    @BindView(R.id.detail_collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_tag_chip_cloud)
    ChipCloud mTagChipCloud;
    @BindView(R.id.detail_fab)
    FloatingActionButton mFab;
    @BindView(R.id.detail_instructions_text_view)
    TextView mInstructionsTextView;
    @BindView(R.id.detail_tag_container)
    View mTagsContainer;
    private Recipe mRecipe;
    private DetailContract.UserActionsListener mPresenter;
    private boolean mIsFavorite;


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
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initCollapsingToolbarLayout();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        new DetailPresenter(this, new DetailRepository(this));

        if (getIntent() != null && getIntent().getExtras() != null) {
            mRecipe = Parcels.unwrap(getIntent().getExtras().getParcelable(Recipe.class.getName()));
            loadInitialValues(mRecipe);
        }

    }

    /**
     * To set title typeface & title text color & ....
     */
    private void initCollapsingToolbarLayout() {
        mCollapsingToolbarLayout.setExpandedTitleTypeface(FontCache.getTypeface(this, "AtlantaBook.ttf"));
        mCollapsingToolbarLayout.setCollapsedTitleTypeface(FontCache.getTypeface(this, "AtlantaBook.ttf"));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));
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
    protected void onStart() {
        super.onStart();
        mPresenter.start();
        mFab.setOnClickListener(new OnFABClickListener());
    }


    /**
     * To load initial values of {@link Recipe}
     * like title, image, tags, instructions,....
     *
     * @param recipe
     */
    private void loadInitialValues(final Recipe recipe) {

        try {
            Picasso.with(this)
                    .load(recipe.getImage())
                    .into(mToolbarImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCollapsingToolbarLayout.setTitle(recipe.getTitle());
        mInstructionsTextView.setText(recipe.getInstructions());
        checkRecipeIsFavorite(recipe);

        final String[] tags = mPresenter.loadTags(recipe);
        if (tags != null && tags.length != 0) {
            mTagsContainer.setVisibility(View.VISIBLE);
            new ChipCloud.Configure()
                    .chipCloud(mTagChipCloud)
                    .labels(tags)
                    .mode(ChipCloud.Mode.SINGLE)
                    .allCaps(false)
                    .gravity(ChipCloud.Gravity.CENTER)
                    .chipListener(new OnChipListener(tags))
                    .build();
        }

    }


    /**
     * To check {@link Recipe} is favorite or not?!
     *
     * @param recipe
     */
    private void checkRecipeIsFavorite(Recipe recipe) {
        if (mPresenter.loadFavorite(recipe) != null) {
            mIsFavorite = true;
            mFab.setImageResource(R.drawable.ic_star_full_vector);
        } else {
            mIsFavorite = false;
            mFab.setImageResource(R.drawable.ic_star_empty_white_vector);
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(@NotNull DetailContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void navigateToSearchWithTag(String tag) {
        SearchActivity.startWithText(DetailActivity.this, tag);
    }


    private class OnFABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mRecipe == null) return;
            String message;
            if (mIsFavorite) {
                mIsFavorite = false;
                mFab.setImageResource(R.drawable.ic_star_empty_white_vector);
                message = getString(R.string.deleted_this_recipe_from_your_favorite_list);
                mPresenter.removeFromFavorite(mRecipe);
            } else {
                mIsFavorite = true;
                mFab.setImageResource(R.drawable.ic_star_full_vector);
                message = getString(R.string.added_this_recipe_to_your_favorite_list);
                mPresenter.insertToFavorite(mRecipe);
            }
            Snackbar.make(mCollapsingToolbarLayout
                    , message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private class OnChipListener implements ChipListener {

        private final String[] tags;

        public OnChipListener(String[] tags) {
            this.tags = tags;
        }

        @Override
        public void chipSelected(int i) {
            Log.d(TAG, "chipSelected() called with: i = [" + i + "]+ Tag: +" + tags[i] + ")");
            mPresenter.searchByTag(tags[i]);
        }

        @Override
        public void chipDeselected(int i) {
            Log.d(TAG, "chipDeselected() called with: i = [" + i + "]");
        }
    }
}
