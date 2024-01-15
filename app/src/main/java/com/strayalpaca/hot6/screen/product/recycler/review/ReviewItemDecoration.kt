package com.strayalpaca.hot6.screen.product.recycler.review

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.utils.dpToPx

class ReviewItemDecoration(context : Context) : RecyclerView.ItemDecoration() {
    private val horizontalPadding = dpToPx(context, 16)
    private val verticalPadding = dpToPx(context, 12)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalPadding
        outRect.bottom = verticalPadding
        outRect.right = horizontalPadding
        outRect.left = horizontalPadding
    }
}