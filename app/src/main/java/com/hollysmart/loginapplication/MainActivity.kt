package com.hollysmart.loginapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.hollysmart.loginmodule.activity.LoginActivity
import com.hollysmart.loginmodule.common.ConFig
import com.hollysmart.loginmodule.common.LoginConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loginConfig = LoginConfig()
        loginConfig.topLogoResourceId = R.mipmap.icon_login_head
        loginConfig.userNameTitle = "用户名"
        loginConfig.passwordTitle = "密码"
        loginConfig.inputModel = ConFig.INPUTMODEL_USER
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.putExtra("loginConfig", Gson().toJson(loginConfig))
        startActivity(intent)
    }
}


