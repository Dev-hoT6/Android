package com.strayalpaca.hot6.screen.home.recycler.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.databinding.ItemProductBinding
import com.strayalpaca.hot6.domain.product.ProductItem

class ProductItemAdapter : RecyclerView.Adapter<ProductItemViewHolder>() {

    private val productItemList = mutableListOf<ProductItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductItemViewHolder(binding)
    }

    override fun getItemCount(): Int = productItemList.size

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bind(productItemList[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(newList : List<ProductItem>) {
        productItemList.clear()
        productItemList.addAll(newList)
        notifyDataSetChanged()
    }
}