package com.hollysmart.loginmodule.mvp.contract

import com.hollysmart.loginmodule.mvp.model.bean.RandomImageBean
import com.hollysmart.myfirstkotlin.base.IBaseView
import com.hollysmart.basemodule.base.IPresenter


interface RandomImageContract {

    interface View : IBaseView {

        /**
         * 显示验证码
         */
        fun showRandomImage(randomImage: RandomImageBean)

        /**
         * 显示错误信息
         */
        fun showError(errorMsg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取验证码
         */
        fun getRandomImage(timestamp: String, params: Map<String, String>)
    }

}