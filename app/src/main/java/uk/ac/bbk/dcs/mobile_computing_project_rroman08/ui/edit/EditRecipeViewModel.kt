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

/**
 * ViewModel for creating and editing recipes.
 *
 * Exposes LiveData for:
 * - title and category (MutableLiveData)
 * - ingredient and instruction lists
 *
 * Handles insert, update, and read operations via the DAO.
 */
class EditRecipeViewModel : ViewModel() {

    private val _ingredients = MutableLiveData<List<String>>(emptyList())
    val ingredients: LiveData<List<String>> = _ingredients

    private val _instructions = MutableLiveData<List<String>>(emptyList())
    val instructions: LiveData<List<String>> = _instructions
    val title = MutableLiveData<String>()
    val category = MutableLiveData<RecipeCategory>()

    var recipeDao: RecipeDao? = null

    /**
     * Inserts a new Recipe entity into the db.
     */
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
            Log.d("EditRecipeViewModel", "Saved recipe: $recipe")
        }
    }

    /**
     * Loads an existing Recipe by given ID and populates LiveData fields.
     */
    fun readRecipeById(id: Long) {
        viewModelScope.launch {
            val recipe = recipeDao?.getRecipeById(id)
            recipe?.let {
                title.value = it.title
                category.value = it.category
                _ingredients.value = it.ingredients
                _instructions.value = it.instructions
            }
        }
    }

    /**
     * Updates an existing Recipe entry by given ID.
     */
    fun updateRecipe(id: Long) {
        val updatedTitle = title.value ?: return
        val updatedCategory = category.value ?: return
        val updatedIngredients = _ingredients.value ?: emptyList()
        val updatedInstructions = _instructions.value ?: emptyList()

        val updatedRecipe = Recipe(
            id = id,
            title = updatedTitle,
            category = updatedCategory,
            ingredients = updatedIngredients,
            instructions = updatedInstructions
        )

        viewModelScope.launch {
            recipeDao?.updateRecipe(updatedRecipe)
            Log.d("EditRecipeViewModel", "Updated recipe: $updatedRecipe")
        }
    }

    /** Adds a new item to the ingredients list if not empty (non-blank). */
    fun addIngredient(ingredient: String) {
        if (ingredient.isNotBlank()) {
            _ingredients.value = _ingredients.value?.plus(ingredient)
        }
    }

    /** Removes a specified ingredient from the list. */
    fun removeIngredient(ingredient: String) {
        _ingredients.value = _ingredients.value?.filter { it != ingredient }
    }

    /** Adds a new item to the instructions list if not empty (non-blank). */
    fun addInstruction(instruction: String) {
        if (instruction.isNotBlank()) {
            _instructions.value = _instructions.value?.plus(instruction)
        }
    }

    /** Removes a specified instruction from the list. */
    fun removeInstruction(instruction: String) {
        _instructions.value = _instructions.value?.filter { it != instruction }
    }
}