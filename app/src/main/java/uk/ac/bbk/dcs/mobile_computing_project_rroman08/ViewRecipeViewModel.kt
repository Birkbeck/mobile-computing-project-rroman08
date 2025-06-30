package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewRecipeViewModel : ViewModel() {

    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> = _recipe

    var recipeDao: RecipeDao? = null

    fun setRecipe(newRecipe: Recipe) {
        _recipe.value = newRecipe
    }

    fun readRecipeById(id: Long) {
        viewModelScope.launch {
            recipeDao?.let {
                _recipe.value = it.getRecipeById(id)
            }
        }
    }
}