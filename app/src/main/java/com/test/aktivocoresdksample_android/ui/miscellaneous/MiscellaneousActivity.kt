package com.test.aktivocoresdksample_android.ui.miscellaneous

import android.os.Bundle
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityMiscellaneousBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter

class MiscellaneousActivity : BaseActivity() {
    private lateinit var binding: ActivityMiscellaneousBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiscellaneousBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Profile"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mutableListOf(), object : ActionListener {
                override fun onActionClick(action: Action) {

                }

            })
        }
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}