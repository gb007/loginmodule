package com.hollysmart.loginmodule.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import androidx.fragment.app.DialogFragment
import com.hollysmart.loginmodule.R
import com.hollysmart.loginmodule.activity.LoginActivity
import com.hollysmart.loginmodule.activity.SettingActivity
import com.hollysmart.loginmodule.common.ConFig
import com.hollysmart.loginmodule.eventbus.EB_Login_Type_Result
import com.hollysmart.loginmodule.gesture.GesturePwdCheckActivity
import com.hollysmart.loginmodule.utils.ShareUtil
import org.greenrobot.eventbus.EventBus
import javax.crypto.Cipher

class FingerprintDialog : DialogFragment {
    constructor() : super()

    private lateinit var mActivity: Activity
    private lateinit var fingerprintManagerCompat: FingerprintManagerCompat
    private lateinit var errorMsg: TextView
    private lateinit var cancel: TextView
    private lateinit var mCancellationSignal: CancellationSignal

    // 标识用户是否是主动取消的认证
    private var isSelfCancelled = false
    private lateinit var mCipher: Cipher

    // 1为校验登录； 2为开启指纹； 3为关闭指纹
    private var fingPrintModel: Int = 1


    fun setCipher(cipher: Cipher) {
        mCipher = cipher
    }

    fun setFingPrintModel(fingPrintModel: Int) {
        this.fingPrintModel = fingPrintModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN) {

            if (activity is LoginActivity) {
                mActivity = activity as LoginActivity
            } else if (activity is GesturePwdCheckActivity) {
                mActivity = activity as GesturePwdCheckActivity
            }

//            mActivity = activity as LoginActivity


        } else if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_OPEN || fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_CLOSE) {
            mActivity = activity as SettingActivity
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fingerprintManagerCompat = FingerprintManagerCompat.from(context!!)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_module_fingerprint_fragment, container, false)
        errorMsg = view.findViewById(R.id.fingerprint_error_tv)
        cancel = view.findViewById(R.id.fingerprint_cancel_tv)
        cancel.setOnClickListener {
            dismiss()
            stopListening()
//            mActivity.finish()
        }
        return view
    }

    /**
     * 停止指纹监听
     */
    private fun stopListening() {
        if (null != mCancellationSignal) {
            mCancellationSignal.cancel()
            isSelfCancelled = true
        }
    }

    override fun onResume() {
        super.onResume()
        startListening()
    }

    /**
     * 开始指纹监听
     */
    @SuppressLint("MissingPermission")
    private fun startListening() {
        isSelfCancelled = false
        mCancellationSignal = CancellationSignal()
        fingerprintManagerCompat.authenticate(
            FingerprintManagerCompat.CryptoObject(mCipher),
            0,
            mCancellationSignal,
            object : FingerprintManagerCompat.AuthenticationCallback() {
                override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                    if (!isSelfCancelled) {
                        errorMsg.text = errString
                        if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                            Toast.makeText(mActivity, errString, Toast.LENGTH_SHORT).show()
                            dismiss()
                            mActivity.finish()
                        }
                    }
                    if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN) {

                    } else if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_OPEN) {
                        var ebLoginTypeResult = EB_Login_Type_Result()
                        ebLoginTypeResult.loginSettingType = ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT
                        ebLoginTypeResult.isResult = false
                        EventBus.getDefault().post(ebLoginTypeResult)

                    } else if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_CLOSE) {
                        var ebLoginTypeResult = EB_Login_Type_Result()
                        ebLoginTypeResult.loginSettingType = ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT
                        ebLoginTypeResult.isResult = true
                        EventBus.getDefault().post(ebLoginTypeResult)

                    }
                }

                override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                    if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN) {


//                        LoginActivity.startActivity(mActivity as LoginActivity)

//                        if (activity is LoginActivity) {
//                            mActivity = activity as LoginActivity
//
//                            val intent = Intent(activity, SettingActivity::class.java)
//                            mActivity.startActivity(intent)
//
//                        } else if (activity is GesturePwdCheckActivity) {
//                            val intent = Intent(activity, SettingActivity::class.java)
//                            mActivity.startActivity(intent)
//                        }

                        val intent = Intent(activity, SettingActivity::class.java)
                        mActivity.startActivity(intent)

                    } else if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_OPEN) {
                        //发送登录成功消息，通知我的页面重新加载
                        var ebLoginTypeResult = EB_Login_Type_Result()
                        ebLoginTypeResult.loginSettingType = ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT
                        ebLoginTypeResult.isResult = true
                        EventBus.getDefault().post(ebLoginTypeResult)
                        ShareUtil.putBoolean("isOpenedFingerPrint", true, mActivity)
                        Toast.makeText(mActivity, "指纹登录功能开启成功", Toast.LENGTH_LONG).show()
                        dismiss()
                    } else if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_CLOSE) {
                        var ebLoginTypeResult = EB_Login_Type_Result()
                        ebLoginTypeResult.loginSettingType = ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT
                        ebLoginTypeResult.isResult = false
                        EventBus.getDefault().post(ebLoginTypeResult)
                        ShareUtil.putBoolean("isOpenedFingerPrint", false, mActivity)
                        Toast.makeText(mActivity, "指纹登录功能已关闭", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }

                override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                    errorMsg.text = helpString
                }

                override fun onAuthenticationFailed() {
                    errorMsg.text = "指纹验证失败，请重试"

                    if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN) {

                    } else if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_OPEN) {
                        var ebLoginTypeResult = EB_Login_Type_Result()
                        ebLoginTypeResult.loginSettingType = ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT
                        ebLoginTypeResult.isResult = false
                        EventBus.getDefault().post(ebLoginTypeResult)

                    } else if (fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_CLOSE) {
                        var ebLoginTypeResult = EB_Login_Type_Result()
                        ebLoginTypeResult.loginSettingType = ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT
                        ebLoginTypeResult.isResult = true
                        EventBus.getDefault().post(ebLoginTypeResult)
                    }
                }
            },
            null
        )

    }


    /**
     * 指纹传感器和摄像头一样 不能多个应用同时使用
     */
    override fun onPause() {
        super.onPause()
        stopListening()
    }

}


