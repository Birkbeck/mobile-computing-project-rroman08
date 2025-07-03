package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.Recipe
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDao

/**
 * ViewModel for MainActivity.
 *
 * Manages fetching all recipes from the database and exposing via LiveData.
 */
class MainViewModel : ViewModel() {

    private val _recipes = MutableLiveData(listOf<Recipe>())
    val recipes: LiveData<List<Recipe>> = _recipes

    var recipeDao: RecipeDao? = null

    /** Reads all recipes and updates LiveData, logging counts for debugging purposes. */
    fun readAllRecipes() {
        viewModelScope.launch {
            recipeDao?.let {
                val recipesList = it.getAllRecipes()
                Log.i("MainViewModel", "Fetched ${recipesList.size} recipes from DB")
                recipesList.forEach { recipe ->
                    Log.d("MainViewModel", recipe.toString())
                }
                _recipes.value = recipesList
            } ?: Log.w("MainViewModel", "RecipeDao is null, cannot fetch recipes")
        }
    }
}