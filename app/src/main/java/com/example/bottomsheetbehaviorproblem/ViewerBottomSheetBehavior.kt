package com.example.bottomsheetbehaviorproblem

import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ViewerBottomSheetBehavior<V: View> (bottomInsetHeightPixels: Int, menuItemHeightPixels: Int):
    BottomSheetBehavior<V>() {

    init {
        // As we are using Material Design 2, we need to turn off fit-to-contents feature manually here.
        isFitToContents = false

        // Determines the top offset of the BottomSheet in the STATE_EXPANDED state when fitsToContent is false.
        // The default value is 0, which results in the sheet matching the parent's top.
        expandedOffset = 0

        peekHeight = menuItemHeightPixels * 4 + bottomInsetHeightPixels
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        return false
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        return false
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return false
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        type: Int
    ) {
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }



}