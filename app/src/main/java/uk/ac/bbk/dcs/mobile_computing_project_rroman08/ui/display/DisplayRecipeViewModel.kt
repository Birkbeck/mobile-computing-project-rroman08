package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.display

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.Recipe
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDao

/**
 * ViewModel for DisplayRecipeActivity:
 * Fetches a recipe by ID.
 * Deletes a recipe and notifies activity.
 *
 * @property recipe LiveData providing the current recipe (nullable if not found).
 * @property deleteComplete LiveData informs Activity when deletion has been carried out.
 */
class DisplayRecipeViewModel : ViewModel() {

    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: LiveData<Recipe> = _recipe as LiveData<Recipe>

    private val _deleteComplete = MutableLiveData<Boolean>()
    val deleteComplete: LiveData<Boolean> = _deleteComplete

    var recipeDao: RecipeDao? = null

    /**
     * Fetch recipe from db asynchronously using the given ID.
     * If not found or failed to fetch, it logs errors for debugging purposes.
     */
    fun readRecipeById(id: Long) {
        viewModelScope.launch {
            try {
                val result = recipeDao?.getRecipeById(id)
                if (result != null) {
                    _recipe.value = result
                } else {
                    Log.e("DisplayRecipeVieWModel", "No recipe found with ID: $id")
                }
            } catch (e: Exception) {
                Log.e("DisplayRecipeVieWModel", "Failed to fetch recipe: ${e.message}")
            }
        }
    }

    /**
     * Deletes the loaded recipe and notifies Activity after the operation is completed by
     * setting the flag (deletionComplete) to true.
     * Logs an error for debugging purposes.
     */
    fun deleteRecipe() {
        viewModelScope.launch {
            try {
                recipe.value?.let {
                    recipeDao?.deleteRecipe(it)
                    _deleteComplete.postValue(true)
                }
            } catch (e: Exception) {
                Log.e("DisplayRecipeVieWModel", "Failed to delete recipe: ${e.message}")
            }
        }
    }
}