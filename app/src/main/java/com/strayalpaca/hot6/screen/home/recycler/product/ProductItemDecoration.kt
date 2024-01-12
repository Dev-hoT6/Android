package com.strayalpaca.hot6.screen.home.recycler.product

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.utils.dpToPx

class ProductItemDecoration(context : Context) : RecyclerView.ItemDecoration() {
    private val bottomPadding = dpToPx(context, 24)
    private val horizontalPadding = dpToPx(context, 4)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = horizontalPadding
        outRect.right = horizontalPadding
        outRect.bottom = bottomPadding
    }
}