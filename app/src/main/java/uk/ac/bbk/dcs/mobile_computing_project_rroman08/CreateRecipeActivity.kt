package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityCreateRecipeBinding
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.createItemView
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.showInputDialog

class CreateRecipeActivity : AppCompatActivity() {

    private val viewModel: CreateRecipeViewModel by viewModels()
    private lateinit var binding: ActivityCreateRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup spinner
        val categories = RecipeCategory.entries
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        val dao = RecipeDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipeDao = dao

        // Add this Save button click listener near the end of onCreate
        binding.saveButton.setOnClickListener {
            viewModel.title.value = binding.editTextRecipeTitle.text.toString()
            viewModel.category.value = binding.spinnerCategory.selectedItem as RecipeCategory
            viewModel.saveRecipe()

            finish()
        }

        // Observe ingredients and instructions
        viewModel.ingredients.observe(this) {
            updateListViews(it, binding.linearLayoutIngredientsData) { ingredient ->
                viewModel.removeIngredient(ingredient)
            }
        }

        viewModel.instructions.observe(this) {
            updateListViews(it, binding.linearLayoutInstructionsDataContainer) { instruction ->
                viewModel.removeInstruction(instruction)
            }
        }

        binding.buttonAddIngredients.setOnClickListener {
            showInputDialog("Add Ingredient") { viewModel.addIngredient(it) }
        }

        binding.buttonAddInstructions.setOnClickListener {
            showInputDialog("Add Instruction") { viewModel.addInstruction(it) }
        }
    }

    private fun updateListViews(
        items: List<String>,
        container: android.widget.LinearLayout,
        onDelete: (String) -> Unit
    ) {
        container.removeAllViews()
        items.forEach { item ->
            val view = createItemView(item) { onDelete(item) }
            container.addView(view)
        }
    }
}