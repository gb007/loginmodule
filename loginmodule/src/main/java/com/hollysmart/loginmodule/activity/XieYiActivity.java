package com.hollysmart.loginmodule.activity;

import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.hollysmart.loginmodule.R;
import com.hollysmart.loginmodule.base.StyleAnimActivity;
import com.hollysmart.loginmodule.utils.Utils;


public class XieYiActivity extends StyleAnimActivity {
    @Override
    public int layoutResID() {
        return R.layout.login_module_activity_xieyi;
    }


    private TextView tv_title;
    private WebView wv_web;
    private String title;
    private String privacyUrl;
    private String serviceUrl;

    @Override
    public void findView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        wv_web = findViewById(R.id.wv_web);
        wv_web.setVisibility(View.VISIBLE);
        WebSettings webSettings = wv_web.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setTextZoom(100);//设置字体占屏幕宽度
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 图片大小

        //设置支持DomStorage
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wv_web.clearCache(true);
        wv_web.setVerticalScrollBarEnabled(false); //垂直不显示
        clearWebViewCache();
    }

    public void clearWebViewCache() {
        // 清除cookie即可彻底清除缓存
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
    }

    private String id;

    @Override
    public void init() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        if ("2190".equals(id)) {//服务协议
            serviceUrl = getIntent().getStringExtra("serviceUrl");
        } else if ("2191".equals(id)) {//隐私政策
            privacyUrl = getIntent().getStringExtra("privacyUrl");
        }
        tv_title.setText(title);
        getDetail(id);
    }


    private void getDetail(String id) {
        if ("2190".equals(id)) { //服务协议
            wv_web.loadUrl(serviceUrl);//"https://qnimg.daolan.com.cn/yangliufeixufuwuxieyi.html"

        } else if ("2191".equals(id)) {//隐私政策
            wv_web.loadUrl(privacyUrl);//"https://qnimg.daolan.com.cn/yangliufeixufangzhiyinsizhengce.html"

        }
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastClick())
            return;

        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

}