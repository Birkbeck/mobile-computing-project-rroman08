package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import androidx.databinding.BindingConversion

@BindingConversion
fun convertRecipeCategory(recipeCategory: RecipeCategory): String {
    return recipeCategory.toString()
}

