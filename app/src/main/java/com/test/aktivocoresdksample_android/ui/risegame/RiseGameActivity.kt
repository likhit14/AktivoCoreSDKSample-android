package com.test.aktivocoresdksample_android.ui.risegame

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aktivolabs.aktivocore.data.models.queries.RiseGameDailyPointsQuery
import com.aktivolabs.aktivocore.data.models.queries.RiseGameRewardsQuery
import com.aktivolabs.aktivocore.data.models.queries.RiseGameWeeklyPointsQuery
import com.aktivolabs.aktivocore.data.models.risegame.RiseGame
import com.aktivolabs.aktivocore.data.models.risegame.dailypoints.RGPDailyPoints
import com.aktivolabs.aktivocore.data.models.risegame.rewards.RiseGameReward
import com.aktivolabs.aktivocore.data.models.risegame.weeklypoints.RGPWeeklyPoints
import com.test.aktivocoresdksample_android.base.BaseActivity
import com.test.aktivocoresdksample_android.data.enums.ActionEnum
import com.test.aktivocoresdksample_android.data.model.Action
import com.test.aktivocoresdksample_android.databinding.ActivityRiseGameBinding
import com.test.aktivocoresdksample_android.listeners.ActionListener
import com.test.aktivocoresdksample_android.utils.actionadapter.ActionAdapter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class RiseGameActivity : BaseActivity() {

    companion object {
        val TAG = RiseGameActivity.javaClass.simpleName
    }

    private lateinit var binding: ActivityRiseGameBinding

    private var gameId: String? = null
    val START_DATE = "2021-01-01"
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val todaysDate = format.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiseGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init() {
        binding.toolbar.title = "RiseGame"

        binding.actionLayout.actionBtnRV.apply {
            adapter = ActionAdapter(mappingUtils.getRiseGameActions(), object : ActionListener {
                override fun onActionClick(action: Action) {
                    when (action.actionEnum) {
                        ActionEnum.RISE_GAME -> {
                            getRiseGame()
                        }
                        ActionEnum.RISE_GAME_REWARDS -> {
                            getRiseGameRewards()
                        }
                        ActionEnum.DAILY_RISE_GAME_POINTS -> {
                            getDailyRiseGamePoints()
                        }
                        ActionEnum.WEEKLY_RISE_GAME_POINTS -> {
                            getWeeklyRiseGamePoints()
                        }
                    }
                }

            })
        }
    }

    private fun getWeeklyRiseGamePoints() {
        val riseGameWeeklyPointsQuery = RiseGameWeeklyPointsQuery(
            gameId,
            START_DATE, todaysDate
        )
        aktivoManager.getWeeklyRiseGamePoints(riseGameWeeklyPointsQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<RGPWeeklyPoints?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(rgpWeeklyPointsList: List<RGPWeeklyPoints?>) {
                    Log.e(TAG, "getWeeklyRiseGamePoints $rgpWeeklyPointsList")
                    Toast.makeText(
                        this@RiseGameActivity,
                        "getWeeklyRiseGamePoints: $rgpWeeklyPointsList", Toast.LENGTH_SHORT
                    ).show()
                    val message = "WeeklyRiseGamePoints $rgpWeeklyPointsList"
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getWeeklyRiseGamePoints, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getDailyRiseGamePoints() {
        val riseGameDailyPointsQuery = RiseGameDailyPointsQuery(
            gameId,
            START_DATE, todaysDate
        )
        aktivoManager.getDailyRiseGamePoints(riseGameDailyPointsQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<RGPDailyPoints?>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(rgpDailyPointsList: List<RGPDailyPoints?>) {
                    Log.e(TAG, "getDailyRiseGamePoints $rgpDailyPointsList")
                    Toast.makeText(
                        this@RiseGameActivity,
                        "getDailyRiseGamePoints: $rgpDailyPointsList",
                        Toast.LENGTH_SHORT
                    ).show()
                    val message = "DailyRiseGamePoints: $rgpDailyPointsList"
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "getDailyRiseGamePoints, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getRiseGameRewards() {
        val riseGameRewardsQuery = RiseGameRewardsQuery(gameId)
        aktivoManager.getRiseGameRewards(riseGameRewardsQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<RiseGameReward> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(riseGameReward: RiseGameReward) {
                    Log.e(TAG, "riseGameReward $riseGameReward")
                    Toast.makeText(
                        this@RiseGameActivity,
                        "riseGameReward: $riseGameReward",
                        Toast.LENGTH_SHORT
                    ).show()
                    val message = "riseGameReward $riseGameReward"
                    showText(message)
                }

                override fun onError(e: Throwable) {
                    val message = "riseGameReward, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    private fun getRiseGame() {
        aktivoManager.riseGame
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<RiseGame?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(riseGame: RiseGame) {
                    if (riseGame.riseGameAccount != null) {
                        gameId = riseGame.riseGameAccount.gameId
                        Log.e(TAG, "getRiseGame $riseGame")
                        Log.e(TAG, "getRiseGame: ID " + riseGame.riseGameAccount.gameId)
                        Toast.makeText(
                            this@RiseGameActivity,
                            "getRiseGame: $riseGame",
                            Toast.LENGTH_SHORT
                        ).show()
                        val message =
                            "RiseGame $riseGame RiseGame: ID  $riseGame.riseGameAccount.gameId"
                        showText(message)
                    }
                }

                override fun onError(e: Throwable) {
                    val message = "getRiseGame, error: " + e.message
                    Log.e(TAG, message, e)
                    showText(message)
                }
            })
    }

    override fun showText(text: String) {
        binding.actionLayout.actionTV.setText(text)
    }
}