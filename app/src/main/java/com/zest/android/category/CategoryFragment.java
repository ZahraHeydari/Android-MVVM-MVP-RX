package com.zest.android.category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zest.android.R;
import com.zest.android.data.Category;
import com.zest.android.data.source.CategoryRepository;
import com.zest.android.home.HomeCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Category}s. User can choose to view the subs of each category.
 *
 * Created by ZARA on 08/10/2018.
 */
public class CategoryFragment extends Fragment implements CategoryContract.View {

    public static final String FRAGMENT_NAME = CategoryFragment.class.getName();
    private static final String TAG = CategoryFragment.class.getSimpleName();
    @BindView(R.id.category_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.category_progress_bar)
    ProgressBar mProgressBar;
    private View root;
    private CategoryContract.UserActionsListener mPresenter;
    private CategoryAdapter mAdapter;
    private HomeCallback mCallback;
    private List<Category> categories = new ArrayList<>();


    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeCallback) {
            mCallback = (HomeCallback) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement HomeCallback!");
        }
        mCallback.hideFab();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CategoryPresenter(this, new CategoryRepository(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, root);

        mAdapter = new CategoryAdapter(this, categories);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.loadCategories();

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        root = null;
        mAdapter = null;
        mCallback = null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(CategoryContract.UserActionsListener presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showSubCategories(Category category) {
        mCallback.showSubCategoriesByCategoryTitle(category);
    }

    @Override
    public void setResult(List<Category> categories) {
        this.categories.addAll(categories);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
