package com.hollysmart.loginmodule.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.cxmscb.cxm.timerbuttonlib.TimerButton
import com.google.gson.Gson
import com.hollysmart.loginmodule.R
import com.hollysmart.loginmodule.common.ConFig
import com.hollysmart.loginmodule.common.LoginConfig
import com.hollysmart.loginmodule.common.PrivacyConfig
import com.hollysmart.loginmodule.dialog.ScreenViewDialog
import com.hollysmart.loginmodule.dialog.ScreenViewDialog.OnClickListener
import com.hollysmart.loginmodule.utils.ShareUtil
import com.hollysmart.loginmodule.utils.Utils
import com.kpa.fingerprintdemo.FingerLoginUtil
import com.hollysmart.loginmodule.gesture.GesturePwdCheckActivity
import com.tencent.tauth.Tencent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import android.widget.Toast
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild

import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext

import com.umeng.socialize.bean.SHARE_MEDIA

import com.umeng.socialize.UMAuthListener


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var img_top_logo: ImageView
    lateinit var usernameTitle: TextView
    lateinit var passwordTitle: TextView
    lateinit var tv_register: TextView
    lateinit var accountEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var requestAuthCodeButton: TimerButton
    lateinit var ll_logintypeGesture: LinearLayout
    lateinit var ll_logintypeFinger: LinearLayout
    lateinit var ll_logintype_wechat: LinearLayout
    lateinit var ll_logintype_qq: LinearLayout
    lateinit var loginButton: Button
    lateinit var loginConfig: LoginConfig
    lateinit var privacyConfig: PrivacyConfig

    lateinit var privacyDialog: ScreenViewDialog

    lateinit var authListener: UMAuthListener
    lateinit var mShareAPI: UMShareAPI

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_module_activity_login)
        initView()
        setLoginModel()
        initThirdAuth()


    }

    private fun initView() {
        img_top_logo = findViewById(R.id.img_top_logo)
        usernameTitle = findViewById(R.id.tv_username)
        passwordTitle = findViewById(R.id.tv_password)
        accountEditText = findViewById(R.id.accountEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        requestAuthCodeButton = findViewById(R.id.btn_verificationCode)
        ll_logintypeGesture = findViewById(R.id.ll_logintypeGesture)
        ll_logintypeFinger = findViewById(R.id.ll_logintypeFinger)
        ll_logintype_wechat = findViewById(R.id.ll_logintype_wechat)
        ll_logintype_qq = findViewById(R.id.ll_logintype_qq)
        tv_register = findViewById(R.id.tv_register)
        loginButton.setOnClickListener(this)
        requestAuthCodeButton.setOnClickListener(this)
        ll_logintypeGesture.setOnClickListener(this)
        ll_logintypeFinger.setOnClickListener(this)
        ll_logintype_wechat.setOnClickListener(this)
        ll_logintype_qq.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        getExtra()

        var agreedTag = ShareUtil.getBoolean("agreed", this)
        if (!agreedTag) {
            initPrivacy()
            privacyDialog.show()
        }

        img_top_logo.setImageResource(loginConfig.topLogoResourceId)
        usernameTitle.text = loginConfig.userNameTitle
        passwordTitle.text = loginConfig.passwordTitle
        accountEditText.hint = "请输入" + loginConfig.userNameTitle
        passwordEditText.hint = "请输入" + loginConfig.passwordTitle

        if (loginConfig.inputModel == ConFig.INPUTMODEL_USER) {//用户名，密码
            requestAuthCodeButton.visibility = View.GONE
        } else if (loginConfig.inputModel == ConFig.INPUTMODEL_PHONE) {//手机号，验证码
            requestAuthCodeButton.visibility = View.VISIBLE
        }

        accountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val phone: String = editable.toString().trim { it <= ' ' }
                if (Utils.checkMobilePhone(phone)) {
                    Utils.showToast(this@LoginActivity, "手机号正确")
                    requestAuthCodeButton.isEnabled = true
                    requestAuthCodeButton.background =
                        resources.getDrawable(R.drawable.shape_btn_vscode)
                } else {
                    loginButton.isEnabled = false
                    requestAuthCodeButton.isEnabled = false
                    requestAuthCodeButton.background =
                        resources.getDrawable(R.drawable.shape_btn_vscode_enable)
                }
            }
        })

        accountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable.toString().length > 2) {
                    loginButton.isEnabled = true
                }
            }

        })
    }


    /**
     * 设置登录方式（
     * 1.有开启指纹优先指纹登录;
     * 2.有开启手势登录则优先手势登录；
     * 3.指纹登录和手势登录都开启，优先指纹；
     * 4.指纹登录和手势登录都未开启，则使用账号密码
     *
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setLoginModel() {

        var isFingerOpened = ShareUtil.getBoolean("isOpenedFingerPrint", this)
        var isGestureOpened = ShareUtil.getBoolean("isOpenedGuesture", this)

        if (isFingerOpened) {
            FingerLoginUtil.instance.FingerLogin(this, ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN)
            return
        }
        if (isGestureOpened) {
            val intent = Intent(this@LoginActivity, GesturePwdCheckActivity::class.java)
            intent.putExtra("checkModel", ConFig.CHECK_GUESTURE_MODEL_LOGIN)
            startActivity(intent)
        }

    }

    /**
     * 隐私政策和服务协议初始化
     *
     */
    private fun initPrivacy() {
        privacyDialog = ScreenViewDialog(
            this,
            R.style.login_module_dialog,
            privacyConfig.privacyTitle,
            privacyConfig.serviceTitle,
            privacyConfig.content,
            privacyConfig.privacyUrl,
            privacyConfig.serviceUrl
        )
        privacyDialog.setOnClickOkListener(object : OnClickListener {
            override fun OnClickOK(view: View?) {
                ShareUtil.putBoolean("agreed", true, this@LoginActivity)
            }

            override fun OnClickBack(view: View?) {
                privacyDialog.dismiss()
            }
        })
        privacyDialog.setCancelable(false)
    }

    /**
     * 第三方授权登录初始化
     *
     */
    private fun initThirdAuth() {

        UMConfigure.init(
            this, "23964aa317aa87760aaa122", "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
        );

        // 微信设置
        PlatformConfig.setWeixin("wx19d82d4e169d37c5", "45ed3b39c5b023ef56bea4142948a614");
        PlatformConfig.setWXFileProvider("com.hollysmart.loginmodule.fileprovider");
        // QQ设置
        PlatformConfig.setQQZone("1112189842", "T4cChe0BvGGfRM56");
        PlatformConfig.setQQFileProvider("com.hollysmart.loginmodule.fileprovider");

        authListener = object : UMAuthListener {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            override fun onStart(platform: SHARE_MEDIA) {}

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param data 用户资料返回
             */
            //openid -> o-dt96X-Vs4Z-u9Br0xDY3_yUTUI
            //uid -> ox1fxwMeFSMKxKpfpnU2HLZR2S9c
            override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {
                Toast.makeText(this@LoginActivity, "成功了", Toast.LENGTH_LONG).show()
            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {
                Toast.makeText(this@LoginActivity, "失败：" + t.message, Toast.LENGTH_LONG).show()
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            override fun onCancel(platform: SHARE_MEDIA, action: Int) {
                Toast.makeText(this@LoginActivity, "取消了", Toast.LENGTH_LONG).show()
            }
        }
        mShareAPI = UMShareAPI.get(this);

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.loginButton -> {
                val intent = Intent(this@LoginActivity, SettingActivity::class.java)
                startActivity(intent)

            }

            R.id.tv_register -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_verificationCode -> {

                val account = accountEditText.text.toString().trim { it <= ' ' }
                if (Utils.isEmpty(account)) {
                    Utils.showToast(applicationContext, "请输入手机号")
                    return
                }
                if (!Utils.checkMobilePhone(account)) {
                    Utils.showToast(applicationContext, "请输入正确的手机号")
                    return
                }

                requestAuthCodeButton.startTimer()
                requestAuthCodeButton.init("重新获取", 120000)
                requestAuthCodeButton.isEnabled = false

            }
            R.id.ll_logintypeGesture -> {

                val isOpenedGuesture = ShareUtil.getBoolean("isOpenedGuesture", this)
                if (isOpenedGuesture) {
                    val intent = Intent(this@LoginActivity, GesturePwdCheckActivity::class.java)
                    intent.putExtra("checkModel", ConFig.CHECK_GUESTURE_MODEL_LOGIN)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "未设置手势登录功能,请使用其它方式登录", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.ll_logintypeFinger -> {
                FingerLoginUtil.instance.FingerLogin(this, ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN)
            }


            R.id.ll_logintype_wechat -> {

                if (mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener)
                } else {
                    Toast.makeText(this, "请先下载安装微信客户端", Toast.LENGTH_LONG).show()
                }

            }
            R.id.ll_logintype_qq -> {

                if (mShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                    mShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, authListener)
                } else {
                    Toast.makeText(this, "请先下载安装QQ客户端", Toast.LENGTH_LONG).show()
                }

            }
        }
    }


    companion object {
        fun startActivity(activity: LoginActivity) {
            val intent = Intent(activity, SettingActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private fun getExtra() {
        loginConfig = Gson().fromJson(intent.getStringExtra("loginConfig"), LoginConfig::class.java)
        privacyConfig =
            Gson().fromJson(intent.getStringExtra("privacyConfig"), PrivacyConfig::class.java)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this@LoginActivity).onActivityResult(requestCode, resultCode, data)
    }

}