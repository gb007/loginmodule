package com.hollysmart.loginmodule.mvp.model.bean


data class RandomImageBean(
    val success: Boolean,
    val message: String,
    val code: Int,
    val result: String,
    val timestamp: Long
) {
}