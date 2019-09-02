package com.lvideo.http;

import android.text.TextUtils;

import com.agx.scaffold.JxApplicationContext;
import com.agx.scaffold.JxMd5Utils;
import com.lvideo.http.utils.TokenUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * Author: liuxiaojiang(liuxiaojiang@lvideo.com)
 * Date: 2016-03-14
 * Time: 15:16
 * Description:
 */
public class SignChecker {
    public synchronized static String getSign(Map<String, String> bundle) {
        if (bundle == null) {
            return "";
        }
        StringBuilder       sb      = new StringBuilder();
        Map<String, String> treeMap = new TreeMap<>();
        for (String key : bundle.keySet()) {
            if (!TextUtils.isEmpty(bundle.get(key))) {
                treeMap.put(key, bundle.get(key));
            }
        }
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue());
        }
        // JxLogger.i("%s", sb.toString());

        String timeStamp = bundle.get("ts");
        if (TextUtils.isEmpty(timeStamp)) {
            return "";
        }
        // JxLogger.i("%s", sb.toString());

        String token = TokenUtil.getToken(JxApplicationContext.shared(), timeStamp);
        sb.append(token);
        // JxLogger.i("%s", sb.toString());
        return JxMd5Utils.md5(sb.toString().replaceAll("\n", ""));
    }
}
