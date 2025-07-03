package uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.edit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.R
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeDatabase
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityEditRecipeBinding
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.ui.main.MainActivity
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.createItemView
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.updateListViews
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils.showInputDialog

/**
 * Activity for creating or editing a recipe, was designed with dual-use, hence some additional logic.
 * Handles UI including title, category spinner, ingredient/instruction lists, and save/discard
 * changes logic.
 */
class EditRecipeActivity : AppCompatActivity() {

    private val viewModel: EditRecipeViewModel by viewModels()
    private lateinit var binding: ActivityEditRecipeBinding
    private var recipeId: Long? = null // null = create, not null = edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inject DAO into ViewModel
        val dao = RecipeDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipeDao = dao

        // Check if editing existing recipe, i.e., an ID was passed along
        recipeId = intent.getLongExtra("RECIPE_ID", -1L).takeIf { it != -1L }
        recipeId?.let { id ->
            viewModel.readRecipeById(id)
        }

        // Create discard button variable to keep button 'switched on or off' depending on 'mode'
        val discardButton = binding.buttonDiscardChanges

        if (recipeId != null) {
            // If there is an ID, Activity is in 'edit mode'
            binding.textViewEditRecipe.text = getString(R.string.edit_recipe)
            binding.saveButton.text = getString(R.string.save_changes)
            discardButton.visibility = View.VISIBLE  // keep button 'on' UI
            discardButton.setOnClickListener {
                recipeId?.let { id ->
                    viewModel.readRecipeById(id)
                    Toast.makeText(this, "Changes discarded", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            // In 'create mode'
            binding.textViewEditRecipe.text = getString(R.string.create_recipe)
            binding.saveButton.text = getString(R.string.save_recipe)
            discardButton.visibility = View.GONE  // remove button from UI
        }

        // Setup category spinner drop-down
        val categories = RecipeCategory.entries
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Observe ViewModel fields and sync UI: title and category spinner
        viewModel.title.observe(this) {
            binding.editTextRecipeTitle.setText(it)
        }

        viewModel.category.observe(this) { selectedCategory ->
            val index = categories.indexOf(selectedCategory)
            if (index >= 0) binding.spinnerCategory.setSelection(index)
        }

        // Observe changes to the list of ingredients and instructions stored in the ViewModel's
        // LiveData
        viewModel.ingredients.observe(this) { ingredients ->
            // Whenever the ingredient list updates, call updateListViews to rebuild the UI
            updateListViews(
                binding.linearLayoutIngredientsData,  // the 'container' layout where items will be added
                ingredients,  // current list of items
                ::createItemView  // fn reference that creates the view for each item
                // Remove item when 'trash' icon is clicked
            ) { ingredient -> viewModel.removeIngredient(ingredient) }
        }

        viewModel.instructions.observe(this) { instructions ->
            updateListViews(
                binding.linearLayoutInstructionsDataContainer,
                instructions,
                ::createItemView
            ) { instruction -> viewModel.removeInstruction(instruction) }
        }

        // Add button to launch input dialog for ingredients
        binding.buttonAddIngredients.setOnClickListener {
            showInputDialog("Add Ingredient") { viewModel.addIngredient(it) }
        }

        // Add button to launch input dialog for instructions
        binding.buttonAddInstructions.setOnClickListener {
            showInputDialog("Add Instruction") { viewModel.addInstruction(it) }
        }

        // Save or update recipe in room db with whatever is currently bound to UI
        // when button is clicked
        binding.saveButton.setOnClickListener {
            val title = binding.editTextRecipeTitle.text.toString()
            val category = binding.spinnerCategory.selectedItem as RecipeCategory

            viewModel.title.value = title
            viewModel.category.value = category

            // Either update existing recipe or save new recipe
            if (recipeId != null) {
                viewModel.updateRecipe(recipeId!!)
            } else {
                viewModel.saveRecipe()
            }

            // After saving the recipe, app navigates back to Main Activity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
