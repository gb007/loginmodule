package com.hollysmart.loginmodule.mvp.model

import com.google.gson.JsonObject
import com.hollysmart.basemodule.rx.scheduler.SchedulerUtils
import com.hollysmart.loginmodule.mvp.retrofit.RetrofitManager
import io.reactivex.Observable
import okhttp3.RequestBody


class LoginModel {

    fun login(params: RequestBody): Observable<JsonObject> {
        return RetrofitManager.service.login(params)
            .compose(SchedulerUtils.ioToMain())
    }

}