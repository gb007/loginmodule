package com.hollysmart.loginmodule.common;

import java.io.Serializable;

public class ThirdAuthConfig implements Serializable {

    private String UMENG_APP_KEY;
    private String WECHAT_APP_ID;
    private String WECHAT_APP_SECRET;
    private String QQ_APP_ID;
    private String QQ_APP_SECRET;

    public String getUMENG_APP_KEY() {
        return UMENG_APP_KEY;
    }

    public void setUMENG_APP_KEY(String UMENG_APP_KEY) {
        this.UMENG_APP_KEY = UMENG_APP_KEY;
    }

    public String getWECHAT_APP_ID() {
        return WECHAT_APP_ID;
    }

    public void setWECHAT_APP_ID(String WECHAT_APP_ID) {
        this.WECHAT_APP_ID = WECHAT_APP_ID;
    }

    public String getWECHAT_APP_SECRET() {
        return WECHAT_APP_SECRET;
    }

    public void setWECHAT_APP_SECRET(String WECHAT_APP_SECRET) {
        this.WECHAT_APP_SECRET = WECHAT_APP_SECRET;
    }

    public String getQQ_APP_ID() {
        return QQ_APP_ID;
    }

    public void setQQ_APP_ID(String QQ_APP_ID) {
        this.QQ_APP_ID = QQ_APP_ID;
    }

    public String getQQ_APP_SECRET() {
        return QQ_APP_SECRET;
    }

    public void setQQ_APP_SECRET(String QQ_APP_SECRET) {
        this.QQ_APP_SECRET = QQ_APP_SECRET;
    }
}
