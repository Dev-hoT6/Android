package com.strayalpaca.hot6.screen.review

import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.strayalpaca.hot6.R
import com.strayalpaca.hot6.ai.ImageHandler
import com.strayalpaca.hot6.base.ViewBindingActivity
import com.strayalpaca.hot6.base.dialog.OneButtonDialog
import com.strayalpaca.hot6.databinding.ActivityReviewBinding
import com.strayalpaca.hot6.ai.classifier.ImageCategoryClassifier
import com.strayalpaca.hot6.ai.classifier.Yolov8Classifier
import com.strayalpaca.hot6.ai.review.FullyConnectedReviewClassifier
import com.strayalpaca.hot6.base.retrofit.RetrofitClient
import com.strayalpaca.hot6.data.review.RemoteReviewRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewActivity : ViewBindingActivity<ActivityReviewBinding>(ActivityReviewBinding::inflate) {

    private val viewModel by viewModels<ReviewViewModel> {
        ReviewViewModel.Companion.Factory(
            RemoteReviewRepository(RetrofitClient.getInstance()),
            Yolov8Classifier(baseContext),
            FullyConnectedReviewClassifier(baseContext),
            ImageHandler(baseContext)
        )
    }
    private val maxReviewCount = 120
    private lateinit var imageCategoryClassifier : ImageCategoryClassifier

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
        viewModel.setImage(uri?.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productId = intent.getStringExtra("product_id")
        val categoryIdList = intent.getStringArrayListExtra("category_id_list")

        if (productId == null || categoryIdList == null) {
            handleInvalidIntentExtra()
            return
        }
        viewModel.setProductIdAndCategoryId(productId, categoryIdList)

        initText()
        initButton()
        initEditText()
        initObserver()

        imageCategoryClassifier = Yolov8Classifier(baseContext)
    }

    override fun onDestroy() {
        super.onDestroy()

        imageCategoryClassifier.close()
    }

    private fun handleInvalidIntentExtra() {
        finish()
    }

    private fun initText() {
        binding.tvCaptionInputReview.text = getString(R.string.form_text_max_thres, maxReviewCount)
    }

    private fun initButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnWriteReview.setOnClickListener {
            viewModel.uploadReview()
        }

        binding.btnAddPhoto.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.btnRemovePhoto.setOnClickListener {
            viewModel.setImage(null)
        }
    }

    private fun initEditText() {
        binding.editReview.filters = arrayOf(InputFilter.LengthFilter(maxReviewCount))

        binding.editReview.addTextChangedListener {
            viewModel.setReviewText(it.toString())
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.reviewText.collectLatest { reviewText ->
                        binding.tvTextCount.text = getString(R.string.form_text_count, reviewText.length, maxReviewCount)

                        if (reviewText == binding.editReview.text.toString()) return@collectLatest
                        binding.editReview.setText(reviewText)
                    }
                }

                launch {
                    viewModel.imageUrl.collectLatest { imageUrl ->
                        toggleImageContainerAndImageAdder(showContainer = imageUrl != null)
                        imageUrl?.let { url ->
                            Glide.with(baseContext).load(url).into(binding.imgPhoto)
                        }
                    }
                }

                launch {
                    viewModel.reviewState.collectLatest { state ->
                        applyReviewState(state)
                    }
                }

                launch {
                    viewModel.showEmptyTextMessage.collectLatest { trigger ->
                        if (trigger)
                            Toast.makeText(baseContext, getString(R.string.error_empty_review), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun toggleImageContainerAndImageAdder(showContainer : Boolean) {
        binding.layoutPhoto.isVisible = showContainer
        binding.btnAddPhoto.isVisible = !showContainer
    }

    private fun applyReviewState(state : ReviewState) {
        when (state) {
            ReviewState.IDLE -> {
                setLoading(show = false)
                setUiInteractionEnabled(enabled = true)
            }
            ReviewState.Error -> {
                setLoading(show = false)
                Toast.makeText(baseContext, getString(R.string.error_request_review), Toast.LENGTH_SHORT).show()
                setUiInteractionEnabled(enabled = true)
            }
            ReviewState.Loading -> {
                setLoading(show = true)
                setUiInteractionEnabled(enabled = false)
            }
            ReviewState.Reject -> {
                setLoading(show = false)
                setUiInteractionEnabled(enabled = true)
                callReviewRejectDialog()
            }
            ReviewState.UploadSuccess -> {
                setLoading(show = false)
                Toast.makeText(baseContext, getString(R.string.success_request_review), Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
            ReviewState.ModelLoading -> {
                setLoading(show = true)
                setUiInteractionEnabled(enabled = false)
            }
            ReviewState.ImageReject -> {
                setLoading(show = false)
                setUiInteractionEnabled(enabled = true)
                callImageRejectDialog()
            }
        }
    }

    private fun setLoading(show : Boolean) {
        binding.viewLoading.root.isVisible = show
    }

    private fun setUiInteractionEnabled(enabled : Boolean) {
        binding.btnBack.isEnabled = enabled
        binding.btnWriteReview.isEnabled = enabled
        binding.btnAddPhoto.isEnabled = enabled
        binding.btnRemovePhoto.isEnabled = enabled
        binding.editReview.isEnabled = enabled
    }

    private fun callReviewRejectDialog() {
        OneButtonDialog(
            title = getString(R.string.reject_request_review),
            caption = getString(R.string.caption_reject_request_review),
            buttonText = getString(R.string.confirmation),
            buttonClick = viewModel::closeRejectDialog
        ).show(supportFragmentManager, "review reject dialog")
    }

    private fun callImageRejectDialog() {
        OneButtonDialog(
            title = getString(R.string.reject_image),
            caption = getString(R.string.caption_reject_image),
            buttonText = getString(R.string.confirmation),
            buttonClick = viewModel::closeRejectDialog
        ).show(supportFragmentManager, "review reject dialog")
    }
}