package com.hollysmart.loginmodule.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import com.hollysmart.loginmodule.common.ThirdAuthConfig
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild

import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext

import com.umeng.socialize.bean.SHARE_MEDIA

import com.umeng.socialize.UMAuthListener
import android.view.ViewGroup

import android.view.WindowManager
import com.hollysmart.loginmodule.view.statusbar.StatusBarUtil
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.util.Base64
import com.google.gson.JsonObject
import com.hollysmart.loginmodule.mvp.contract.LoginContract
import com.hollysmart.loginmodule.mvp.contract.RandomImageContract
import com.hollysmart.loginmodule.mvp.model.bean.LoginBean
import com.hollysmart.loginmodule.mvp.model.bean.RandomImageBean
import com.hollysmart.loginmodule.mvp.presenter.LoginPresenter
import com.hollysmart.loginmodule.mvp.presenter.RandomImagePresenter
import com.hollysmart.loginmodule.utils.MD5Utils
import com.hollysmart.myfirstkotlin.common.AppConst
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.lang.Exception
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap


class LoginActivity : AppCompatActivity(), View.OnClickListener, RandomImageContract.View,
    LoginContract.View {

    lateinit var img_top_logo: ImageView
    lateinit var img_icon_password: ImageView
    lateinit var img_verify_code: ImageView
    lateinit var usernameTitle: TextView
    lateinit var passwordTitle: TextView
    lateinit var tv_register: TextView
    lateinit var tv_find_password: TextView
    lateinit var tv_login_title: TextView
    lateinit var accountEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var verify_code_EditText: EditText
    lateinit var requestAuthCodeButton: TimerButton
    lateinit var img_logintypeGesture: ImageView
    lateinit var img_logintypeFinger: ImageView
    lateinit var img_logintype_wechat: ImageView
    lateinit var img_logintype_qq: ImageView
    lateinit var img_logintype_phone: ImageView
    lateinit var img_logintype_user: ImageView
    lateinit var loginButton: Button
    lateinit var loginConfig: LoginConfig
    lateinit var privacyConfig: PrivacyConfig
    lateinit var thirdAuthConfig: ThirdAuthConfig

    lateinit var privacyDialog: ScreenViewDialog

    lateinit var authListener: UMAuthListener
    lateinit var mShareAPI: UMShareAPI

    private var showPassword = false


    private lateinit var timestamp: String
    private val mPresenter by lazy { RandomImagePresenter() }
    private val loginPresenter by lazy { LoginPresenter() }

    init {
        mPresenter.attachView(this)
        loginPresenter.attachView(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //????????????????????????????????????
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.login_module_activity_login)
        initView()
        init()
        setLoginModel()
        initThirdAuth()
        getVerifyCode()

    }

    private fun init(){
        AppConst.API_SERVER_URL = AppConst.API_HTTP + loginConfig.baseUrl + ":" + loginConfig.port
    }


    private fun initView() {


        //??????????????? ??????setRootViewFitsSystemWindows ?????? winContent.getChildCount()=0 ????????????????????????
        //?????????????????????setContentView????????????????????? setRootViewFitsSystemWindows
        //???FitsSystemWindows?????? true ?????????????????????????????????????????????????????? padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        //?????????????????????
        //?????????????????????
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)

        img_top_logo = findViewById(R.id.img_top_logo)
        img_icon_password = findViewById(R.id.img_icon_password)
        img_verify_code = findViewById(R.id.img_verify_code)
        usernameTitle = findViewById(R.id.tv_username)
        passwordTitle = findViewById(R.id.tv_password)
        accountEditText = findViewById(R.id.accountEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        verify_code_EditText = findViewById(R.id.verify_code_EditText)
        loginButton = findViewById(R.id.loginButton)
        requestAuthCodeButton = findViewById(R.id.btn_verificationCode)

        img_logintypeGesture = findViewById(R.id.img_guesture)
        img_logintypeFinger = findViewById(R.id.img_fingerprint)
        img_logintype_wechat = findViewById(R.id.img_wechat)
        img_logintype_qq = findViewById(R.id.img_qq)
        img_logintype_phone = findViewById(R.id.img_phone)
        img_logintype_user = findViewById(R.id.img_user)

        tv_login_title = findViewById(R.id.tv_login_title)
        tv_register = findViewById(R.id.tv_register)
        tv_find_password = findViewById(R.id.tv_find_password)
        img_icon_password.setOnClickListener(this)
        loginButton.setOnClickListener(this)
        requestAuthCodeButton.setOnClickListener(this)
        img_logintypeGesture.setOnClickListener(this)
        img_logintypeFinger.setOnClickListener(this)
        img_logintype_wechat.setOnClickListener(this)
        img_logintype_qq.setOnClickListener(this)
        img_logintype_phone.setOnClickListener(this)
        img_logintype_user.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        tv_find_password.setOnClickListener(this)
        img_verify_code.setOnClickListener(this)
        getExtra()

        var agreedTag = ShareUtil.getBoolean("agreed", this)
        if (!agreedTag) {
            initPrivacy()
            privacyDialog.show()
        }

        img_top_logo.setImageResource(loginConfig.topLogoResourceId)
        alertLoginMode()
        accountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun afterTextChanged(editable: Editable?) {
                val phone: String = editable.toString().trim { it <= ' ' }
                if (Utils.checkMobilePhone(phone)) {
                    Utils.showToast(this@LoginActivity, "???????????????")
                    requestAuthCodeButton.isEnabled = true
                    requestAuthCodeButton.setTextColor(getColor(R.color.login_module_blue_34))
                } else {
                    loginButton.isEnabled = false
                    requestAuthCodeButton.isEnabled = false
                    requestAuthCodeButton.setTextColor(getColor(R.color.login_module_gray_a7))
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
     * ?????????????????????
     * 1.?????????????????????????????????;
     * 2.?????????????????????????????????????????????
     * 3.??????????????????????????????????????????????????????
     * 4.???????????????????????????????????????????????????????????????
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
            intent.putExtra("thirdAuthConfig", Gson().toJson(thirdAuthConfig))
            startActivity(intent)
        }

    }

    /**
     * ????????????????????????????????????
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
                finish()
            }
        })
        privacyDialog.setCancelable(false)
    }

    /**
     * ??????????????????????????????
     *
     */
    private fun initThirdAuth() {

        //umeng??????
        UMConfigure.init(
            this, thirdAuthConfig.umenG_APP_KEY, "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
        )
        // ????????????
        PlatformConfig.setWeixin(thirdAuthConfig.wechaT_APP_ID, thirdAuthConfig.wechaT_APP_SECRET)
        PlatformConfig.setWXFileProvider("com.hollysmart.loginmodule.fileprovider")
        // QQ??????
        PlatformConfig.setQQZone(thirdAuthConfig.qQ_APP_ID, thirdAuthConfig.qQ_APP_SECRET)
        PlatformConfig.setQQFileProvider("com.hollysmart.loginmodule.fileprovider")


//        UMConfigure.init(
//            this, "23964aa317aa87760aaa122", "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
//        )
//        // ????????????
//        PlatformConfig.setWeixin("wx19d82d4e169d37c5", "45ed3b39c5b023ef56bea4142948a614")
//        PlatformConfig.setWXFileProvider("com.hollysmart.loginmodule.fileprovider")
//        // QQ??????
//        PlatformConfig.setQQZone("1112189842", "T4cChe0BvGGfRM56")
//        PlatformConfig.setQQFileProvider("com.hollysmart.loginmodule.fileprovider")

        authListener = object : UMAuthListener {
            /**
             * @desc ?????????????????????
             * @param platform ????????????
             */
            override fun onStart(platform: SHARE_MEDIA) {}

            /**
             * @desc ?????????????????????
             * @param platform ????????????
             * @param action ?????????????????????????????????
             * @param data ??????????????????
             */
            //openid -> o-dt96X-Vs4Z-u9Br0xDY3_yUTUI
            //uid -> ox1fxwMeFSMKxKpfpnU2HLZR2S9c
            override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {
                Toast.makeText(this@LoginActivity, "?????????", Toast.LENGTH_LONG).show()
            }

            /**
             * @desc ?????????????????????
             * @param platform ????????????
             * @param action ?????????????????????????????????
             * @param t ????????????
             */
            override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {
                Toast.makeText(this@LoginActivity, "?????????" + t.message, Toast.LENGTH_LONG).show()
            }

            /**
             * @desc ?????????????????????
             * @param platform ????????????
             * @param action ?????????????????????????????????
             */
            override fun onCancel(platform: SHARE_MEDIA, action: Int) {
                Toast.makeText(this@LoginActivity, "?????????", Toast.LENGTH_LONG).show()
            }
        }
        mShareAPI = UMShareAPI.get(this);

    }

    /**
     * ??????
     */
    private fun login() {

        var loginJsonObject = JsonObject()
        loginJsonObject.addProperty("username", accountEditText.text.toString())
        loginJsonObject.addProperty(
            "password",
            MD5Utils.getMD5Code(passwordEditText.text.toString())
        )
        loginJsonObject.addProperty("remember_me", true)
        loginJsonObject.addProperty("captcha", verify_code_EditText.text.toString())
        loginJsonObject.addProperty("checkKey", timestamp)

        val gson = Gson()
        val str_dataJson = gson.toJson(loginJsonObject)
        val requestBody =
            RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), str_dataJson)
        loginPresenter.login(requestBody)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.loginButton -> {
                login()
            }
            R.id.img_icon_password -> {
                showPassword = !showPassword
                hideOrshowPassword()
            }
            R.id.tv_register -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_find_password -> {
                val intent = Intent(this@LoginActivity, FindPasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_verificationCode -> {

                val account = accountEditText.text.toString().trim { it <= ' ' }
                if (Utils.isEmpty(account)) {
                    Utils.showToast(applicationContext, "??????????????????")
                    return
                }
                if (!Utils.checkMobilePhone(account)) {
                    Utils.showToast(applicationContext, "???????????????????????????")
                    return
                }

                requestAuthCodeButton.startTimer()
                requestAuthCodeButton.init("????????????", 120000)
                requestAuthCodeButton.isEnabled = false

            }
            R.id.img_guesture -> {

                val isOpenedGuesture = ShareUtil.getBoolean("isOpenedGuesture", this)
                if (isOpenedGuesture) {
                    val intent = Intent(this@LoginActivity, GesturePwdCheckActivity::class.java)
                    intent.putExtra("checkModel", ConFig.CHECK_GUESTURE_MODEL_LOGIN)
                    intent.putExtra("thirdAuthConfig", Gson().toJson(thirdAuthConfig))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "???????????????????????????,???????????????????????????", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.img_fingerprint -> {
                FingerLoginUtil.instance.FingerLogin(this, ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN)
            }


            R.id.img_wechat -> {

                if (mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener)
                } else {
                    Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_LONG).show()
                }

            }
            R.id.img_qq -> {

                if (mShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                    mShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, authListener)
                } else {
                    Toast.makeText(this, "??????????????????QQ?????????", Toast.LENGTH_LONG).show()
                }

            }

            R.id.img_phone -> {
                //??????????????????????????????????????????
                loginConfig.userNameTitle = "?????????"
                //???????????????????????????????????????
                loginConfig.passwordTitle = "???????????????"
                img_logintype_phone.visibility = View.GONE
                img_logintype_user.visibility = View.VISIBLE
                loginConfig.inputModel = ConFig.INPUTMODEL_PHONE
                alertLoginMode()
            }

            R.id.img_user -> {
                //??????????????????????????????????????????
                loginConfig.userNameTitle = "??????"
                //???????????????????????????????????????
                loginConfig.passwordTitle = "??????"
                img_logintype_phone.visibility = View.VISIBLE
                img_logintype_user.visibility = View.GONE
                loginConfig.inputModel = ConFig.INPUTMODEL_USER

                alertLoginMode()
            }

            R.id.img_verify_code -> {
                getVerifyCode()
            }

        }
    }

    /**
     * ??????????????????
     */
    private fun getVerifyCode() {
        var time = Date().time.toString()
        timestamp = time
        val params = mutableMapOf<String, String>()
        params["_t"] = time
        mPresenter.getRandomImage(time, params)
    }


    private fun hideOrshowPassword() {
        if (!showPassword) {
            img_icon_password.setImageResource(R.mipmap.login_module_icon_password_hide)
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            img_icon_password.setImageResource(R.mipmap.login_module_icon_password_show)
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
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
        thirdAuthConfig =
            Gson().fromJson(intent.getStringExtra("thirdAuthConfig"), ThirdAuthConfig::class.java)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this@LoginActivity).onActivityResult(requestCode, resultCode, data)
    }

    /**
     * ??????????????????/???????????????
     */
    private fun alertLoginMode() {
        showPassword = false
        hideOrshowPassword()
        usernameTitle.text = loginConfig.userNameTitle
        passwordTitle.text = loginConfig.passwordTitle
        accountEditText.hint = "?????????" + loginConfig.userNameTitle
        passwordEditText.hint = "?????????" + loginConfig.passwordTitle
        if (loginConfig.inputModel == ConFig.INPUTMODEL_USER) {//??????????????????
            tv_login_title.text = "????????????"
            requestAuthCodeButton.visibility = View.GONE
            img_icon_password.visibility = View.VISIBLE
            tv_find_password.visibility = View.VISIBLE
            loginButton.text = "??????"
            accountEditText.inputType = InputType.TYPE_CLASS_TEXT
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        } else if (loginConfig.inputModel == ConFig.INPUTMODEL_PHONE) {//?????????????????????
            tv_login_title.text = "????????????"
            requestAuthCodeButton.visibility = View.VISIBLE
            img_icon_password.visibility = View.GONE
            tv_find_password.visibility = View.GONE
            loginButton.text = "??????/??????"
            accountEditText.inputType = InputType.TYPE_CLASS_PHONE
            passwordEditText.inputType = InputType.TYPE_CLASS_NUMBER
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }

        accountEditText.setText("")
        passwordEditText.setText("")

    }


    private fun stringtoBitmap(base64: String?): Bitmap? {
        //?????????????????????Bitmap??????
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    override fun showRandomImage(randomImage: RandomImageBean) {
        var base64_image = randomImage.result.substring(22)
        img_verify_code.setImageBitmap(stringtoBitmap(base64_image))
    }

    override fun showError(errorMsg: String, errorCode: Int) {

    }

    override fun showLoginResult(loginBean: LoginBean) {
        val intent = Intent(this@LoginActivity, SettingActivity::class.java)
        startActivity(intent)
    }

    override fun showLoginError(errorMsg: String, errorCode: Int) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {

    }

    override fun showLoadingFailed() {

    }

    override fun showLoadingSuccess() {

    }
}