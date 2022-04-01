package com.hollysmart.loginmodule.gesture;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.hollysmart.loginmodule.R;
import com.hollysmart.loginmodule.activity.LoginActivity;
import com.hollysmart.loginmodule.activity.SettingActivity;
import com.hollysmart.loginmodule.base.GestureBaseActivity;
import com.hollysmart.loginmodule.common.ConFig;
import com.hollysmart.loginmodule.common.ThirdAuthConfig;
import com.hollysmart.loginmodule.eventbus.EB_Login_Type_Result;
import com.hollysmart.loginmodule.utils.ShareUtil;
import com.hollysmart.loginmodule.view.EasyGestureLockLayout;
import com.kpa.fingerprintdemo.FingerLoginUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


/**
 * 手势密码 登录校验界面
 */
public class GesturePwdCheckActivity extends GestureBaseActivity implements View.OnClickListener {

    TextView tv_go;
    EasyGestureLockLayout layout_parent;
    private int checkModel;

    private LinearLayout ll_other_login;
    private LinearLayout ll_logintypeUser;
    private LinearLayout ll_logintypeFinger;
    private LinearLayout ll_logintype_wechat;
    private LinearLayout ll_logintype_qq;
    private UMAuthListener authListener;
    private UMShareAPI mShareAPI;
    private ThirdAuthConfig thirdAuthConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_layout_gesture_pwd_check);
        initView();
        initLayoutView();
        if (checkModel == ConFig.CHECK_GUESTURE_MODEL_LOGIN) {
            initThirdAuth();
        }
    }

    private void initView() {
        tv_go = findViewById(R.id.tv_go);
        layout_parent = findViewById(R.id.layout_parent);
        ll_other_login = findViewById(R.id.ll_other_login);
        ll_logintypeUser = findViewById(R.id.ll_logintypeUser);
        ll_logintypeFinger = findViewById(R.id.ll_logintypeFinger);
        ll_logintype_wechat = findViewById(R.id.ll_logintype_wechat);
        ll_logintype_qq = findViewById(R.id.ll_logintype_qq);
        ll_logintypeUser.setOnClickListener(this);
        ll_logintypeFinger.setOnClickListener(this);
        ll_logintype_wechat.setOnClickListener(this);
        ll_logintype_qq.setOnClickListener(this);
        checkModel = getIntent().getIntExtra("checkModel", 1);
        if (checkModel == ConFig.CHECK_GUESTURE_MODEL_LOGIN) {
            ll_other_login.setVisibility(View.VISIBLE);
            thirdAuthConfig = new Gson().fromJson(getIntent().getStringExtra("thirdAuthConfig"), ThirdAuthConfig.class);
        } else {
            ll_other_login.setVisibility(View.GONE);
        }
    }


    protected void initLayoutView() {
        EasyGestureLockLayout.GestureEventCallbackAdapter adapter = new EasyGestureLockLayout.GestureEventCallbackAdapter() {
            @Override
            public void onCheckFinish(boolean succeedOrFailed) {
                String str = succeedOrFailed ? "解锁成功" : "解锁失败";
                Toast.makeText(GesturePwdCheckActivity.this, str, Toast.LENGTH_SHORT).show();
                if (succeedOrFailed) {
                    if (checkModel == ConFig.CHECK_GUESTURE_MODEL_LOGIN) {
                        Intent intent = new Intent();
                        intent.setClass(GesturePwdCheckActivity.this, SettingActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (checkModel == ConFig.CHECK_GUESTURE_MODEL_CLOSE) {
                        ShareUtil.INSTANCE.putBoolean("isOpenedGuesture", false, GesturePwdCheckActivity.this);
                        Toast.makeText(GesturePwdCheckActivity.this, "手势登录功能已关闭", Toast.LENGTH_SHORT).show();
                        EB_Login_Type_Result ebLoginTypeResult = new EB_Login_Type_Result();
                        ebLoginTypeResult.setLoginSettingType(ConFig.LOGIN_SETTING_TYPE_GUESTURE);
                        ebLoginTypeResult.setResult(false);
                        EventBus.getDefault().post(ebLoginTypeResult);
                        finish();
                    }
                } else {

                    if (checkModel == ConFig.CHECK_GUESTURE_MODEL_LOGIN) {

                    } else if (checkModel == ConFig.CHECK_GUESTURE_MODEL_CLOSE) {

                    }
                }
            }

            @Override
            public void onSwipeMore() {
                //执行动画
                animate(tv_go);
            }

            @Override
            public void onToast(String s, int textColor) {
                tv_go.setText(s);
                if (textColor != 0)
                    tv_go.setTextColor(textColor);
                if (textColor == 0xffFF3232) {
                    animate(tv_go);
                }
            }
        };
        layout_parent.setGestureFinishedCallback(adapter);

        //使用check模式
        layout_parent.switchToCheckMode(parsePwdStr(getPwd()), 5);//校验密码
    }

    private void initThirdAuth() {

        //umeng设置
        UMConfigure.init(
                this, thirdAuthConfig.getUMENG_APP_KEY(), "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
        );
        // 微信设置
        PlatformConfig.setWeixin(thirdAuthConfig.getWECHAT_APP_ID(), thirdAuthConfig.getWECHAT_APP_SECRET());
        PlatformConfig.setWXFileProvider("com.hollysmart.loginmodule.fileprovider");
        // QQ设置
        PlatformConfig.setQQZone(thirdAuthConfig.getQQ_APP_ID(), thirdAuthConfig.getQQ_APP_SECRET());
        PlatformConfig.setQQFileProvider("com.hollysmart.loginmodule.fileprovider");

//        UMConfigure.init(
//                this, "23964aa317aa87760aaa122", "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
//        );
//
//        // 微信设置
//        PlatformConfig.setWeixin("wx19d82d4e169d37c5", "45ed3b39c5b023ef56bea4142948a614");
//        PlatformConfig.setWXFileProvider("com.hollysmart.loginmodule.fileprovider");
//        // QQ设置
//        PlatformConfig.setQQZone("1112189842", "T4cChe0BvGGfRM56");
//        PlatformConfig.setQQFileProvider("com.hollysmart.loginmodule.fileprovider");

        authListener = new UMAuthListener() {

            /**
             * @desc 授权开始的回调
             * @param share_media 平台名称
             */

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            /**
             * @desc 授权成功的回调
             * @param share_media 平台名称
             * @param i 行为序号，开发者用不上
             * @param map 用户资料返回
             */
            //openid -> o-dt96X-Vs4Z-u9Br0xDY3_yUTUI
            //uid -> ox1fxwMeFSMKxKpfpnU2HLZR2S9c
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Toast.makeText(GesturePwdCheckActivity.this, "成功了", Toast.LENGTH_LONG).show();
            }

            /**
             * @desc 授权失败的回调
             * @param share_media 平台名称
             * @param i 行为序号，开发者用不上
             * @param throwable 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(GesturePwdCheckActivity.this, "失败：" + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            /**
             * @desc 授权取消的回调
             * @param share_media 平台名称
             * @param i 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toast.makeText(GesturePwdCheckActivity.this, "取消了", Toast.LENGTH_LONG).show();
            }
        };

        mShareAPI = UMShareAPI.get(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ll_logintypeUser) {

//            Intent intent = new Intent();
//            intent.setClass(GesturePwdCheckActivity.this, LoginActivity.class);
//            startActivity(intent);
            finish();

        } else if (view.getId() == R.id.ll_logintypeFinger) {

            FingerLoginUtil.Companion.getInstance().FingerLogin(this, ConFig.CHECK_PRINT_FINGER_MODEL_LOGIN);

        } else if (view.getId() == R.id.ll_logintype_wechat) {

            if (mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
            } else {
                Toast.makeText(this, "请先下载安装微信客户端", Toast.LENGTH_LONG).show();
            }

        } else if (view.getId() == R.id.ll_logintype_qq) {

            if (mShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, authListener);
            } else {
                Toast.makeText(this, "请先下载安装QQ客户端", Toast.LENGTH_LONG).show();
            }

        }
    }
}
