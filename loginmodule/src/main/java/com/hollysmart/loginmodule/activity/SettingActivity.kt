package com.hollysmart.loginmodule.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.hollysmart.loginmodule.R
import com.hollysmart.loginmodule.common.ConFig
import com.hollysmart.loginmodule.eventbus.EB_Login_Type_Result
import com.hollysmart.loginmodule.gesture.GesturePwdCheckActivity
import com.hollysmart.loginmodule.gesture.GesturePwdSettingActivity
import com.hollysmart.loginmodule.utils.ShareUtil
import com.kpa.fingerprintdemo.FingerLoginUtil
import com.kyleduo.switchbutton.SwitchButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private var isFingerOpened: Boolean = false
    private var isGestureOpened: Boolean = false
    private lateinit var tv_fanhui: ImageView
    private lateinit var sbtn_gestureSwitch: SwitchButton
    private lateinit var sbtn_finger: SwitchButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_module_activity_setting)
        EventBus.getDefault().register(this)
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initView() {
        sbtn_gestureSwitch = findViewById(R.id.sbtn_gestureSwitch)
        sbtn_finger = findViewById(R.id.sbtn_finger)
        tv_fanhui = findViewById(R.id.tv_fanhui)
        sbtn_gestureSwitch.setOnClickListener(this)
        sbtn_finger.setOnClickListener(this)
        tv_fanhui.setOnClickListener(this)

    }

    private fun initData() {

        isFingerOpened = ShareUtil.getBoolean("isOpenedFingerPrint", this)
        isGestureOpened = ShareUtil.getBoolean("isOpenedGuesture", this)
        sbtn_finger.isChecked = isFingerOpened
        sbtn_gestureSwitch.isChecked = isGestureOpened

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginTypeSettingResult(result: EB_Login_Type_Result) {
        if(result.loginSettingType == ConFig.LOGIN_SETTING_TYPE_GUESTURE){//1为手势
            sbtn_gestureSwitch.isChecked = result.isResult
        }else if(result.loginSettingType == ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT){//2为指纹
            sbtn_finger.isChecked = result.isResult
        }
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_fanhui -> {
                finish()
            }
            R.id.sbtn_gestureSwitch -> {
                if (isGestureOpened) {//关闭手势登录
                    val intent = Intent(this@SettingActivity, GesturePwdCheckActivity::class.java)
                    intent.putExtra("checkModel",ConFig.CHECK_GUESTURE_MODEL_CLOSE)
                    startActivity(intent)
                } else {//开启手势登陆
                    val intent = Intent(this@SettingActivity, GesturePwdSettingActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.sbtn_finger -> {
                if (isFingerOpened) {//关闭指纹登录
                    FingerLoginUtil.instance.FingerLogin(this, ConFig.CHECK_PRINT_FINGER_MODEL_CLOSE)
                } else {//开启指纹登录
                    FingerLoginUtil.instance.FingerLogin(this, ConFig.CHECK_PRINT_FINGER_MODEL_OPEN)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}