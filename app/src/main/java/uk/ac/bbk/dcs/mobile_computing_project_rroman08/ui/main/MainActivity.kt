package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.edit.EditRecipeActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityMainBinding
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.display.DisplayRecipeActivity

/**
 * Main screen/UI showing all recipes in a RecyclerView.
 * Clicking the 'see recipe' button on a item opens it. Also there is a button that
 * allows for the creation of new recipes.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView with click listener to launch DisplayRecipeActivity
        val adapter = RecipeAdapter { id ->
            val intent = Intent(this, DisplayRecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", id)
            startActivity(intent)
        }
        binding.recyclerViewData.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewData.adapter = adapter

        // Navigate to create recipe screen
        binding.buttonCreateRecipe.setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            startActivity(intent)
        }

        val dao = RecipeDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipeDao = dao
        viewModel.readAllRecipes()

        viewModel.recipes.observe(this) { recipes ->
            adapter.updateRecipes(recipes)
        }
    }

    /** Refreshes recipe list on resume to reflect potential changes. */
    override fun onResume() {
        super.onResume()
        viewModel.readAllRecipes()
    }
}