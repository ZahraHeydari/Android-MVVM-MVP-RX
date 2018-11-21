package com.zest.android.list;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

import com.zest.android.LifecycleLoggingActivity;
import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.databinding.ActivityListBinding;
import com.zest.android.detail.DetailActivity;
import com.zest.android.favorite.FavoriteFragment;
import com.zest.android.util.ActivityUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * @Author ZARA.
 */
public class ListActivity extends LifecycleLoggingActivity implements OnListCallback {


    private static final String TAG = ListActivity.class.getSimpleName();
    private static final String ACTION_FAVORITE = "com.zest.android.ACTION_FAVORITE";
    private ActivityListBinding activityListBinding;

    /**
     * To start activity with favorite mode
     *
     * @param context
     * @return
     */
    public static void startWithFavorite(Context context) {
        Intent starter = new Intent(context, ListActivity.class);
        starter.setAction(ACTION_FAVORITE);
        starter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(starter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        setSupportActionBar(activityListBinding.listToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        activityListBinding.listToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        if (getIntent() != null && getIntent().getAction() != null) {
            switch (getIntent().getAction()) {
                case ACTION_FAVORITE:
                    ActivityUtils.addFragmentToActivity(
                            getSupportFragmentManager(),
                            FavoriteFragment.newInstance(),
                            R.id.list_container);
                    break;
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
    public void updateActionBarTitle(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resId);
        }
    }

    @Override
    public void gotoDetailPage(Recipe recipe) {
        DetailActivity.start(this, recipe);
    }

}
