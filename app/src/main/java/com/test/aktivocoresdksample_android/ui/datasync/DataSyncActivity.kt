package com.test.aktivocoresdksample_android.ui.datasync

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityDataSyncBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.supplementaryData.SyncSupplementaryFitnessDataActivity
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class DataSyncActivity : BaseActivity() {
    companion object {
        val TAG = DataSyncActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityDataSyncBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataSyncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Data Sync"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getDataSyncActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.SYNC_FITNESS_DATA -> {
                            syncFitnessData()
                        }
                        ActionEnum.SYNC_SUPPLEMENTARY_FITNESS_DATA -> {
                            //syncSupplementaryFitnessData();

                            //syncSupplementaryFitnessData();
                            startActivity(
                                Intent(
                                    this@DataSyncActivity,
                                    SyncSupplementaryFitnessDataActivity::class.java
                                )
                            )
                        }
                        ActionEnum.CLEAR_DATABASE -> {
//                            aktivoManager.ClearDatabaseTask().execute()
                        }
                    }
                }

            })
        }
    }

    private fun syncFitnessData() {
        compositeDisposable.add(
            aktivoManager
                .syncFitnessData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        val message = "Data synced"
                        Log.e(TAG, message)
                        Toast.makeText(this@DataSyncActivity, message, Toast.LENGTH_LONG)
                            .show()
                        showText(message)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        val message = "Data Sync error: " + e.message
                        Log.e(TAG, message)
                        Toast.makeText(
                            this@DataSyncActivity, message, Toast.LENGTH_LONG
                        ).show()
                        showText(message)
                    }
                })
        )
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}