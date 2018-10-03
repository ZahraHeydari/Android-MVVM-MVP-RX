package com.zest.android.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.util.NetworkStateReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Recipe}s. User can choose to view each recipe.
 *
 * Created by ZARA on 08/10/2018.
 */
public class HomeFragment extends Fragment implements HomeContract.View,
        NetworkStateReceiver.OnNetworkStateReceiverListener {


    public static final String FRAGMENT_NAME = HomeFragment.class.getName();
    private static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.home_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.home_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.home_empty_view)
    View mEmptyView;
    @BindView(R.id.empty_text_view)
    TextView mEmptyTextView;
    private HomeContract.UserActionsListener mPresenter;
    private View root;
    private List<Recipe> mRecipes = new ArrayList<>();
    private RecipesAdapter mAdapter;
    private HomeCallback mCallback;
    private NetworkStateReceiver mNetworkReceiver;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeCallback) {
            mCallback = (HomeCallback) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement HomeCallback!");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RecipesAdapter(this, mRecipes);

        mNetworkReceiver = new NetworkStateReceiver();
        mNetworkReceiver.addListener(this);
        if (getContext() != null) {
            getContext().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


    public void scrollUp() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        mCallback.showFab();
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called");
        super.onStart();
        mPresenter.start();
        //mPresenter.getRecipes();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        root = null;
        mCallback = null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(HomeContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMessage(int message) {
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void gotoDetailPage(Recipe recipe) {
        mCallback.gotoDetailPage(recipe);
    }

    @Override
    public void loadRecipes(List<Recipe> recipes) {
        Log.d(TAG, "loadRecipes() called with: mRecipes = [" + recipes + "]");
        mRecipes.clear();
        mRecipes.addAll(recipes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Recipe loadFavorite(Recipe recipe) {
        return mPresenter.loadFavoriteByRecipeId(recipe);
    }

    @Override
    public void removeFavorite(Recipe recipe) {
        Log.d(TAG, "removeFavorite() called with: recipe = [" + recipe + "]");
        mPresenter.deleteFavoriteByRecipeId(recipe);
    }

    @Override
    public void insertFavorite(Recipe recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [" + recipe + "]");
        mPresenter.insertFavoriteRecipe(recipe);
    }

    @Override
    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void networkAvailable() {
        Log.d(TAG, "networkAvailable() called");
        mPresenter.getRecipes();
    }

    @Override
    public void networkUnavailable() {
        Log.d(TAG, "networkUnavailable() called");
        if (mRecipes == null || mRecipes.isEmpty()) {
            showEmptyView(true);
        }
        showProgressBar(false);
        mEmptyTextView.setText(R.string.check_internet_and_try_again_please);
    }

    @Override
    public void showEmptyView(boolean visibility) {
        mEmptyView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNetworkReceiver.removeListener(this);
        unregisterNetworkChanges();
    }

    protected void unregisterNetworkChanges() {
        Log.d(TAG, "unregisterNetworkChanges() called");
        try {
            if (getContext() != null) {
                getContext().unregisterReceiver(mNetworkReceiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
