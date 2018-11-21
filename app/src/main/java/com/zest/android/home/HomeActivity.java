package com.zest.android.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zest.android.LifecycleLoggingActivity;
import com.zest.android.R;
import com.zest.android.category.CategoryFragment;
import com.zest.android.data.Category;
import com.zest.android.data.Recipe;
import com.zest.android.databinding.ActivityHomeBinding;
import com.zest.android.detail.DetailActivity;
import com.zest.android.list.ListActivity;
import com.zest.android.search.SearchActivity;
import com.zest.android.util.ActivityUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * @Author ZARA.
 */
public class HomeActivity extends LifecycleLoggingActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnHomeCallback {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private ActivityHomeBinding activityHomeBinding;


    public static void start(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        setSupportActionBar(activityHomeBinding.homeToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityHomeBinding.drawerLayout, activityHomeBinding.homeToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityHomeBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Fragment homeFragment = getSupportFragmentManager().findFragmentById(R.id.home_container);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    homeFragment, R.id.home_container);
        }
        activityHomeBinding.homeToolbarTitleImageView.setOnClickListener(new OnToolbarImageClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityHomeBinding.homeFab.setOnClickListener(new OnFABClickListener());
        activityHomeBinding.navView.setNavigationItemSelectedListener(this);
        activityHomeBinding.navView.setCheckedItem(R.id.nav_recipes);
    }

    @Override
    public void onBackPressed() {
        if (activityHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            SearchActivity.start(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_recipes) {
            // Handle the favorite action
        } else if (id == R.id.nav_favorite) {
            ListActivity.startWithFavorite(this);
        }
        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showFab(boolean visibility) {
        if (visibility) activityHomeBinding.homeFab.show();
        else activityHomeBinding.homeFab.hide();
    }

    @Override
    public void gotoDetailPage(Recipe recipe) {
        DetailActivity.start(this, recipe);
    }

    @Override
    public void showSubCategoriesByCategoryTitle(Category category) {
        SearchActivity.startWithText(HomeActivity.this, category.getTitle());
    }

    private class OnFABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ActivityUtils.replaceFragmentInActivity(
                    getSupportFragmentManager(),
                    CategoryFragment.newInstance(),
                    CategoryFragment.FRAGMENT_NAME,
                    R.id.home_container);
        }
    }

    private class OnToolbarImageClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.home_container);
            if (fragmentById != null && fragmentById instanceof HomeFragment) {
                ((HomeFragment) fragmentById).scrollUp();
            }else if(fragmentById instanceof CategoryFragment){
                ((CategoryFragment) fragmentById).scrollUp();
            }
        }
    }
}