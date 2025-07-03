package uk.ac.bbk.dcs.mobile_computing_project_rroman08.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import uk.ac.bbk.dcs.mobile_computing_project_rroman08.R

/**
 * Creates a horizontally arranged item view containing a label and a delete button (bin can).
 *
 * @param text The text to display in the item.
 * @param onDelete Callback to invoke when the delete button is pressed.
 * @return A [LinearLayout] containing the item view.
 */
fun Context.createItemView(text: String, onDelete: () -> Unit): LinearLayout {
    return LinearLayout(this).apply {
        // Set layout to horizontal and center its children vertically
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        // Define layout params with vertical margins
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 4.dp, 0, 4.dp)
        }

        val textView = TextView(context).apply {
            this.text = text
            // Use weight to fill remaining space after delete button
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
            }
            textSize = 16f
        }

        // Create the delete (bin can) button
        val deleteButton = ImageButton(context).apply {
            setImageResource(R.drawable.ic_bin_grey)
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = 8.dp
            }
            // Call the onDelete callback when pressed
            setOnClickListener { onDelete() }
        }

        // Add both views to the parent layout
        addView(textView)
        addView(deleteButton)
    }
}

/**
 * Displays an AlertDialog with a text input field. It is used to prompt the user for a
 * single-line input for an ingredient or an instruction.
 *
 * @param title The dialog title.
 * @param onInputConfirmed Callback invoked when the user confirms input with non-empty text.
 */
fun Context.showInputDialog(title: String, onInputConfirmed: (String) -> Unit) {
    // Input field for user to type
    val editText = EditText(this)

    // Build and show the dialog
    AlertDialog.Builder(this)
        .setTitle(title)
        .setView(editText)
        .setPositiveButton("add") { dialog, _ ->
            // Extract and trim input text
            val input = editText.text.toString().trim()
            if (input.isNotEmpty()) {
                onInputConfirmed(input)  // pass to callback
            }
            dialog.dismiss()
        }
        .setNegativeButton("cancel") { dialog, _ -> dialog.dismiss() }
        .show()
}

/**
 * Populates a vertical [LinearLayout] container with a numbered list of strings.
 *
 * Each item is shown as a separate [TextView].
 *
 * @param container The layout container to populate.
 * @param list The list of items to display.
 */
fun Context.populateList(container: LinearLayout, list: List<String>) {
    container.removeAllViews()
    list.forEachIndexed { index, item ->
        val textView = TextView(this).apply {
            text = "${index + 1}. $item"  // adding an entry number to each item using idx
            textSize = 16f
            setTextColor(getColor(R.color.dark_grey))
            setPadding(0, 4, 0, 4)
        }
        container.addView(textView)
    }
}

/**
 * Updates a [LinearLayout] container with interactive views for each item.
 *
 * @param container The layout container to update.
 * @param items The list of items to represent.
 * @param createItemView A function to create a view for a given item and delete callback.
 * @param onDelete A callback to handle item deletion.
 */
fun Context.updateListViews(
    container: LinearLayout,
    items: List<String>,
    createItemView: (String, () -> Unit) -> android.view.View,
    onDelete: (String) -> Unit
) {
    container.removeAllViews()
    // Generate a view for each item using the created factory method and add the new view to the
    // layout
    items.forEach { item ->
        val view = createItemView(item) { onDelete(item) }
        container.addView(view)
    }
}