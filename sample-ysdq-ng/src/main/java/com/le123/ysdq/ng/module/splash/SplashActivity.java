package com.le123.ysdq.ng.module.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agx.scaffold.JxLogger;
import com.agx.scaffold.JxThreadPool;
import com.agx.scaffold.JxThreadRunnableImpl;
import com.facebook.samples.litho.R;
import com.facebook.samples.litho.lithography.LithographyActivity;
import com.le123.ysdq.ng.AppInfo;
import com.le123.ysdq.ng.EasyPermissions;
import com.le123.ysdq.ng.Retrofits;
import com.le123.ysdq.ng.WeakRefHandler;
import com.le123.ysdq.ng.module.app.InitFetcher;
import com.le123.ysdq.ng.module.app.YsApplication;
import com.le123.ysdq.ng.module.privacy.CheckActivity;

import java.util.UUID;


//import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by wangyemin on 2015/11/3.
 * <p/>
 * 启动页
 */
public class SplashActivity extends AppCompatActivity implements OnClickListener {

    private static int            TIMEINTERVAL            = 500; // 无广告时的等待时间
    private static String         START_FROM_NOTIFICATION = "start_from_notification";
    public         boolean        canJump                 = false;
    private        String         TAG                     = "SplashActivity";
    //    private RelativeLayout mBaiduAdLayout;
    //    private View adView;
    //    private ImageView mAdImage;
    private        TextView       mAdTextTime;
    private        int            adTime                  = 1;//5; // 广告显示时间
    private        int            leftTime;
    private        WeakRefHandler mHandler;
    private        Runnable       mTask;
    private        boolean        isRun;
    private        Context        mContext;
    private        String         cityInfo;
    private        int            mResumeTimes            = 0;
    private        boolean        isReportQuit            = true; // 是否上报启动前退出
    //back  unlockScreen
    private        String         mFrom;
    private        String         mAdSid;
    private        boolean        mIsInit;
    private        long           noExitTime              = 0;
    private        String         mAdId;
    /**
     * 是否当次向代理层广告请求中已经作为打底广告加载过 1 dadi 2 dadi2
     */
    private        int            spareAdType             = 0;


