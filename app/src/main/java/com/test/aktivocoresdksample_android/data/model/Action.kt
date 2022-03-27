package com.test.aktivocoresdksample_android.data.model

import com.test.aktivocoresdksample_android.data.enums.ActionEnum

data class Action(
    val actionId: String,
    val actionName: String,
    val actionEnum: ActionEnum
)
