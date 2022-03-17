package com.hollysmart.loginapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.hollysmart.loginmodule.activity.LoginActivity
import com.hollysmart.loginmodule.common.ConFig
import com.hollysmart.loginmodule.common.LoginConfig
import com.hollysmart.loginmodule.common.PrivacyConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loginConfig = LoginConfig()
        loginConfig.topLogoResourceId = R.mipmap.icon_login_head
        loginConfig.userNameTitle = "用户名"
        loginConfig.passwordTitle = "密码"
        loginConfig.inputModel = ConFig.INPUTMODEL_USER

        var privacyConfig = PrivacyConfig()
        privacyConfig.privacyTitle = "杨柳飞絮防治隐私政策"
        privacyConfig.serviceTitle = "杨柳飞絮防治服务"
        privacyConfig.content = "感谢您信任并使用杨柳飞絮防治的产品和服务！" +
                "\n" +
                "\n" +
                "我们依据最新的法律法规、监管政策要求，更新了《杨柳飞絮防治服务协议》，特向您推送本提示，请您仔细阅读并充分理解相关条款。" +
                "\n" +
                "\n" +
                "本次我们就《杨柳飞絮防治服务协议》更新的条款主要包括：进一步明确用户不可以通过机关服务平台提供的产品和服务谈论和传播涉密及敏感信息(第三部分4)。" +
                "\n" +
                "\n" +
                "您可通过《杨柳飞絮防治服务》和《杨柳飞絮防治隐私政策》查阅完整的协议内容。"

        privacyConfig.privacyUrl =
            "https://qnimg.daolan.com.cn/yangliufeixufangzhiyinsizhengce.html"
        privacyConfig.serviceUrl = "https://qnimg.daolan.com.cn/yangliufeixufuwuxieyi.html"


        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.putExtra("loginConfig", Gson().toJson(loginConfig))
        intent.putExtra("privacyConfig", Gson().toJson(privacyConfig))
        startActivity(intent)
    }
}


