package com.zest.android.home;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.zest.android.R;
import com.zest.android.data.Recipe;
import com.zest.android.data.source.DetailRepository;
import com.zest.android.data.source.FavoriteRepository;
import com.zest.android.data.source.HomeRepository;
import com.zest.android.data.source.SearchRepository;
import com.zest.android.detail.OnDetailCallback;
import com.zest.android.favorite.OnFavoriteFragmentInteractionListener;
import com.zest.android.search.OnSearchCallback;

import java.util.Arrays;
import java.util.List;

public class RecipeViewModel extends BaseObservable {

    private static final String TAG = RecipeViewModel.class.getSimpleName();
    private SearchRepository searchRepository;
    private OnSearchCallback searchCallback;
    private HomeRepository homeRepository;
    private OnHomeFragmentInteractionListener homeListener;
    private DetailRepository detailRepository;
    private FavoriteRepository favoriteRepository;
    private OnFavoriteFragmentInteractionListener favoriteListener;
    private OnDetailCallback detailCallback;
    private ObservableField<Recipe> model = new ObservableField<>();
    private ObservableBoolean hasTag = new ObservableBoolean(false);
    private ObservableBoolean hasRecipe = new ObservableBoolean(false);
    private ObservableBoolean isLoading = new ObservableBoolean(false);
    private ObservableBoolean isFavorite = new ObservableBoolean(false);
    private ObservableBoolean isDetailFavorite = new ObservableBoolean(false);


    public RecipeViewModel(Recipe recipe, DetailRepository detailRepository, OnDetailCallback callback) {
        this.detailRepository = detailRepository;
        this.detailCallback = callback;
        this.model.set(recipe);
    }

