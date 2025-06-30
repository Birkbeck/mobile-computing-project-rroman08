package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.CreateRecipeActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityMainBinding
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.display.DisplayRecipeActivity


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Adapter with click listener to launch ViewRecipeActivity
        val adapter = RecipeAdapter { id ->
            val intent = Intent(this, DisplayRecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", id)
            startActivity(intent)
        }
        binding.recyclerViewData.adapter = adapter

        // Create recipe button navigates to other activity
        binding.buttonCreateRecipe.setOnClickListener {
            val intent = Intent(this, CreateRecipeActivity::class.java)
            startActivity(intent)
        }

        val dao = RecipeDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipeDao = dao
        viewModel.readAllRecipes()

        viewModel.recipes.observe(this) { recipes ->
            adapter.updateRecipes(recipes)
        }
    }
}