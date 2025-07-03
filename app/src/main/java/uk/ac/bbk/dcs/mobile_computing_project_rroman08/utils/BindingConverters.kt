package uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils

import androidx.databinding.BindingConversion
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory

/**
 * Converts a [RecipeCategory] enum to a [String] representation.
 *
 * @param recipeCategory The category enum value to convert.
 * @return The string representation of the given [recipeCategory].
 */
@BindingConversion
fun convertRecipeCategory(recipeCategory: RecipeCategory): String {
    return recipeCategory.toString()
}

