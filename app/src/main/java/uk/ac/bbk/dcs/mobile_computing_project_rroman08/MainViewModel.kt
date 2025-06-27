package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _recipes = MutableLiveData(listOf<Recipe>())
    val recipes: LiveData<List<Recipe>> = _recipes

    var recipeDao: RecipeDao? = null

    fun readAllRecipes() {
        viewModelScope.launch {
            recipeDao?.let {
                val expenses = it.getAllRecipes()

                Log.i("BBK", expenses.toString())
                _recipes.value = expenses
            }
        }
    }
}