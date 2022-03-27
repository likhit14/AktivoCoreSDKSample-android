package com.test.aktivocoresdksample_android.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.aktivolabs.aktivocore.data.models.feed.FeedNotificationMetadata
import com.aktivolabs.aktivocore.data.models.queries.MarkNotificationReadQuery
import com.aktivolabs.aktivocore.data.models.queries.NotificationsListQuery
import com.aktivolabs.aktivocore.data.models.queries.UnreadNotificationCountQuery
import com.test.aktivocoresdksample_android.R
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityNotificationBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class NotificationActivity : BaseActivity() {

    companion object {
        val TAG = NotificationActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Notifications"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getNotificationActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.FEED_NOTIFICATIONS -> {
                            getFeedNotifications()
                        }
                        ActionEnum.UNREAD_NOTIFICATION_COUNT -> {
                            getUnreadNotificationCount()
                        }
                        ActionEnum.MARK_NOTIFICATION_READ -> {
                            enterNotificationId()
                        }
                        ActionEnum.SET_SLEEP_NOTIFICATION_TITLE -> {
                            setNotificationTitle()
                        }
                        ActionEnum.SET_SLEEP_NOTIFICATION_DESCRIPTION -> {
                            setNotificationDescription()
                        }
                        ActionEnum.SET_SLEEP_NOTIFICATION_ICON -> {
                            setNotificationIcon()
                        }
                        ActionEnum.SET_SLEEP_NOTIFICATION_CHANNEL_NAME -> {
                            updateNotificationChannelName()
                        }
                        ActionEnum.SET_SLEEP_NOTIFICATION_CHANNEL_ID -> {
                            updateNotificationChannelId()
                        }
                    }
                }

            })
        }
    }

    private fun updateNotificationChannelId() {
        aktivoManager.setSleepNotificationChannelId("Channel Id goes here")
    }

    private fun updateNotificationChannelName() {
        aktivoManager.setSleepNotificationChannelName("Channel name goes here")
    }

    private fun setNotificationIcon() {
        aktivoManager.setSleepNotificationIcon(R.mipmap.ic_launcher)
    }

    private fun setNotificationDescription() {
        aktivoManager.setSleepNotificationDescription("Sleep notification desc goes here")
    }

    private fun setNotificationTitle() {
        aktivoManager.setSleepNotificationTitle("Sleep notification title goes here")
    }

    private fun markNotificationAsRead(notificationId: String) {
        //String notificationID = "60b50e68b034d80012ab9887";
        val markNotificationReadQuery = MarkNotificationReadQuery(
            localRepository.userId,
            notificationId,
            true
        )
        aktivoManager.markNotificationAsRead(markNotificationReadQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    val message = "marked notification as read successfully"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "error in marked notification as read"
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getUnreadNotificationCount() {
        val unreadNotificationCountQuery = UnreadNotificationCountQuery(
            localRepository.userId
        )
        aktivoManager.getUnreadNotificationCount(unreadNotificationCountQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(unreadCount: Int) {
                    val message = "getUnreadNotificationCount $unreadCount"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getUnreadNotificationCount, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getFeedNotifications() {
        val offset = 0
        val pageSize = 10
        val isRead = false
        val notificationsListQuery = NotificationsListQuery(
            localRepository.userId,
            offset,
            pageSize,
            isRead
        )
        aktivoManager.getFeedNotifications(notificationsListQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<FeedNotificationMetadata?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(feedNotificationList: List<FeedNotificationMetadata?>) {
                    val message = "getFeedNotifications $feedNotificationList"
                    Log.e(TAG, message)
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getFeedNotifications, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun enterNotificationId() {
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialog = builder.create()
        val dialogView = inflater.inflate(R.layout.layout_username_token, null)
        val userDetailsTV = dialogView.findViewById<TextView>(R.id.userDetailsTV)
        val submitBtn = dialogView.findViewById<Button>(R.id.submitBtn)
        val userNameET = dialogView.findViewById<EditText>(R.id.userNameET)
        val tokenET = dialogView.findViewById<EditText>(R.id.accessTokenET)
        val selectorCB = dialogView.findViewById<CheckBox>(R.id.selectorCB)
        selectorCB.visibility = View.GONE
        userNameET.setHint(R.string.notification_id)
        tokenET.visibility = View.GONE
        userDetailsTV.setText(R.string.notification_details)
        submitBtn.setOnClickListener { v: View? ->
            val notificationId = userNameET.text.toString().trim { it <= ' ' }
            markNotificationAsRead(notificationId)
            dialog.dismiss()
        }
        dialog.setView(dialogView)
        dialog.show()
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}