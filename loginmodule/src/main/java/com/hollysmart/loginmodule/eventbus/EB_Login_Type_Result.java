package com.hollysmart.loginmodule.eventbus;

import java.io.Serializable;

public class EB_Login_Type_Result implements Serializable {

    //1为手势    2为指纹
    private int loginSettingType;
    private boolean result;

    public int getLoginSettingType() {
        return loginSettingType;
    }

    public void setLoginSettingType(int loginSettingType) {
        this.loginSettingType = loginSettingType;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
