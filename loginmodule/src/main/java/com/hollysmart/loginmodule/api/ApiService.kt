package com.hollysmart.loginmodule.api


import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * Created by gbin on 2022/01/10.
 * Api 接口
 */

interface ApiService {


    /**
     * 获取图形验证码
     */

    @GET("/sys/randomImage/{timestamp}")
    fun getRandomImage(
        @Path("timestamp") timestamp: String,
        @QueryMap options: Map<String, String>
    ): Observable<JsonObject>


    @Headers("Content-Type: application/json")
    @POST("sys/login")
    fun login(@Body data: RequestBody): Observable<JsonObject>
}