package com.hollysmart.loginmodule.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 700) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    private static final String TAG = "Utils";
    /*
     * global-phone-number = ["+"] 1*( DIGIT / written-sep ) written-sep =
     * ("-"/".")
     */
    private static final Pattern GLOBAL_PHONE_NUMBER_PATTERN = Pattern
            .compile("[\\+]?[0-9.-]+");


    /**
     * dip 转换成 px
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
    /**
     * 区号+座机号码+分机号码
     * @param fixedPhone
     * @return
     */
    public static boolean isFixedPhone(String fixedPhone){
        String reg="^(0\\d{2,3}-)?(\\d{7,8})((-|\\s)\\d{1,4})?$";
        return Pattern.matches(reg, fixedPhone);
    }

    /**
     * @return 如果是符合网址格式的字符串,返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isHttp( String str ){
        String regex = "http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*" ;
        return match( regex ,str );
    }

    /**
     * @return 如果是符合网址格式的字符串,返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isHttps( String str ){
        String regex = "https://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*" ;
        return match( regex ,str );
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex , String str ){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher( str );
        return matcher.matches();
    }

    public static int diffDay(Date lastDate) {
        Date date = new Date();
        long i = date.getTime();
        long j = lastDate.getTime();
        if (j > i) {
            return 0;
        }
        if (i == j) {
            return 0;
        }
        long diff = i - j;
        int day = (int) (diff / (24 * 60 * 60 * 1000));
        return day;
    }

    /**
     * 判断密码格式是否正确
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        String strPattern = "^[a-zA-Z0-9_-]{3,16}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }
    /**
     * 字符串是否为空判断
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || "null".equals(str);
    }

    public static String getEmptyStr(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return "暂无";
        }
        return str;
    }

    /**
     * 简单判断电话号码格式是否正确
     *
     * @param cellPhone
     * @return
     */
    public static boolean checkPhone(String cellPhone) {
        if (TextUtils.isEmpty(cellPhone)) {
            return false;
        }

        Matcher match = GLOBAL_PHONE_NUMBER_PATTERN.matcher(cellPhone);
        return match.matches();

    }

    public static boolean isPostCode(String postCode) {
        if (TextUtils.isEmpty(postCode)) {
            return false;
        } else {
            String reg = "[1-9]\\d{5}";
            return Pattern.matches(reg, postCode);
        }
    }

    /**
     * 判断手机号码格式是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkMobilePhone(String phone) {
        Pattern p = Pattern
                .compile("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$");
        Matcher matcher = p.matcher(phone);

        return matcher.matches();
    }
    /**
     * 判断身份证是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkIDCard(String phone) {
        Pattern p = Pattern
                .compile("^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$");
        Matcher matcher = p.matcher(phone);

        return matcher.matches();
    }
    /**
     * 判断身份证是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkCetificateNumber(String phone) {

        if (phone.length() == 18) {
            Pattern p = Pattern
                    .compile("^\\d{6}(18|19|20)+\\d{2}(0[1-9]|1[012])(0[1-9]|[012]\\d|3[01])\\d{3}(\\d|X)$");
            Matcher matcher = p.matcher(phone);

            if (matcher.matches()) {
                return true;
            }

        }

        if (phone.length() == 15) {
            Pattern p = Pattern
                    .compile("^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$");//修改；
            Matcher matcher = p.matcher(phone);

            return matcher.matches();

        }
        return false;
    }
    /**
     * 判断护照是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkHuZhao(String phone) {
        Pattern p = Pattern
                .compile("^1[45][0-9]{7}|([P|p|S|s]\\d{7})|([S|s|G|g]\\d{8})|([Gg|Tt|Ss|Ll|Qq|Dd|Aa|Ff]\\d{8})|([H|h|M|m]\\d{8,10})$");
        Matcher matcher = p.matcher(phone);

        return matcher.matches();
    }
    /**
     * 判断军官证是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkJunGuan(String phone) {
        Pattern p = Pattern
                .compile("^([\\u4e00-\\u9fa5]{1,}[\\u4e00-\\u9fa50-9()（）-]{5,})$");
        Matcher matcher = p.matcher(phone);

        return matcher.matches();
    }
    /**
     * 判断户口薄是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkHuKou(String phone) {
        Pattern p = Pattern
                .compile("^[a-zA-Z0-9]{3,21}$");
        Matcher matcher = p.matcher(phone);

        return matcher.matches();
    }
    /**
     * 判断驾照是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkJiaShiZheng(String phone) {
        Pattern p = Pattern
                .compile("^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$");
        Matcher matcher = p.matcher(phone);

        return matcher.matches();
    }
    /**
     * 判断外国人居留证是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkwaiGuoRen(String phone) {
        Pattern p = Pattern
                .compile("^[a-zA-Z]{3}\\d{12}$");
        Matcher matcher = p.matcher(phone);

        return matcher.matches();
    }

    /**
     * GBK转UTF-8
     *
     * @param str
     * @return
     */
    public static String toUtf8(String str) {
        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }





    /**
     *  Regular expression pattern to match most part of RFC 3987
     *  Internationalized URLs, aka IRIs.  Commonly used Unicode characters are
     *  added.
     */





    public static String getUrl_toStr(String text) {
        Pattern pattern = Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getzhongwei(String text) {
        //使用正则表达式
        Pattern pattern = Pattern.compile("[^\u4E00-\u9FA5]");
        //[\u4E00-\u9FA5]是unicode2的中文区间
        Matcher matcher = pattern.matcher(text);

        return matcher.replaceAll("");

    }





    public static boolean verifyName(String str) {
        if (str.contains("·") || str.contains("•")) {
            return str.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$");
        } else {
            return str.matches("^[\\u4e00-\\u9fa5]+$");
        }
    }

    /**
     * MD5加密算法
     *
     * @param secretKey
     * @return
     */
    public static String md5Sign(String secretKey) {
        if (Utils.isEmpty(secretKey)) {
            return "";
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(secretKey.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
            Log.e(TAG, "md5 sign error " + e.getMessage());
        }

        byte[] byteArray = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                sb.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return sb.toString();
    }

    /**
     * 格式化当前时间
     *
     * @return
     */
    public static String formatDateTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSS");
        return format.format(date);
    }

    /**
     * 根据字体大小获取字体高度
     *
     * @param fontSize
     * @return
     */
    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 将textview中的字符全角化
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 从文件读取信息
     *
     * @param path
     * @return
     */
    public static String getContentFromFile(String path) {
        StringBuffer sb = new StringBuffer();
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (true) {
                    String content = reader.readLine();
                    if (content == null) {
                        break;
                    }
                    sb.append(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 比较两个时间大小
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean diffTime(Date date1, Date date2) {
        return date1.getTime() > date2.getTime();
    }

    /**
     * 日期格式化，将date类型转成string类型
     *
     * @param date_str
     * @return
     */
    public static String dateToString(Date date_str) {
        if (date_str == null) {
            return "";
        }
        String datestr = "";
        try {
            java.text.DateFormat df = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            datestr = df.format(date_str);
        } catch (Exception ex) {
            Log.e(TAG, "date to string error " + ex.getMessage());
        }
        return datestr;
    }

    /**
     * 日期格式化，将date类型转成string类型
     *
     * @param date_str
     * @return
     */
    public static String dateToString(Date date_str, String str) {
        if (date_str == null) {
            return "";
        }
        String datestr = "";
        try {
            java.text.DateFormat df = new SimpleDateFormat(str);
            datestr = df.format(date_str);
        } catch (Exception ex) {
            Log.e(TAG, "date to string error " + ex.getMessage());
        }
        return datestr;
    }

    /**
     * 把一个字符串（yyyy-MM-dd）转化成Date
     *
     * @return String
     */
    public static Date getDateByStr(String str) {
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(str);
        } catch (Exception e) {
            System.out.println("String to Date error" + e.getMessage());
        }
        return date;
    }

    /**
     * 把一个字符串（yyyy-MM-dd）转化成Date
     *
     * @return String
     */
    public static Date getDateByStr(String str, String format) {
        Date date = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    format);
            date = sdf.parse(str);
        } catch (Exception e) {
            System.out.println("String to Date error" + e.getMessage());
        }
        return date;
    }


    /*

     * 将时间转换为时间戳

     */
    public static long dateToStamp(String s)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }


    /*

     * 将时间转换为时间戳

     */
    public static long dateToStampForyyyymmddhhmm(String s)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }

    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }


    /**
     * 获取月，天，小时和分钟组成的时间
     *
     * @param str1
     * @return
     */
    public static String getShortTime(String str1) {
        Date date = new Date();
        date = getDateByStr(str1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int mMouth = cal.get(Calendar.MONTH) + 1;
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinuts = cal.get(Calendar.MINUTE);
        String mMouthStr = mMouth + "";
        String mDayStr = mDay + "";
        String mHourStr = mHour + "";
        String mMinutsStr = mMinuts + "";
        if (mMouth < 10) {
            mMouthStr = "0" + mMouth + "";
        }
        if (mDay < 10) {
            mDayStr = "0" + mDay + "";
        }
        if (mHour < 10) {
            mHourStr = "0" + mHour + "";
        }
        if (mMinuts < 10) {
            mMinutsStr = "0" + mMinuts + "";
        }
        return (mMouthStr + "-" + mDayStr + " " + mHourStr + ":" + mMinutsStr);
    }

    /**
     * 退出一个异步任务,如果这个任务正在运行中，则这个任务会被中断
     *
     * @param task
     */
    public static void cancelTaskInterrupt(AsyncTask<?, ?, ?> task) {
        cancelTask(task, true);
    }

    /**
     * 退出一个异步任务
     *
     * @param task
     */
    public static void cancelTask(AsyncTask<?, ?, ?> task,
                                  boolean mayInterruptIfRunning) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(mayInterruptIfRunning);
        }
    }

    public static String getEndDayStr(Integer endTime) {
        long diff = endTime * 1000l - System.currentTimeMillis();
        if (diff <= 0) {
            return "已结束";
        }
        int days = (int) (diff / (24 * 60 * 60 * 1000));
        diff = diff % (24 * 60 * 60 * 100);
        int hours = (int) (diff / (60 * 60 * 1000));
        diff = diff % (60 * 60 * 100);
        int minues = (int) (diff / (60 * 1000));
        String returnStr = "";
        if (days > 0) {
            returnStr = "" + days + "天";
        }
        hours = hours + 8;
        if (hours > 24) {
            days += 1;
            hours -= 24;
        }
        if (hours > 0) {
            returnStr += "" + hours + "小时";
        } else {
            if (!Utils.isEmpty(returnStr)) {
                returnStr += "0小时";
            }
        }
        if (minues > 0) {
            returnStr += "" + minues + "分";
        } else {
            if (!Utils.isEmpty(returnStr)) {
                returnStr += "0分";
            }
        }
        return returnStr;
    }

    public static String diffTime(Date firstDate) {
        long firstTime = firstDate.getTime();
        long nowTime = new Date().getTime();
        long diff = (nowTime - firstTime) / 1000; // 秒数
        if (diff <= 60) {
            return "1分钟前";
        } else if (diff <= (60 * 60)) {
            int i = (int) (diff / 60);
            int j = (int) (diff % 60);
            if (i == 60) {
                return "1小时前";
            }
            if (j == 0) {
                return "" + i + "分钟前";
            } else {
                if (i == 59) {
                    return "1小时前";
                }
                return "" + (i + 1) + "分钟前";
            }
        } else if (diff <= (60 * 60 * 24)) {
            int i = (int) (diff / (60 * 60));
            int j = (int) (diff % (60 * 60));
            if (i == 24) {
                return "1天前";
            }
            if (j == 0) {
                return "" + i + "小时前";
            } else {
                if (i == 23) {
                    return "1天前";
                }
                return "" + (i + 1) + "小时前";
            }
        } else if (diff <= (60 * 60 * 24 * 7)) {
            int i = (int) (diff / (60 * 60 * 24));
            int j = (int) (diff % (60 * 60 * 24));
            if (i == 7) {
                return "1周前";
            }
            if (j == 0) {
                return "" + i + "天前";
            } else {
                if (i == 6) {
                    return "1周前";
                }
                return "" + (i + 1) + "天前";
            }
        } else {
            return dateToString(firstDate, "yyyy-MM-dd");
        }
    }

    /**
     * MD5加密
     *
     * @param secret_key
     * @return
     */
    public static String createSign(String secret_key) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(secret_key.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.exit(-1);
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 获取设备号id
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 判断2个字符串是否相等
     *
     * @param str
     * @param str1
     * @return
     */
    public static boolean isStringEquals(String str, String str1) {
        return str.equals(str1) || (str == str1);
    }

    /**
     * 计算字符串的字符数
     *
     * @param str
     * @return
     */
    public static int countContentLength(String str) {
        int length = 0;
        str = filterHtml(str);
        String target = "http://";
        int targetLen = target.length();
        int begin = str.indexOf(target);
        if (begin != -1) {
            while (begin != -1) {
                length += begin;
                if (begin + targetLen == str.length()) {
                    str = str.substring(begin);
                    break;
                }
                int i = begin + targetLen;
                char c = str.charAt(i);
                while (((c <= 'Z') && (c >= 'A')) || ((c <= 'z') && (c >= 'a'))
                        || ((c <= '9') && (c >= '0')) || (c == '_')
                        || (c == '.') || (c == '?') || (c == '/') || (c == '%')
                        || (c == '&') || (c == ':') || (c == '=') || (c == '-')) {
                    i++;
                    if (i < str.length()) {
                        c = str.charAt(i);
                    } else {
                        i--;
                        length--;
                        break;
                    }
                }

                length += 10;

                str = str.substring(i);
                begin = str.indexOf(target);
            }

            length += str.length();
        } else {
            length = str.length();
        }

        return length;
    }

    private static String filterHtml(String str) {
        str = str.replaceAll("<(?!br|img)[^>]+>", "").trim();
        str = unicodeToGBK(str);
        str = parseHtml(str);
        str = str.trim();

        return str;
    }

    private static String parseHtml(String newStatus) {
        String temp = "";
        String target = "<img src=";
        int begin = newStatus.indexOf(target);
        if (begin != -1) {
            while (begin != -1) {
                temp = temp + newStatus.substring(0, begin);
                int end = newStatus.indexOf(">", begin + target.length());
                // String t = newStatus.substring(begin + 10, end - 1);

                // temp = temp + (String)ImageAdapter.hashmap.get(t);

                newStatus = newStatus.substring(end + 1);
                begin = newStatus.indexOf(target);
            }
            temp = temp + newStatus;
        } else {
            temp = newStatus;
        }

        return temp;
    }

    private static String unicodeToGBK(String s) {
        String[] k = s.split(";");
        String rs = "";
        for (int i = 0; i < k.length; i++) {
            int strIndex = k[i].indexOf("&#");
            String newstr = k[i];
            if (strIndex > -1) {
                String kstr = "";
                if (strIndex > 0) {
                    kstr = newstr.substring(0, strIndex);
                    rs = rs + kstr;
                    newstr = newstr.substring(strIndex);
                }

                int m = Integer.parseInt(newstr.replace("&#", ""));
                char c = (char) m;
                rs = rs + c;
            } else {
                rs = rs + k[i];
            }
        }
        return rs;
    }

    /**
     * 悬浮框提示
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context, String message,boolean longOrShort) {
        if (longOrShort) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 悬浮框提示
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



    public static void showDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        AlertDialog dialog = builder.create();

        if(!((Activity) context ).isFinishing())
        {
            dialog.show();
        }

    }

    /**
     * 获得当前日期前多少天
     *
     * @param days
     * @return
     */
    public static String getBeforeDays(int days) {
        Date d = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        Date beforeDate = now.getTime();
        String s = Utils.dateToString(beforeDate, "yyyy-MM-dd");
        return s;
    }

    /**
     * 两个地理位置的间距 返回单位暂未处理
     *
     * @param wd1
     * @param jd1
     * @param wd2
     * @param jd2
     * @return
     */
    public static double D_jw(double wd1, double jd1, double wd2, double jd2) {
        double x, y, out;
        double PI = 3.14159265;
        double R = 6.371229 * 1e6;
        x = (jd2 - jd1) * PI * R * Math.cos(((wd1 + wd2) / 2) * PI / 180) / 180;
        y = (wd2 - wd1) * PI * R / 180;
        out = Math.hypot(x, y);
        return out / 1000;
    }

    /**
     * double转str并格式化
     *
     * @param d
     * @return
     */
    public static String doubleToStr(double d) {
        DecimalFormat df = new DecimalFormat("0.##");
        String str = df.format(d);
        return str;
    }

    /**
     * 拨打电话
     *
     * @param mContext
     * @param phoneNumber
     */
    public static void makeCall(Context mContext, String phoneNumber) {
//        if (Utils.isEmpty(phoneNum)) {
//            Utils.showToast(context, "号码为空");
//            return;
//        }
//        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNum));
//        context.startActivity(intent);

        if (Utils.isEmpty(phoneNumber) || phoneNumber.equals("暂无")) {
            Utils.showToast(mContext, "没有可拨打的电话");
        } else {
            Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
            mContext.startActivity(dialIntent);
        }

    }

    public static void makeWeb(Context mContext, String strUrl){
        if (Utils.isEmpty(strUrl) || strUrl.equals("暂无")){
            Utils.showToast(mContext, "没有可浏览的网页");
        }else {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(strUrl);
            intent.setData(content_url);
            mContext.startActivity(intent);
        }
    }


    public static String getTxtContent(Context context, String fileName) {
        try {
            StringBuilder builder = new StringBuilder();
            InputStream input = context.getResources().getAssets()
                    .open(fileName);
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader mReader = new BufferedReader(reader);
            String content = null;
            while ((content = mReader.readLine()) != null) {
                builder.append(content);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//	/**
//	 * 判断当前日期是星期几
//	 *
//	 * @param pTime
//	 *            修要判断的时间
//	 * @return dayForWeek 判断结果
//	 * @Exception 发生异常
//	 */
//	public static int dayForWeek(String pTime) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar c = Calendar.getInstance();
//		try {
//			c.setTime(format.parse(pTime));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		int dayForWeek = 0;
//		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
//			dayForWeek = 7;
//		} else {
//			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
//		}
//		Log.i(TAG, "DAY OF WEEK:" + dayForWeek);
//		return dayForWeek;
//	}

    public static void browser(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(Utils.class.getName(), "browser url is error");
        }
    }


    public static String unitType(String unitType) {
        String str = null;
        if (unitType.equals("1")) {
            str = "美景";
        } else if (unitType.equals("2")) {
            str = "美食";
        } else if (unitType.equals("3")) {
            str = "美宿";
        } else if (unitType.equals("4")) {
            str = "休闲";
        } else if (unitType.equals("5")) {
            str = "购物";
        }
        return str;
    }


    public static String dianZan(int saygood) {
        String num = null;

        if (saygood > 9999) {
            num = saygood / 10000 + "万+";
        } else
            num = saygood + "";
        return num;
    }


    public static String dizi(String fileInfo) {
        if (fileInfo == null || fileInfo.equals("")) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(fileInfo);
            String street = jsonObject.getJSONObject("address").getString("street");
            return street;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * b 转换成 mb
     *
     * @param b
     * @return
     */
    public static String bToKbToMb(long b) {
        if (b >= 1024 * 1024) {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(b / 1024f / 1024f) + "MB";
        } else if (b >= 1024) {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(b / 1024f) + "KB";
        } else {
            return b + "B";
        }
    }

    public static boolean saveFile(String AbsolutePath, Serializable info) {
        File file = new File(AbsolutePath);
        ObjectOutputStream oout;
        try {
            oout = new ObjectOutputStream(new FileOutputStream(file));
            oout.writeObject(info);
            oout.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Serializable readFile(String AbsolutePath) {
        List<Serializable> infos = new ArrayList<>();
        File file = new File(AbsolutePath);
        String[] test;
        test = file.list();
        if (test != null) {
            for (int i = 0; i < test.length; i++) {
                try {
                    FileInputStream fis = new FileInputStream(AbsolutePath + test[i]);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Serializable info = (Serializable) ois.readObject();
                    ois.close();
                    infos.add(info);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return (Serializable) infos;
    }


    public static int readFileNum(String AbsolutePath) {
        File file = new File(AbsolutePath);
        return file.list().length;
    }

    public static Serializable readFile2(String AbsolutePath) {
        try {
            FileInputStream fis = new FileInputStream(AbsolutePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Serializable info = (Serializable) ois.readObject();
            ois.close();
            return info;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }





    /***
     * 判断网络是否连通
     */


    //没有网络
    public static final int NETWORK_NONE=1;
    //移动网络
    public static final int NETWORK_MOBILE=0;
    //无线网络
    public static final int NETWORW_WIFI=2;
    //获取网络启动
    public static int getNetWorkStart(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                //连接服务 CONNECTIVITY_SERVICE
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //网络信息 NetworkInfo
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo!=null&&activeNetworkInfo.isConnected()){
            //判断是否是wifi
            if (activeNetworkInfo.getType()==(ConnectivityManager.TYPE_WIFI)){
                //返回无线网络
                return NETWORW_WIFI;
                //判断是否移动网络
            }else if (activeNetworkInfo.getType()==(ConnectivityManager.TYPE_MOBILE)){
                //返回移动网络
                return NETWORK_MOBILE;
            }
        }else {
            //没有网络
            return NETWORK_NONE;
        }
        //默认返回  没有网络
        return NETWORK_NONE;
    }


    public static Map<String, String> getBirAgeSex(String certificateNo) {


        String birthday = "";

        String age = "";

        String sexCode = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = certificateNo.toCharArray();
        boolean flag = true;


//        if (number.length == 15) {
//            for (int x = 0; x < number.length; x++) {
//
//                if (!flag)
//                    return new HashMap<String, String>();
//                flag = Character.isDigit(number[x]);
//            }
//
//        } else if (number.length == 18) {
//
//            for (int x = 0; x < number.length; x++) {
//
//                if (!flag)
//                    return new HashMap<String, String>();
//                flag = Character.isDigit(number[x]);
//            }
//
//        }


        if ( certificateNo.length() == 15) {

            birthday = "19" + certificateNo.substring(6, 8) + "-" + certificateNo.substring(8, 10) + "-"

                    + certificateNo.substring(10, 12);

            sexCode = Integer.parseInt(certificateNo.substring(certificateNo.length() - 3))

                    % 2 == 0 ? "F" : "M";

            age = (year - Integer.parseInt("19" + certificateNo.substring(6, 8))) + "";
        } else if (certificateNo.length() == 18) {

            birthday = certificateNo.substring(6, 10) + "-" + certificateNo.substring(10, 12) + "-"

                    + certificateNo.substring(12, 14);

            String substring = certificateNo.substring(certificateNo.length() - 2, certificateNo.length()-1);

            sexCode = Integer.parseInt(substring)

                    % 2 == 0 ? "F" : "M";

            age = (year - Integer.parseInt(certificateNo.substring(6, 10))) + "";
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("birthday", birthday);
        map.put("age", age);

        map.put("sexCode", sexCode);
        return map;
    }




    //复杂版处理  (适配多种API)
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
        if (sdkVersion < 19) return getRealPathFromUri_Api11To18(context, uri);
        else return getRealPathFromUri_AboveApi19(context, uri);
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    contentUri = MediaStore.Files.getContentUri("external");
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }


        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    @SuppressLint("Range")
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        //这个有两个包不知道是哪个。。。。不过这个复杂版一般用不到
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    @SuppressLint("Range")
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.MediaColumns.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }

    public static Map<String, String> splitStr(String url) {
//        Mlog.d("url=====" + url);
        Map<String, String> map = new HashMap<String, String>();
        int i1 = url.indexOf("?");
        String substring = url.substring(i1 + 1);
        try {
            if (substring != null) {
                String[] valuse = substring.split("&");
                if (valuse.length > 0) {
                    for (int i = 0; i < valuse.length; i++) {
                        String s = valuse[i];
                        String[] split = s.split("=");
                        map.put(split[0], URLDecoder.decode(split[1], "UTF-8"));

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

//    public static boolean isInstallApp(String packageName) {
//        final PackageManager packageManager = MyApp.getContext().getPackageManager();// 获取packagemanager
//        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
//        if (pinfo != null) {
//            for (int i = 0; i < pinfo.size(); i++) {
//                String pn = pinfo.get(i).packageName;
//                if (pn.equals(packageName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }




}
