package com.strayalpaca.hot6.screen.product.recycler.hashtag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.databinding.ItemHashtagBinding

class HashtagItemAdapter : RecyclerView.Adapter<HashtagItemViewHolder>() {
    private val hashtagList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashtagItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHashtagBinding.inflate(inflater, parent, false)
        return HashtagItemViewHolder(binding)
    }

    override fun getItemCount(): Int = hashtagList.size

    override fun onBindViewHolder(holder: HashtagItemViewHolder, position: Int) {
        holder.bind(hashtagList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setHashtag(hashtagList : List<String>) {
        this.hashtagList.clear()
        this.hashtagList.addAll(hashtagList)
        notifyDataSetChanged()
    }
}