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
import com.hollysmart.loginmodule.activity.FindPasswordActivity;
import com.hollysmart.loginmodule.activity.LoginActivity;
import com.hollysmart.loginmodule.activity.SettingActivity;
import com.hollysmart.loginmodule.base.GestureBaseActivity;
import com.hollysmart.loginmodule.common.ConFig;
import com.hollysmart.loginmodule.common.ThirdAuthConfig;
import com.hollysmart.loginmodule.eventbus.EB_Login_Type_Result;
import com.hollysmart.loginmodule.utils.ShareUtil;
import com.hollysmart.loginmodule.view.EasyGestureLockLayout;
import com.hollysmart.loginmodule.view.statusbar.StatusBarUtil;
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

    private TextView tv_go;
    private TextView tv_login_account;
    private TextView tv_find_password;
    private EasyGestureLockLayout layout_parent;
    private int checkModel;

    private LinearLayout ll_other_login;

    private UMAuthListener authListener;
    private UMShareAPI mShareAPI;
    private ThirdAuthConfig thirdAuthConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_layout_gesture_pwd_check);
        initView();
        initLayoutView();
//        if (checkModel == ConFig.CHECK_GUESTURE_MODEL_LOGIN) {
//            ll_other_login.setVisibility(View.VISIBLE);
//        }else{
//            ll_other_login.setVisibility(View.GONE);
//        }
    }

    private void initView() {
        //这里注意下 调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        tv_go = findViewById(R.id.tv_go);
        tv_find_password = findViewById(R.id.tv_find_password);
        tv_login_account = findViewById(R.id.tv_login_account);
        layout_parent = findViewById(R.id.layout_parent);
        ll_other_login = findViewById(R.id.ll_other_login);
        tv_find_password.setOnClickListener(this);
        tv_login_account.setOnClickListener(this);
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




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tv_find_password) {
            Intent intent = new Intent();
            intent.setClass(GesturePwdCheckActivity.this, FindPasswordActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.tv_login_account){
            finish();
        }
    }
}
