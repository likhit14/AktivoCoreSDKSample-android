package com.test.aktivocoresdksample_android.ui.badges

import android.os.Bundle
import android.util.Log
import com.aktivolabs.aktivocore.data.models.badges.AktivoBadgeType
import com.aktivolabs.aktivocore.data.models.badges.BadgeSummary
import com.aktivolabs.aktivocore.data.models.badges.DailyBadge
import com.aktivolabs.aktivocore.data.models.badges.HistoryBadgeType
import com.aktivolabs.aktivocore.data.models.queries.BadgeByDateQuery
import com.aktivolabs.aktivocore.data.models.queries.BadgeHistoryQuery
import com.aktivolabs.aktivocore.data.models.queries.BadgeSummaryQuery
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityBadgeBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class BadgeActivity : BaseActivity() {

    companion object {
        val TAG = BadgeActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityBadgeBinding

    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val todaysDate = format.format(Date())

    val START_DATE = "2021-01-01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBadgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "Badges"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getBadgesActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.BADGE_BY_DATE -> {
                            getBadgesByDate()
                        }
                        ActionEnum.BADGE_SUMMARY -> {
                            getBadgesSummary()
                        }
                        ActionEnum.BADGE_HISTORY -> {
                            getBadgesHistory()
                        }
                        ActionEnum.ALL_BADGES -> {
                            getAktivoBadgeTypes()
                        }
                    }
                }

            })
        }
    }

    private fun getBadgesByDate() {
        val badgeByDateQuery = BadgeByDateQuery(todaysDate)
        aktivoManager.queryBadgeByDate(badgeByDateQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<DailyBadge> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(dailyBadge: DailyBadge) {
                    if (dailyBadge.badgeType != null) {
                        val message = "BadgesByDate, getRefDate: " + dailyBadge.refDate +
                                " dailyBadge: type: " + dailyBadge.badgeType.badgeTypeEnum.toString() +
                                " dailyBadge: type: " + dailyBadge.badgeType.title
                        Log.e(TAG, message)
                        showText(message)
                    } else {
                        val message = "BadgesByDate, getRefDate: " + dailyBadge.refDate
                        Log.e(TAG, message)
                        showText(message)
                    }
                }

                override fun onError(e: Throwable) {
                    val message = "BadgesByDate, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getBadgesSummary() {
        val badgeSummaryQuery = BadgeSummaryQuery(START_DATE, todaysDate)
        aktivoManager.queryBadgeSummary(badgeSummaryQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<BadgeSummary> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(badgeSummary: BadgeSummary) {
                    var messageMain = "BadgesSummary, getAllTimeTotal: " +
                            badgeSummary.allTimeTotal + " periodTotal: " + badgeSummary.periodTotal
                    Log.e(TAG, messageMain)
                    if (badgeSummary.lastEarnedBadge != null) {
                        val message = "getLastEarnedBadge() " + badgeSummary.lastEarnedBadge.refDate
                        Log.e(TAG, message)
                        messageMain = "$messageMain $message"
                        if (badgeSummary.lastEarnedBadge.lastEarnedBadgeType != null) {
                            val message = "getLastEarnedBadge(): type" +
                                    badgeSummary.lastEarnedBadge.lastEarnedBadgeType.badgeTypeEnum.toString() +
                                    " title: " + badgeSummary.lastEarnedBadge.lastEarnedBadgeType.title
                            Log.e(TAG, message)
                            messageMain = "$messageMain $message"
                        }
                    }
                    showText(messageMain)
                }

                override fun onError(e: Throwable) {
                    val message = "getBadgesSummary, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getBadgesHistory() {
        val badgeHistoryQuery = BadgeHistoryQuery(START_DATE, todaysDate)
        aktivoManager.queryBadgeHistory(badgeHistoryQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Map<LocalDate, HistoryBadgeType>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(localDateHistoryBadgeTypeMap: Map<LocalDate, HistoryBadgeType>) {
                    var messageMain = "Result "
                    for ((key, value) in localDateHistoryBadgeTypeMap) {
                        val message = "BadgesHistory, refDate: " + key +
                                "badgeTypeEnum: " + value.badgeTypeEnum.toString() + "title: " + value.title
                        Log.e(TAG, message)
                        messageMain = "$messageMain $message"
                    }
                    showText(messageMain)
                }

                override fun onError(e: Throwable) {
                    val message = "getBadgesHistory, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getAktivoBadgeTypes() {
        aktivoManager.queryAllBadges()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<AktivoBadgeType>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(aktivoBadgeTypes: List<AktivoBadgeType>) {
                    var messageMain = "Result "
                    for (aktivoBadgeType in aktivoBadgeTypes) {
                        val message = "AktivoBadgeTypes, title: " + aktivoBadgeType.title
                        Log.e(TAG, message)
                        messageMain = "$messageMain $message"
                    }
                    showText(messageMain)
                }

                override fun onError(e: Throwable) {
                    val message = "getAktivoBadgeTypes, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}