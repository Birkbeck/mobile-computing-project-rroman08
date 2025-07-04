package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.R
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.edit.EditRecipeActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.display.DisplayRecipeActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.Recipe
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setup() = runBlocking {
        Intents.init()

        // Clear persistent DB
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dao = RecipeDatabase.getInstance(context).recipeDao()
        dao.clearAll()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testRecyclerViewIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.recycler_view_data)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateRecipeButtonLaunchesEditRecipeActivity() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_create_recipe)).perform(click())
        // Check whether launches right Activity
        Intents.intended(hasComponent(EditRecipeActivity::class.java.name))
    }

    @Test
    fun testClickingRecipeItemLaunchesDisplayRecipeActivity() = runBlocking {
        // Requires inserting a recipe into the database before launching activity
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dao = RecipeDatabase.getInstance(context).recipeDao()
        val dummyRecipe = Recipe(
            title = "Monkey Soup",
            category = RecipeCategory.LUNCH,
            ingredients = listOf("3 Bananas", "1 Tin of coconut milk", "..."),
            instructions = listOf("Char bananas on bbq", "Mix bananas with coconut milk in large pot", "...")
        )

        // Because .insertRecipe() is a suspend fn, it requires to wrap whole test in runBlocking{}
        val id = dao.insertRecipe(dummyRecipe)

        ActivityScenario.launch(MainActivity::class.java)

        // Wait for RecyclerView to load
        Thread.sleep(1000)

        // "Click" on the first recipe item
        onView(
            allOf(
                withId(R.id.button_see_recipe),
                isDescendantOfA(withId(R.id.recycler_view_data))
            )
        ).perform(click())
        // Check whether launches right Activity
        Intents.intended(hasComponent(DisplayRecipeActivity::class.java.name))
    }
}
