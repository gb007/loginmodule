package com.hollysmart.loginmodule.common;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 登录页配置
 */
public class LoginConfig implements Serializable {

    //登录页顶部logo
    private int topLogoResourceId;

    //登录方式 1为用户名密码 2为手机号验证码
    private int inputModel = 1;

    //用户名（手机号）输入框title
    private String userNameTitle;

    //密码（验证码）输入框title
    private String passwordTitle;

    //服务器ip
    private String baseUrl;
    //服务器端口
    private String port;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getTopLogoResourceId() {
        return topLogoResourceId;
    }

    public void setTopLogoResourceId(int topLogoResourceId) {
        this.topLogoResourceId = topLogoResourceId;
    }

    public int getInputModel() {
        return inputModel;
    }

    public void setInputModel(int inputModel) {
        this.inputModel = inputModel;
    }

    public String getUserNameTitle() {
        return userNameTitle;
    }

    public void setUserNameTitle(String userNameTitle) {
        this.userNameTitle = userNameTitle;
    }

    public String getPasswordTitle() {
        return passwordTitle;
    }

    public void setPasswordTitle(String passwordTitle) {
        this.passwordTitle = passwordTitle;
    }

}
