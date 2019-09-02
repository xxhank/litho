package com.lvideo.http.utils;

import android.content.Context;

import androidx.annotation.Nullable;

/**
 * Created by lxj on 2018/6/26.
 */

public class TokenUtil {

    static {
        System.loadLibrary("TokenUtil");
    }

    public static native @Nullable String getToken(Context context, String timeStamp);

    public static native @Nullable String getKey(Context context);

    public static native @Nullable String getCode(Context context);

    public static native @Nullable byte[] getCert(Context context);

    public static native @Nullable String getDuid(Context context, int product, String info);

    public static native @Nullable String getDuid128(Context context, int product, String info, String oldLdid);
}
