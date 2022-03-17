package com.kpa.fingerprintdemo

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.FragmentActivity
import com.hollysmart.loginmodule.common.ConFig
import com.hollysmart.loginmodule.dialog.FingerprintDialog
import com.hollysmart.loginmodule.eventbus.EB_Login_Type_Result
import com.hollysmart.loginmodule.utils.ShareUtil
import org.greenrobot.eventbus.EventBus
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/** * @name FingerprintDemo-master
 * @class name：com.kpa.fingerprintdemo *
@class describe *
@anthor chuangcui 邮箱:919953719@qq.com
 * @time 2021/6/10 5:08 下午
 */

class FingerLoginUtil {

    private lateinit var keyStore: KeyStore
    private val DEFAULT_KEY_NAME = "default_key"

    // 1为校验登录； 2为开启指纹； 3为关闭指纹
    private var fingPrintModel = 1

    companion object {
        //单列模式
        val instance: FingerLoginUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FingerLoginUtil()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun FingerLogin(context: Context?, fingPrintModel: Int) {
        this.fingPrintModel = fingPrintModel
        //判断是否支持该功能
        if (supportFingerprint(context)) {
            initKey() //生成一个对称加密的key
            initCipher(context) //生成一个Cipher对象
        } else {
            var ebLoginTypeResult = EB_Login_Type_Result()
            ebLoginTypeResult.loginSettingType = ConFig.LOGIN_SETTING_TYPE_FINGER_PRINT
            ebLoginTypeResult.isResult = false
            EventBus.getDefault().post(ebLoginTypeResult)
        }
    }

    fun supportFingerprint(context: Context?): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(context, "系统不支持指纹功能", Toast.LENGTH_SHORT).show()
            return false
        } else {
            val keyguardManager = context?.getSystemService(KeyguardManager::class.java)
            val managerCompat = FingerprintManagerCompat.from(context!!)
            if (!managerCompat.isHardwareDetected) {
                Toast.makeText(context, "系统不支持指纹功能", Toast.LENGTH_SHORT).show()
                return false
            } else if (!keyguardManager!!.isKeyguardSecure) {
                Toast.makeText(context, "屏幕未设置锁屏 请先设置锁屏并添加一个指纹", Toast.LENGTH_SHORT).show()
                return false
            } else if (!managerCompat.hasEnrolledFingerprints()) {
                Toast.makeText(context, "至少在系统中添加一个指纹", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    fun initCipher(mContext: Context?) {
        val key = keyStore.getKey(DEFAULT_KEY_NAME, null) as SecretKey
        val cipher = Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
        cipher.init(Cipher.ENCRYPT_MODE, key)

        if(fingPrintModel == ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN){//登录
            if (mContext != null) {
                val isOpenedFingerPrint = ShareUtil.getBoolean("isOpenedFingerPrint", mContext)
                if (isOpenedFingerPrint) {
                    showFingerPrintDialog(cipher, mContext)
                } else {
                    Toast.makeText(mContext, "未设置指纹登录功能,请使用其它方式登录", Toast.LENGTH_SHORT).show()
                }
            }
        }else{//开启或者关闭指纹登录
            showFingerPrintDialog(cipher, mContext)
        }
    }


    private fun showFingerPrintDialog(cipher: Cipher?, mContext: Context?) {
        val fingerprintDialog = FingerprintDialog()
        if (cipher != null) {
            fingerprintDialog.setCipher(cipher)
        }
        fingerprintDialog.setFingPrintModel(fingPrintModel)
        var activity = mContext as FragmentActivity
        fingerprintDialog.show(activity.supportFragmentManager, "fingerprint")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun initKey() {
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val builder = KeyGenParameterSpec.Builder(
            DEFAULT_KEY_NAME,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setUserAuthenticationRequired(true)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        keyGenerator.init(builder.build())
        keyGenerator.generateKey()
    }

}