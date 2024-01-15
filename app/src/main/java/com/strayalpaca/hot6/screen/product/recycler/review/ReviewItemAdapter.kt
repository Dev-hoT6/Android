package com.strayalpaca.hot6.screen.product.recycler.review

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.databinding.ItemReviewBinding
import com.strayalpaca.hot6.domain.review.Review
import kotlin.math.min

class ReviewItemAdapter : RecyclerView.Adapter<ReviewItemViewHolder>() {
    private val reviewList = mutableListOf<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ReviewItemViewHolder(binding)
    }

    // 한 화면에 최대 리뷰는 5개까지만 표시
    override fun getItemCount(): Int = min(5, reviewList.size)

    override fun onBindViewHolder(holder: ReviewItemViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setReview(reviewList : List<Review>) {
        this.reviewList.clear()
        this.reviewList.addAll(reviewList)
        notifyDataSetChanged()
    }
}