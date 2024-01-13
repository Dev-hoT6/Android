package com.strayalpaca.hot6.base.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.strayalpaca.hot6.databinding.DialogOneButtonBinding

class OneButtonDialog(
    private val title : String,
    private val caption : String,
    private val buttonText : String,
    private val buttonClick : () -> Unit
) : DialogFragment() {
    private lateinit var binding : DialogOneButtonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogOneButtonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
        initText()
    }

    private fun initButton() {
        binding.btnConfirmation.setOnClickListener {
            dismiss()
            buttonClick()
        }
    }

    private fun initText() {
        binding.tvTitle.text = title
        binding.tvCaption.text = caption
        binding.btnConfirmation.text = buttonText
    }
}