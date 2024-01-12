package com.strayalpaca.hot6.screen.home.recycler.category

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.strayalpaca.hot6.R
import com.strayalpaca.hot6.databinding.ItemCategoryBinding
import com.strayalpaca.hot6.domain.product.Category

class CategoryItemAdapter(
    private val clickItem : (String) -> Unit
) : RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {

    private val categoryList = mutableListOf<Category>()
    private var selectedPosition : Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryItemViewHolder(binding)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(category = categoryList[position], position = position)
    }

    fun setCategory(categoryId : String?) {
        val prevSelectedPosition = selectedPosition
        selectedPosition = categoryList.indexOfFirst { it.id == categoryId }
        if (selectedPosition == -1) { // indexOf 에서 element를 찾지 못하면 -1을 리턴하는데 이를 null로 변경
            selectedPosition = null
        }

        prevSelectedPosition?.let { notifyItemChanged(it) }
        selectedPosition?.let { notifyItemChanged(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(categoryList : List<Category>) {
        this.categoryList.clear()
        this.categoryList.addAll(categoryList)
        notifyDataSetChanged()
    }

    inner class CategoryItemViewHolder(private val binding : ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var category : Category

        init {
            binding.root.setOnClickListener {
                clickItem(category.id)
            }
        }

        fun bind(category : Category, position : Int) {
            this.category = category
            binding.tvCategory.text = category.name

            binding.tvCategory.background = getBackground(selectedPosition == position)
            binding.tvCategory.setTextColor(getTextColor(selectedPosition == position))
        }

        private fun getBackground(selected : Boolean) : Drawable? {
            return if (selected) {
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.background_chip_clicked
                )
            } else {
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.background_chip_normal
                )
            }
        }

        private fun getTextColor(selected: Boolean) : Int {
            return if (selected) {
                ContextCompat.getColor(binding.root.context, R.color.white)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.black)
            }
        }
    }
}