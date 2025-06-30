package uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils

import androidx.databinding.BindingConversion
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory

@BindingConversion
fun convertRecipeCategory(recipeCategory: RecipeCategory): String {
    return recipeCategory.toString()
}

