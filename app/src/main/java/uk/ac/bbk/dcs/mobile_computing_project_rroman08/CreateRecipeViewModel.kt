package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateRecipeViewModel : ViewModel() {

    private val _ingredients = MutableLiveData<List<String>>(emptyList())
    val ingredients: LiveData<List<String>> = _ingredients

    private val _instructions = MutableLiveData<List<String>>(emptyList())
    val instructions: LiveData<List<String>> = _instructions

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