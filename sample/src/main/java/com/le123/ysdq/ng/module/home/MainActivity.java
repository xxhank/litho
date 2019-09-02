package com.le123.ysdq.ng.module.home;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.agx.scaffold.JxLogger;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    protected List<Call> callList = new ArrayList<>(1);

    public static String getSignInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs       = packageInfo.signatures;
            Signature   sign        = signs[0];
            int         code        = sign.hashCode();
//            System.out.printf("%u" ,sign.hashCode());
//            System.out.printf("%u" ,sign.hashCode());
            return (code & 0xFFFFFFFFL) + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_activity);
        ComponentContext context = new ComponentContext(this);

        Component component = RecyclerCollectionComponent.create(context)
            .disablePTR(true)
            .section(AlbumList.create(new SectionContext(context)).build())
            .build();

        LithoView view = LithoView.create(context, component);
        setContentView(view);


        String x = getSignInfo(this);
        JxLogger.i("sig:%s", x);

        ChannelHeaderFetcher.fetch(value -> {
            value.ifPresent(value1 -> {
                ChannelHeaderFetcher.Response response = value1.value;
                if (response.channels.size() == 0) {
                    return;
                }
                ChannelHeaderFetcher.Response.Channel channel = response.channels.get(0);
                ChannelPageFetcher.fetch(channel.page, 1, pageValue -> {
                    pageValue.ifPresent(page -> {
                        for (ChannelPageFetcher.Response.Data.Rec rec : page.value.data.rec) {
                            JxLogger.i("%s %s", rec.name, rec.data);
                        }
                    }).orElse(p -> {
                    });
                });

//                for (ChannelHeaderFetcher.Response.Channel channel : response.channels) {
//                    JxLogger.i("%s %s", channel.name, channel.icon);
//
//                    ChannelPageFetcher.fetch(channel.page, 1, pageValue -> {
//                        pageValue.ifPresent(page -> {
//                            for (ChannelPageFetcher.Response.Data.Rec rec : page.value.data.rec) {
//                                JxLogger.i("%s %s", rec.name, rec.data);
//                            }
//                        }).orElse(p -> {
//                        });
//                    });
//                }
            }).orElse(value1 -> {
            });
        });
    }

//    public ApiService getApiService() {
//        return ApiRequestManager.getInstance().getService();
//    }
//
//    void fetchInit() {
//        Call<BaseResponseBean<List<HotDownloadBean>>> call = getApiService().requestHotCollect();
//        callList.add(call);
//
//        ResponseCallBack callBack = new ResponseCallBack<List<HotDownloadBean>>() {
//            @Override
//            protected void onResponseSuc(List<HotDownloadBean> beanList) {
//                listener.requestHotFavoritetSuc(beanList);
//            }
//
//            @Override
//            protected void onResponseFail(String code, String message) {
//                listener.requestHotFavoriteFail(code, message);
//            }
//
//            @Override
//            protected void onNetError() {
//                listener.onNetError();
//            }
//
//            @Override
//            protected void onSysError() {
//                listener.onSysError();
//            }
//
//        };
//
//        call.enqueue(callBack);
//    }
//
//    public interface ApiService {
//        //获取热门收藏
//        @GET("/api/v2/hot/favorite")
//        Call<BaseResponseBean<List<HotDownloadBean>>> requestHotCollect();
//    }
//
//    static class BaseResponseBean<T> {
//        public String code;
//        public String msg;
//        public String microtime;
//        public T      data;
//    }
//
//    static class HotDownloadBean {
//
//    }
//
//    static public class ApiRequestManager extends RetrofitManager {
//
//        private static ApiRequestManager mInstacce;
//
//        private ApiRequestManager() {
//            super();
//        }
//
//        public static ApiRequestManager getInstance() {
//            if (mInstacce == null) {
//                synchronized (ApiRequestManager.class) {
//                    if (mInstacce == null) {
//                        mInstacce = new ApiRequestManager();
//                    }
//                }
//            }
//            return mInstacce;
//        }
//
//        @Override
//        protected String getBaseUrl() {
//            return "https://api.yingshidq.com.cn/";
//        }
//
//        public ApiService getService() {
//            return getService(ApiService.class);
//        }
//    }

}
