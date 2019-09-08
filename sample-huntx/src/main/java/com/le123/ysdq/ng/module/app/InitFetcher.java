package com.le123.ysdq.ng.module.app;

import com.agx.scaffold.JxFunc;
import com.agx.scaffold.JxLogger;
import com.google.gson.annotations.SerializedName;
import com.le123.ysdq.ng.Fetcher;
import com.le123.ysdq.ng.Retrofits;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public class InitFetcher {
    public static Call<Response> fetch(JxFunc.Action<Fetcher.Result<Response, Object>> callback) {
        Call<Response> call = Retrofits.api.create(Service.class).fetch();
        call.enqueue(new Callback<Response>() {
            @Override public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                JxLogger.d("%s", response);
                callback.yield(Fetcher.Result.result(response.body(), call, response));
            }

            @Override public void onFailure(Call<Response> call, Throwable t) {
                JxLogger.d("%s", t);
                callback.yield(Fetcher.Result.error(t, call, t));
            }
        });

        return call;
    }

    public interface Service {

        //GET /api/v2/init?_f=772962510882&_p=aVbbwJUnWJ5yoWMUpAJhZA%3D%3D&ts=1566903272504&sig=34acfc5063dfdff36eee6e4727a6992d&apiversion=2&platform=Le123Plat002tencent HTTP/1.1
        @GET("/api/v2/init")
        //@GET("newspage/data/landingsuper?context=%7B\"nid\"%3A\"news_9597967822828781819\"%7D&n_type=0&p_from=1")
        Call<Response> fetch();
    }
    //

    public static class Response extends Fetcher.Response {
        /**
         * code : 200
         * data : {}
         * msg : success
         * ts : 1566959151091
         */

        @SerializedName("data") public Data data;

        public static class Data {
            /**
             * check : false
             * city :
             * downloadRate : 1500
             * dyTag : {}
             * haotu : NlqBs7BLXeoQGpqo6+IZ2aeUKsDHOlmxH0SE3bm4WHw=
             * lawPolicy : https://s.yingshidq.com.cn/app/yingshidq-low-20190314.html
             * ppocy : https://s.yingshidq.com.cn/app/privacy_20180712.html
             * privacyPolicy : https://s.yingshidq.com.cn/app/yingshidq-privacy-20190314.html
             * report : true
             * splash :
             * splashInterval : 30
             * splashLimit : 99999
             * splashScreen : 30
             * tagArgs : {}
             * tags : ""
             * upgrade : false
             * yiDianTag : yidian
             * youku : ""
             */

            @SerializedName("check") public          boolean check;
            @SerializedName("city") public           String  city;
            @SerializedName("downloadRate") public   int     downloadRate;
            @SerializedName("dyTag") public          DyTag   dyTag;
            @SerializedName("haotu") public          String  haotu;
            @SerializedName("lawPolicy") public      String  lawPolicy;
            @SerializedName("ppocy") public          String  ppocy;
            @SerializedName("privacyPolicy") public  String  privacyPolicy;
            @SerializedName("report") public         boolean report;
            @SerializedName("splash") public         String  splash;
            @SerializedName("splashInterval") public int     splashInterval;
            @SerializedName("splashLimit") public    int     splashLimit;
            @SerializedName("splashScreen") public   int     splashScreen;
            @SerializedName("tagArgs") public        TagArgs tagArgs;
            @SerializedName("tags") public           String  tags;
            @SerializedName("upgrade") public        boolean upgrade;
            @SerializedName("yiDianTag") public      String  yiDianTag;
            @SerializedName("youku") public          String  youku;

            public static class DyTag {
                /**
                 * signature : ""
                 * link : http://d.dyreader.cn/apk/com.dotreader.dnovel-v0.3.0-201906031747-false-inner_030_jiagu_sign.apk
                 * tag :
                 * packageName : com.dotreader.dnovel
                 * silence : 1
                 * md5 : d968beb966bcfa050750ffb5e996863f
                 */

                @SerializedName("signature") public   String signature;
                @SerializedName("link") public        String link;
                @SerializedName("tag") public         String tag;
                @SerializedName("packageName") public String packageName;
                @SerializedName("silence") public     int    silence;
                @SerializedName("md5") public         String md5;
            }

            public static class TagArgs {
                /**
                 * AB_test5 : {"feed_area":"rec_0711"}
                 * sug : {"size":"5"}
                 */

                @SerializedName("AB_test5") public ABTest5 ABTest5;
                @SerializedName("sug") public      Sug     sug;

                public static class ABTest5 {
                    /**
                     * feed_area : rec_0711
                     */

                    @SerializedName("feed_area") public String feedArea;
                }

                public static class Sug {
                    /**
                     * size : 5
                     */

                    @SerializedName("size") public String size;
                }
            }
        }
    }
}
