package uk.ac.bbk.dcs.mobile_computing_project_rroman08

import androidx.activity.viewModels
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.data.local.RecipeCategory
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.databinding.ActivityCreateRecipeBinding

class CreateRecipeActivity : AppCompatActivity() {

    private val viewModel: CreateRecipeViewModel by viewModels()
    private lateinit var binding: ActivityCreateRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Populate the spinner with enum values
        val categories = RecipeCategory.entries
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Observe ingredients list
        viewModel.ingredients.observe(this) { ingredients ->
            updateIngredientViews(ingredients)
        }

        // Observe instructions list
        viewModel.instructions.observe(this) { instructions ->
            updateInstructionViews(instructions)
        }

        binding.buttonAddIngredients.setOnClickListener {
            showInputDialog("Add Ingredient") { input ->
                viewModel.addIngredient(input)
            }
        }

        binding.buttonAddInstructions.setOnClickListener {
            showInputDialog("Add Instruction") { input ->
                viewModel.addInstruction(input)
            }
        }
    }

    private fun updateIngredientViews(ingredients: List<String>) {
        val container = binding.linearLayoutIngredientsData
        container.removeAllViews()

        ingredients.forEach { ingredient ->
            val view = createItemView(ingredient) {
                viewModel.removeIngredient(ingredient)
            }
            container.addView(view)
        }
    }

    private fun updateInstructionViews(instructions: List<String>) {
        val container = binding.linearLayoutInstructionsDataContainer
        container.removeAllViews()

        instructions.forEach { instruction ->
            val view = createItemView(instruction) {
                viewModel.removeInstruction(instruction)
            }
            container.addView(view)
        }
    }

    // Create a horizontal layout with text + delete button
    private fun createItemView(text: String, onDelete: () -> Unit): LinearLayout {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 8, 0, 8)
            layoutParams = params
        }

        val textView = TextView(this).apply {
            this.text = text
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            textSize = 16f
        }

        val deleteButton = ImageButton(this).apply {
            setImageResource(R.drawable.ic_bin_grey)
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener { onDelete() }
        }

        layout.addView(textView)
        layout.addView(deleteButton)

        return layout
    }

    // Input dialog for adding ingredients or instructions
    private fun showInputDialog(title: String, onInputConfirmed: (String) -> Unit) {
        val editText = EditText(this)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("Add") { dialog, _ ->
                val input = editText.text.toString().trim()
                if (input.isNotEmpty()) {
                    onInputConfirmed(input)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}