package com.hollysmart.loginmodule.mvp.contract

import com.hollysmart.loginmodule.mvp.model.bean.RandomImageBean
import com.hollysmart.myfirstkotlin.base.IBaseView
import com.hollysmart.basemodule.base.IPresenter
import com.hollysmart.loginmodule.mvp.model.bean.LoginBean
import okhttp3.RequestBody


interface LoginContract {

    interface View : IBaseView {

        /**
         * 显示验证码
         */
        fun showLoginResult(loginBean: LoginBean)

        /**
         * 显示错误信息
         */
        fun showLoginError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 登录
         */
        fun login(params: RequestBody)
    }

}