package com.hollysmart.loginmodule.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cxmscb.cxm.timerbuttonlib.TimerButton
import com.hollysmart.loginmodule.R
import com.hollysmart.loginmodule.utils.Validator
import com.hollysmart.loginmodule.view.statusbar.StatusBarUtil
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var img_back: ImageView
    lateinit var accountEditText: EditText
    lateinit var codeEditText: EditText
    lateinit var btn_verificationCode: TimerButton
    lateinit var edt_password: EditText
    lateinit var edt_password_confirm: EditText
    lateinit var registerButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_module_activity_register)
        initView()
        initData()
    }

    private fun initView() {
        //这里注意下 调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        //设置状态栏透明
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        img_back = findViewById(R.id.img_back)
        accountEditText = findViewById(R.id.accountEditText)
        codeEditText = findViewById(R.id.codeEditText)
        btn_verificationCode = findViewById(R.id.btn_verificationCode)
        edt_password = findViewById(R.id.edt_password)
        edt_password_confirm = findViewById(R.id.edt_password_confirm)
        registerButton = findViewById(R.id.registerButton)
        btn_verificationCode.setOnClickListener(this)
        registerButton.setOnClickListener(this)
        img_back.setOnClickListener(this)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    private fun initData() {


    }

    private fun rigster(){

        if(accountEditText.text.isEmpty()){
            showToast("邮箱或手机不能为空")
            return
        }

        if(codeEditText.text.isEmpty()){
            showToast("验证码不能为空")
            return
        }

        if(edt_password.text.isEmpty()){
            showToast("密码不能为空")
            return
        }

        if(edt_password_confirm.text.isEmpty()){
            showToast("确认密码不能为空")
            return
        }

        if(!edt_password.text.toString().equals(edt_password_confirm.text.toString())){
            showToast("两次输入密码不一致")
            return
        }

        if(!Validator.isMobile(accountEditText.text.toString()) && !Validator.isEmail(accountEditText.text.toString())){
            showToast("请输入正确的邮箱或手机号码")
            return
        }


    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_verificationCode -> {
                btn_verificationCode.startTimer()
                btn_verificationCode.init("重新获取", 120000)

            }

            R.id.registerButton -> {
                rigster()
            }
            R.id.img_back ->{
                finish()
            }
        }
    }


    private fun showToast(text:String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

}