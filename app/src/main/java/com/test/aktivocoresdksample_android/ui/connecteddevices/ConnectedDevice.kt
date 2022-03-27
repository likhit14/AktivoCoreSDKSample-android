package com.test.aktivocoresdksample_android.ui.connecteddevices

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.aktivolabs.aktivocore.data.models.User
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTracker
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTrackerAuthUrlInfo
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityConnectedDeviceBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ConnectedDevice : BaseActivity() {

    companion object {
        val TAG = ConnectedDevice.javaClass.simpleName
    }

    private lateinit var binding: ActivityConnectedDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectedDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Connected Devices"

        binding.actionLayout.actionBtnRV.apply {
            adapter =
                ActionAdapter(mappingUtils.getConnectedDevicesActions(), object : ActionListener {
                    override fun onActionClick(action: Action) {
                        when (action.actionEnum) {
                            ActionEnum.FITNESS_TRACKER -> {
                                getFitnessTrackers()
                            }
                            ActionEnum.AUTH_URL_FITBIT -> {
                                getFitnessPlatformAuthUrlForFitbit()
                            }
                            ActionEnum.AUTH_URL_GARMIN -> {
                                getFitnessPlatformAuthUrlForGarmin()
                            }
                            ActionEnum.DISCONNECT_FITBIT -> {
                                disconnectFitbit()
                            }
                            ActionEnum.DISCONNECT_GARMIN -> {
                                disconnectGarmin()
                            }
                            ActionEnum.INVALIDATE_USER -> {
                                invalidateUser()
                            }
                        }
                    }

                })
        }
    }

    private fun getFitnessTrackers() {
        aktivoManager.fitnessTrackers
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<FitnessTracker>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(fitnessTrackers: List<FitnessTracker>) {
                    var messageMain = " Result "
                    for (fitnessTracker in fitnessTrackers) {
                        val message =
                            "getFitnessTrackers, fitnessTracker: state: " + fitnessTracker.fitnessTrackerConnectionState +
                                    " platform: " + Objects.requireNonNull(fitnessTracker.platform)!!.name
                        Log.e(TAG, message)
                        messageMain = "$messageMain $message"
                    }
                    showText(messageMain)
                }

                override fun onError(e: Throwable) {
                    val message = "getFitnessTrackers, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getFitnessPlatformAuthUrlForFitbit() {
        aktivoManager.getFitnessPlatformAuthUrl(FitnessTracker.Platform.FITBIT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<FitnessTrackerAuthUrlInfo> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(fitnessTrackerAuthUrlInfo: FitnessTrackerAuthUrlInfo) {
                    val message =
                        "getFitnessPlatformAuthUrlForFitbit, fitnessTrackerAuthUrlInfo: " + fitnessTrackerAuthUrlInfo.authUrl
                    Log.e(TAG, message)
                    showText(message)
                    /*Intent i = new Intent(MainActivity.this, WebActivity.class);
                        i.putExtra("url", fitnessTrackerAuthUrlInfo.getAuthUrl());
                        startActivity(i);*/
                    val customTabIntent = CustomTabsIntent.Builder()
                    openCustomTab(
                        customTabIntent.build(),
                        Uri.parse(fitnessTrackerAuthUrlInfo.authUrl)
                    )
                }

                override fun onError(e: Throwable) {
                    val message = "getFitnessPlatformAuthUrlForFitbit, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun openCustomTab(customTabsIntent: CustomTabsIntent, uri: Uri) {
        customTabsIntent.intent.setPackage("com.android.chrome")
        customTabsIntent.launchUrl(this, uri)
    }

    private fun getFitnessPlatformAuthUrlForGarmin() {
        aktivoManager.getFitnessPlatformAuthUrl(FitnessTracker.Platform.GARMIN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<FitnessTrackerAuthUrlInfo> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(fitnessTrackerAuthUrlInfo: FitnessTrackerAuthUrlInfo) {
                    val message =
                        "getFitnessPlatformAuthUrlForGarmin, fitnessTrackerAuthUrlInfo: " + fitnessTrackerAuthUrlInfo.authUrl
                    Log.e(TAG, message)
                    showText(message)
                    /*Intent i = new Intent(MainActivity.this, WebActivity.class);
                        i.putExtra("url", fitnessTrackerAuthUrlInfo.getAuthUrl());
                        startActivity(i);*/
                    val customTabIntent = CustomTabsIntent.Builder()
                    openCustomTab(
                        customTabIntent.build(),
                        Uri.parse(fitnessTrackerAuthUrlInfo.authUrl)
                    )
                }

                override fun onError(e: Throwable) {
                    val message = "getFitnessPlatformAuthUrlForGarmin, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun disconnectFitbit() {
        aktivoManager.disconnectFitnessPlatform(FitnessTracker.Platform.FITBIT)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Boolean> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(aBoolean: Boolean) {
                    val message = "disconnectFitbit, state $aBoolean"
                    Log.e(TAG, message)
                    Toast.makeText(
                        this@ConnectedDevice, "Disconnected fitbit successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "disconnectFitbit, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun disconnectGarmin() {
        aktivoManager.disconnectFitnessPlatform(FitnessTracker.Platform.GARMIN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Boolean> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(aBoolean: Boolean) {
                    val message = "disconnectGarmin, state $aBoolean"
                    Log.e(TAG, message)
                    Toast.makeText(
                        this@ConnectedDevice, "Disconnected garmin successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "disconnectGarmin, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun invalidateUser() {
        aktivoManager
            .invalidateUser(User(localRepository.userId), this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "User invalidated"
                    Toast.makeText(this@ConnectedDevice, message, Toast.LENGTH_SHORT)
                        .show()
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }

}