package com.test.aktivocoresdksample_android.ui.physicallifestyle

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aktivolabs.aktivocore.data.models.Stats
import com.aktivolabs.aktivocore.data.models.heartrate.HeartRateStats
import com.aktivolabs.aktivocore.data.models.queries.*
import com.aktivolabs.aktivocore.data.models.score.ScoreStats
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats
import com.aktivolabs.aktivocore.data.models.steps.StepStats
import com.aktivolabs.aktivoelk.data.models.ElkLog
import com.aktivolabs.aktivoelk.managers.ElkManager
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import com.test.aktivocoresdksample_android.databinding.ActivityPhysicalLifestyleBinding
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class PhysicalLifestyleActivity : BaseActivity() {

    companion object {
        val TAG = PhysicalLifestyleActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityPhysicalLifestyleBinding

    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val todaysDate = format.format(Date())

    val START_DATE = "2021-01-01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhysicalLifestyleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "PhysicalLifestyle"

        binding.actionLayout.actionBtnRV.apply {
            adapter =
                ActionAdapter(mappingUtils.getPhysicalLifeStyleActions(), object : ActionListener {
                    override fun onActionClick(action: Action) {
                        when (action.actionEnum) {
                            ActionEnum.SCORE_STATS -> {
                                getScoreStats()
                            }
                            ActionEnum.STEPS_STATS -> {
                                getStepsStats()
                            }
                            ActionEnum.SLEEP_STATS -> {
                                getSleepStats()
                            }
                            ActionEnum.HEART_RATE_STATS -> {
                                getHeartRateStats()
                            }
                            ActionEnum.LATEST_SCORE_STATS -> {
                                getScoreStatsLatest()
                            }
                            ActionEnum.BATCH_QUERY -> {
                                getBatchStats()
                            }
                            ActionEnum.SCORE_INSIGHT -> {}
                        }
                    }

                })
        }
    }

    private fun getScoreStats() {
        Log.e("MainActivity", "todays Date: $todaysDate")
        aktivoManager.query(ScoreQuery(START_DATE, todaysDate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Map<LocalDate, ScoreStats>> { localDateScoreStatsMap -> localDateScoreStatsMap }
            .subscribe(object : SingleObserver<Map<LocalDate, ScoreStats>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(localDateScoreStatsMap: Map<LocalDate, ScoreStats>) {
                    logBulkData()
                    val keySet = localDateScoreStatsMap.keys
                    var mainMessage = "Result "
                    for (localDate in keySet) {
                        val message =
                            "Score stats: Date: $localDate Score: " + localDateScoreStatsMap[localDate]!!.score
                        Log.e(TAG, message)
                        mainMessage = "$mainMessage $message"
                    }
                    showText(mainMessage)
                }

                override fun onError(e: Throwable) {
                    logBulkData()
                    val message = "Error in getScoreStats: " + e.message
                    Log.e(TAG, message)
                    showText(message)
                }
            })
    }

    private fun getSleepStats() {
        aktivoManager.query(SleepQuery(START_DATE, todaysDate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Map<LocalDate, SleepStats>> { localDateSleepStatsMap -> localDateSleepStatsMap }
            .subscribe(object : SingleObserver<Map<LocalDate, SleepStats>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(localDateSleepStatsMap: Map<LocalDate, SleepStats>) {
                    val keySet = localDateSleepStatsMap.keys
                    var mainMessage = "Result "
                    for (localDate in keySet) {
                        val message =
                            "Sleep stats: Date: $localDate Sleep: " + localDateSleepStatsMap[localDate]!!.value
                        Log.e(TAG, message)
                        mainMessage = "$mainMessage $message"
                    }
                    showText(mainMessage)
                }

                override fun onError(e: Throwable) {
                    val message = "Error in getSleepStats: " + e.message
                    Log.e(TAG, message)
                    showText(message)
                }
            })
    }

    private fun getStepsStats() {
        aktivoManager.query(StepsQuery(START_DATE, todaysDate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Map<LocalDate, StepStats>> { localDateStepStatsMap -> localDateStepStatsMap }
            .subscribe(object : SingleObserver<Map<LocalDate, StepStats>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(localDateStepStatsMap: Map<LocalDate, StepStats>) {
                    val keySet = localDateStepStatsMap.keys
                    var mainMessage = "Result "
                    for (localDate in keySet) {
                        val message =
                            "Steps stats: Date: $localDate Steps: " + localDateStepStatsMap[localDate]!!.value
                        Log.e(TAG, message)
                        mainMessage = "$mainMessage $message"
                    }
                    showText(mainMessage)
                }

                override fun onError(e: Throwable) {
                    val message = "Error in getStepsStats: " + e.message
                    Log.e(TAG, message)
                    showText(message)
                }
            })
    }

    private fun getHeartRateStats() {
        aktivoManager.query(HeartRateQuery(START_DATE, todaysDate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Map<LocalDate, HeartRateStats>> { localDateHeartRateStatsMap -> localDateHeartRateStatsMap }
            .subscribe(object : SingleObserver<Map<LocalDate, HeartRateStats>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(localDateHeartRateStatsMap: Map<LocalDate, HeartRateStats>) {
                    val keySet = localDateHeartRateStatsMap.keys
                    var mainMessage = "Result "
                    for (localDate in keySet) {
                        val message =
                            "Heart rate stats: Date: $localDate Heart rate: " + localDateHeartRateStatsMap[localDate]!!.value
                        Log.e(TAG, message)
                        mainMessage = "$mainMessage $message"
                    }
                    showText(mainMessage)
                }

                override fun onError(e: Throwable) {
                    val message = "Error in getHeartRateStats: " + e.message
                    Log.e(TAG, message)
                    showText(message)
                }
            })
    }

    private fun getScoreStatsLatest() {
        Log.e("MainActivity", "todays Date: $todaysDate")
        aktivoManager.query(ScoreLatestQuery(todaysDate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Map<LocalDate, ScoreStats>> { localDateScoreStatsMap -> localDateScoreStatsMap }
            .subscribe(object : SingleObserver<Map<LocalDate, ScoreStats>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(localDateScoreStatsMap: Map<LocalDate, ScoreStats>) {
                    logBulkData()
                    val keySet = localDateScoreStatsMap.keys
                    var mainMessage = "Result "
                    for (localDate in keySet) {
                        val message =
                            "Score stats: Date: $localDate Score: " + localDateScoreStatsMap[localDate]!!.score
                        Log.e(TAG, message)
                        mainMessage = "$mainMessage $message"
                    }
                    showText(mainMessage)
                }

                override fun onError(e: Throwable) {
                    logBulkData()
                    val message = "Error in getScoreStats: " + e.message
                    Log.e(TAG, message)
                    showText(message)
                }
            })
    }

    private fun logBulkData() {
        if (localRepository.elkBaseUrl.isEmpty()) return
        val elkManager = ElkManager(
            this,
            localRepository.elkBaseUrl,
            localRepository.elkApiKey
        )
        val elkLogList: MutableList<ElkLog> = ArrayList()
        elkLogList.add(
            ElkLog(
                "android_client",
                "_doc",
                "android",
                "1.0.68",
                "staging",
                "debug",
                "5d64f5d26d89460015ebab72",
                "Log from android 001",
                "2020-06-26T04:54:07.913Z"
            )
        )
        elkLogList.add(
            ElkLog(
                "android_client",
                "_doc",
                "android",
                "1.0.68",
                "staging",
                "debug",
                "5d64f5d26d89460015ebab72",
                "Log from android 002",
                "2020-06-26T04:54:07.913Z"
            )
        )
        elkManager.logBulkData(elkLogList).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    val message = "Bulk upload successful"
                    Toast.makeText(
                        this@PhysicalLifestyleActivity, message, Toast.LENGTH_SHORT
                    ).show()
//                    showText(message)
                }

                override fun onError(e: Throwable) {}
            })
    }

    private fun getBatchStats() {
        val queryList: MutableList<Query> = ArrayList()
        queryList.add(ScoreQuery(START_DATE, todaysDate))
        queryList.add(StepsQuery(START_DATE, todaysDate))
        queryList.add(SleepQuery(START_DATE, todaysDate))
        queryList.add(HeartRateQuery(START_DATE, todaysDate))
        compositeDisposable.add(
            aktivoManager
                .query(queryList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<List<Map<LocalDate, Stats>>> { maps -> maps }.subscribeWith(object :
                    DisposableSingleObserver<List<Map<LocalDate, Stats>>>() {
                    override fun onSuccess(mapList: List<Map<LocalDate, Stats>>) {
                        Log.e(TAG, "Map size: " + mapList.size)
                        Log.e(TAG, "Map: $mapList")
                        var mainMessage = "Map size: " + mapList.size + " Map: $mapList"
                        for (i in mapList.indices) {
                            val keySet = mapList[i].keys
                            if (queryList[i] is ScoreQuery) {
                                for (`object` in keySet) {
                                    val message =
                                        "Date: " + `object` + " value: " + (mapList[i][`object`] as ScoreStats?)!!.score
                                    Log.e(TAG, message)
                                    mainMessage = "$mainMessage $message"
                                }
                            } else if (queryList[i] is StepsQuery) {
                                for (`object` in keySet) {
                                    val message =
                                        "Date: " + `object` + " value: " + (mapList[i][`object`] as StepStats?)!!.value
                                    Log.e(TAG, message)
                                    mainMessage = "$mainMessage $message"
                                }
                            } else if (queryList[i] is SleepQuery) {
                                for (`object` in keySet) {
                                    val message =
                                        "Date: " + `object` + " value: " + (mapList[i][`object`] as SleepStats?)!!.value
                                    Log.e(TAG, message)
                                    mainMessage = "$mainMessage $message"
                                }
                            } else if (queryList[i] is HeartRateQuery) {
                                for (`object` in keySet) {
                                    val message =
                                        "Date: " + `object` + " value: " + (mapList[i][`object`] as HeartRateStats?)!!.value
                                    Log.e(TAG, message)
                                    mainMessage = "$mainMessage $message"
                                }
                            }
                        }
                        showText(mainMessage)
                    }

                    override fun onError(e: Throwable) {
                        val message = "Home stats error: " + e.localizedMessage + "---" + e.message
                        Log.e(TAG, message) //TODO remove after fixing infinite loader
                        showText(message)
                    }
                })
        )
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}