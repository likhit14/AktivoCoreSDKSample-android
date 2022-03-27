package com.test.aktivocoresdksample_android.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityDashboardBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.ui.authentication.AuthenticationActivity
import com.test.aktivocoresdksample_android.ui.badges.BadgeActivity
import com.test.aktivocoresdksample_android.ui.challenges.ChallengeActivity
import com.test.aktivocoresdksample_android.ui.connecteddevices.ConnectedDevice
import com.test.aktivocoresdksample_android.ui.datasync.DataSyncActivity
import com.test.aktivocoresdksample_android.ui.miscellaneous.MiscellaneousActivity
import com.test.aktivocoresdksample_android.ui.notification.NotificationActivity
import com.test.aktivocoresdksample_android.ui.personalise.PersonaliseActivity
import com.test.aktivocoresdksample_android.ui.physicallifestyle.PhysicalLifestyleActivity
import com.test.aktivocoresdksample_android.ui.profile.ProfileActivity
import com.test.aktivocoresdksample_android.ui.risegame.RiseGameActivity
import com.test.aktivocoresdksample_android.ui.social.SocialActivity
import com.test.aktivocoresdksample_android.utils.ACTIVITY_RECOGNITION_REQUEST_CODE
import com.test.aktivocoresdksample_android.utils.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE
import com.test.aktivocoresdksample_android.utils.REQUEST_PERMISSION_READ_STORAGE
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter

class DashboardActivity : BaseActivity() {

    companion object {
        val TAG = DashboardActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Dashboard"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getFeature(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.FEATURE_AUTHENTICATION -> {
                            launchActivity(AuthenticationActivity())
                        }
                        ActionEnum.FEATURE_BADGES -> {
                            launchActivity(BadgeActivity())
                        }
                        ActionEnum.FEATURE_CHALLENGE -> {
                            launchActivity(ChallengeActivity())
                        }
                        ActionEnum.FEATURE_CONNECTED_DEVICE -> {
                            launchActivity(ConnectedDevice())
                        }
                        ActionEnum.FEATURE_DATA_SYNC -> {
                            launchActivity(DataSyncActivity())
                        }
                        ActionEnum.FEATURE_MISCELLANEOUS -> {
                            launchActivity(MiscellaneousActivity())
                        }
                        ActionEnum.FEATURE_NOTIFICATION -> {
                            launchActivity(NotificationActivity())
                        }
                        ActionEnum.FEATURE_PERSONALISE -> {
                            launchActivity(PersonaliseActivity())
                        }
                        ActionEnum.FEATURE_PHYSICAL_LIFESTYLE -> {
                            launchActivity(PhysicalLifestyleActivity())
                        }
                        ActionEnum.FEATURE_PROFILE -> {
                            launchActivity(ProfileActivity())
                        }
                        ActionEnum.FEATURE_RISE_GAME -> {
                            launchActivity(RiseGameActivity())
                        }
                        ActionEnum.FEATURE_SOCIAL -> {
                            launchActivity(SocialActivity())
                        }
                    }
                }

            })
        }

        // Permission Check
        checkStoragePermission()
    }

    private fun launchActivity(activity: BaseActivity) {
        startActivity(Intent(this, activity::class.java))
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (isPermissionNotGranted(permissions)) {
                requestPermissions(permissions, REQUEST_PERMISSION_READ_STORAGE)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun isPermissionNotGranted(permissions: Array<String>): Boolean {
        var flag = false
        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                flag = true
                break
            }
        }
        return flag
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_READ_STORAGE) {
            val message = "read & write permission granted"
            Log.e(TAG, message)
            showText(message)
        }
        if (requestCode == ACTIVITY_RECOGNITION_REQUEST_CODE) {
            val message = "activity recognition permission granted"
            Log.e(TAG, message)
            showText(message)
        }
        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            val message = "google fit permission permission granted"
            Log.e(TAG, message)
            showText(message)
        }
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}