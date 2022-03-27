package com.test.aktivocoresdksample_android.ui.challenges

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.aktivolabs.aktivocore.data.models.challenge.Challenge
import com.aktivolabs.aktivocore.data.models.challenge.Enroll
import com.aktivolabs.aktivocore.data.models.queries.ChallengeEnrollQuery
import com.aktivolabs.aktivocore.data.models.queries.ChallengeListQuery
import com.aktivolabs.aktivocore.data.models.queries.ChallengeQuery
import com.aktivolabs.aktivocore.data.repositories.LocalRepository
import com.test.aktivocoresdksample_android.R
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityChallengeBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.ENROL_TO_CHALLENGE
import com.test.aktivocoresdksample_android.utils.GET_CHALLENGE
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChallengeActivity : BaseActivity() {

    companion object {
        val TAG = ChallengeActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityChallengeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Challenges"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getChallengesActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.ONGOING_CHALLENGES -> {
                            getOngoingChallenges()
                        }
                        ActionEnum.OVER_CHALLENGES -> {
                            getOverChallenges()
                        }
                        ActionEnum.CHALLENGE -> {
                            enterChallengeId(GET_CHALLENGE)
                        }
                        ActionEnum.ENROLL_CHALLENGE -> {
                            enterChallengeId(ENROL_TO_CHALLENGE)
                        }
                    }
                }

            })
        }
    }

    private fun getOngoingChallenges() {
        aktivoManager.getOngoingChallenges(ChallengeListQuery(localRepository.userId, true))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Challenge?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(challenges: List<Challenge?>) {
                    val message = "ongoing challenges fetched successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getOngoingChallenges error:"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getOverChallenges() {
        aktivoManager.getOverChallenges(ChallengeListQuery(localRepository.userId, true))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Challenge?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(challenges: List<Challenge?>) {
                    val message = "over challenges fetched successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getOverChallenges error:"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getChallenge(challengeId: String) {
        aktivoManager.getChallenge(ChallengeQuery(localRepository.userId, challengeId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Challenge> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(challenge: Challenge) {
                    val message = "getChallenge:$challenge"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getChallenge error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun enterChallengeId(type: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialog = builder.create()
        val dialogView: View = inflater.inflate(R.layout.layout_username_token, null)
        val userDetailsTV = dialogView.findViewById<TextView>(R.id.userDetailsTV)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)
        val userNameET = dialogView.findViewById<EditText>(R.id.userNameET)
        val tokenET = dialogView.findViewById<EditText>(R.id.accessTokenET)
        val selectorCB = dialogView.findViewById<CheckBox>(R.id.selectorCB)
        selectorCB.visibility = View.GONE
        userNameET.setHint(R.string.challenge_id)
        tokenET.visibility = View.GONE
        userDetailsTV.setText(R.string.challenge_details)
        submitBtn.setOnClickListener { v: View? ->
            val challengeId = userNameET.text.toString().trim { it <= ' ' }
            if (type == ENROL_TO_CHALLENGE) enrollToChallenge(challengeId) else getChallenge(
                challengeId
            )
            dialog.dismiss()
        }
        dialog.setView(dialogView)
        dialog.show()
    }

    private fun enrollToChallenge(challengeId: String) {
        val localRepository = LocalRepository(this)
        //String challengeId = "606b07058aee1e0012fa6e8f";
        val bodyMap = HashMap<String, Any>()
        bodyMap["enroll"] = true
        aktivoManager.enrollToChallenge(
            ChallengeEnrollQuery(
                localRepository.userId,
                challengeId,
                bodyMap
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Enroll> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(enroll: Enroll) {
                    val message = "enrolToChallenge: " + enroll.message
                    Log.e(TAG, message)
                    Toast.makeText(
                        this@ChallengeActivity, message, Toast.LENGTH_SHORT
                    ).show()
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "enrolToChallenge error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}