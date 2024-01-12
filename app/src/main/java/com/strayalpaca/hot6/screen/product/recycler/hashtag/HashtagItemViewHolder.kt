package com.strayalpaca.hot6.screen.product.recycler.hashtag

import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.databinding.ItemHashtagBinding

class HashtagItemViewHolder(private val binding : ItemHashtagBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(hashtag: String) {
        binding.tvName.text = hashtag
    }
}