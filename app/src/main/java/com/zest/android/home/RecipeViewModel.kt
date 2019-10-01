package com.zest.android.home

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.TextUtils
import android.util.Log
import com.zest.android.R
import com.zest.android.data.Recipe
import com.zest.android.data.source.RecipeRepository
import com.zest.android.detail.OnDetailCallback
import com.zest.android.favorite.OnFavoriteFragmentInteractionListener
import com.zest.android.search.OnSearchCallback
import java.util.*

class RecipeViewModel : BaseObservable {


    private var searchCallback: OnSearchCallback? = null
    private var homeListener: OnHomeFragmentInteractionListener? = null
    private var favoriteListener: OnFavoriteFragmentInteractionListener? = null
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var detailCallback: OnDetailCallback
    private val model = ObservableField<Recipe>()
    private val hasTag = ObservableBoolean(false)
    private val hasRecipe = ObservableBoolean(false)
    private val isLoading = ObservableBoolean(false)
    private val isFavorite = ObservableBoolean(false)
    private val isDetailFavorite = ObservableBoolean(false)



    constructor(recipe: Recipe?, recipeRepository: RecipeRepository, callback: OnDetailCallback) {
        this.recipeRepository = recipeRepository
        this.detailCallback = callback
        this.model.set(recipe)
        this.model.notifyChange()
        updateIsDetailFavorite()
    }

    private fun updateIsDetailFavorite() {
        setIsDetailFavorite(recipeRepository.getFavoriteByRecipeId(this.model.get()!!) != null)
    }

    constructor(favoriteRepository: RecipeRepository, listener: OnFavoriteFragmentInteractionListener) {
        this.recipeRepository = favoriteRepository
        this.favoriteListener = listener
    }

    constructor(recipe: Recipe, listener: OnFavoriteFragmentInteractionListener) {
        this.favoriteListener = listener
        this.model.set(recipe)
    }

    constructor(recipe: Recipe, recipeRepository: RecipeRepository, listener: OnHomeFragmentInteractionListener) {
        this.recipeRepository = recipeRepository
        homeListener = listener
        this.model.set(recipe)
    }

    constructor(recipeRepository: RecipeRepository, listener: OnHomeFragmentInteractionListener) {
        this.recipeRepository = recipeRepository
        homeListener = listener
    }

    constructor(recipeRepository: RecipeRepository, searchCallback: OnSearchCallback) {
        this.recipeRepository = recipeRepository
        this.searchCallback = searchCallback
    }

    constructor(recipe: Recipe, searchCallback: OnSearchCallback) {
        this.model.set(recipe)
        this.searchCallback = searchCallback
    }

    @Bindable
    fun getHasRecipe(): Boolean {
        return this.hasRecipe.get()
    }

    @Bindable
    fun getTitle():String{
        return this.model.get()!!.title!!
    }

    @Bindable
    fun getInstructions():String{
        return this.model.get()!!.instructions!!
    }

    @Bindable
    fun getImage():String{
        return this.model.get()!!.image!!
    }

    @Bindable
    fun getHasTag(): Boolean {
        return this.hasTag.get()
    }

    fun setHasTag(isLoaded: Boolean) {
        this.hasTag.set(isLoaded)
        notifyChange()
    }

    fun setHasRecipe(isLoaded: Boolean) {
        this.hasRecipe.set(isLoaded)
        notifyChange()
    }

    fun loadTags(recipe: Recipe): Array<String>? {
        Log.d(TAG, "loadTags() called with: recipe = [$recipe]")
        val tags = recipe.tag
        if (TextUtils.isEmpty(tags)) {
            setHasTag(false)
            return null
        }
        setHasTag(true)
        return tags!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }

    fun loadTagTitles(recipe: Recipe): List<String>? {
        Log.d(TAG, "loadTagTitles() called with: recipe = [$recipe]")
        val tags = recipe.tag
        if (TextUtils.isEmpty(tags)) return null
        val split = tags!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Arrays.asList(*split)
    }

    fun updateRecipe(recipe: Recipe) {
        Log.d(TAG, "showDeleteFavoriteDialog() called with: recipe = [$recipe]")
        recipeRepository.updateRecipe(recipe)
    }

    @Bindable
    fun getIsLoading(): Boolean {
        return this.isLoading.get()
    }

    fun setLoading(isLoaded: Boolean) {
        this.isLoading.set(isLoaded)
        notifyChange()
    }

    fun loadFavorite(recipe: Recipe): Recipe {
        Log.d(TAG, "loadFavorite() called with: recipe = [$recipe]")
        return recipeRepository.getFavoriteByRecipeId(recipe)!!
    }

