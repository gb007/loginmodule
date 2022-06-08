package com.hollysmart.loginmodule.mvp.presenter

import com.google.gson.Gson
import com.hollysmart.basemodule.base.BasePresenter
import com.hollysmart.basemodule.net.exception.ExceptionHandle
import com.hollysmart.loginmodule.mvp.contract.LoginContract
import com.hollysmart.loginmodule.mvp.contract.RandomImageContract
import com.hollysmart.loginmodule.mvp.model.LoginModel
import com.hollysmart.loginmodule.mvp.model.RandomImageModel
import com.hollysmart.loginmodule.mvp.model.bean.BaseBean
import com.hollysmart.loginmodule.mvp.model.bean.BaseBeanError
import com.hollysmart.loginmodule.mvp.model.bean.LoginBean
import com.hollysmart.loginmodule.mvp.model.bean.RandomImageBean
import okhttp3.RequestBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class LoginPresenter : BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {

    private val loginModel: LoginModel by lazy { LoginModel() }

    override fun login(params: RequestBody) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = loginModel.login(params)
            .subscribe({ result ->
                mRootView?.apply {
                    val data = Gson().fromJson(result, BaseBeanError::class.java)
                    if (data.success) {
                        val loginResult = Gson().fromJson(result, BaseBean::class.java)
                        showLoginResult(Gson().fromJson(loginResult.result, LoginBean::class.java))
                    } else {
                        showLoginError(data.message, data.code)
                    }
                }
            }, { throwable ->
                mRootView?.apply {
                    //处理异常
                    showLoginError(
                        ExceptionHandle.handleException(throwable),
                        ExceptionHandle.errorCode
                    )
                    showLoadingFailed()
                }
            })
        addSubscription(disposable)
    }
}