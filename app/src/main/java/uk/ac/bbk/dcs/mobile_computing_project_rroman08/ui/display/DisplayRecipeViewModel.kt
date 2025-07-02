package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.display

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.Recipe
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDao

class DisplayRecipeViewModel : ViewModel() {

    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe> = _recipe as LiveData<Recipe>

    private val _deleteComplete = MutableLiveData<Boolean>()
    val deleteComplete: LiveData<Boolean> = _deleteComplete

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

    fun deleteRecipe() {
        viewModelScope.launch {
            try {
                recipe.value?.let {
                    recipeDao?.deleteRecipe(it)
                    _deleteComplete.postValue(true)
                }
            } catch (e: Exception) {
                Log.e("ViewRecipeVM", "Failed to delete recipe: ${e.message}")
            }
        }
    }
}