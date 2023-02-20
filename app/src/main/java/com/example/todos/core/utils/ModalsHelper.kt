package com.example.todos.core.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ModalsHelper {
    companion object {
        fun showTodoDeletionModal(
            context: Context,
            onDeletionConfirm: () -> Unit
        ) {
            MaterialAlertDialogBuilder(context)
                .setTitle("Delete TODO")
                .setMessage("Are you sure you want to delete this TODO?")
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("YES") { dialog, _ ->
                    onDeletionConfirm()
                    dialog.dismiss()
                }
                .show()
        }

        fun showCategoryDeletionModal(
            context: Context,
            onDeletionConfirm: () -> Unit
        ) {
            MaterialAlertDialogBuilder(context)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("YES") { dialog, _ ->
                    onDeletionConfirm()
                    dialog.dismiss()
                }
                .show()
        }
    }
}