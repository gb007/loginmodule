package com.hollysmart.loginmodule.gesture;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.hollysmart.loginmodule.R;
import com.hollysmart.loginmodule.base.GestureBaseActivity;
import com.hollysmart.loginmodule.common.ConFig;
import com.hollysmart.loginmodule.eventbus.EB_Login_Type_Result;
import com.hollysmart.loginmodule.utils.ShareUtil;
import com.hollysmart.loginmodule.view.EasyGestureLockLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 手势密码 设置界面
 */
public class GesturePwdSettingActivity extends GestureBaseActivity {

    EasyGestureLockLayout layout_small;
    TextView tv_go;
    TextView tv_redraw;
    EasyGestureLockLayout layout_parent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_layout_gesture_pwd_setting);
        initView();
        initLayoutView();
    }

    private void initView() {
        tv_go = findViewById(R.id.tv_go);
        layout_parent = findViewById(R.id.layout_parent);
        layout_small = findViewById(R.id.layout_small);
        tv_redraw = findViewById(R.id.tv_redraw);
    }


    protected void initLayoutView() {

        EasyGestureLockLayout.GestureEventCallbackAdapter adapter = new EasyGestureLockLayout.GestureEventCallbackAdapter() {
            @Override
            public void onSwipeFinish(List<Integer> pwd) {
                layout_small.refreshPwdKeyboard(pwd);//通知另一个小密码盘，将密码点展示出来，但是不展示轨迹线
                tv_redraw.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResetFinish(List<Integer> pwd) {// 当密码设置完成
                savePwd(showPwd("showGesturePwdInt", pwd));//保存密码到本地
                Toast.makeText(GesturePwdSettingActivity.this, "密码已保存", Toast.LENGTH_SHORT).show();
                ShareUtil.INSTANCE.putBoolean("isOpenedGuesture", true, GesturePwdSettingActivity.this);
                Toast.makeText(GesturePwdSettingActivity.this, "手势登录功能已开启", Toast.LENGTH_SHORT).show();
                EB_Login_Type_Result ebLoginTypeResult = new  EB_Login_Type_Result();
                ebLoginTypeResult.setLoginSettingType(ConFig.LOGIN_SETTING_TYPE_GUESTURE);
                ebLoginTypeResult.setResult(true);
                EventBus.getDefault().post(ebLoginTypeResult);
                finish();
            }

            @Override
            public void onCheckFinish(boolean succeedOrFailed) {
                String str = succeedOrFailed ? "解锁成功" : "解锁失败";
                Toast.makeText(GesturePwdSettingActivity.this, str, Toast.LENGTH_SHORT).show();
                if (succeedOrFailed) {//如果解锁成功，则切换到set模式
                    layout_parent.switchToResetMode();
                } else {
                    onCheckFailed();
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

        //使用rest模式
        layout_parent.switchToResetMode();

        tv_redraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_parent.initCurrentTimes();
                tv_redraw.setVisibility(View.INVISIBLE);
                layout_small.refreshPwdKeyboard(null);
                tv_go.setText("请重新绘制");
            }
        });
    }



}