    public RecipeViewModel(FavoriteRepository favoriteRepository, OnFavoriteFragmentInteractionListener listener) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteListener = listener;
    }

    public RecipeViewModel(Recipe recipe, OnFavoriteFragmentInteractionListener listener) {
        this.favoriteListener = listener;
        this.model.set(recipe);
    }

    public RecipeViewModel(Recipe recipe, HomeRepository homeRepository, OnHomeFragmentInteractionListener listener) {
        this.homeRepository = homeRepository;
        homeListener = listener;
        this.model.set(recipe);
    }

    public RecipeViewModel(HomeRepository homeRepository, OnHomeFragmentInteractionListener listener) {
        this.homeRepository = homeRepository;
        homeListener = listener;
    }

    public RecipeViewModel(SearchRepository searchRepository, OnSearchCallback searchCallback) {
        this.searchRepository = searchRepository;
        this.searchCallback = searchCallback;
    }

    public RecipeViewModel(Recipe recipe, OnSearchCallback searchCallback) {
        this.model.set(recipe);
        this.searchCallback = searchCallback;
    }

    @Bindable
    public boolean getHasTag() {
        return this.hasTag.get();
    }

    public void setHasTag(boolean isLoaded) {
        this.hasTag.set(isLoaded);
        notifyChange();
    }

    @Bindable
    public String getTitle() {
        return this.model.get().getTitle();
    }

    @Bindable
    public String getInstructions() {
        return this.model.get().getInstructions();
    }

    @Bindable
    public String getImage() {
        return this.model.get().getImage();
    }

    @Bindable
    public boolean getHasRecipe() {
        return this.hasRecipe.get();
    }

    public void setHasRecipe(boolean isLoaded) {
        this.hasRecipe.set(isLoaded);
        notifyChange();
    }

    public List<String> loadTagTitles(Recipe recipe) {
        Log.d(TAG, "loadTagTitles() called with: recipe = [" + recipe + "]");
        final String tags = recipe.getTag();
        if (TextUtils.isEmpty(tags)) return null;
        final String[] split = tags.split(",");
        return Arrays.asList(split);
    }

    public void updateRecipe(Recipe recipe) {
        Log.d(TAG, "showDeleteFavoriteDialog() called with: recipe = [" + recipe + "]");
        detailRepository.updateRecipe(recipe);
    }

    @Bindable
    public boolean getIsLoading() {
        return this.isLoading.get();
    }

    public void setLoading(boolean isLoaded) {
        this.isLoading.set(isLoaded);
        notifyChange();
    }

    public Recipe loadFavorite(Recipe recipe) {
        Log.d(TAG, "loadFavorite() called with: recipe = [" + recipe + "]");
        return detailRepository.getFavoriteByRecipeId(recipe);
    }

    public void onRecipeItemClick() {
        homeListener.gotoDetailPage(this.model.get());
    }

    public void getRecipes() {
        setLoading(true);
        setHasRecipe(true);//for the first load, consider this true
        homeRepository.loadRecipeList(new RecipesCallbackImp());
    }

    public Recipe loadFavoriteByRecipeId(Recipe recipe) {
        Log.d(TAG, "loadFavoriteByRecipeId() called with: recipe = [" + recipe + "]");
        return homeRepository.getFavoriteByRecipeId(recipe);
    }

    public void deleteFavoriteByRecipeId(Recipe recipe) {
        Log.d(TAG, "deleteFavoriteByRecipeId() called with: recipe = [" + recipe + "]");
        homeRepository.deleteFavorite(recipe);
    }

    public void insertFavoriteRecipe(Recipe recipe) {
        Log.d(TAG, "insertFavoriteRecipe() called with: recipe = [" + recipe + "]");
        homeRepository.insertFavorite(recipe);
    }

    public List<Recipe> loadFavorites() {
        final List<Recipe> recipes = favoriteRepository.loadAllFavorites();
        if (recipes != null && !recipes.isEmpty()) setHasRecipe(true);
        else setHasRecipe(false);
        return recipes;
    }

    public void deleteFavoriteRecipe(Recipe recipe) {
        favoriteRepository.deleteFavoriteRecipe(recipe);
    }

    public void onDetailFavoriteClick() {
        if (model == null) return;
        if (detailRepository.getFavoriteByRecipeId(this.model.get()) != null) {
            detailCallback.showMessage(R.string.deleted_this_recipe_from_your_favorite_list);
            detailRepository.removeFavorite(model.get());
            setIsDetailFavorite(false);
        } else {
            detailCallback.showMessage(R.string.added_this_recipe_to_your_favorite_list);
            detailRepository.insertFavorite(model.get());
            setIsDetailFavorite(true);
        }
    }

    @Bindable
    public boolean getIsFavorite() {
        setIsFavorite(homeRepository.getFavoriteByRecipeId(this.model.get()) != null);
        return isFavorite.get();
    }

    public void setIsFavorite(boolean status){
        isFavorite.set(status);
        notifyChange();
    }

    @Bindable
    public boolean getIsDetailFavorite() {
        setIsDetailFavorite(detailRepository.getFavoriteByRecipeId(this.model.get()) != null);
        return isDetailFavorite.get();
    }

    public void setIsDetailFavorite(boolean status){
        isDetailFavorite.set(status);
        notifyChange();
    }

    public void onRecipeFavoriteClick() {
        if (model == null) return;
        if (homeRepository.getFavoriteByRecipeId(this.model.get()) != null) {
            homeRepository.deleteFavorite(model.get());
            setIsFavorite(false);
        } else {
            homeRepository.insertFavorite(model.get());
            setIsFavorite(true);
        }
    }

    public void onDeleteFavoriteClick() {
        favoriteListener.showDeleteFavoriteDialog(this.model.get());
    }

    public void onFavoriteItemClick() {
        favoriteListener.gotoDetailPage(this.model.get());
    }

    public String[] loadTags(Recipe recipe) {
        Log.d(TAG, "loadTags() called with: recipe = [" + recipe + "]");
        final String tags = recipe.getTag();
        if (TextUtils.isEmpty(tags)) {
            setHasTag(false);
            return null;
        }
        setHasTag(true);
        final String[] splitTags = tags.split(",");
        return splitTags;
    }

    public void onSearchItemClick() {
        searchCallback.gotoDetailPage(this.model.get());
    }

    public void searchQuery(String searchQuery) {
        setLoading(true);
        searchCallback.showEmptyView(false);
        searchRepository.getAllRecipesByQuery(searchQuery, new SearchCallbackImp());
    }

    /**
     * To make an interaction between {@link RecipeViewModel} and
     * {@link SearchCallbackImp},{@link RecipesCallbackImp}
     */
    public interface OnResultCallback {

        void loadData(List<Recipe> recipes);

        void noData();
    }

    public class SearchCallbackImp implements OnResultCallback {

        public void loadData(List<Recipe> recipes) {
            setLoading(false);
            searchCallback.showEmptyView(recipes == null || recipes.isEmpty());
            searchCallback.setResult(recipes);
        }

        @Override
        public void noData() {
            setLoading(false);
            searchCallback.showEmptyView(true);
            searchCallback.noResult();
        }
    }

    public class RecipesCallbackImp implements OnResultCallback {

        @Override
        public void loadData(List<Recipe> recipes) {
            setLoading(false);
            setHasRecipe(true);
            homeListener.loadRecipes(recipes);
        }

        @Override
        public void noData() {
            setLoading(false);
            setHasRecipe(false);
        }
    }
}
