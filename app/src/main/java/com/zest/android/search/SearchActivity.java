package com.zest.android.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zest.android.LifecycleLoggingActivity;
import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.SearchRepository;
import com.zest.android.databinding.ActivitySearchBinding;
import com.zest.android.detail.DetailActivity;
import com.zest.android.home.RecipeViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * @Author ZARA.
 */
public class SearchActivity extends LifecycleLoggingActivity implements OnSearchCallback {


    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String Action_SEARCH_TAG = "com.zest.android.ACTION_SEARCH_TAG";
    private String mQuery;
    private SearchView mSearchView;
    private SearchAdapter mAdapter;
    private MenuItem mMenuSearchItem;
    private String searchQuery;
    private ActivitySearchBinding activitySearchBinding;
    private RecipeViewModel recipeViewModel;

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
     * get new Intent to start activity with searchQuery
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
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        recipeViewModel = new RecipeViewModel(new SearchRepository(this), this);
        activitySearchBinding.setRecipeViewModel(recipeViewModel);
        activitySearchBinding.executePendingBindings();

        setSupportActionBar(activitySearchBinding.searchToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ((TextView) activitySearchBinding.searchEmptyView.findViewById(R.id.empty_text_view))
                .setText(getString(R.string.no_result));

        mAdapter = new SearchAdapter(this);
        activitySearchBinding.searchRecyclerView.setAdapter(mAdapter);
        if (getIntent() != null && Action_SEARCH_TAG.equals(getIntent().getAction())) {
            if (getIntent().getExtras() != null) {
                searchQuery = Parcels.unwrap(getIntent().getExtras().getParcelable(String.class.getName()));
                recipeViewModel.searchQuery(searchQuery);
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
            //when received with searchQuery
            mSearchView.setQuery(searchQuery, false);
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
    public void gotoDetailPage(Recipe recipe) {
        DetailActivity.start(this, recipe);
    }

    @Override
    public void showEmptyView(boolean visibility) {
        activitySearchBinding.searchEmptyView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void noResult() {
        mAdapter.removePreviousData();
    }

    @Override
    public void setResult(List<Recipe> recipes) {
        mAdapter.addData(recipes);
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
            recipeViewModel.searchQuery(mQuery);
            MenuItemCompat.expandActionView(mMenuSearchItem);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    }

}
