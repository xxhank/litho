package com.le123.ysdq.ng.module.privacy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agx.scaffold.JxApplicationContext;
import com.facebook.samples.litho.R;
import com.le123.ysdq.ng.EasyPermissions;

import java.util.List;

/**
 * Created by wangyemin on 2015/11/3.
 * <p/>
 * 启动页
 */
public class CheckActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final int  REQUEST_CHECK = 430;
    public static final int  CHECK_FINISH  = 431;
    public static final int  CHECK_EXIT    = 432;
    private             long noExitTime    = 0;

    public static void startCheckActivity(Activity activity) {
        Intent intent = new Intent(activity, CheckActivity.class);
        activity.startActivityForResult(intent, REQUEST_CHECK);
    }

    public static int dipToPx(int dipValue) {
        float scale = JxApplicationContext.shared().getResources()
            .getDisplayMetrics().density;
        int pxValue = (int) (dipValue * scale + 0.5f);

        return pxValue;
    }

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
        setContentView(R.layout.activity_check);
        checkPermissons();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //展示隐私保护
    private void showPrivacy() {
        View   privacyView   = View.inflate(this, R.layout.privacy_protect_dialog, null);
        Dialog privacyDialog = new Dialog(this, R.style.noframe_dialog);
        privacyDialog.setContentView(privacyView);
        privacyDialog.setCanceledOnTouchOutside(false);
        privacyDialog.setCancelable(false);
        Window                     window = privacyDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = dipToPx(290);            //使用这种方式更改了dialog的框宽
        window.setAttributes(params);

        String                 privacyNotice = "尊敬的用户：\n衷心感谢您选用视频聚合极速版产品!\n我们非常尊重并保护您的个人信息、隐私，并为此特别拟定专门的隐私政策，在此烦请您仔细阅读并理解，进而作出您认为合适的选择。如您有任何疑惑，可通过电子邮箱：\ncopyright@jinshangtechnology.com向我方询问，我方将及时为您解答。\n我们的隐私政策概要如下：\n1.适用范围、相关词语定义\n2.我方收集和使用您的个人信息方式\n3.我方使用Cookie和同类技术的方式\n4.我方共享、转让、公开披露您个人信息的方式\n5.您有权控制您的个人信息\n6.我方存储、保护您个人信息的方式\n7.未成年人保护\n8.本隐私政策的更新\n9.联系方式\n10.本隐私政策解释及争议解决\n如您想了解更多，烦请点阅《视频聚合极速版隐私政策》。";
        SpannableStringBuilder style         = new SpannableStringBuilder();
        style.append(privacyNotice);
        ForegroundColorSpan grayColorSpan1  = new ForegroundColorSpan(Color.parseColor("#66000000"));
        ForegroundColorSpan blackColorSpan1 = new ForegroundColorSpan(Color.parseColor("#000000"));
        ForegroundColorSpan grayColorSpan2  = new ForegroundColorSpan(Color.parseColor("#66000000"));
        ForegroundColorSpan blackColorSpan2 = new ForegroundColorSpan(Color.parseColor("#000000"));
        ForegroundColorSpan blueColorSpan   = new ForegroundColorSpan(Color.parseColor("#3599f8"));
        UnderlineSpan underlineSpan = new UnderlineSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#3599f8"));
            }
        };
        String ppocy = "https://s.yingshidq.com.cn/app/yingshidq-privacy-20190314.html";
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//                Intent   lawIntent = new Intent();
//                JumpData jumpData  = new JumpData();
//                jumpData.setPlayUrl(ppocy);
//                lawIntent.setClass(CheckActivity.this, CommonWebViewActivity.class);
//                Bundle jumpbundle = new Bundle();
//                jumpbundle.putSerializable(Utils.CHANNEL_DTAT, jumpData);
//                lawIntent.putExtras(jumpbundle);
//                startActivity(lawIntent);
//            }
//        };
        style.setSpan(grayColorSpan1, 0, 56, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(blackColorSpan1, 56, 82, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(grayColorSpan2, 82, 161, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(blackColorSpan2, 161, 324, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(clickableSpan, 324, 335, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(blueColorSpan, 324, 335, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(underlineSpan, 324, 335, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        TextView tv = privacyView.findViewById(R.id.privacy_tv);
        tv.setText(style);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        privacyView.findViewById(R.id.negative_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - noExitTime > 2000) {
                    noExitTime = System.currentTimeMillis();
//                    ToastUtil.show(CheckActivity.this, "再按一次退出视频聚合极速版");
                } else {
                    setResult(CheckActivity.CHECK_EXIT);
                    finish();
                }
            }
        });
        privacyView.findViewById(R.id.positive_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyDialog.dismiss();
//                SharePreferenceManager.put(CheckActivity.this, SharePreferenceManager.AGREE_PRIVACY, true);
                checkPermissons();
            }
        });
        privacyDialog.show();
    }

    private void checkPermissons() {
        //判断必要权限
        if (EasyPermissions.hasPermissions(this, EasyPermissions.MUST_PERMISSIONS)) {
//            YsApplication.getInstance().applicationInit();
            setResult(CHECK_FINISH);
            finish();
        } else {
            EasyPermissions.showPermissonsDialog(this, EasyPermissions.PERMISSION_REQUEST, EasyPermissions.FIRST_PERMISSIONS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.size() == 2 && perms.contains(EasyPermissions.LOCATION_PERMISSION[0]) && perms.contains(EasyPermissions.LOCATION_PERMISSION[1])) {
            //如果仅仅是定位权限被拒绝了依然可以进入app
//            YsApplication.getInstance().applicationInit();
            setResult(CHECK_FINISH);
            finish();
        } else {    //其他权限被拒依旧进入权限授予
            EasyPermissions.requestPermissions(this, EasyPermissions.PERMISSION_REQUEST, perms.toArray(new String[perms.size()]));
        }
    }

    @Override
    public void onPermissionsAllGranted() {
//        YsApplication.getInstance().applicationInit();
        setResult(CHECK_FINISH);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            //设置返回
            checkPermissons();
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
