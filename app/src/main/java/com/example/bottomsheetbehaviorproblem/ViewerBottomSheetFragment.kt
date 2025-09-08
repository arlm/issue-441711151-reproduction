package com.example.bottomsheetbehaviorproblem

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ViewerBottomSheetFragment() : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val contentView = LayoutInflater.from (context).inflate (R.layout.viewer_bottom_sheet, null)
        dialog.setContentView (contentView)

        val layoutParams = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams?
        val menuHeightPixels = resources.getDimensionPixelSize(R.dimen.menu_item_height)

        contentView.post {
            val insets = dialog.window?.decorView?.rootWindowInsets

            if (insets == null)
            {
                return@post
            }

            val systemBars = insets.getInsets (WindowInsetsCompat.Type.systemBars ())
            val sheetContainer = (contentView.parent as View?) ?: contentView

            // As the BottomSheet does not reach the top of the screen we must take into account
            // only the bottom insets by adding the value as bottom padding.
            sheetContainer.setPadding(0, 0, 0, systemBars.bottom)

            val behaviour = ViewerBottomSheetBehavior<LinearLayout> (systemBars.bottom, menuHeightPixels)
            layoutParams?.behavior = behaviour
        }
    }
}