package com.test.aktivocoresdksample_android.ui.personalise

import android.os.Bundle
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityPersonaliseBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter

class PersonaliseActivity : BaseActivity() {
    private lateinit var binding: ActivityPersonaliseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonaliseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Personalize"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getPersonaliseActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.PERSONALIZE -> {
                            callPersonalizeAPI()
                        }
                    }
                }

            })
        }
    }

    private fun callPersonalizeAPI() {}

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}