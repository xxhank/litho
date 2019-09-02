package com.lvideo.http;


import com.facebook.samples.litho.BuildConfig;

/**
 * Author: wangkai(wangkai@tv365.net)
 * Date: 2018-12-17
 * Time: 15:55
 * Description:
 */
public class PacketVersion {

    private static PacketVersion instance;

    private String mVersionName;
    private int    mVersionCode;


    private PacketVersion() {
        //versionName
//        if (BuildConfig.IS_GRAY) {
//            mVersionName = BuildConfig.GRAY_VERSION_NAME;
//        } else {
//            mVersionName = BuildConfig.VERSION_NAME;
//        }
        mVersionName = BuildConfig.VERSION_NAME;
        //verisonCode
        mVersionCode = BuildConfig.VERSION_CODE;
    }

    public static PacketVersion getInstance() {
        if (instance == null) {
            instance = new PacketVersion();
        }
        return instance;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public int getVersionCode() {
        return mVersionCode;
    }
}
