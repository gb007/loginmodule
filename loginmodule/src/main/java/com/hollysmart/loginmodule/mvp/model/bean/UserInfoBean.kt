package com.hollysmart.loginmodule.mvp.model.bean


data class UserInfoBean(
    val id: String,
    val username: String,
    val realname: String,
    val password: String,
    val orgCode: String,
    val avatar: String,
    val birthday: String,
    val sex: Int,
    val email: String,
    val phone: String,
    val status: Int,
    val delFlag: Int,
    val activitiSync: Int,
    val createTime: String,
    val userIdentity: Int,
    val departIds: String,
    val post: String,
    val telephone: String,
    val relTenantIds: String,
    val clientId: String,
    val tenantId: String,
    val departType: String,
    val openId: String,
    val roleCode:Array<String>,

    ) {
}
