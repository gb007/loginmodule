package com.hollysmart.loginmodule.utils

import android.content.Context
import android.content.SharedPreferences


/**
 * SP工具类
 */
object ShareUtil {

    private var sps: SharedPreferences? = null

    /**
     * sp文件名称为config
     */
    private fun getSps(context: Context): SharedPreferences {
        if (sps == null) {
            sps = context.getSharedPreferences("login_config", Context.MODE_PRIVATE)
        }
        return sps!!
    }

    fun putString(key: String, value: String?, context: Context) {
        if (!value.isNullOrBlank()) {
            var editor: SharedPreferences.Editor = getSps(context).edit()
            editor.putString(key, value)
            editor.commit()
        }
    }

    fun getString(key: String, context: Context): String? {
        if (!key.isNullOrBlank()) {
            var sps: SharedPreferences = getSps(context)
            return sps.getString(key, null)
        }
        return null
    }

    fun putBoolean(key: String, value: Boolean, context: Context) {
        var editor: SharedPreferences.Editor = getSps(context).edit()
        editor.putBoolean(key, value)
        editor.commit()

    }

    fun getBoolean(key: String, context: Context): Boolean {
        var sps: SharedPreferences = getSps(context)
        return sps.getBoolean(key, false)
    }

    fun putInt(key: String, value: Int, context: Context) {

        var editor: SharedPreferences.Editor = getSps(context).edit()
        editor.putInt(key, value)
        editor.commit()

    }

    fun getInt(key: String, context: Context): Int {
        if (!key.isNullOrBlank()) {
            var sps: SharedPreferences = getSps(context)
            return sps.getInt(key, 0)
        }
        return 0
    }


}