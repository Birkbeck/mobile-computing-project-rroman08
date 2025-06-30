package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewRecipeViewModel : ViewModel() {

    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe> = _recipe as LiveData<Recipe>

    var recipeDao: RecipeDao? = null

    fun readRecipeById(id: Long) {
        viewModelScope.launch {
            try {
                val result = recipeDao?.getRecipeById(id)
                if (result != null) {
                    _recipe.value = result
                } else {
                    Log.e("ViewRecipeVieModel", "No recipe found with ID: $id")
                }
            } catch (e: Exception) {
                Log.e("ViewRecipeVM", "Failed to fetch recipe: ${e.message}")
            }
//            recipeDao?.let {
//                _recipe.value = it.getRecipeById(id)
//            }
        }
    }
}