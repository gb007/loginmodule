# loginmodule模块使用说明

## 1.在工程的根目录build.gradle中添加jitpack库依赖

````

allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
	
````

## 2.在需要引用此类库模块的build.gradle中引入依赖

 ````
 
dependencies {
	implementation 'com.github.gb007:loginmodule:Tag'
	//Gson json解析
    implementation 'com.google.code.gson:gson:2.8.9'
	}

````

## 3.在第三方（友盟，微信，QQ）登录平台创建应用，申请appid和appkey



## 4.配置第三方（微信，QQ）授权登录

### 4.1 在工程的根目录build.gradle中添加微信sdk依赖，方便在主工程中接收微信授权或分享callback

````

dependencies {
    implementation 'com.umeng.umsdk:share-wx:7.1.5' //微信完整版
    implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:6.7.0'//微信官方依赖库，必选
}

````

## 4.2 在工程的根目录build.gradle中，defaultConfig属性添加manifestPlaceholders，设置QQ_SECRET，qqappid

```` 

defaultConfig {

        applicationId "com.hollysmart.loginapplication"
        minSdk 21
        targetSdk 31
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders = [
                QQ_SECRET : "T4cChe0BvGGfRM56",
                qqappid : "1112189842"
        ]
    }
    
````

## 4.3 在工程的包名目录下创建wxapi文件夹，新建一个名为WXEntryActivity的activity继承WXCallbackActivity,并配置Android Manifest XML

````

<activity
android:name=".wxapi.WXEntryActivity"
android:configChanges="keyboardHidden|orientation|screenSize"
android:exported="true"
android:theme="@android:style/Theme.Translucent.NoTitleBar"/>

````

## 5.初始化登录模块，配置登录页面信息，隐私政策/服务协议信息，第三方（友盟，微信，QQ）授权登录信息

````

 //登录页面设置
        var loginConfig = LoginConfig()
        //登录页面顶部logo图案
        loginConfig.topLogoResourceId = R.mipmap.icon_login_head
        //输入框标题（用户名或手机号）
        loginConfig.userNameTitle = "用户名"
        //输入框标题（密码或验证码）
        loginConfig.passwordTitle = "密码"
        //登录类型（用户名或手机号）  INPUTMODEL_USER，INPUTMODEL_PHONE
        loginConfig.inputModel = ConFig.INPUTMODEL_USER
        //弹出隐私提示框设置
        var privacyConfig = PrivacyConfig()
        //隐私政策名称
        privacyConfig.privacyTitle = "杨柳飞絮防治隐私政策"
        //服务协议名称
        privacyConfig.serviceTitle = "杨柳飞絮防治服务"
        //弹窗内容
        privacyConfig.content = "感谢您信任并使用杨柳飞絮防治的产品和服务！" +
                "\n" +
                "\n" +
                "我们依据最新的法律法规、监管政策要求，更新了《杨柳飞絮防治服务协议》，特向您推送本提示，请您仔细阅读并充分理解相关条款。" +
                "\n" +
                "\n" +
                "本次我们就《杨柳飞絮防治服务协议》更新的条款主要包括：进一步明确用户不可以通过机关服务平台提供的产品和服务谈论和传播涉密及敏感信息(第三部分4)。" +
                "\n" +
                "\n" +
                "您可通过《杨柳飞絮防治服务》和《杨柳飞絮防治隐私政策》查阅完整的协议内容。"
        //隐私政策点击跳转详情页面url
        privacyConfig.privacyUrl =
            "https://qnimg.daolan.com.cn/yangliufeixufangzhiyinsizhengce.html"
        //服务协议点击跳转详情页面url
        privacyConfig.serviceUrl = "https://qnimg.daolan.com.cn/yangliufeixufuwuxieyi.html"

        //第三方登录设置
        var thirdAuthConfig = ThirdAuthConfig()
        //友盟APP_KEY
        thirdAuthConfig.umenG_APP_KEY = "23964aa317aa87760aaa122"
        //微信appid
        thirdAuthConfig.wechaT_APP_ID = "wx19d82d4e169d37c5"
        //微信app_secret
        thirdAuthConfig.wechaT_APP_SECRET = "45ed3b39c5b023ef56bea4142948a614"
        //qq app_id
        thirdAuthConfig.qQ_APP_ID = "1112189842"
        //qq app_secret
        thirdAuthConfig.qQ_APP_SECRET = "T4cChe0BvGGfRM56"

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.putExtra("loginConfig", Gson().toJson(loginConfig))
        intent.putExtra("privacyConfig", Gson().toJson(privacyConfig))
        intent.putExtra("thirdAuthConfig", Gson().toJson(thirdAuthConfig))
        startActivity(intent)
        
````

 

