package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {
    // Get all recipes ordered by title
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    suspend fun getAllRecipes(): List<Recipe>

    // Retrieve a recipe by ID from db
    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Long): Recipe?

    // Insert a new recipe into db
    @Insert()
    suspend fun insertRecipe(recipe: Recipe)

    // Update an existing recipe currently in db
    @Update
    suspend fun updateRecipe(recipe: Recipe)

    // Delete a recipe from db
    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    // IMPLEMENT LATER IN UI, IF THERE IS TIME REMAINING
    // Optional: Get recipes by category
//    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY title ASC")
//    fun getRecipesByCategory(category: RecipeCategory): List<RecipeEntity>
}