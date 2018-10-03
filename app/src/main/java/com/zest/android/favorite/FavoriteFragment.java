package com.zest.android.favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.FavoriteRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Display a grid of Favorite {@link Recipe}s. User can choose to view each favorite recipe.
 *
 * Created by ZARA on 08/10/2018.
 */
public class FavoriteFragment extends Fragment implements FavoriteContract.View {


    public static final String FRAGMENT_NAME = FavoriteFragment.class.getName();
    private static final String TAG = FavoriteFragment.class.getSimpleName();
    private static final int SPAN_COUNT = 2;
    @BindView(R.id.favorite_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.favorite_empty_view)
    View mEmptyView;
    @BindView(R.id.empty_text_view)
    TextView mEmptyTextView;
    private View root;
    private OnFavoriteFragmentInteractionListener mCallback;
    private FavoriteContract.UserActionsListener mPresenter;
    private FavoriteAdapter mAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteFragmentInteractionListener) {
            mCallback = (OnFavoriteFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement OnFavoriteFragmentInteractionListener!");
        }

        new FavoritePresenter(this, new FavoriteRepository(getContext()));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback.updateToolbarTitle(R.string.favorite);
        mRecipes = mPresenter.loadFavorites();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkEmptyView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, root);

        mEmptyTextView.setText(R.string.you_have_not_any_favorite);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mAdapter = new FavoriteAdapter(this, mRecipes);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        root = null;
        mCallback = null;
        mAdapter = null;
    }

    @Override
    public void setPresenter(FavoriteContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * To check the display of empty view
     */
    public void checkEmptyView() {
        final List<Recipe> recipes = mPresenter.loadFavorites();
        mEmptyView.setVisibility(recipes == null || recipes.isEmpty() ? View.VISIBLE : View.GONE);
    }


    @Override
    public void deleteFavorite(Recipe recipe) {
        mPresenter.deleteFavoriteRecipe(recipe);
        mRecipes.clear();
        final List<Recipe> recipes = mPresenter.loadFavorites();
        if (recipes != null && !recipes.isEmpty()) {
            this.mRecipes.addAll(recipes);
        }
        mAdapter.notifyDataSetChanged();

        Snackbar.make(root, getString(R.string.deleted_this_recipe_from_your_favorite_list)
                , Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


    @Override
    public void gotoDetailPage(Recipe recipe) {
        mCallback.gotoDetailPage(recipe);
    }

}
