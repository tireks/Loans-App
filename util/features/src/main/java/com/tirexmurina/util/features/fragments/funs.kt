package com.tirexmurina.util.features.fragments

import android.app.AlertDialog
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast

fun getThemeColor(context: Context, attr: Int): Int {
    val typedValue = TypedValue()
    val theme = context.theme
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun showToast(context: Context, content : String) {
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
}

fun showDialog(
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    viewIdForInflater: Int,
    viewIdForText: Int,
    context: Context,
    positiveButtonName : String,
    negativeButtonName : String,
    dialogMessage: String
) {
    val dialogView = LayoutInflater.from(context).inflate(viewIdForInflater, null)
    val dialogTitle: TextView = dialogView.findViewById(viewIdForText)
    dialogTitle.text = dialogMessage
    val dialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .setPositiveButton(positiveButtonName) { dialog, _ ->
            onPositiveClick()
            dialog.dismiss()
        }
        .setNegativeButton(negativeButtonName) { dialog, _ ->
            onNegativeClick()
            dialog.dismiss()
        }
        .create()
    dialog.setOnShowListener {
        val titleColor = getThemeColor(context, android.R.attr.titleTextColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(titleColor)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(titleColor)
    }
    dialog.show()
}

fun showDialog(
    onPositiveClick: () -> Unit,
    viewIdForInflater: Int,
    viewIdForText: Int,
    context: Context,
    positiveButtonName : String,
    dialogMessage: String
) {
    val dialogView = LayoutInflater.from(context).inflate(viewIdForInflater, null)
    val dialogTitle: TextView = dialogView.findViewById(viewIdForText)
    dialogTitle.text = dialogMessage
    val dialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .setPositiveButton(positiveButtonName) { dialog, _ ->
            onPositiveClick()
            dialog.dismiss()
        }
        .create()
    dialog.setOnShowListener {
        val titleColor = getThemeColor(context, android.R.attr.titleTextColor)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(titleColor)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(titleColor)
    }
    dialog.show()
}