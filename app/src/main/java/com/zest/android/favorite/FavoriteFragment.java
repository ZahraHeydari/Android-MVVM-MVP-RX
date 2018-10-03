package com.zest.android.favorite;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.FavoriteRepository;
import com.zest.android.databinding.FragmentFavoriteBinding;
import com.zest.android.home.RecipeViewModel;
import com.zest.android.list.OnListCallback;

/**
 * Display a grid of Favorite {@link Recipe}s. User can choose to view each favorite recipe.
 *
 * Created by ZARA on 08/10/2018.
 */
public class FavoriteFragment extends Fragment implements OnFavoriteFragmentInteractionListener {


    public static final String FRAGMENT_NAME = FavoriteFragment.class.getName();
    private static final String TAG = FavoriteFragment.class.getSimpleName();
    private static final int SPAN_COUNT = 2;
    private OnListCallback mCallback;
    private FragmentFavoriteBinding fragmentFavoriteBinding;
    private FavoriteAdapter mAdapter;
    private RecipeViewModel recipeViewModel;

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListCallback) {
            mCallback = (OnListCallback) context;
        } else {
            throw new ClassCastException(context.toString() + "must implement OnListCallback!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback.updateActionBarTitle(R.string.favorite);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentFavoriteBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_favorite, container, false);
        recipeViewModel = new RecipeViewModel(new FavoriteRepository(getContext()), this);
        fragmentFavoriteBinding.setRecipeViewModel(recipeViewModel);
        fragmentFavoriteBinding.executePendingBindings();
        ((TextView) fragmentFavoriteBinding.favoriteEmptyView.findViewById(R.id.empty_text_view))
                .setText(R.string.you_have_not_any_favorite);
        fragmentFavoriteBinding.favoriteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mAdapter = new FavoriteAdapter(this, recipeViewModel.loadFavorites());
        fragmentFavoriteBinding.favoriteRecyclerView.setAdapter(mAdapter);

        return fragmentFavoriteBinding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        mAdapter = null;
    }

    @Override
    public void showDeleteFavoriteDialog(Recipe recipe) {
        if (getContext() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.are_you_sure_want_to_delete_this_item_from_favorite_list))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new OnPositiveButtonClickListener(recipe))
                .setNegativeButton(getString(R.string.no), new OnNegativeClickListener());
        builder.create();
        builder.show();
    }

    /**
     * To cancel delete favorite recipe
     */
    private class OnNegativeClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();// User cancelled the dialog
        }
    }

    /**
     * To Submit the delete favorite recipe
     */
    private class OnPositiveButtonClickListener implements DialogInterface.OnClickListener {
        private final Recipe recipe;

        OnPositiveButtonClickListener(Recipe recipe) {
            this.recipe = recipe;
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();//Cancel the dialog
            recipeViewModel.deleteFavoriteRecipe(recipe);
            mAdapter.addData(recipeViewModel.loadFavorites());
            Snackbar.make(fragmentFavoriteBinding.getRoot(), getString(R.string.deleted_this_recipe_from_your_favorite_list)
                    , Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    public void gotoDetailPage(Recipe recipe) {
        mCallback.gotoDetailPage(recipe);
    }

}
