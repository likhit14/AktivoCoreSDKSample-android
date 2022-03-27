package com.test.aktivocoresdksample_android.ui.profile

import android.os.Bundle
import android.util.Log
import com.aktivolabs.aktivocore.data.models.userprofile.UserProfile
import com.aktivolabs.aktivocore.data.models.userprofile.UserProfile.Gender
import com.aktivolabs.aktivocore.data.models.userprofile.height.Height
import com.aktivolabs.aktivocore.data.models.userprofile.height.HeightCm
import com.aktivolabs.aktivocore.data.models.userprofile.height.HeightFtIn
import com.aktivolabs.aktivocore.data.models.userprofile.weight.Weight
import com.aktivolabs.aktivocore.data.models.userprofile.weight.WeightKg
import com.aktivolabs.aktivocore.data.models.userprofile.weight.WeightLbs
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityProfileBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class ProfileActivity : BaseActivity() {

    companion object {
        val TAG = ProfileActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Profile"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getProfileActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.USER_PROFILE -> {
                            getUserProfile()
                        }
                        ActionEnum.UPDATE_USER_PROFILE -> {
                            updateUserProfile()
                        }
                        else -> {}
                    }
                }

            })
        }
    }

    private fun updateUserProfile() {
        val dateOfBirth = LocalDate.parse("1982/11/13", DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val bedTime = LocalTime.parse("05:00")
        val wakeTime = LocalTime.parse("10:00")
        val gender = Gender.Male
        val heightCms = Height(HeightCm(250))
        val heightFtInch = Height(HeightFtIn(6, 2))
        val weightKgs = Weight(WeightKg(1451))
        val weightLbs = Weight(WeightLbs(1451))
        val userProfile =
            UserProfile(dateOfBirth, gender, bedTime, wakeTime, heightFtInch, weightLbs)
        aktivoManager.updateUserProfile(userProfile).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Boolean> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(aBoolean: Boolean) {
                    val message = "response for update profile: $aBoolean"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "response for update profile: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getUserProfile() {
        aktivoManager.userProfile.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<UserProfile> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(userProfile: UserProfile) {
                    val message =
                        "successfully get the user profile, dob: " + userProfile.dateOfBirth +
                                " gender: " + userProfile.gender +
                                " bedTime: " + userProfile.bedTime +
                                " wakeTime: " + userProfile.wakeTime +
                                " heightUnit: " + userProfile.height!!.heightUnit +
                                " weightUnit: " + userProfile.weight!!.weightUnit +
                                " heightFt: " + userProfile.height!!.heightFtIn.heightInFt +
                                " heightIn: " + userProfile.height!!.heightFtIn.heightInInch +
                                " heightCms: " + userProfile.height!!.heightCm.heightInCm +
                                " weightUnit: " + userProfile.weight!!.weightUnit +
                                " weightKg: " + userProfile.weight!!.weightKg.weightInKg +
                                " weightLbs: " + userProfile.weight!!.weightLbs.weightInLbs
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {}
            })
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }

}