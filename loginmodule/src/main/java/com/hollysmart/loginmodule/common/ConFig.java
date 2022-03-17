package com.hollysmart.loginmodule.common;

public class ConFig {

    //登录方式 1为用户名密码 2为手机号验证码
    public static final int INPUTMODEL_USER = 1;
    public static final int INPUTMODEL_PHONE = 2;

    //1为校验登录； 2为开启指纹登录； 3为关闭指纹登录
    public static final int CHECK_PRINT_FINGER_MODEL_LOGIN = 1;
    public static final int CHECK_PRINT_FINGER_MODEL_OPEN = 2;
    public static final int CHECK_PRINT_FINGER_MODEL_CLOSE = 3;

    //1为校验登录；    3为关闭手势登录
    public static final int CHECK_GUESTURE_MODEL_LOGIN = 1;
    public static final int CHECK_GUESTURE_MODEL_CLOSE = 3;

    //1为手势    2为指纹
    public static final int LOGIN_SETTING_TYPE_GUESTURE = 1;
    public static final int LOGIN_SETTING_TYPE_FINGER_PRINT = 2;


}
