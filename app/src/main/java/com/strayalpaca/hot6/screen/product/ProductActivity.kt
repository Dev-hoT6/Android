package com.strayalpaca.hot6.screen.product

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.strayalpaca.hot6.R
import com.strayalpaca.hot6.base.ViewBindingActivity
import com.strayalpaca.hot6.databinding.ActivityProductBinding
import com.strayalpaca.hot6.screen.product.recycler.hashtag.HashtagItemAdapter
import com.strayalpaca.hot6.screen.product.recycler.hashtag.HashtagItemDecoration
import com.strayalpaca.hot6.screen.product.recycler.review.ReviewItemAdapter
import com.strayalpaca.hot6.screen.product.recycler.review.ReviewItemDecoration
import com.strayalpaca.hot6.screen.review.ReviewActivity
import com.strayalpaca.hot6.utils.joinToStringExceptSingle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList

class ProductActivity : ViewBindingActivity<ActivityProductBinding>(ActivityProductBinding::inflate) {

    private val viewModel by viewModels<ProductViewModel> { ProductViewModel.Factory }
    private val reviewScreenLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.loadReviewList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productId = intent.getStringExtra("product_id")

        if (productId == null) {
            moveBack()
            return
        }

        viewModel.setProductId(productId)
        viewModel.loadProductDetail()
        viewModel.loadReviewList()

        initRecyclerView()
        initButton()
        initObserver()
    }

    private fun moveBack() {
        finish()
    }
    private fun initRecyclerView() {
        binding.listHashtag.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        binding.listHashtag.adapter = HashtagItemAdapter()
        binding.listHashtag.addItemDecoration(HashtagItemDecoration(baseContext))

        binding.listReview.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        binding.listReview.adapter = ReviewItemAdapter()
        binding.listReview.addItemDecoration(ReviewItemDecoration(baseContext))
    }

    private fun initButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tvbtnWriteReview.setOnClickListener {
            Intent(this, ReviewActivity::class.java)
                .putExtra("product_id", viewModel.getProductId())
                .putStringArrayListExtra("category_id_list", ArrayList(viewModel.getCategoryIdList()))
                .run {
                    reviewScreenLauncher.launch(this)
                }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.productDetailState.collectLatest { productDetailState ->
                        applyProductDetailState(productDetailState)
                    }
                }

                launch {
                    viewModel.reviewListState.collectLatest { reviewListState ->
                        applyReviewListState(reviewListState)
                    }
                }
            }
        }
    }

    private fun applyReviewListState(state : ReviewListState) {
        when(state) {
            ReviewListState.Loading -> {
                setReviewListLoading(true)
                setReviewListError(false)
            }
            is ReviewListState.Success -> {
                setReviewListError(false)
                setReviewListLoading(false)
                applyReviewListData(state.data)
            }
            ReviewListState.Error -> {
                setReviewListLoading(false)
                setReviewListError(true)
            }
        }
    }

    private fun setReviewListLoading(show : Boolean) {
        binding.viewReviewLoading.root.isVisible = show
    }

    private fun setReviewListError(show : Boolean) {
        binding.viewReviewError.root.isVisible = show
    }

    private fun applyReviewListData(reviewListData: ReviewListData) {
        binding.tvReviewCount.text = getString(R.string.form_review_count, reviewListData.totalCount)
        (binding.listReview.adapter as ReviewItemAdapter).setReview(reviewListData.reviews)
    }

    private fun applyProductDetailState(state : ProductDetailState) {
        when (state) {
            ProductDetailState.Loading -> {
                setProductDetailLoading(true)
                setProductDetailError(false)
            }
            is ProductDetailState.Success -> {
                setProductDetailError(false)
                setProductDetailLoading(false)
                applyProductDetailData(state.data)
            }
            ProductDetailState.Error -> {
                setProductDetailLoading(false)
                setProductDetailError(true)
            }
        }
    }

    private fun setProductDetailLoading(show : Boolean) {
        binding.viewProductLoading.root.isVisible = show
    }

    private fun setProductDetailError(show : Boolean) {
        binding.viewProductError.root.isVisible = show
    }

    private fun applyProductDetailData(data : ProductDetailData) {
        binding.tvProductName.text = data.name
        binding.tvCategory.text = data.categories.map { it.name }.joinToStringExceptSingle(" > ")
        binding.tvProductPrice.text = getString(R.string.form_price, data.price)
        binding.tvDetailProduct.text = data.caption
        binding.tvCaptionProvide.text = getString(R.string.caption_provide)

        (binding.listHashtag.adapter as HashtagItemAdapter).setHashtag(data.hashtags)
        binding.listHashtag.isVisible = data.hashtags.isNotEmpty()
    }
}