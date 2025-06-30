package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import android.util.Log
import android.os.Bundle
import android.widget.TextView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityViewRecipeBinding

class ViewRecipeActivity : AppCompatActivity() {

    private val viewModel: ViewRecipeViewModel by viewModels()
    private lateinit var binding: ActivityViewRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
