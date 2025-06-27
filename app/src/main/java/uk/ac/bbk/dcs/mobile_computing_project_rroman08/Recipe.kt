package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: RecipeCategory,
    val ingredients: List<String>,
    val instructions: List<String>
) : Serializable
