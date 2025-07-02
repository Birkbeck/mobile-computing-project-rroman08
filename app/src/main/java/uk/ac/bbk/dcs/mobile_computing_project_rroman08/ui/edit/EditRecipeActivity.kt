package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.edit

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityEditRecipeBinding
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.createItemView
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.showInputDialog

class EditRecipeActivity : AppCompatActivity() {

    private val viewModel: EditRecipeViewModel by viewModels()
    private lateinit var binding: ActivityEditRecipeBinding
    private var recipeId: Long? = null // null = create, not null = edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inject DAO manually (since you're not using DI framework here)
        val dao = RecipeDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipeDao = dao

        // Setup category spinner
        val categories = RecipeCategory.entries
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Check if editing existing recipe
        recipeId = intent.getLongExtra("RECIPE_ID", -1L).takeIf { it != -1L }
        recipeId?.let { id ->
            viewModel.readRecipeById(id)
        }

        // Observe and populate fields
        viewModel.title.observe(this) {
            binding.editTextRecipeTitle.setText(it)
        }

        viewModel.category.observe(this) { selectedCategory ->
            val index = categories.indexOf(selectedCategory)
            if (index >= 0) binding.spinnerCategory.setSelection(index)
        }

        viewModel.ingredients.observe(this) { ingredients ->
            updateListViews(ingredients, binding.linearLayoutIngredientsData) { ingredient ->
                viewModel.removeIngredient(ingredient)
            }
        }

        viewModel.instructions.observe(this) { instructions ->
            updateListViews(instructions, binding.linearLayoutInstructionsDataContainer) { instruction ->
                viewModel.removeInstruction(instruction)
            }
        }

        // Handle add buttons
        binding.buttonAddIngredients.setOnClickListener {
            showInputDialog("Add Ingredient") { viewModel.addIngredient(it) }
        }

        binding.buttonAddInstructions.setOnClickListener {
            showInputDialog("Add Instruction") { viewModel.addInstruction(it) }
        }

        // Save or update recipe
        binding.saveButton.setOnClickListener {
            val title = binding.editTextRecipeTitle.text.toString()
            val category = binding.spinnerCategory.selectedItem as RecipeCategory

            viewModel.title.value = title
            viewModel.category.value = category

            if (recipeId != null) {
                viewModel.updateRecipe(recipeId!!)
            } else {
                viewModel.saveRecipe()
            }

            finish()
        }
    }

    private fun updateListViews(
        items: List<String>,
        container: LinearLayout,
        onDelete: (String) -> Unit
    ) {
        container.removeAllViews()
        items.forEach { item ->
            val view = createItemView(item) { onDelete(item) }
            container.addView(view)
        }
    }
}
