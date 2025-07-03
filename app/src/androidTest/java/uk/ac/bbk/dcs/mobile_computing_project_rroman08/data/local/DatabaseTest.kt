package uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var recipeDao: RecipeDao
    private lateinit var recipeDatabase: RecipeDatabase

    val testRecipes = listOf(
        Recipe(
            id = 1,
            title = "Monkey Soup",
            category = RecipeCategory.LUNCH,
            ingredients = listOf(
                "3 Bananas", "1 Tin of coconut milk", "..."
            ),
            instructions = listOf(
                "Char bananas on bbq", "Mix bananas with coconut milk in large pot", "..."
            )
        ),
        Recipe(
            id = 2,
            title = "Oyster Tiramisu",
            category = RecipeCategory.DESSERT,
            ingredients = listOf(
                "10 Oysters", "200g Flour", "3 Eggs", "..."
            ),
            instructions = listOf(
                "Finely mince oysters", "Beat eggs with whisker until foamy", "..."
            )
        ),
    )

    @Before
    fun createDatabase() {
        val context: Context = ApplicationProvider.getApplicationContext()
        recipeDatabase = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recipeDao = recipeDatabase.recipeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() = recipeDatabase.close()

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieve() = runBlocking {
        // .insertRecipe() & getAllRecipes() are suspend functions, hence the runBlocking wrapper
        testRecipes.forEach { recipe ->
            recipeDao.insertRecipe(recipe)
        }
        val recipes = recipeDao.getAllRecipes()
        assert(recipes.size == 2)
    }
}