    private Runnable mRedirectToMainRunnable = new Runnable() {
        @Override
        public void run() {
            redirectToMain();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        if (EasyPermissions.hasPermissions(this, EasyPermissions.MUST_PERMISSIONS)) {
            afterPerm();
        } else {
            CheckActivity.startCheckActivity(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        canJump = true;
        if (!mIsInit) {
            return;
        }
        JxLogger.i("onResume");
        if (mResumeTimes == 0) {
            mResumeTimes = 1;
        } else {
            redirectToMain();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
        if (!mIsInit) {
            return;
        }
        JxLogger.i("onPause");
        if (isReportQuit) {
            reportQuit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//    private void bindListener() {
//        mAdTextTime.setOnClickListener(this);
//        mAdImage.setOnClickListener(this);
//    }


//    private void initData() {
//        cityInfo = SharePreferenceManager.get(mContext,
//                MyLocationManager.LOCATIONKEY, null);
//        Logger.i("%s", "1-->splash_launch cityInfo is " + cityInfo);
//        load();
//    }

//    private void load() {
//        boolean isFromPush = false;
//        if (getIntent() != null) {
//            isFromPush = getIntent().getBooleanExtra(START_FROM_NOTIFICATION, false);
//        }
//        if (isFromPush) {
//            showDefaultLaunchAndRedirect();
//        } else if (NetWorkTypeUtils.isNetAvailable() && !Utils.isMonkeyRunning()) {
//            YsApplication instance = YsApplication.getInstance();
//            new RequestIpidTask(instance).start();
//            mAdId = "";
//            RequestAdTask requestAdTask = new RequestAdTask(instance, MoviesHttpApi.LAUNCH, new LunchAdRequestResultListener());
//            requestAdTask.setmAdSid(mAdSid);
//            requestAdTask.start();
//
//            AdRequestStatis.selfAdRequestReport(SelfDataConstant.AC_AD_REQUEST, "splash", mAdSid);
//            UmengEventPoster.postADRequest("splash");
//
//            if (instance.getmLaunchTimeData() != null) {
//                instance.getmLaunchTimeData().setApiAdRequest(System.currentTimeMillis());
//            }
//        } else {
//            Logger.e("net is err");
//            showDefaultLaunchAndRedirect();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.releaseMessageQueue();
            mHandler = null;
        }
    }

    private void afterPerm() {
        YsApplication instance = YsApplication.getInstance();
//        instance.setTopActivity(SplashActivity.this);
        mAdSid = UUID.randomUUID().toString();
//        AdRequestStatis.selfAdRequestReport(SelfDataConstant.AC_SPLASH_SHOW, "splash", mAdSid);

        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
//        if (instance.getmLaunchTimeData() != null) {
//            instance.getmLaunchTimeData().setSplashOncreate(System.currentTimeMillis());
//        } else {
//            if ("back".equals(mFrom) || "unlockScreen".equals(mFrom)) {
//                后台回来或者解锁屏展示开屏页不算启动
//            } else {
//                instance.setmLaunchTimeData(new LaunchTimeData(SelfDataConstant.AC_WARM_BOOT));
//                instance.getmLaunchTimeData().setSplashOncreate(System.currentTimeMillis());
//            }
//        }

        initView();
        // bindListener();
        // initData();
//        PageInfoManager.getInstance().setLastPageClassName(TAG);
//        mIsInit = true;
    }

    private void initView() {
        mContext = this;
//        mAdImage = findViewById(R.id.ad_image);
        mAdTextTime = findViewById(R.id.ad_text_skip_time);
//  mBaiduAdLayout = findViewById(R.id.baidu_ad_layout);
//   adView = findViewById(R.id.nativeADContainer);
        mHandler = new WeakRefHandler();
        mTask = this::runTask;
        start();
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ad_text_skip_time:
//                if (Utils.GDT_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportSplashAdClose("splash", SelfDataConstant.AD_GDT, UmengEventPoster.GDTAD_CLOSE, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                } else if (Utils.BAIDU_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportSplashAdClose("splash", SelfDataConstant.AD_BD, UmengEventPoster.BAIDUAD_CLOSE, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                } else if (Utils.GDT_SPLASH_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportSplashAdClose("splash_launch", SelfDataConstant.AD_GDT, UmengEventPoster.GDTAD_CLOSE, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                } else if (Utils.JD_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportSplashAdClose("splash", SelfDataConstant.AD_JD, UmengEventPoster.JD_AD_CLOSE, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                } else if (Utils.FANCY_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportSplashAdClose("splash", SelfDataConstant.AD_FANCY, UmengEventPoster.FANCY_AD_CLOSE, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                }
//                redirectToMain();
//                break;
//            case R.id.ad_image:
//                break;
//            default:
//                break;
//        }
    }

    // 开始计时
    public void start() {
        leftTime = adTime;
        isRun = true;
        runTask();
    }

    //    private void showLaunchAd(NewAdDataBean result) {
//        // 启动广告图
//        ZySplashAdView.SplashAdListener splashAdListener = new ZySplashAdView.SplashAdListener() {
//            @Override
//            public void redirectToMain() {
//                SplashActivity.this.redirectToMain();
//            }
//
//            @Override
//            public void start() {
//                SplashActivity.this.start();
//            }
//        };
//        ZySplashAdView splashAdView = new ZySplashAdView(this, splashAdListener, mAdSid);
//        splashAdView.setDeliveryId(result.deliveryId);
//        splashAdView.setdisRtReport(result.disRtReport);
//        splashAdView.showLaunchAd(result);
//    }

//    private void reportVivoAdRequestFail(int errorCode) {
//        HashMap<String, String> gdtPropertyMap = new HashMap<>();
//        gdtPropertyMap.put("position", "splash");
//        UmengEventPoster.doPost(UmengEventPoster.VIVO_AD_REQUEST_ERROR, gdtPropertyMap);
//
//        AdRecord record = MyDataRecordCreater.build(AdRecord.class);
//        if (record != null) {
//            record.setActionCode(SelfDataConstant.AC_ADREQUEST_FAIL);
//            record.setAd_pro(SelfDataConstant.AD_VIVO);
//            record.setAd_po("splash");
//            record.setAdSid(mAdSid);
//            record.setErrorcode(String.valueOf(errorCode));
//            if (spareAdType == BaseAdView.SPARETYPE_DADI) {
//                record.setAd_type(AdRecord.AD_TYPE);
//            }
//            RealmDaoManager.addMyDataRecord(record);
//        }
//    }

    // 倒计时
    public void runTask() {
        if (isRun) {
            mAdTextTime.setText(getResources().getString(R.string.ad_text, leftTime));
            if (leftTime == 0) {
                isRun = false;
                redirectToMain();
//                String adstatus = "back";
//                if (canJump) {
//                    adstatus = "front";
//                }
//                if (Utils.GDT_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportAdShow5s("splash", SelfDataConstant.AD_GDT, adstatus, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                } else if (Utils.BAIDU_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportAdShow5s("splash", SelfDataConstant.AD_BD, adstatus, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                } else if (Utils.GDT_SPLASH_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    SelfCommonStatis.reportAdShow5s("splash_launch", SelfDataConstant.AD_GDT, adstatus, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                }

//                if (!Utils.GDT_SPLASH_AD.equalsIgnoreCase(mLaunchAdType)) {
//                    redirectToMain();
//                }
                return;
            }
            leftTime--;
            if (mHandler != null) {
                mHandler.postDelayed(new WeakRefHandler.WeakRefRunnable(mTask), 1000);
            }
        } else {
            redirectToMain();
        }
    }

    private void redirectToMain() {
        if (!canJump) {
            return;
        }
        isReportQuit = false;
//        if (YsApplication.getInstance().getActivity() == null) {
//            removeTask();
//            if ("0".equals(SharePreferenceManager.get(SplashActivity.this, SharePreferenceManager.PREFER_GENDER, "0"))) {
//                ChooseActivity.launchChooseActivity(SplashActivity.this);
//            } else {
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//            }
//            overridePendingTransition(0, 0);
//        }

        JxThreadPool.shared().post(new JxThreadRunnableImpl<SplashActivity>("app", this) {
            @Override public void run(SplashActivity application) {
                AppInfo.initialize(getApplicationContext());
                Retrofits.initialize(getApplicationContext());
                InitFetcher.fetch(value -> {
                    value.ifPresent(value1 -> {
                        application.runOnUiThread(new Runnable() {
                            @Override public void run() {
                                Intent intent = new Intent(application, LithographyActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }).orElse(value1 -> {
                        application.runOnUiThread(new Runnable() {
                            @Override public void run() {
                                Intent intent = new Intent(application, LithographyActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    });
                });
            }
        });


    }

//    private class LunchAdRequestResultListener implements
//            RequestResultListener<NewAdDataBean> {
//
//        @Override
//        public boolean onRequestFailed() {
//            AdRequestStatis.selfNoAdReport("fail", SelfDataConstant.AD_PO_SPLASH, mAdSid);
//            YsApplication instance = YsApplication.getInstance();
//            if (instance.getmLaunchTimeData() != null) {
//                instance.getmLaunchTimeData().setApiAdReponse(System.currentTimeMillis());
//            }
//            showDefaultLaunchAndRedirect();
//            return false;
//        }
//
//        @Override
//        public void onRequestSuccess(int updateID, NewAdDataBean result) {
//            YsApplication instance = YsApplication.getInstance();
//            if (instance.getmLaunchTimeData() != null) {
//                instance.getmLaunchTimeData().setApiAdReponse(System.currentTimeMillis());
//                if (result != null) {
//                    instance.getmLaunchTimeData().setAdtype(result.provider);
//                }
//            }
//            if (result != null) {
//                mLaunchAdType = result.provider;
//                mDeliveryId = result.deliveryId;
//                mDisRtReport = result.disRtReport;
//                mNewAdDataBean = result;
//                AdRequestStatis.selfResponseReport(result, SelfDataConstant.AD_PO_SPLASH, "", mAdSid, "");
//                if (Utils.GDT_AD.equalsIgnoreCase(result.provider)) {
//                    if (PreferencesManager.getInstance().getSplashGdtShowtimes() < PreferencesManager.getInstance().getSplashLimit()) {
//                        showGdtNativeAd(((SDKPayload) result.payload).adId, result.deliveryId, result.disRtReport);
//                    } else {
//                        redirectToMain();
//                    }
//                } else if (Utils.BAIDU_AD.equalsIgnoreCase(result.provider)) {
//                    AdStatisticsSp.savaOneUp(SplashActivity.this, AdStatisticsSp.BAIDUSPLASHADRESPONSE);
//                    showBaiduSplashAd(((SDKPayload) result.payload).adId, result.deliveryId, result.disRtReport);
//                } else if (Utils.GDT_SPLASH_AD.equalsIgnoreCase(result.provider)) {
//                    showGdtSplashAd(((SDKPayload) result.payload).adId, result.deliveryId, result.disRtReport);
//                } else if (Utils.HZ_AD.equals(result.provider)) {
//                    HzPayAdView hzAdView = new HzPayAdView(SplashActivity.this, adView, "splash");
//                    hzAdView.setDeliveryId(result.deliveryId);
//                    hzAdView.setdisRtReport(result.disRtReport);
//                    hzAdView.setAdSid(mAdSid);
//                    hzAdView.setHzAd(((HzPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.YZ_AD.equals(result.provider)) {
//                    YzPayAdView yzAdView = new YzPayAdView(SplashActivity.this, adView, "splash");
//                    yzAdView.setDeliveryId(result.deliveryId);
//                    yzAdView.setdisRtReport(result.disRtReport);
//                    yzAdView.setAdSid(mAdSid);
//                    yzAdView.setYzAd(((YzPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.XF_AD.equals(result.provider)) {
//                    XfPayAdView xfAdView = new XfPayAdView(SplashActivity.this, adView, "splash");
//                    xfAdView.setDeliveryId(result.deliveryId);
//                    xfAdView.setdisRtReport(result.disRtReport);
//                    xfAdView.setAdSid(mAdSid);
//                    xfAdView.setXfAd(((XfPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.TT_AD.equals(result.provider)) {
//                    TtPayAdView ttPayAdView = new TtPayAdView(SplashActivity.this, adView, "splash");
//                    ttPayAdView.setDeliveryId(result.deliveryId);
//                    ttPayAdView.setdisRtReport(result.disRtReport);
//                    ttPayAdView.setAdSid(mAdSid);
//                    ttPayAdView.setTtAd(((TtPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.QH_AD.equals(result.provider)) {
//                    QhPayAdView qhAdView = new QhPayAdView(SplashActivity.this, adView, "splash");
//                    qhAdView.setDeliveryId(result.deliveryId);
//                    qhAdView.setdisRtReport(result.disRtReport);
//                    qhAdView.setAdSid(mAdSid);
//                    qhAdView.setQhAd(((QhPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.XD_AD.equals(result.provider)) {
//                    XdPayAdView admAdView = new XdPayAdView(SplashActivity.this, adView, "splash");
//                    admAdView.setDeliveryId(result.deliveryId);
//                    admAdView.setdisRtReport(result.disRtReport);
//                    admAdView.setAdSid(mAdSid);
//                    admAdView.setAdmNativeAd(((XdPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.SN_AD.equals(result.provider)) {
//                    SnPayAdView admAdView = new SnPayAdView(SplashActivity.this, adView, "splash");
//                    admAdView.setDeliveryId(result.deliveryId);
//                    admAdView.setdisRtReport(result.disRtReport);
//                    admAdView.setAdSid(mAdSid);
//                    admAdView.setXmAd(((SnPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.OPPO_AD.equals(result.provider)) {
//                    showOppoSplashAd(((SDKPayload) result.payload).adId, result.deliveryId, result.disRtReport);
//                } else if (Utils.JD_AD.equals(result.provider)) {
//                    JdAdView jdAdView = new JdAdView(SplashActivity.this, adView, "splash");
//                    jdAdView.setDeliveryId(result.deliveryId);
//                    jdAdView.setdisRtReport(result.disRtReport);
//                    jdAdView.setAdSid(mAdSid);
//                    jdAdView.setJdAd(((JDPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.FANCY_AD.equals(result.provider)) {
//                    FancyAdView fancyAdView = new FancyAdView(SplashActivity.this, adView, "splash");
//                    fancyAdView.setDeliveryId(result.deliveryId);
//                    fancyAdView.setdisRtReport(result.disRtReport);
//                    fancyAdView.setAdSid(mAdSid);
//                    fancyAdView.setFancyAd(((FancyPayload) result.payload).ad);
//                    start();
//                    mAdTextTime.setVisibility(View.VISIBLE);
//                } else if (Utils.VIVO_AD.equals(result.provider)) {
//                    loadVivoAd(((SDKPayload) result.payload).adId, result.deliveryId, result.disRtReport);
//                } else if (Utils.TT_SDK_AD.equals(result.provider)) {
//                    showTtSplashAd(((SDKPayload) result.payload).adId, result.deliveryId, result.disRtReport);
//                } else {
//                    showLaunchAd(result);
//                }
//            } else {
//                AdRequestStatis.selfNoAdReport("nofill", SelfDataConstant.AD_PO_SPLASH, mAdSid);
//                showDefaultLaunchAndRedirect();
//            }
//        }
//
//        @Override
//        public void onPreRequest() {
//
//        }
//    }

//    private void loadVivoAd(final String adId, final String deliveryId, final String disRtReport) {
//        mAdId = adId;
//        final VivoAdView vivoAdView = new VivoAdView(SplashActivity.this, adView, "splash", mAdSid);
//        vivoAdView.setDeliveryId(deliveryId);
//        vivoAdView.setdisRtReport(disRtReport);
//        vivoAdView.setmSpareAdType(spareAdType);
//        vivoAdView.fetchSplashAD(adId, new SplashADListener() {
//            @Override
//            public void onADDismissed() {
//                removeTask();
//                redirectToMain();
//            }
//
//            @Override
//            public void onNoAD(AdError adError) {
//                reportVivoAdRequestFail(adError.getErrorCode());
//                vivoAdView.mIsSplashAdLoaded = true;
//                if (adError.getErrorCode() != 107) { //非超时失败
//                    loadSpareSdkAd(mNewAdDataBean);
//                } else {
//                    redirectToMain();
//                }
//            }
//
//            @Override
//            public void onADPresent() {
//                destroyOppoAdView();
//                vivoAdView.mIsSplashAdLoaded = true;
//                SelfCommonStatis.selfReportAdShow(SelfDataConstant.AD_VIVO, "splash", UmengEventPoster.VIVO_AD_SHOW, mAdSid, spareAdType, deliveryId, disRtReport, adId);
//            }
//
//            @Override
//            public void onADClicked() {
//                SelfCommonStatis.selfReportAdClick(SelfDataConstant.AD_VIVO, "splash", UmengEventPoster.VIVO_AD_CLICK, mAdSid, spareAdType, deliveryId, disRtReport, adId);
//                Logger.i("%s", "click is do");
//                YsApplication instance = YsApplication.getInstance();
//                if (instance.getmLaunchTimeData() != null) {
//                    instance.getmLaunchTimeData().setAction(SelfDataConstant.ACTION_CLICKAD);
//                }
//            }
//        });
//    }

    private void reportQuit() {
        YsApplication instance = YsApplication.getInstance();
//        if (instance.getmLaunchTimeData() != null) {
//            SelfCommonStatis.selfReportLaunchTime(instance.getmLaunchTimeData());
//            instance.setmLaunchTimeData(null);
//            UploadUtil.getInstance().upload();
//        }
    }

//    private void loadSpareSdkAd(NewAdDataBean result) {
//        if (spareAdType > 0) { //如果打底广告加载过，则直接跳首页，避免打底广告加载失败后还走到此方法里形成死循环
//            redirectToMain();
//            return;
//        }
//        spareAdType = BaseAdView.SPARETYPE_DADI;
//        if (result == null || result.candidates == null || result.candidates.size() == 0) {
//            redirectToMain();
//            return;
//        }
//        NewAdDataBean.SpareSdkAd spareSdkAd = result.candidates.get(0);
//        if (spareSdkAd == null) {
//            redirectToMain();
//            return;
//        }
//        mLaunchAdType = spareSdkAd.provider;
//        mDeliveryId = spareSdkAd.deliveryId;
//        mDisRtReport = spareSdkAd.disRtReport;
//        String sdkAdId = ((SDKPayload) spareSdkAd.payload).adId;
//
//        if (Utils.GDT_AD.equals(mLaunchAdType)) {
//            if (PreferencesManager.getInstance().getSplashGdtShowtimes() < PreferencesManager.getInstance().getSplashLimit()) {
//                showGdtNativeAd(sdkAdId, spareSdkAd.deliveryId, spareSdkAd.disRtReport);
//            } else {
//                redirectToMain();
//            }
//        } else if (Utils.GDT_SPLASH_AD.equals(mLaunchAdType)) {
//            showGdtSplashAd(sdkAdId, spareSdkAd.deliveryId, spareSdkAd.disRtReport);
//        } else if (Utils.BAIDU_AD.equals(mLaunchAdType)) {
//            AdStatisticsSp.savaOneUp(SplashActivity.this, AdStatisticsSp.BAIDUSPLASHADRESPONSE);
//            showBaiduSplashAd(sdkAdId, spareSdkAd.deliveryId, spareSdkAd.disRtReport);
//        } else if (Utils.OPPO_AD.equals(mLaunchAdType)) {
//            showOppoSplashAd(sdkAdId, spareSdkAd.deliveryId, spareSdkAd.disRtReport);
//        } else if (Utils.VIVO_AD.equals(mLaunchAdType)) {
//            loadVivoAd(sdkAdId, spareSdkAd.deliveryId, spareSdkAd.disRtReport);
//        } else if (Utils.TT_SDK_AD.equals(result.provider)) {
//            showTtSplashAd(sdkAdId, spareSdkAd.deliveryId, spareSdkAd.disRtReport);
//        } else {
//            redirectToMain();
//            Logger.i("%s", "do not need to load spare ad, the spare ad's provider is:" + spareSdkAd.provider);
//        }
//    }

    /**
     * 设置广点通的闪屏广告
     */
//    private void showGdtNativeAd(String gdtId, String deliveryId, String disRtReport) {
//        mAdId = gdtId;
//        GdtNativeAdView gdtSplashAd = new GdtNativeAdView(SplashActivity.this, "splash", "splash");
//        gdtSplashAd.setDeliveryId(deliveryId);
//        gdtSplashAd.setdisRtReport(disRtReport);
//        gdtSplashAd.setAdSid(mAdSid);
//        gdtSplashAd.setFrom(mFrom);
//        gdtSplashAd.setmSpareAdType(spareAdType);
//        gdtSplashAd.setAdListener(new OnAdLoadListener() {
//            @Override
//            public void onNoAd() {
//                loadSpareSdkAd(mNewAdDataBean);
//            }
//
//            @Override
//            public void onAdDismissed() {
//
//            }
//
//            @Override
//            public void onAdFailed() {
//                redirectToMain();
//            }
//
//            @Override
//            public void onAdPresent() {
//
//            }
//
//            @Override
//            public void onAdSuccess(Object obj) {
//                destroyOppoAdView();
//                start();
//                mAdTextTime.setVisibility(View.VISIBLE);
//                YsApplication instance = YsApplication.getInstance();
//                if (instance.getmLaunchTimeData() != null) {
//                    instance.getmLaunchTimeData().setSdkAdReponse(System.currentTimeMillis());
//                }
//            }
//        });
//        gdtSplashAd.loadAD(gdtId);
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 设置oppo的闪屏广告
     */
//    private void showOppoSplashAd(String adid, String deliveryId, String disRtReport) {
//        mAdId = adid;
//        mOppoAd = new OppoSplashAdView(SplashActivity.this, "splash", mAdSid);
//        mOppoAd.setDeliveryId(deliveryId);
//        mOppoAd.setdisRtReport(disRtReport);
//        mOppoAd.setmSpareAdType(spareAdType);
//        mOppoAd.setAdListener(new OnAdLoadListener() {
//            @Override
//            public void onNoAd() {
//                loadSpareSdkAd(mNewAdDataBean);
//            }
//
//            @Override
//            public void onAdDismissed() {
//                SelfCommonStatis.reportSplashAdClose("splash", SelfDataConstant.AD_OPPO, UmengEventPoster.OPPOAD_CLOSE, mAdSid, spareAdType, mDeliveryId, mDisRtReport, mAdId);
//                removeTask();
//                redirectToMain();
//            }
//
//            @Override
//            public void onAdFailed() {
//                redirectToMain();
//            }
//
//            @Override
//            public void onAdPresent() {
//
//            }
//
//            @Override
//            public void onAdSuccess(Object obj) {
//                YsApplication instance = YsApplication.getInstance();
//                if (instance.getmLaunchTimeData() != null) {
//                    instance.getmLaunchTimeData().setSdkAdReponse(System.currentTimeMillis());
//                }
//            }
//        });
//        mOppoAd.loadAD(adid);
//    }

//    private void showTtSplashAd(String adId, String deliveryId, String disRtReport) {
//        mAdId = adId;
//        TtSdkAdView ttSdkAdView = new TtSdkAdView(SplashActivity.this, adView, "splash", mAdSid);
//        ttSdkAdView.setFrom(mFrom);
//        ttSdkAdView.setDeliveryId(deliveryId);
//        ttSdkAdView.setdisRtReport(disRtReport);
//        ttSdkAdView.setmSpareAdType(spareAdType);
//        ttSdkAdView.setOnAdLoadListener(new OnAdLoadListener() {
//            @Override
//            public void onNoAd() {
//                loadSpareSdkAd(mNewAdDataBean);
//            }
//
//            @Override
//            public void onAdDismissed() {
//                redirectToMain();
//            }
//
//            @Override
//            public void onAdFailed() {
//                redirectToMain();
//            }
//
//            @Override
//            public void onAdPresent() {
//
//            }
//
//            @Override
//            public void onAdSuccess(Object obj) {
//
//            }
//        });
//        ttSdkAdView.loadSplashAd(adId);
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CheckActivity.REQUEST_CHECK) {
            if (resultCode == CheckActivity.CHECK_FINISH) {
                afterPerm();
            } else if (resultCode == CheckActivity.CHECK_EXIT) {
                finish();
            }
        }
    }


    private void showDefaultLaunchAndRedirect() {
        if (mHandler != null) {
            mHandler.postDelayed(new WeakRefHandler.WeakRefRunnable(mRedirectToMainRunnable), TIMEINTERVAL);
        }
    }

    private void removeTask() {
        if (mHandler != null && mTask != null) {
            mHandler.removeCallbacks(mTask);
        }
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