    fun onRecipeItemClick() {
        homeListener!!.gotoDetailPage(this.model.get()!!)
    }

    fun getRecipes() {
        setLoading(true)
        setHasRecipe(true)//for the first load, consider this true
        recipeRepository.loadRecipeList(RecipesCallbackImp())
    }

    fun loadFavoriteByRecipeId(recipe: Recipe): Recipe {
        Log.d(TAG, "loadFavoriteByRecipeId() called with: recipe = [$recipe]")
        return recipeRepository.getFavoriteByRecipeId(recipe)!!
    }

    fun deleteFavoriteByRecipeId(recipe: Recipe) {
        Log.d(TAG, "deleteFavoriteByRecipeId() called with: recipe = [$recipe]")
        recipeRepository.deleteFavorite(recipe)
    }

    fun insertFavoriteRecipe(recipe: Recipe) {
        Log.d(TAG, "insertFavoriteRecipe() called with: recipe = [$recipe]")
        recipeRepository.insertFavorite(recipe)
    }

    fun loadFavorites(): MutableList<Recipe>? {
        val recipes : MutableList<Recipe> = recipeRepository.loadAllFavorites()
        //val recipes = recipeRepository.loadAllFavorites()
        if (recipes != null && !recipes.isEmpty())
            setHasRecipe(true)
        else
            setHasRecipe(false)
        return recipes
    }

    fun deleteFavoriteRecipe(recipe: Recipe) {
        recipeRepository.deleteFavoriteRecipe(recipe)
    }

    fun onDetailFavoriteClick() {
        if (model == null) return
        if (isDetailFavorite.get()) {
            detailCallback.showMessage(R.string.deleted_this_recipe_from_your_favorite_list)
            recipeRepository.removeFavorite(model.get()!!)
            setIsDetailFavorite(false)
        } else {
            detailCallback.showMessage(R.string.added_this_recipe_to_your_favorite_list)
            recipeRepository.insertFavorite(model.get()!!)
            setIsDetailFavorite(true)
        }
    }

    @Bindable
    fun getIsFavorite(): Boolean {
        setIsFavorite(recipeRepository.getFavoriteByRecipeId(this.model.get()!!) != null)
        return isFavorite.get()
    }

    fun setIsFavorite(status: Boolean) {
        isFavorite.set(status)
        notifyChange()
    }

    @Bindable
    fun getIsDetailFavorite(): Boolean {
        return isDetailFavorite.get()
    }

    fun setIsDetailFavorite(status: Boolean) {
        isDetailFavorite.set(status)
        notifyChange()
    }

    fun onRecipeFavoriteClick() {
        if (model == null) return
        if (recipeRepository.getFavoriteByRecipeId(this.model.get()!!) != null) {
            recipeRepository.deleteFavorite(model.get()!!)
            setIsFavorite(false)
        } else {
            recipeRepository.insertFavorite(model.get()!!)
            setIsFavorite(true)
        }
    }

    fun onDeleteFavoriteClick() {
        favoriteListener!!.showDeleteFavoriteDialog(this.model.get()!!)
    }

    fun onFavoriteItemClick() {
        favoriteListener!!.gotoDetailPage(this.model.get()!!)
    }

    fun onSearchItemClick() {
        searchCallback!!.gotoDetailPage(this.model.get()!!)
    }

    fun searchQuery(searchQuery: String) {
        setLoading(true)
        searchCallback!!.showEmptyView(false)
        recipeRepository.getAllRecipesByQuery(searchQuery, SearchCallbackImp())
    }

    /**
     * To make an interaction between [RecipeViewModel] and
     * [SearchCallbackImp],[RecipesCallbackImp]
     */
    interface OnResultCallback {

        fun loadData(recipes: List<Recipe>)

        fun noData()
    }

    inner class SearchCallbackImp : OnResultCallback {

        override fun loadData(recipes: List<Recipe>) {
            setLoading(false)
            searchCallback!!.showEmptyView(recipes.isEmpty())
            searchCallback!!.setResult(recipes)
        }

        override fun noData() {
            setLoading(false)
            searchCallback!!.showEmptyView(true)
            searchCallback!!.noResult()
        }
    }

    inner class RecipesCallbackImp : OnResultCallback {

        override fun loadData(recipes: List<Recipe>) {
            setLoading(false)
            setHasRecipe(true)
            homeListener!!.loadRecipes(recipes)
        }

        override fun noData() {
            setLoading(false)
            setHasRecipe(false)
        }
    }

    companion object {

        private val TAG = RecipeViewModel::class.java.simpleName
    }
}
