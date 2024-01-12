package com.strayalpaca.hot6.screen.home

import android.os.Bundle
import com.strayalpaca.hot6.R
import com.strayalpaca.hot6.base.ViewBindingActivity
import com.strayalpaca.hot6.databinding.ActivityHomeBinding

class HomeActivity : ViewBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
    }
}