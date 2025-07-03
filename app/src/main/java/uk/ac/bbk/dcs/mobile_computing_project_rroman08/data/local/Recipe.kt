package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Represents a recipe entity in the Room database.
 *
 * @property id Unique identifier for each recipe (auto-generated: starting from 0).
 * @property title Title of the recipe.
 * @property category The category to which the recipe belongs.
 * @property ingredients A list of ingredients required for the recipe.
 * @property instructions A list of cooking instructions.
 */
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: RecipeCategory,
    val ingredients: List<String>,
    val instructions: List<String>
) : Serializable