package com.test.aktivocoresdksample_android.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aktivolabs.aktivocore.data.models.User
import com.aktivolabs.aktivocore.data.repositories.LocalRepository
import com.test.aktivocoresdksample_android.PermissionActivity
import com.test.aktivocoresdksample_android.R
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityAuthenticationBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AuthenticationActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Authentication"

        binding.actionLayout.actionBtnRV.apply {
            adapter =
                ActionAdapter(mappingUtils.getAuthenticationActions(), object : ActionListener {
                    override fun onActionClick(action: Action) {
                        when (action.actionEnum) {
                            ActionEnum.REQUEST_PERMISSION -> {
                                startActivity(
                                    Intent(
                                        this@AuthenticationActivity,
                                        PermissionActivity::class.java
                                    )
                                )
                            }
                            ActionEnum.AUTHENTICATE_USER -> {
                                authenticateUser()
                            }
                            ActionEnum.BYPASS_AUTHENTICATE_USER_TOKEN -> {
                                byPassAuthenticateUserUsingToken()
                            }
                        }
                    }

                })
        }
    }

    private fun authenticateUser() {
        aktivoManager
            .authenticateUser(User("5ec50e8bc9eda80012c5a71d"), this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "User Authenticated"
                    Toast.makeText(
                        this@AuthenticationActivity, message, Toast.LENGTH_LONG
                    ).show()
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "Auth error: " + e.message
                    Toast.makeText(
                        this@AuthenticationActivity, message, Toast.LENGTH_LONG
                    ).show()
                    showText(message)
                }
            })

        /*LocalRepository localRepository = new LocalRepository(this);
        localRepository.putUserId("61b1b32887d98ccf6a302939");
        localRepository.putAccessToken("0ed603eac0edcdc2b5b29fed8a37addf01a7cb06");*/
    }

    private fun authenticateUser(userId: String) {
        aktivoManager
            .authenticateUser(User(userId), this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "User Authenticated"
                    Toast.makeText(
                        this@AuthenticationActivity, message, Toast.LENGTH_LONG
                    ).show()
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "Auth error: " + e.message
                    Toast.makeText(
                        this@AuthenticationActivity, message, Toast.LENGTH_LONG
                    ).show()
                    showText(message)
                }
            })

        /*LocalRepository localRepository = new LocalRepository(this);
        localRepository.putUserId("61b1b32887d98ccf6a302939");
        localRepository.putAccessToken("0ed603eac0edcdc2b5b29fed8a37addf01a7cb06");*/
    }

    private fun byPassAuthenticateUserUsingToken() {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialog = builder.create()
        val dialogView: View = inflater.inflate(R.layout.layout_username_token, null)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)
        val userNameET = dialogView.findViewById<EditText>(R.id.userNameET)
        val tokenET = dialogView.findViewById<EditText>(R.id.accessTokenET)
        val selectorCB = dialogView.findViewById<CheckBox>(R.id.selectorCB)
        selectorCB.visibility = View.GONE
        val localRepository = LocalRepository(this)
        userNameET.setText(localRepository.userId)
        tokenET.setText(localRepository.accessToken)
        submitBtn.setOnClickListener { v: View? ->
            val username = userNameET.text.toString().trim { it <= ' ' }
            val token = tokenET.text.toString().trim { it <= ' ' }
            authenticateUser(username)
            val message = "User details updated"
            Toast.makeText(this@AuthenticationActivity, message, Toast.LENGTH_LONG)
                .show()
            showText(message)
            dialog.dismiss()
        }
        dialog.setView(dialogView)
        dialog.show()
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }

}