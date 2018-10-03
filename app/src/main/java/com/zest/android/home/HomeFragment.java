package com.zest.android.home;

import android.content.Context;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.HomeRepository;
import com.zest.android.databinding.FragmentHomeBinding;
import com.zest.android.util.NetworkStateReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Display a grid of {@link Recipe}s. User can choose to view each recipe.
 *
 * Created by ZARA on 08/10/2018.
 */
public class HomeFragment extends Fragment implements
        NetworkStateReceiver.OnNetworkStateReceiverListener, OnHomeFragmentInteractionListener {

    public static final String FRAGMENT_NAME = HomeFragment.class.getName();
    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<Recipe> mRecipes = new ArrayList<>();
    private RecipesAdapter mAdapter;
    private OnHomeCallback mCallback;
    private NetworkStateReceiver mNetworkReceiver;
    private FragmentHomeBinding fragmentHomeBinding;
    private RecipeViewModel recipeViewModel;
    private HomeRepository homeRepository;
    private TextView mEmptyTextView;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeCallback) {
            mCallback = (OnHomeCallback) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement OnHomeCallback!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeRepository = new HomeRepository(getContext());
        mAdapter = new RecipesAdapter(this);
        mNetworkReceiver = new NetworkStateReceiver();
        mNetworkReceiver.addListener(this);
        if (getContext() != null) {
            getContext().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void scrollUp() {
        fragmentHomeBinding.homeRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                fragmentHomeBinding.homeRecyclerView.smoothScrollToPosition(0);// Call smooth scroll
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        recipeViewModel = new RecipeViewModel(homeRepository, this);
        fragmentHomeBinding.setRecipeViewModel(recipeViewModel);
        fragmentHomeBinding.executePendingBindings();

        mEmptyTextView = fragmentHomeBinding.homeEmptyView.findViewById(R.id.empty_text_view);
        mEmptyTextView.setText(getString(R.string.there_is_no_recipe));
        mCallback.showFab(true);
        fragmentHomeBinding.homeRecyclerView.setAdapter(mAdapter);
        recipeViewModel.getRecipes();

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        mAdapter = null;
    }

    public void showMessage(int message) {
        Snackbar.make(fragmentHomeBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void gotoDetailPage(Recipe recipe) {
        mCallback.gotoDetailPage(recipe);
    }

    @Override
    public void loadRecipes(List<Recipe> recipes) {
        Log.d(TAG, "loadRecipes() called with: mRecipes = [" + recipes + "]");
        mAdapter.addData(recipes);
    }

    @Override
    public Recipe loadFavorite(Recipe recipe) {
        return recipeViewModel.loadFavoriteByRecipeId(recipe);
    }

    @Override
    public void removeFavorite(Recipe recipe) {
        Log.d(TAG, "removeFavorite() called with: recipe = [" + recipe + "]");
        recipeViewModel.deleteFavoriteByRecipeId(recipe);
    }

    @Override
    public void insertFavorite(Recipe recipe) {
        Log.d(TAG, "insertFavorite() called with: recipe = [" + recipe + "]");
        recipeViewModel.insertFavoriteRecipe(recipe);
    }

    @Override
    public void networkAvailable() {
        Log.d(TAG, "networkAvailable() called");
        recipeViewModel.getRecipes();
    }

    @Override
    public void networkUnavailable() {
        Log.d(TAG, "networkUnavailable() called");
        if (mRecipes == null || mRecipes.isEmpty()) {
            showEmptyView(true);
        }
        mEmptyTextView.setText(R.string.check_internet_and_try_again_please);
    }

    @Override
    public void showEmptyView(boolean visibility) {
        fragmentHomeBinding.homeEmptyView.setVisibility(visibility ? View.VISIBLE : View.GONE);
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
