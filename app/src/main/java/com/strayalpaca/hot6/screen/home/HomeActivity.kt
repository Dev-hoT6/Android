package com.strayalpaca.hot6.screen.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.strayalpaca.hot6.R
import com.strayalpaca.hot6.base.ViewBindingActivity
import com.strayalpaca.hot6.databinding.ActivityHomeBinding
import com.strayalpaca.hot6.screen.home.recycler.category.CategoryItemAdapter
import com.strayalpaca.hot6.screen.home.recycler.category.CategoryItemDecoration
import com.strayalpaca.hot6.screen.home.recycler.product.ProductItemAdapter
import com.strayalpaca.hot6.screen.home.recycler.product.ProductItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : ViewBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel> { HomeViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRecyclerView()
        initObserver()
    }

    private fun initRecyclerView() {
        binding.listProduct.layoutManager = GridLayoutManager(baseContext, 3, GridLayoutManager.VERTICAL, false)
        binding.listProduct.adapter = ProductItemAdapter()
        binding.listProduct.addItemDecoration(ProductItemDecoration(baseContext))

        binding.listCategory.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.listCategory.adapter = CategoryItemAdapter(viewModel::setCategory)
        binding.listCategory.addItemDecoration(CategoryItemDecoration(baseContext))
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.homeScreenState.collectLatest { state ->
                applyHomeScreenState(state)
            }
        }
    }

    private fun applyHomeScreenState(homeScreenState: HomeScreenState) {
        when (homeScreenState) {
            is HomeScreenState.Success -> {
                hideLoadingUi()
                hideErrorUi()
                applyHomeScreenData(homeScreenData = homeScreenState.data)
            }
            is HomeScreenState.Loading -> {
                hideErrorUi()
                showLoadingUi()
            }
            is HomeScreenState.Error -> {
                hideLoadingUi()
                showErrorUi()
            }
        }
    }

    private fun applyHomeScreenData(homeScreenData: HomeScreenData) {
        (binding.listProduct.adapter as ProductItemAdapter).setProductList(homeScreenData.productList)
        (binding.listCategory.adapter as CategoryItemAdapter).setCategoryList(homeScreenData.categories)
        (binding.listCategory.adapter as CategoryItemAdapter).setCategory(homeScreenData.selectedCategoryId)
        binding.tvProductCount.text = getString(R.string.form_total_count, homeScreenData.totalCount)
    }

    private fun showErrorUi() {

    }

    private fun hideErrorUi() {

    }

    private fun showLoadingUi() {

    }

    private fun hideLoadingUi() {

    }
}