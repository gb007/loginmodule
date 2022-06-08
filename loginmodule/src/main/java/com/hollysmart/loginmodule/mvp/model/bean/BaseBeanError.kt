package com.hollysmart.loginmodule.mvp.model.bean

import com.google.gson.JsonObject


data class BaseBeanError(
    val success: Boolean,
    val message: String,
    val code: Int,
    val timestamp: Long
) {




}