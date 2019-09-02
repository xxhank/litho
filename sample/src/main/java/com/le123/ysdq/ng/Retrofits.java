package com.le123.ysdq.ng;

import android.content.Context;
import android.net.Uri;

import com.agx.scaffold.JxTextUtils;
import com.agx.scaffold.JxThreadPool;
import com.lvideo.http.LVideoRequestHeader;
import com.lvideo.http.SignChecker;
import com.lvideo.http.TrustSSLSocketFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofits {
    public static Retrofit api;

    // call in application onCreate
    public static void initialize(Context context) {
        api = build(context);
    }

    private static Retrofit build(Context context) {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(
                TrustSSLSocketFactory.createKeyManagers(context),
                new TrustManager[]{new MyTrustManager()},
                new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (sc != null) {
            builder.sslSocketFactory(sc.getSocketFactory(), new MyTrustManager()); //SSL
        }

        OkHttpClient client = builder
            .addInterceptor(Interceptors.addHeaders()) //加Header
            .addInterceptor(Interceptors.addSignature())  //加sign
            .build();

        Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("http://mbd.baidu.com")//
            .baseUrl("https://api.yingshidq.com.cn/")
            // .baseUrl("http://test.api.yingshidq.com.cn/")
            .client(client)
            .callbackExecutor(JxThreadPool.shared().executor())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        return retrofit;
    }

    private static class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public static class Interceptors {

        public static LVideoRequestHeader getHeader() {
            LVideoRequestHeader header = new LVideoRequestHeader();
            //todo::add user info
            // header.setkToken(UserSpManager.getTicket());
            // header.setkUid(UserSpManager.getUid());

            header.setkAppid(AppInfo.PLATFORM_IN_UPGRADE);
            header.setkMarket(AppInfo.APP_CHANNEL);
            header.setkVersion(AppInfo.APP_VERSION);
            header.setkTeminal(AppInfo.PLATTYPE_VAULE);
            header.setkModel(AppInfo.OS_MODEL);
            header.setkScreen(AppInfo.DEV_RESOLUTION);
            header.setkOS("Android " + AppInfo.OS_VERSION);
            header.setkSdk(AppInfo.OS_SDK);
            header.setkUuid(AppInfo.UUID);
            header.setkDc(AppInfo.DEVICE_ID);
            header.setkAuid(AppInfo.AUID);
            header.setkLdid(AppInfo.LDID);

            return header;
        }

        //统一加Header
        public static Interceptor addHeaders() {
            return chain -> {
                LVideoRequestHeader header = getHeader();
                Request request = chain.request().newBuilder()
                    .addHeader("k-token", header.getkToken())
                    .addHeader("k-appid", header.getkAppid())
                    .addHeader("k-market", header.getkMarket())
                    .addHeader("k-version", header.getkVersion())
                    .addHeader("k-terminal", header.getkTeminal())
                    .addHeader("k-model", header.getkModel())
                    .addHeader("k-screen", header.getkScreen())
                    .addHeader("k-os", header.getkOS())
                    .addHeader("k-sdk", header.getkSdk())
                    .addHeader("k-uuid", header.getkUuid())
                    .addHeader("k-uid", header.getkUid())
                    .addHeader("k-dc", header.getkDc())
                    .addHeader("k-auid", header.getkAuid())
                    .addHeader("k-ldid", header.getkLdid())

                    .build();


                return chain.proceed(request);
            };
        }

        //统一加sign
        public static Interceptor addSignature() {
            return chain -> {
                Request requestOriginal = chain.request();
                HttpUrl originalHttpUrl = requestOriginal.url();

                FormBody formBody = null;

                if (requestOriginal.body() instanceof FormBody) {
                    formBody = (FormBody) requestOriginal.body();
                }

                /// common params
                Map<String, String> params = new HashMap<>(10);
                params.put("ts", JxTextUtils.format("%d", System.currentTimeMillis()));
                // map.put("ts", "1566903272504");
                params.put("apiversion", "2");
                params.put("gender2", "0");
                params.put("_f", JxTextUtils.format("%d", Math.abs(new Random().nextInt())));
                // map.put("_f", "772962510882");
                // map.put("_p", "aVbbwJUnWJ5yoWMUpAJhZA==");
                params.put("platform", "Le123Plat002360");
                params.put("city", "CN_1_5_1");
                params.put("webp", "1");
                params.put("version", AppInfo.APP_VERSION);
                params.put("uuid", AppInfo.UUID);
                params.put("plattype", "aphone");
                params.put("lc", AppInfo.DEVICE_ID);

                for (int i = 0; i < originalHttpUrl.querySize(); i++) {
                    String name  = originalHttpUrl.queryParameterName(i);
                    String value = originalHttpUrl.queryParameterValue(i);
                    if (JxTextUtils.isEmptyOrNull(value)) {
                        value = "";
                    }
                    params.put(name, value);
                }
                //                Bundle originalBundle = getBundleParam(null);
                //                Bundle allBundle      = new Bundle(originalBundle);
                //
                if (formBody != null) {
                    for (int i = 0; i < formBody.size(); i++) {
                        String key   = formBody.encodedName(i);
                        String value = Uri.decode(formBody.encodedValue(i));
                        if (JxTextUtils.isEmptyOrNull(value)) {
                            value = "";
                        }
                        params.put(key, value);
                    }
                }
                //
                params.put(AppInfo.FIELD_SIG, SignChecker.getSign(params));

                HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();
                for (String key : params.keySet()) {
                    if (key == null) {
                        continue;
                    }

                    urlBuilder.removeAllQueryParameters(key);
                    String value = Uri.encode(params.get(key), "UTF-8");
                    urlBuilder.addEncodedQueryParameter(key, value);
                }

                HttpUrl url     = urlBuilder.build();
                Request request = requestOriginal.newBuilder().url(url).build();
                return chain.proceed(request);
            };
        }
    }
}
