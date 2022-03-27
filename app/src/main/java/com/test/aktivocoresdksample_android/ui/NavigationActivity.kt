package com.test.aktivocoresdksample_android.ui

import android.content.Intent
import android.os.Bundle
import com.test.aktivocoresdksample_android.MainActivity
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.databinding.ActivityNavigationBinding
import com.test.aktivocoresdksample_android.ui.dashboard.DashboardActivity

class NavigationActivity : BaseActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    override fun showText(text: String) {

    }

    fun init() {
        binding.toolbar.title = "Navigation"

        binding.javaBtn.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

        binding.kotlinBtn.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

}