package com.hollysmart.loginmodule.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cxmscb.cxm.timerbuttonlib.TimerButton
import com.hollysmart.loginmodule.R
import com.hollysmart.loginmodule.utils.Validator
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var accountEditText: EditText
    lateinit var codeEditText: EditText
    lateinit var btn_verificationCode: TimerButton
    lateinit var edt_password: EditText
    lateinit var edt_password_confirm: EditText
    lateinit var registerButton: Button
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_module_activity_register)
        initView()
        initData()
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        accountEditText = findViewById(R.id.accountEditText)
        codeEditText = findViewById(R.id.codeEditText)
        btn_verificationCode = findViewById(R.id.btn_verificationCode)
        edt_password = findViewById(R.id.edt_password)
        edt_password_confirm = findViewById(R.id.edt_password_confirm)
        registerButton = findViewById(R.id.registerButton)
        btn_verificationCode.setOnClickListener(this)
        registerButton.setOnClickListener(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
        }
    }


    private fun showToast(text:String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

}