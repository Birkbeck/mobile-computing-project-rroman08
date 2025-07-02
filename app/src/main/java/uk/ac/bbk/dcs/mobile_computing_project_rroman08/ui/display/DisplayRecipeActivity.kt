package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.display

import android.util.Log
import android.os.Bundle
import android.widget.TextView
import android.widget.LinearLayout
import androidx.activity.viewModels
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.R
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.Recipe
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityDisplayRecipeBinding
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main.MainActivity

class DisplayRecipeActivity : AppCompatActivity() {

    private val viewModel: DisplayRecipeViewModel by viewModels()
    private lateinit var binding: ActivityDisplayRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Empty placeholder recipe to avoid null binding crash
        binding.recipe = Recipe(
            id = 0,
            title = "",
            category = RecipeCategory.OTHER,
            ingredients = emptyList(),
            instructions = emptyList()
        )

        // Inject DAO
        val dao = RecipeDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipeDao = dao

        val recipeId = intent.getLongExtra("RECIPE_ID", -1L)
        if (recipeId != -1L) {
            // Use ViewModel to read recipe from database with given id
            viewModel.readRecipeById(recipeId)
        } else {
            Log.e("ViewRecipeActivity", "Invalid recipe ID: $recipeId")
            finish() // << exit early so no null data is bound??>>>
        }

        // Observe LiveData and bind recipe to the view
        viewModel.recipe.observe(this) { recipe ->
            if (recipe != null) {
                binding.recipe = recipe
                populateList(binding.linearLayoutIngredientsData, recipe.ingredients)
                populateList(binding.linearLayoutInstructionsData, recipe.instructions)
            } else {
                Log.e("ViewRecipeActivity", "Recipe was null")
            }
        }

        // Delete button logic
        binding.buttonDeleteRecipe.setOnClickListener {
            viewModel.deleteRecipe()
        }

        // Observe deletion completion and navigate back to MainActivity
        viewModel.deleteComplete.observe(this) { deleted ->
            if (deleted) {
                val intent = Intent(this, MainActivity::class.java)
                // Use Intent.FLAG_ACTIVITY_CLEAR_TOP to avoid stacking activities
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
    }

    private fun populateList(container: LinearLayout, list: List<String>) {
        container.removeAllViews()
        list.forEachIndexed { index, item ->
            val textView = TextView(this).apply {
                text = "${index + 1}. $item"
                textSize = 16f
                setTextColor(resources.getColor(R.color.dark_grey, theme))
                setPadding(0, 4, 0, 4)
            }
            container.addView(textView)
        }
    }
}
