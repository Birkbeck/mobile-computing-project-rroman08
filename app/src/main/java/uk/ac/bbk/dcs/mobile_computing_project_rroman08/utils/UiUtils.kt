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

fun Context.createItemView(text: String, onDelete: () -> Unit): LinearLayout {
    return LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 4.dp, 0, 4.dp)
        }

        val textView = TextView(context).apply {
            this.text = text
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
            }
            textSize = 16f
        }

        val deleteButton = ImageButton(context).apply {
            setImageResource(R.drawable.ic_bin_grey)
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = 8.dp
            }
            setOnClickListener { onDelete() }
        }

        addView(textView)
        addView(deleteButton)
    }
}

fun Context.showInputDialog(title: String, onInputConfirmed: (String) -> Unit) {
    val editText = EditText(this)

    AlertDialog.Builder(this)
        .setTitle(title)
        .setView(editText)
        .setPositiveButton("add") { dialog, _ ->
            val input = editText.text.toString().trim()
            if (input.isNotEmpty()) {
                onInputConfirmed(input)
            }
            dialog.dismiss()
        }
        .setNegativeButton("cancel") { dialog, _ -> dialog.dismiss() }
        .show()
}

fun Context.populateList(container: LinearLayout, list: List<String>) {
    container.removeAllViews()
    list.forEachIndexed { index, item ->
        val textView = TextView(this).apply {
            text = "${index + 1}. $item"
            textSize = 16f
            setTextColor(getColor(R.color.dark_grey))
            setPadding(0, 4, 0, 4)
        }
        container.addView(textView)
    }
}