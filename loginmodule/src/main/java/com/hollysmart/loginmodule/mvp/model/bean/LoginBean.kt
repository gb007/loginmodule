package com.hollysmart.loginmodule.mvp.model.bean


data class LoginBean(
    val token: String,
    val userInfoBean: UserInfoBean
) {
}
