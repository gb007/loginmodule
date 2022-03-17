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
import com.hollysmart.loginmodule.utils.ShareUtil
import com.hollysmart.loginmodule.utils.Utils
import com.kpa.fingerprintdemo.FingerLoginUtil
import com.hollysmart.loginmodule.gesture.GesturePwdCheckActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var img_top_logo: ImageView
    lateinit var usernameTitle: TextView
    lateinit var passwordTitle: TextView
    lateinit var accountEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var requestAuthCodeButton: TimerButton
    lateinit var ll_logintypeGesture: LinearLayout
    lateinit var ll_logintypeFinger: LinearLayout
    lateinit var loginButton: Button

    lateinit var loginConfig: LoginConfig

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_module_activity_login)
        initView()
        setLoginModel()
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
        loginButton.setOnClickListener(this)
        requestAuthCodeButton.setOnClickListener(this)
        ll_logintypeGesture.setOnClickListener(this)
        ll_logintypeFinger.setOnClickListener(this)
        getExtra()
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
    private fun setLoginModel(){

       var isFingerOpened = ShareUtil.getBoolean("isOpenedFingerPrint", this)
       var isGestureOpened = ShareUtil.getBoolean("isOpenedGuesture", this)

        if(isFingerOpened){
            FingerLoginUtil.instance.FingerLogin(this,ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN)
        }
        if(isGestureOpened){
            val intent = Intent(this@LoginActivity, GesturePwdCheckActivity::class.java)
            intent.putExtra("checkModel",ConFig.CHECK_GUESTURE_MODEL_LOGIN)
            startActivity(intent)
        }

    }




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.loginButton -> {
                val intent = Intent(this@LoginActivity, SettingActivity::class.java)
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
                    intent.putExtra("checkModel",ConFig.CHECK_GUESTURE_MODEL_LOGIN)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "未设置手势登录功能,请使用其它方式登录", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.ll_logintypeFinger -> {
                FingerLoginUtil.instance.FingerLogin(this,ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN)
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
    }


}