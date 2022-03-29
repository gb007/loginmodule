package com.hollysmart.loginapplication

import android.app.Application
import android.content.Context

class BaseApplication : Application() {

    companion object {
        lateinit private var context: Context
        fun getAPPContext() = context!!
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}

