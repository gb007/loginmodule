package com.hollysmart.loginmodule.mvp.presenter

import com.google.gson.Gson
import com.hollysmart.basemodule.base.BasePresenter
import com.hollysmart.basemodule.net.exception.ExceptionHandle
import com.hollysmart.loginmodule.mvp.contract.RandomImageContract
import com.hollysmart.loginmodule.mvp.model.RandomImageModel
import com.hollysmart.loginmodule.mvp.model.bean.RandomImageBean


class RandomImagePresenter : BasePresenter<RandomImageContract.View>(),
    RandomImageContract.Presenter {

    private val deviceStatisticsModel: RandomImageModel by lazy { RandomImageModel() }

    override fun getRandomImage(timestamp: String, params: Map<String, String>) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = deviceStatisticsModel.getRandomImage(timestamp, params)
            .subscribe({ result ->
                mRootView?.apply {
                    val data = Gson().fromJson(result, RandomImageBean::class.java)
                    if (data.success) {
                        showRandomImage(data)
                    } else {
                        showError(data.message, data.code)
                    }
                }
            }, { throwable ->
                mRootView?.apply {
                    //处理异常
                    showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                    showLoadingFailed()
                }
            })
        addSubscription(disposable)
    }
}