package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewRecipeViewModel : ViewModel() {

    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> = _recipe

    fun setRecipe(newRecipe: Recipe) {
        _recipe.value = newRecipe
    }
}