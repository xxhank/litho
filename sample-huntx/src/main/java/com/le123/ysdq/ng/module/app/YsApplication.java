package com.le123.ysdq.ng.module.app;

import android.graphics.Color;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.agx.scaffold.JxApplicationContext;
import com.agx.scaffold.JxFunc;
import com.agx.scaffold.JxLogger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.samples.litho.BuildConfig;
import com.facebook.soloader.SoLoader;
import com.github.moduth.blockcanary.BlockCanary;
import com.le123.ysdq.ng.AppInfo;
import com.le123.ysdq.ng.EasyPermissions;
import com.le123.ysdq.ng.Fetcher;
import com.le123.ysdq.ng.Retrofits;
import com.squareup.leakcanary.LeakCanary;

import jp.wasabeef.takt.Takt;

public class YsApplication extends MultiDexApplication {
    private static YsApplication instance;

    public static YsApplication getInstance() {
        return instance;
    }

    @Override public void onCreate() {
        super.onCreate();
        instance = this;

        installDevelopTools();

        Fresco.initialize(this);
        SoLoader.init(this, false);


        JxApplicationContext.initialize(getApplicationContext());

        if (EasyPermissions.hasPermissions(this, EasyPermissions.MUST_PERMISSIONS)) {
            JxLogger.i("got permission");
            onCreateWithPermission();
        } else {
            JxLogger.w("not permission");
            onCreateWithPermission();
        }
    }

    private void installDevelopTools() {
        if (!BuildConfig.DEBUG) {
            return;
        }

//        if (FlipperUtils.shouldEnableFlipper(this)) {
//            FlipperClient     client            = AndroidFlipperClient.getInstance(this);
//            DescriptorMapping descriptorMapping = DescriptorMapping.withDefaults();
//            LithoFlipperDescriptors.add(descriptorMapping);
//            client.addPlugin(new InspectorFlipperPlugin(this, descriptorMapping));
//            client.start();
//        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        Takt.stock(this).color(Color.RED);
        BlockCanary.install(this, new AppBlockCanaryContext(this)).start();
        LeakCanary.install(this);

        StrictMode.setThreadPolicy(
            new StrictMode.ThreadPolicy.Builder().detectAll()
                .penaltyLog().build());
        StrictMode.setVmPolicy(
            new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog().build());

    }

    private void onCreateWithPermission() {
        AppInfo.initialize(getApplicationContext());
        Retrofits.initialize(getApplicationContext());
        InitFetcher.fetch(new JxFunc.Action<Fetcher.Result<InitFetcher.Response, Object>>() {
            @Override public void yield(@NonNull Fetcher.Result<InitFetcher.Response, Object> value) {

            }
        });
    }
}
