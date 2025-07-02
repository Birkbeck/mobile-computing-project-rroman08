package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.edit

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.R
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityEditRecipeBinding
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main.MainActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.createDiscardChangesButton
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.createItemView
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.updateListViews
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

        // Check if editing existing recipe
        recipeId = intent.getLongExtra("RECIPE_ID", -1L).takeIf { it != -1L }
        recipeId?.let { id ->
            viewModel.readRecipeById(id)
        }

        // Set header text dynamically
        if (recipeId != null) {
            binding.textViewEditRecipe.text = getString(R.string.edit_recipe)

            // Add Discard Changes button dynamically above the Save button

            // Assuming you have a LinearLayout with id button_container that wraps your buttons
            val discardButton = createDiscardChangesButton(this) {
                // Reload previously saved recipe data to discard changes
                recipeId?.let { id ->
                    viewModel.readRecipeById(id)
                    android.widget.Toast.makeText(this, "Changes discarded", android.widget.Toast.LENGTH_SHORT).show()
                }
            }

            binding.buttonContainer.addView(discardButton, 0)


        } else {
            binding.textViewEditRecipe.text = getString(R.string.create_recipe)
        }

        // Setup category spinner
        val categories = RecipeCategory.entries
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Observe and populate fields
        viewModel.title.observe(this) {
            binding.editTextRecipeTitle.setText(it)
        }

        viewModel.category.observe(this) { selectedCategory ->
            val index = categories.indexOf(selectedCategory)
            if (index >= 0) binding.spinnerCategory.setSelection(index)
        }

        viewModel.ingredients.observe(this) { ingredients ->
            updateListViews(binding.linearLayoutIngredientsData, ingredients, ::createItemView) { ingredient ->
                viewModel.removeIngredient(ingredient)
            }
        }

        viewModel.instructions.observe(this) { instructions ->
            updateListViews(binding.linearLayoutInstructionsDataContainer, instructions, ::createItemView) { instruction ->
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

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
