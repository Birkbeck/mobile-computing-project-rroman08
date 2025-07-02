package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.Recipe
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDao

class EditRecipeViewModel : ViewModel() {

    private val _ingredients = MutableLiveData<List<String>>(emptyList())
    val ingredients: LiveData<List<String>> = _ingredients

    private val _instructions = MutableLiveData<List<String>>(emptyList())
    val instructions: LiveData<List<String>> = _instructions
    val title = MutableLiveData<String>()
    val category = MutableLiveData<RecipeCategory>()

    var recipeDao: RecipeDao? = null

    fun saveRecipe() {
        val currentTitle = title.value ?: return
        val currentCategory = category.value ?: return
        val currentIngredients = _ingredients.value ?: emptyList()
        val currentInstructions = _instructions.value ?: emptyList()

        val recipe = Recipe(
            title = currentTitle,
            category = currentCategory,
            ingredients = currentIngredients,
            instructions = currentInstructions
        )

        viewModelScope.launch {
            recipeDao?.insertRecipe(recipe)
            Log.d("CreateRecipeViewModel", "Saved recipe: $recipe")
        }
    }

    // Add ingredient
    fun addIngredient(ingredient: String) {
        if (ingredient.isNotBlank()) {
            _ingredients.value = _ingredients.value?.plus(ingredient)
        }
    }

    // Remove ingredient
    fun removeIngredient(ingredient: String) {
        _ingredients.value = _ingredients.value?.filter { it != ingredient }
    }

    // Add instruction
    fun addInstruction(instruction: String) {
        if (instruction.isNotBlank()) {
            _instructions.value = _instructions.value?.plus(instruction)
        }
    }

    // Remove instruction
    fun removeInstruction(instruction: String) {
        _instructions.value = _instructions.value?.filter { it != instruction }
    }
}