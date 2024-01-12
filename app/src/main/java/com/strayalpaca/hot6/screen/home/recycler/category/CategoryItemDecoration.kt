package com.strayalpaca.hot6.screen.home.recycler.category

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.utils.dpToPx

class CategoryItemDecoration(context : Context) : RecyclerView.ItemDecoration() {
    private val horizontalPadding = dpToPx(context, 8)
    private val endPointPadding = dpToPx(context, 16)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        outRect.left = if (position == 0) {
            endPointPadding
        } else {
            horizontalPadding
        }

        if (position == state.itemCount - 1) {
            outRect.right = endPointPadding
        }
    }
}