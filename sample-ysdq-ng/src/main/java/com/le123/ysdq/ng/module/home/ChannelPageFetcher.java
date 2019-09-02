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
import retrofit2.http.Query;

public class ChannelPageFetcher {
    public static Call<Response> fetch(String page, int pageIndex, JxFunc.Action<Fetcher.Result<Response, Object>> callback) {
        Call<Response> call = Retrofits.api.create(Service.class).fetch(page, pageIndex);
        // Integer        i    = 0;
        //JxThreadPool.shared().post(new JxThreadRunnableImpl<Integer>("channel-page-fetcher", (i)) {
        //    @Override public void run(Integer value) {
        // JxLogger.d("%s", i);

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
        //    }
        //});


        return call;
    }

    public interface Service {
        // https://api.yingshidq.com.cn/api/v2/index/page?code=346e5b9d1bd97036&page=page_index_episode&times=1&pageindex=1
        @GET("/api/v2/index/page?code=346e5b9d1bd97036&times=1") Call<Response> fetch(@Query("page") String page, @Query("pageindex") int pageIndex);
    }
    //

    public static class Response extends Fetcher.Response {
        /**
         * code : 200
         * data : {}
         * msg : success
         * ts : 1566959151091
         */

        @SerializedName("data") public Data data = new Data();


        public static class Data {
            /**
             * rec : []
             * total : 4
             * icon : []
             * focus : []
             * has_more : 1
             */

            @SerializedName("total") public    int         total;
            @SerializedName("has_more") public int         hasMore;
            @SerializedName("rec") public      List<Rec>   rec   = new ArrayList<>();
            @SerializedName("icon") public     List<Icon>  icon  = new ArrayList<>();
            @SerializedName("focus") public    List<Focus> focus = new ArrayList<>();

            public static class Rec {
                /**
                 * recreport :""
                 * is_pull : 0
                 * data : []
                 * style : 2
                 * reccode :
                 * recname : 热剧精选
                 * recid : CHANNEL_TV_NEW
                 * vt : 1
                 */
                public static final String STYLE_SPECIAL          = "1"; /*1列 MODEL_SPECIAL*/
                public static final String STYLE_TWO              = "2"; /*2列 MODEL_HALF*/
                public static final String STYLE_THREE            = "3"; /*3列 MODEL_THIRD*/
                public static final String STYLE_FEED             = "4"; /*1列 MODEL_FEED*/
                public static final String STYLE_MIXTURE_TWO      = "5"; /*1拖2 MODEL_FULL, MODEL_HALF*/
                public static final String STYLE_MIXTURE_THREE    = "6"; /*1拖3 MODEL_FULL,MODEL_THIRD*/
                public static final String STYLE_FEED_SHORT_VIDEO = "10"; /*短视频模板 1列 MODEL_FEED_SHORT_VIDEO*/

                @SerializedName("recid") public     String      id;
                @SerializedName("reccode") public   String      code;/**/
                @SerializedName("recname") public   String      name;/*推荐位名称*/
                @SerializedName("recreport") public String      recreport;
                @SerializedName("is_pull") public   String      isPull;
                @SerializedName("style") public     String      style;/*模板类型*/
                @SerializedName("vt") public        String      videoType;
                @SerializedName("data") public      List<DataX> data;

                public static class DataX {
                    /**
                     * category_name : 电视剧
                     * src : 2
                     * subname : 年代版<都挺好>治愈心灵
                     * display : album
                     * rating : 7.4
                     * description : 《哥哥姐姐的花样年华》：女知青赵春雷回城第一天就惊呆了，父亲
                     * pich : http://s.yingshidq.com.cn/img/sign/2019/07/17/3124063064
                     * pich2 : {"size":["618_348"],"type":"webp","base":"http://s.yingshidq.com.cn/img/sign/2019/07/17/3132109465"}
                     * cornerColor :
                     * isEnd : 1
                     * playurl :
                     * cornerTitle :
                     * name : 哥哥姐姐的花样年华
                     * nowepisodes : 50
                     * aid : 2_841996
                     * episodes : 56
                     * vt : 1
                     */

                    @SerializedName("category_name") public String categoryName;
                    @SerializedName("src") public           String src;
                    @SerializedName("subname") public       String subname;
                    @SerializedName("display") public       String display;
                    @SerializedName("rating") public        String rating;
                    @SerializedName("description") public   String description;
                    @SerializedName("pich") public          String pich;
                    @SerializedName("pich2") public         Pich2  pich2;
                    @SerializedName("pic") public           String pic;
                    @SerializedName("pic2") public          Pich2  pic2;
                    @SerializedName("cornerColor") public   String cornerColor;
                    @SerializedName("isEnd") public         String isEnd;
                    @SerializedName("playurl") public       String playurl;
                    @SerializedName("cornerTitle") public   String cornerTitle;
                    @SerializedName("name") public          String name;
                    @SerializedName("nowepisodes") public   String nowepisodes;
                    @SerializedName("aid") public           String aid;
                    @SerializedName("episodes") public      String episodes;
                    @SerializedName("duration") public      String duration;

                    @SerializedName("vt") public String vt = "";

                    public static class Pich2 {
                        /**
                         * size : ["618_348"]
                         * type : webp
                         * base : http://s.yingshidq.com.cn/img/sign/2019/07/17/3132109465
                         */

                        @SerializedName("type") public String       type;
                        @SerializedName("base") public String       base;
                        @SerializedName("size") public List<String> size;
                    }
                }
            }

            public static class Icon {
                /**
                 * jumpurl : itemjump://?actionType=4&videoType=1
                 * name : 排行
                 * icon : http://s.yingshidq.com.cn/tp/tp20180518/xjf0002.png
                 * sort : 1
                 */

                @SerializedName("jumpurl") public String jumpurl = "";
                @SerializedName("name") public    String name    = "";
                @SerializedName("icon") public    String icon    = "";
                @SerializedName("sort") public    int    sort    = -1;
            }

            public static class Focus {
                /**
                 * showtime :
                 * display : album
                 * subname :
                 * name : 兄弟营
                 * themeid :
                 * pic : http://s.yingshidq.com.cn/img/sign/2019/08/14/997455476
                 * pic2 : {"size":["1242_621"],"type":"webp","base":"http://s.yingshidq.com.cn/img/sign/2019/08/14/998372677"}
                 * aid : 2_866248
                 * vt : 1
                 * playurl :
                 */

                @SerializedName("showtime") public String showtime;
                @SerializedName("display") public  String display;
                @SerializedName("subname") public  String subname;
                @SerializedName("name") public     String name;
                @SerializedName("themeid") public  String themeid;
                @SerializedName("pic") public      String pic;
                @SerializedName("pic2") public     Pic2   pic2;
                @SerializedName("aid") public      String aid;
                @SerializedName("vt") public       String vt;
                @SerializedName("playurl") public  String playurl;

                public static class Pic2 {
                    /**
                     * size : ["1242_621"]
                     * type : webp
                     * base : http://s.yingshidq.com.cn/img/sign/2019/08/14/998372677
                     */

                    @SerializedName("type") public String       type;
                    @SerializedName("base") public String       base;
                    @SerializedName("size") public List<String> size;
                }
            }
        }
    }
}