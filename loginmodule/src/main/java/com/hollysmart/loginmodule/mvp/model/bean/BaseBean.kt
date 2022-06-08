package com.hollysmart.loginmodule.mvp.model.bean

import com.google.gson.JsonObject


data class BaseBean(
    val success: Boolean,
    val message: String,
    val code: Int,
    val result:JsonObject?,
    val timestamp: Long
) {

}