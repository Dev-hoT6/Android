package com.strayalpaca.hot6.screen.product.recycler.review

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.strayalpaca.hot6.R
import com.strayalpaca.hot6.databinding.ItemReviewBinding
import com.strayalpaca.hot6.domain.review.Review

class ReviewItemViewHolder(private val binding : ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(review: Review) {
        binding.tvName.text = review.writerName
        binding.tvReview.text = review.text

        binding.tvPoint.isVisible = review.point != null
        review.point?.let { binding.tvPoint.text = binding.root.context.getString(R.string.form_point, it) }

        binding.imgReview.isVisible = review.imageUrl != null
        review.imageUrl?.let { Glide.with(binding.root.context).load(it).into(binding.imgReview) }
    }
}