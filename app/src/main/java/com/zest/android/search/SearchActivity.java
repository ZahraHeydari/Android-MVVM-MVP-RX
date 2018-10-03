package com.zest.android.search;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zest.android.LifecycleLoggingActivity;
import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.SearchRepository;
import com.zest.android.detail.DetailActivity;
import com.zest.android.util.NetworkStateReceiver;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.support.v4.util.Preconditions.checkNotNull;


/**
 * @Author ZARA.
 */
public class SearchActivity extends LifecycleLoggingActivity implements SearchContract.View{


    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String Action_SEARCH_TAG = "com.zest.android.ACTION_SEARCH_TAG";
    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_empty_view)
    View mEmptyView;
    @BindView(R.id.empty_text_view)
    TextView mEmptyTextView;
    @BindView(R.id.search_progress_bar)
    ProgressBar mProgressBar;
    private SearchContract.UserActionsListener mPresenter;
    private String mQuery;
    private SearchView mSearchView;
    private List<Recipe> mRecipes = new ArrayList<>();
    private SearchAdapter mAdapter;
    private MenuItem mMenuSearchItem;
    private String text;

    /**
     * get new Intent to Start Activity
     *
     * @param context
     * @return
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    /**
     * get new Intent to start activity with text
     *
     * @param context
     * @param text
     * @return
     */
    public static void startWithText(Context context, String text) {
        Intent starter = new Intent(context, SearchActivity.class);
        starter.putExtra(String.class.getName(), Parcels.wrap(text));
        starter.setAction(Action_SEARCH_TAG);
        context.startActivity(starter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mEmptyTextView.setText(R.string.no_result);

        new SearchPresenter(this, new SearchRepository(this));

        mAdapter = new SearchAdapter(this, mRecipes);
        mRecyclerView.setAdapter(mAdapter);

        if (getIntent() != null && Action_SEARCH_TAG.equals(getIntent().getAction())) {
            if (getIntent().getExtras() != null) {
                text = Parcels.unwrap(getIntent().getExtras().getParcelable(String.class.getName()));
                mPresenter.searchQuery(text);
            }
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        mMenuSearchItem = menu.findItem(R.id.search);
        mMenuSearchItem.expandActionView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSearchView.setForegroundGravity(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
        }
        mSearchView.setHorizontalGravity(Gravity.END);
        mSearchView.setQueryHint(getString(R.string.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        mSearchView.setOnQueryTextListener(new OnSearchQueryTextListener());
        mSearchView.setOnCloseListener(new OnSearchCloseListener());

        if (getIntent() != null && Action_SEARCH_TAG.equals(getIntent().getAction())) {
            //when received with text
            mSearchView.setQuery(text, false);
        }
        mSearchView.setIconified(false);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mSearchView != null) {
            finish();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(SearchContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void gotoDetailPage(Recipe recipe) {
        DetailActivity.start(this, recipe);
    }

    @Override
    public void setResult(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        mAdapter.notifyDataSetChanged();
        showEmptyView();
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(mRecipes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }


    private class OnSearchCloseListener implements SearchView.OnCloseListener {
        @Override
        public boolean onClose() {
            finish();
            return false;
        }
    }


    private class OnSearchQueryTextListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mQuery = query;
            mPresenter.searchQuery(mQuery);
            MenuItemCompat.expandActionView(mMenuSearchItem);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    }

}
