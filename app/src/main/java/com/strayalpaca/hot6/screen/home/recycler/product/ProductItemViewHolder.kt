package com.strayalpaca.hot6.screen.home.recycler.product

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.strayalpaca.hot6.R
import com.strayalpaca.hot6.databinding.ItemProductBinding
import com.strayalpaca.hot6.domain.product.ProductItem
import com.strayalpaca.hot6.screen.product.ProductActivity

class ProductItemViewHolder(private val binding : ItemProductBinding) : ViewHolder(binding.root) {

    private lateinit var item : ProductItem

    init {
        binding.root.setOnClickListener {
            Intent(binding.root.context, ProductActivity::class.java).putExtra("product_id", item.id).run {
                binding.root.context.startActivity(this)
            }
        }
    }

    fun bind(item : ProductItem, position : Int = 0) {
        this.item = item
        binding.tvProductName.text = item.name
        binding.tvProductPrice.text = binding.root.context.getString(R.string.form_price, item.price)
        Glide.with(binding.root.context).load(item.imageUrl).into(binding.imgProduct)
        binding.tvRank.text = position.toString()
    }
}