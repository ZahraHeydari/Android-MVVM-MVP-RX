package com.zest.android.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zest.android.LifecycleLoggingActivity;
import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.detail.DetailActivity;
import com.zest.android.favorite.FavoriteFragment;
import com.zest.android.favorite.OnFavoriteFragmentInteractionListener;
import com.zest.android.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * @Author ZARA.
 */
public class ListActivity extends LifecycleLoggingActivity implements OnFavoriteFragmentInteractionListener {

    private static final String TAG = ListActivity.class.getSimpleName();
    private static final String ACTION_FAVORITE = "com.zest.android.ACTION_FAVORITE";
    @BindView(R.id.list_toolbar)
    Toolbar mToolbar;


    /**
     * To startWithFavorite activity for favorite
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
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent));


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
    public void updateToolbarTitle(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resId);
        }
    }

    @Override
    public void gotoDetailPage(Recipe recipe) {
        DetailActivity.start(this, recipe);
    }
}
