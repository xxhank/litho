package com.le123.ysdq.ng;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import com.agx.scaffold.JxMd5Utils;
import com.agx.scaffold.JxPreferenceManager;
import com.agx.scaffold.JxSettings;
import com.agx.scaffold.JxTextUtils;
import com.lvideo.http.ChannelUtil;
import com.lvideo.http.PacketVersion;
import com.lvideo.http.utils.TokenUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class AppInfo {
    public static String FIELD_SIG           = "sig";
    public static String DEVICE_ID;
    static        String FIELD_TS            = "ts";
    static        String PLATTYPE_VAULE      = "aphone";
    static        String PLATFORM_IN_UPGRADE = "Le123Plat002";
    static        String FIELD_AUID          = "auid";
    static        String FIELD_LDID          = "ldid";
    static        String FIELD_LDID_INFO     = "ldid-data";
    static        String FIELD_DEVICE_ID     = "deviceIdKey";
    static        String UUID;
    static        String DEV_IMSI;
    static        String DEV_IMEI;
    static        String AUID;
    static        String DEV_MAC;
    static        String OS_VERSION;
    static        String APP_CHANNEL;
    static        String APP_VERSION;
    static        String OS_MODEL;
    static        String DEV_RESOLUTION;
    static        String LDID;
    static        String OS_SDK;

    // call in application's onCreate method
    public static void initialize(Context context) {
        APP_CHANNEL = getClientChannel(context);
        APP_VERSION = getClientVersionName();
        LDID = getLdid128(context);
        UUID = JxSettings.get("uuid", java.util.UUID.randomUUID().toString());
        AUID = getAUID(context);
        DEVICE_ID = getmDeviceId(context);

        DEV_IMSI = getIMSI(context);
        DEV_IMEI = getIMEI(context);
        DEV_MAC = getMacAddress(context);
        OS_MODEL = getDeviceModel();
        OS_VERSION = getOSVersion();
        DEV_RESOLUTION = getResolution(context);
        OS_SDK = getSdk();
    }

    private static String MD5Helper(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[]        byteArray = messageDigest.digest();
            StringBuilder sb        = new StringBuilder();
            for (byte b : byteArray) {
                if (Integer.toHexString(0xFF & b).length() == 1) {
                    sb.append("0").append(
                        Integer.toHexString(0xFF & b));
                } else {
                    sb.append(Integer.toHexString(0xFF & b));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("no device Id");
    }

    private static String getIMSI(Context context) {
        String subscriberId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (null == subscriberId || subscriberId.length() <= 0) {
            // subscriberId = Constant.uuid;
        } else {
            subscriberId = subscriberId.replace(" ", "");
            if (TextUtils.isEmpty(subscriberId)) {
                // subscriberId = Constant.uuid;
            }
        }

        return subscriberId;
    }

    private static String getIMEI(Context context) {
        try {
            String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (null == deviceId || deviceId.length() <= 0) {
                return "";
            } else {
                return deviceId.replace(" ", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getDeviceName() {
        return Build.MODEL;
    }

    private static String getBrandName() {
        return Build.BRAND;
    }

    private static String getMacAddress(Context context) {
        String   macAddress = null;
        WifiInfo wifiInfo   = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        if (wifiInfo != null) {
            macAddress = wifiInfo.getMacAddress();
            if (macAddress == null || macAddress.length() <= 0) {
                return "";
            } else {
                return macAddress;
            }
        } else {
            return "";
        }
    }


    private static String getAuidOrignal(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIMEI(context)).append(getIMSI(context))
            .append(getDeviceName()).append(getBrandName())
            .append(getMacAddress(context));
        return sb.toString();

    }

    private static String getAUID(Context context) {
        return JxSettings.get(FIELD_AUID, JxMd5Utils.md5(getAuidOrignal(context)));
    }

    private static String getTxtExternalStore(Context context, String suffix) {
        String fileName  = JxMd5Utils.md5(context.getPackageName());
        File   sdcardDir = Environment.getExternalStorageDirectory();
        File   file      = new File(sdcardDir, "." + fileName + "_" + suffix);
        try {
            FileInputStream in     = new FileInputStream(file);
            int             length = in.available();
            byte[]          buffer = new byte[length];
            in.read(buffer);
            in.close();
            byte[] b64plain = Base64.decode(buffer, Base64.DEFAULT);
            return new String(b64plain);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    private static void saveTxtToStorage(Context context, String txt, String suffix) {
        try {
            String fileName   = JxMd5Utils.md5(context.getPackageName());
            File   targetFile = new File(Environment.getExternalStorageDirectory(), "." + fileName + "_" + suffix);
            targetFile.createNewFile();
            //以指定文件创建RandomAccessFile对象
            FileWriter writer = new FileWriter(targetFile, false);
            writer.write(Base64.encodeToString(txt.getBytes(), Base64.DEFAULT));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取已经存在的info
    private static String getOwnInfo(Context context) {
        String info = JxPreferenceManager.shared().get("OWN_INFO", "");
        if (TextUtils.isEmpty(info)) {
            info = getTxtExternalStore(context, "info");
        }
        return info;
    }

    private static String generateOwnInfo(Context context) {
        String deviceId  = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        String androidId = android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
        String serial    = Build.SERIAL;
        String osVersion = Build.VERSION.RELEASE;
        return deviceId + androidId + serial + osVersion;
    }

    private static String getLdid128(Context context) {
        String info = JxSettings.get(FIELD_LDID_INFO, generateOwnInfo(context));
        return JxSettings.get(FIELD_LDID, TokenUtil.getDuid128(context, 1, info, ""));
    }

    private static String getClientChannel(Context context) {
        //todo:: set default channel
        return ChannelUtil.getChannel(context, "360");
    }

    private static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    private static String getDeviceModel() {
        try {
            return URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "-";
        }
    }

    private static String getmDeviceId(Context context) {
        try {
            String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (JxTextUtils.isEmptyOrNull(deviceId)) {
                return getSimulateDeviceID(context);
            } else {
                return deviceId.replace(" ", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getSimulateDeviceID(context);
        }
    }

    private static String genRandomCode(int passLength, int type) {
        StringBuffer buffer = null;
        StringBuffer sb     = new StringBuffer();
        Random       r      = new Random();
        r.setSeed(System.currentTimeMillis());
        switch (type) {
            case 0:
                buffer = new StringBuffer("0123456789");
                break;
            case 1:
                buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
                break;
            case 2:
                buffer = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                break;
            case 3:
                buffer = new StringBuffer(
                    "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
                break;
            case 4:
                buffer = new StringBuffer(
                    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                sb.append(buffer.charAt(r.nextInt(buffer.length() - 10)));
                passLength -= 1;
                break;
            case 5:
                String s = java.util.UUID.randomUUID().toString();
                sb.append(s.substring(0, 8) + s.substring(9, 13)
                    + s.substring(14, 18) + s.substring(19, 23)
                    + s.substring(24));
            default:
                break;
        }

        if (type != 5) {
            int range = buffer.length();
            for (int i = 0; i < passLength; ++i) {
                sb.append(buffer.charAt(r.nextInt(range)));
            }
        }
        return sb.toString();
    }

    private static String getSimulateDeviceID(Context context) {
        return JxSettings.get(FIELD_DEVICE_ID, genRandomCode(15, 0));
    }

    private static String getClientVersionName() {
        return PacketVersion.getInstance().getVersionName();
    }

    private static String getResolution(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager == null) {
            return "-";
        }
        Display display = manager.getDefaultDisplay();
        if (display == null) {
            return "-";
        }

        int width  = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        return width + "*" + height;
    }

    private static String getSdk() {
        return Build.VERSION.SDK;
    }
    //
//    /**
//     * 存储模拟 deviceId 的key
//     */
    //
//    public static String optString(String str, String defStr) {
//        if (TextUtils.isEmpty(str)) {
//            return defStr;
//        } else {
//            return str;
//        }
//    }
//
//    /**
//     * 获取海报图上的描述
//     *
//     * @param video
//     * @return zhangshuo 2014年6月5日 下午4:08:53
//     */
//    public static String getEpisodeInfo(Context context, VideoDataBean video) {
//        if (null != video && !TextUtils.isEmpty(video.getVt())) {
//            String vt = video.getVt();
//            // 如果当前的影片类型为电影
//            if (MoviesConstant.VT_MOVIE.equals(vt)) {
//                return String.format("%.1f", video.getRating());
//            } else if (MoviesConstant.VT_CARTOON.equals(vt) || MoviesConstant.VT_TV.equals(vt)
//                || MoviesConstant.VT_CHILDREN.equals(vt)) {
//                if (!TextUtils.isEmpty(video.getIsend())
//                    && !TextUtils.isEmpty(video.getNowepisodes())) {
//                    StringBuffer nameBuff = new StringBuffer();
//                    String       isSend   = video.getIsend();
//                    // 如果正在更新剧集
//                    if (MoviesConstant.EPISODE_UPDATE.equals(isSend)) {
//                        nameBuff.append(context.getString(R.string.updateto));
//                        nameBuff.append(video.getNowepisodes());
//                        nameBuff.append(context.getString(R.string.episode));
//                    } else if (MoviesConstant.EPISODE_UPDATE_END.equals(isSend)) {
//                        // 如果是全集
//                        nameBuff.append(context.getString(R.string.sum));
//                        nameBuff.append(video.getEpisodes());
//                        nameBuff.append(context.getString(R.string.episode));
//
//
//                    }
//                    return nameBuff.toString();
//                }
//            } else if (MoviesConstant.VT_ZONGYI.equals(vt)) {
//                try {
//                    return "更新至" + video.getNowepisodes() + "期";
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return video.getNowepisodes();
//            } else {
//                return "";
//            }
//        }
//        return null;
//    }
//
//
//    public static String getPhoneImei() {
//        String phoneImei    = "";
//        String UNKNOWN_IMEI = "ImeiUnknown";
//        try {
//            TelephonyManager telephonyManager = null;
//            telephonyManager =
//                (TelephonyManager) context.getSystemService(
//                    Context.TELEPHONY_SERVICE);
//            Logger.i("after get system service.");
//            if (null != telephonyManager) {
//                phoneImei = telephonyManager.getDeviceId();
//                Logger.d("%s", "mPhoneImei = " + phoneImei);
//                if (phoneImei == null) {
//                    phoneImei = UNKNOWN_IMEI;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            phoneImei = UNKNOWN_IMEI;
//            Logger.d("in the catch.");
//        }
//        return phoneImei;
//    }
//
//

    //
//    public static String getMacAddress() {
//        try {
//            WifiManager wifiManager =
//                (WifiManager) context.getSystemService(
//                    Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            return wifiInfo.getMacAddress();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    /**
//     * 获取deviceId
//     *
//     * @return
//     */
    //    public static String getMacAddress(Context context) {
//        if (TextUtils.isEmpty(mMACAddress)) {
//            mMACAddress = MacAddressUtils.getMacAddress(context);
//        }
//
//        return mMACAddress;
//    }
    //    private final static String TAG = "AppInfo";
//
//    private static String mOSVersion = "";
//
//    private static String mDeviceModel = "";
//
//    private static String mDeviceId = "";
//
//    private static String mSdk = "";
//
//    public static String timestamp2Recent(long times, Context context) {
//        Resources res   = context.getResources();
//        long      now   = System.currentTimeMillis();
//        long      delta = (now - times) / 1000;
//        if (delta < 60) {
//            return res.getString(R.string.time_less_than_1_min);
//        } else if (delta < 60 * 60) {
//            return String.format(res.getString(R.string.time_less_than_1_hour), delta / 60);
//        } else if (delta < 24 * 60 * 60) {
//            return String.format(res.getString(R.string.time_less_than_1_day), delta / (60 * 60));
//        } else {
//            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("M-dd", Locale.CHINESE);
//            return localSimpleDateFormat.format(new Date(times));
//        }
//    }
//
//
//    public static String seekHistroyTimeFormat(long seekHistroy, Context context) {
//
//        Resources res               = context.getResources();
//        long      tempSeekHistroy   = seekHistroy / 1000;
//        String    formatSeekHistroy = null;
//        if (tempSeekHistroy < 60) {
//
//            formatSeekHistroy = res.getString(R.string.time_less_than_1_min);
//        } else if (tempSeekHistroy < 10 * 60) {
//            long second = tempSeekHistroy % 60;
//            formatSeekHistroy =
//                String.format(res.getString(R.string.time_less_than_ten_minute),
//                    tempSeekHistroy / 60, second >= 10 ? "" + second : "0" + second);
//
//        } else if (tempSeekHistroy < 60 * 60) {
//            long second = tempSeekHistroy % 60;
//            formatSeekHistroy =
//                String.format(res.getString(R.string.time_less_than_1_hour_histroy),
//                    tempSeekHistroy / 60, second >= 10 ? "" + second : "0" + second);
//        } else {
//            long second = tempSeekHistroy % 60;
//            formatSeekHistroy =
//                String.format(res.getString(R.string.time_more_than_1_hour),
//                    String.valueOf(tempSeekHistroy / 60), second >= 10 ? "" + second : "0" + second);
//        }
//        return formatSeekHistroy;
//    }
//
//
//    /**
//     * 得到客户端版本名
//     */

    //
//
//    /**
//     * 得到客户端版本号
//     */
//    public static int getClientVersionCode() {
//        try {
//            PackageInfo packInfo =
//                context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(), 0);
//            return packInfo.versionCode;
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//


    //
//    /**
//     * 获取手机的分辨率
//     *
//     * @param context
//     * @return
//     */

//
//    public static boolean isProductLightning() {
//        return BuildConfig.PRODUCT_NAME.equals(MoviesConstant.PRODUCT_LIGHTNING);
//    }
//
//    public static boolean isProductCjsp() {
//        return BuildConfig.PRODUCT_NAME.equals(MoviesConstant.PRODUCT_CJSP);
//    }
//
//    public static boolean isProductYsdq() {
//        return BuildConfig.PRODUCT_NAME.equals(MoviesConstant.PRODUCT_YSDQ);
//    }
//
//    public static boolean isProductKuaiKan() {
//        return BuildConfig.PRODUCT_NAME.equals(MoviesConstant.PRODUCT_KUAIKAN);
//    }
//
//    public static String getLetvPushAppKey(Context context) {
//        return context.getResources().getString(R.string.letv_push_app_key);
//    }
//
//    public static boolean isNets(String site) {
//        return "nets".equals(site);
//    }
//
//    public static Boolean isLetv(String site) {
//        return "letv".equals(site);
//    }
//
//    public static boolean isSelfProduct(String packageName) {
//        boolean isSelf = false;
//        if (YsApplication.YINGSHIDAQUAN_PACKAGENAME.equals(packageName)) {
//            isSelf = true;
//        }
//        return isSelf;
//    }
//
//    public static String getRandomString(int length) {
//        String       str    = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//含有字符和数字的字符串
//        Random       random = new Random();
//        StringBuffer sb     = new StringBuffer();
//
//        for (int i = 0; i < length; ++i) {
//            int number = random.nextInt(62);// [0,62)
//
//            sb.append(str.charAt(number));
//        }
//        return sb.toString();
//    }
//
//    public static String getMD5(String input) {
//        try {
//            // 获得MD5摘要算法的 MessageDigest 对象
//            MessageDigest mdInst = MessageDigest.getInstance("MD5");
//            // 使用指定的字节更新摘要
//            mdInst.update(input.getBytes());
//            // 获得密文
//            byte[] md = mdInst.digest();
//            // 把密文转换成十六进制的字符串形式
//            StringBuffer mdString = new StringBuffer();
//            // 字节数组转换为 十六进制 数
//            for (int m = 0; m < md.length; m++) {
//                String mdHex = Integer.toHexString(md[m] & 0xFF);
//                if (mdHex.length() < 2) {
//                    mdString.append(0);
//                }
//                mdString.append(mdHex);
//            }
//            return mdString.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public static String getSHA1(String decript) {
//        try {
//            MessageDigest digest = MessageDigest
//                .getInstance("SHA-1");
//            digest.update(decript.getBytes());
//            byte messageDigest[] = digest.digest();
//            // Create Hex String
//            StringBuffer hexString = new StringBuffer();
//            // 字节数组转换为 十六进制 数
//            for (int i = 0; i < messageDigest.length; i++) {
//                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
//                if (shaHex.length() < 2) {
//                    hexString.append(0);
//                }
//                hexString.append(shaHex);
//            }
//            return hexString.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public static String getAppName(Context context) {
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(
//                context.getPackageName(), 0);
//            int labelRes = packageInfo.applicationInfo.labelRes;
//            return context.getResources().getString(labelRes);
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public static boolean isOtherVt(String vt) {
//        boolean isNotOther = MoviesConstant.VT_MOVIE.equals(vt) || MoviesConstant.VT_TV.equals(vt) || MoviesConstant.VT_CARTOON.equals(vt) ||
//            MoviesConstant.VT_ZONGYI.equals(vt) || MoviesConstant.VT_DOCUMENTARY.equals(vt) || MoviesConstant.VT_CHILDREN.equals(vt);
//        return !isNotOther;
//    }
//
//    public static String getTabRefName(int mPosition) {
//        String[] arr = {"episodes_ranking", "movie_ranking", "cartoon_ranking", "entertain_ranking"};
//        return arr[mPosition];
//    }
//
//    public static String playCountFormat(String count) {
//        long   num       = 0;
//        String showCount = "";
//        try {
//            num = Long.parseLong(count);
//        } catch (Exception e) {
//
//        }
//        if (num >= 10000000) {
//            showCount = "1千万次播放";
//        } else if (num >= 100000) {
//            showCount = num / 10000 + "万次播放";
//        } else if (num >= 10000) {
//            showCount = num / 10000 + "." + (num / 1000) % 10 + "万次播放";
//        } else if (num > 0) {
//            showCount = num + "次播放";
//        }
//        return showCount;
//    }
//
//    /**
//     * 获取选择的性别倾向
//     *
//     * @return
//     */
//    public static String getGender2() {
//        String preferGender = SharePreferenceManager.get(context, SharePreferenceManager.PREFER_GENDER, "0");
//        if ("-1".equals(preferGender)) {
//            preferGender = "0";
//        }
//        return preferGender;
//    }
}
