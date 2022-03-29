package com.hollysmart.loginmodule.wxapi

import android.content.Intent
import android.os.Bundle
import com.hollysmart.loginmodule.R
import com.umeng.socialize.weixin.view.WXCallbackActivity

class WXEntryActivity :WXCallbackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_module_activity_wxentry)
    }

    override fun onNewIntent(paramIntent: Intent?) {
//        super.onNewIntent(paramIntent)
    }
}