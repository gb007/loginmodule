package com.hollysmart.loginmodule.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hollysmart.loginmodule.R;
import com.hollysmart.loginmodule.activity.XieYiActivity;


public class ScreenViewDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView web_message;
    private TextView tv_ok;
    private TextView tv_back;
    private OnClickListener onClickListener;

    private String privacyTitle;
    private String serviceTitle;
    private String content;
    private String privacyUrl;
    private String serviceUrl;


    public ScreenViewDialog(@NonNull Context context, int themeResId, String privacyTitle,
                            String serviceTitle, String content, String privacyUrl,
                            String serviceUrl) {
        super(context, themeResId);
        this.mContext = context;
        this.privacyTitle = privacyTitle;
        this.serviceTitle = serviceTitle;
        this.content = content;
        this.privacyUrl = privacyUrl;
        this.serviceUrl = serviceUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.login_module_dialog_screen_privacy_agreenment, null);
        setContentView(mView);

        web_message = mView.findViewById(R.id.web_message);
        tv_ok = mView.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
        tv_back = mView.findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

//        web_message.setText(getClickableHtml(message));
//        //这一句很重要，否则ClickableSpan内的onClick方法将无法触发！！
//        web_message.setMovementMethod(LinkMovementMethod.getInstance());
        setContent();
    }


    private void setContent() {
//        String string = "感谢您信任并使用杨柳飞絮防治的产品和服务！" +
//                "\n" +
//                "\n" +
//                "我们依据最新的法律法规、监管政策要求，更新了《杨柳飞絮防治服务协议》，特向您推送本提示，请您仔细阅读并充分理解相关条款。" +
//                "\n" +
//                "\n" +
//                "本次我们就《杨柳飞絮防治服务协议》更新的条款主要包括：进一步明确用户不可以通过机关服务平台提供的产品和服务谈论和传播涉密及敏感信息(第三部分4)。" +
//                "\n" +
//                "\n" +
//                "您可通过《杨柳飞絮防治服务》和《杨柳飞絮防治隐私政策》查阅完整的协议内容。";
//        String key1 = "过《杨柳飞絮防治服务》和";
//        String key2 = "《杨柳飞絮防治隐私政策》";

        String string = content;

        String key1 = "《" + serviceTitle + "》";
        String key2 = "《" + privacyTitle + "》";

        int index1 = string.indexOf(key1);
        int index2 = string.indexOf(key2);

        //需要显示的字串
        SpannableString spannedString = new SpannableString(string);
        //设置点击字体颜色
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getContext().getResources().getColor(R.color.login_module_blue_color));
        spannedString.setSpan(colorSpan1, index1 + 1, index1 + key1.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getContext().getResources().getColor(R.color.login_module_blue_color));
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击字体大小
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(16, true);
        spannedString.setSpan(sizeSpan1, index1 + 1, index1 + key1.length() - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(16, true);
        spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {

//                String Licensing = BuildConfig.SHOUYE_URL + "/UserAgreement/Licensing.html";
//                WfcWebViewActivity.loadUrl(getContext(), "用户协议", Licensing);
                Intent intent = new Intent(mContext, XieYiActivity.class);
                intent.putExtra("id", "2190");
                intent.putExtra("title", serviceTitle);
                intent.putExtra("serviceUrl", serviceUrl);
                mContext.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {

//                String Privacy = BuildConfig.SHOUYE_URL + "/UserAgreement/Privacy.html";
//                WfcWebViewActivity.loadUrl(getContext(), "隐私政策", Privacy);
                Intent intent = new Intent(mContext, XieYiActivity.class);
                intent.putExtra("id", "2191");
                intent.putExtra("title", privacyTitle);
                intent.putExtra("privacyUrl", privacyUrl);
                mContext.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置点击后的颜色为透明，否则会一直出现高亮
        web_message.setHighlightColor(Color.TRANSPARENT);
        //开始响应点击事件
        web_message.setMovementMethod(LinkMovementMethod.getInstance());
        web_message.setText(spannedString);
    }


    /**
     * 格式化超链接文本内容并设置点击处理
     */
    private CharSequence getClickableHtml(String html) {
        Spanned spannedHtml = Html.fromHtml(html);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
    }

    /**
     * 设置点击超链接对应的处理内容
     */
    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);

        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {
                String url = urlSpan.getURL();
                Log.d("拦截到的 URL", "拦截到的 URL = " + url);
                if (url.contains("fuwu")) {
//                    String Licensing = BuildConfig.SHOUYE_URL + "/UserAgreement/Licensing.html";
//                    WfcWebViewActivity.loadUrl(getContext(), "用户协议", Licensing);
                    Intent intent = new Intent(mContext, XieYiActivity.class);
                    intent.putExtra("id", "2190");
                    intent.putExtra("title", serviceTitle);
                    intent.putExtra("serviceUrl", serviceUrl);
                    mContext.startActivity(intent);

                } else if (url.contains("yinsi")) {
//                    String Privacy = BuildConfig.SHOUYE_URL + "/UserAgreement/Privacy.html";
//                    WfcWebViewActivity.loadUrl(getContext(), "隐私政策", Privacy);
                    Intent intent = new Intent(mContext, XieYiActivity.class);
                    intent.putExtra("id", "2191");
                    intent.putExtra("title", privacyTitle);
                    intent.putExtra("privacyUrl", privacyUrl);
                    mContext.startActivity(intent);

                }
            }
        };

        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }


    public void setOnClickOkListener(OnClickListener onClickOkListener) {
        this.onClickListener = onClickOkListener;
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            int id = v.getId();
            if (id == R.id.tv_ok) {
                onClickListener.OnClickOK(v);
            } else if (id == R.id.tv_back) {
                onClickListener.OnClickBack(v);
            }
        }
        cancel();
    }

    public interface OnClickListener {
        void OnClickOK(View view);

        void OnClickBack(View view);
    }


}