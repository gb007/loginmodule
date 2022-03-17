package com.hollysmart.loginmodule.gesture;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hollysmart.loginmodule.R;
import com.hollysmart.loginmodule.activity.SettingActivity;
import com.hollysmart.loginmodule.base.GestureBaseActivity;
import com.hollysmart.loginmodule.common.ConFig;
import com.hollysmart.loginmodule.eventbus.EB_Login_Type_Result;
import com.hollysmart.loginmodule.utils.ShareUtil;
import com.hollysmart.loginmodule.view.EasyGestureLockLayout;

import org.greenrobot.eventbus.EventBus;


/**
 * 手势密码 登录校验界面
 */
public class GesturePwdCheckActivity extends GestureBaseActivity {

    TextView tv_go;
    EasyGestureLockLayout layout_parent;
    private int checkModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_layout_gesture_pwd_check);
        initView();
        initLayoutView();
    }

    private void initView() {
        tv_go = findViewById(R.id.tv_go);
        layout_parent = findViewById(R.id.layout_parent);
        checkModel = getIntent().getIntExtra("checkModel",1);
    }


    protected void initLayoutView() {
        EasyGestureLockLayout.GestureEventCallbackAdapter adapter = new EasyGestureLockLayout.GestureEventCallbackAdapter() {
            @Override
            public void onCheckFinish(boolean succeedOrFailed) {
                String str = succeedOrFailed ? "解锁成功" : "解锁失败";
                Toast.makeText(GesturePwdCheckActivity.this, str, Toast.LENGTH_SHORT).show();
                if(succeedOrFailed){
                    if(checkModel == ConFig.CHECK_GUESTURE_MODEL_LOGIN){
                        Intent intent = new Intent();
                        intent.setClass(GesturePwdCheckActivity.this, SettingActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(checkModel == ConFig.CHECK_GUESTURE_MODEL_CLOSE){
                        ShareUtil.INSTANCE.putBoolean("isOpenedGuesture", false, GesturePwdCheckActivity.this);
                        Toast.makeText(GesturePwdCheckActivity.this, "手势登录功能已关闭", Toast.LENGTH_SHORT).show();
                        EB_Login_Type_Result ebLoginTypeResult = new  EB_Login_Type_Result();
                        ebLoginTypeResult.setLoginSettingType(ConFig.LOGIN_SETTING_TYPE_GUESTURE);
                        ebLoginTypeResult.setResult(false);
                        EventBus.getDefault().post(ebLoginTypeResult);
                        finish();
                    }
                }else{

                    if(checkModel == ConFig.CHECK_GUESTURE_MODEL_LOGIN){

                    }else if(checkModel == ConFig.CHECK_GUESTURE_MODEL_CLOSE){

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


}
