package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object (DAO) interface for db operations on [Recipe] entities.
 */
@Dao
interface RecipeDao {

    /**
     * Retrieves all recipes from the db, ordered by title in alphabetical order.
     *
     * @return A list of all recipes.
     */
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    suspend fun getAllRecipes(): List<Recipe>

    /**
     * Retrieves a recipe by a given unique ID.
     *
     * @param id The ID of the recipe to retrieve.
     * @return The recipe with the matching ID, or null if not found.
     */
    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Long): Recipe?

    /**
     * Inserts a new recipe into the db.
     *
     * @param recipe The [Recipe] to insert.
     */
    @Insert()
    suspend fun insertRecipe(recipe: Recipe)

    /**
     * Updates an existing recipe in the db.
     *
     * @param recipe The [Recipe] to update.
     */
    @Update
    suspend fun updateRecipe(recipe: Recipe)

    /**
    * Deletes a recipe from the db.
    *
    * @param recipe The [Recipe] to delete.
    */
    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    // IMPLEMENT LATER IN UI, IF THERE IS TIME REMAINING
//    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY title ASC")
//    fun getRecipesByCategory(category: RecipeCategory): List<RecipeEntity>
}