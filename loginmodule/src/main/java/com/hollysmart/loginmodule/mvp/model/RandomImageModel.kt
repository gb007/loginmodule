package com.hollysmart.loginmodule.mvp.model

import com.google.gson.JsonObject
import com.hollysmart.basemodule.rx.scheduler.SchedulerUtils
import com.hollysmart.loginmodule.mvp.retrofit.RetrofitManager
import io.reactivex.Observable



class RandomImageModel {

    fun getRandomImage(timestamp: String, params: Map<String, String>): Observable<JsonObject> {
        return RetrofitManager.service.getRandomImage(timestamp, params)
            .compose(SchedulerUtils.ioToMain())
    }

}