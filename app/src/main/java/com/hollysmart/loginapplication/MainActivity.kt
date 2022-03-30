package com.hollysmart.loginapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.hollysmart.loginmodule.activity.LoginActivity
import com.hollysmart.loginmodule.common.ConFig
import com.hollysmart.loginmodule.common.LoginConfig
import com.hollysmart.loginmodule.common.PrivacyConfig
import com.hollysmart.loginmodule.common.ThirdAuthConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //登录页面设置
        var loginConfig = LoginConfig()
        //登录页面顶部logo图案
        loginConfig.topLogoResourceId = R.mipmap.icon_login_head
        //输入框标题（用户名或手机号）
        loginConfig.userNameTitle = "用户名"
        //输入框标题（密码或验证码）
        loginConfig.passwordTitle = "密码"
        //登录类型（用户名或手机号）  INPUTMODEL_USER，INPUTMODEL_PHONE
        loginConfig.inputModel = ConFig.INPUTMODEL_USER
        //弹出隐私提示框设置
        var privacyConfig = PrivacyConfig()
        //隐私政策名称
        privacyConfig.privacyTitle = "杨柳飞絮防治隐私政策"
        //服务协议名称
        privacyConfig.serviceTitle = "杨柳飞絮防治服务"
        //弹窗内容
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
        //隐私政策点击跳转详情页面url
        privacyConfig.privacyUrl =
            "https://qnimg.daolan.com.cn/yangliufeixufangzhiyinsizhengce.html"
        //服务协议点击跳转详情页面url
        privacyConfig.serviceUrl = "https://qnimg.daolan.com.cn/yangliufeixufuwuxieyi.html"

        //第三方登录设置
        var thirdAuthConfig = ThirdAuthConfig()
        //友盟APP_KEY
        thirdAuthConfig.umenG_APP_KEY = "23964aa317aa87760aaa122"
        //微信appid
        thirdAuthConfig.wechaT_APP_ID = "wx19d82d4e169d37c5"
        //微信app_secret
        thirdAuthConfig.wechaT_APP_SECRET = "45ed3b39c5b023ef56bea4142948a614"
        //qq app_id
        thirdAuthConfig.qQ_APP_ID = "1112189842"
        //qq app_secret
        thirdAuthConfig.qQ_APP_SECRET = "T4cChe0BvGGfRM56"

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.putExtra("loginConfig", Gson().toJson(loginConfig))
        intent.putExtra("privacyConfig", Gson().toJson(privacyConfig))
        intent.putExtra("thirdAuthConfig", Gson().toJson(thirdAuthConfig))
        startActivity(intent)
    }
}


