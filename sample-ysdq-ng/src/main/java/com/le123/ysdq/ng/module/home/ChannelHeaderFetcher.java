package com.le123.ysdq.ng.module.home;

import com.agx.scaffold.JxFunc;
import com.agx.scaffold.JxLogger;
import com.google.gson.annotations.SerializedName;
import com.le123.ysdq.ng.Fetcher;
import com.le123.ysdq.ng.Retrofits;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public class ChannelHeaderFetcher {
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
        // /api/v2/index/header?sqtype=list
        @GET("/api/v2/index/header?sqtype=list") Call<Response> fetch();
    }
    //

    public static class Response extends Fetcher.Response {
        /**
         * code : 200
         * data : [{}]
         * msg : success
         * ts : 1566959151091
         */

        @SerializedName("data") public List<Channel> channels = new ArrayList<>(10);

        public static class Channel {
            /**
             * image :
             * pull_type : 1
             * page_type : 1
             * name : 推荐
             * icon : http://i3.letvimg.com/lc02_search/201612/21/15/00/tmp_640079315446862295.png
             * page : page_index
             * is_default : 1
             * search_query : 七月与安生;快把我哥带走;西虹市首富;釜山行;悲伤逆流成河;素媛;唐人街探案2;最好的我们
             * channel_id :
             * corner_icon :
             * vt : 2
             * capsules : [{"type":"DEFAULT","value":"推荐"},{"type":"JSKP","value":"吐槽"},{"type":"JSKP","value":"美女"},{"type":"JSKP","value":"丧尸"},{"type":"JSKP","value":"喜剧"},{"type":"JSKP","value":"奇幻"}]
             */

            @SerializedName("image") public        String         image;
            @SerializedName("pull_type") public    int            pullType;
            @SerializedName("page_type") public    int            pageType;
            @SerializedName("name") public         String         name;
            @SerializedName("icon") public         String         icon;
            @SerializedName("page") public         String         page;
            @SerializedName("is_default") public   int            isDefault;
            @SerializedName("search_query") public String         searchQuery;
            @SerializedName("channel_id") public   String         channelId;
            @SerializedName("corner_icon") public  String         cornerIcon;
            @SerializedName("vt") public           String         vt;
            @SerializedName("capsules") public     List<Capsules> capsules;

            public static class Capsules {
                /**
                 * type : DEFAULT
                 * value : 推荐
                 */

                @SerializedName("type") public  String type;
                @SerializedName("value") public String value;
            }
        }
    }
}