package com.zest.android.category;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zest.android.R;
import com.zest.android.data.Category;
import com.zest.android.data.source.CategoryRepository;
import com.zest.android.databinding.FragmentCategoryBinding;
import com.zest.android.home.OnHomeCallback;

import java.util.List;

/**
 * Display a grid of {@link Category}s. User can choose to view the subs of each category.
 *
 * Created by ZARA on 09/30/2018.
 */
public class CategoryFragment extends Fragment implements OnCategoryFragmentInteractionListener {

    public static final String FRAGMENT_NAME = CategoryFragment.class.getName();
    private static final String TAG = CategoryFragment.class.getSimpleName();
    private CategoryAdapter mAdapter;
    private OnHomeCallback mCallback;
    private FragmentCategoryBinding fragmentCategoryBinding;
    private CategoryViewModel categoryViewModel;


    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeCallback) {
            mCallback = (OnHomeCallback) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnHomeCallback!");
        }
        mCallback.showFab(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new CategoryViewModel(new CategoryRepository(getContext()), this);
        mAdapter = new CategoryAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCategoryBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_category, container, false);
        fragmentCategoryBinding.setCategoryViewModel(categoryViewModel);
        fragmentCategoryBinding.executePendingBindings();

        fragmentCategoryBinding.categoryRecyclerView.setAdapter(mAdapter);
        categoryViewModel.loadCategories();

        return fragmentCategoryBinding.getRoot();
    }

    @Override
    public void showSubCategories(Category category) {
        mCallback.showSubCategoriesByCategoryTitle(category);
    }

    @Override
    public void setResult(List<Category> categories) {
        mAdapter.addData(categories);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter = null;
        mCallback = null;
    }

    public void scrollUp() {
        fragmentCategoryBinding.categoryRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                fragmentCategoryBinding.categoryRecyclerView.smoothScrollToPosition(0);// Call smooth scroll
            }
        });
    }
}
