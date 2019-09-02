package com.le123.ysdq.ng;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author: atta
 * @date: 2018/5/30
 * @describe: 实现回调弱引用的Handler
 * <p>
 * 注意：传入的Callback不能使用匿名实现的变量，必须与使用这个Handle的对象的生命周期一致，否则会被立即释放掉了
 */

public class WeakRefHandler extends Handler {

    private WeakReference<Callback> mWeakReference;

    public WeakRefHandler() {
        super();
    }

    public WeakRefHandler(Callback callback) {
        mWeakReference = new WeakReference<>(callback);
    }

    public WeakRefHandler(Callback callback, Looper looper) {
        super(looper);
        mWeakReference = new WeakReference<>(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mWeakReference != null && mWeakReference.get() != null) {
            Callback callback = mWeakReference.get();
            callback.handleMessage(msg);
        }
    }

    /**
     * 清空消息队列，在定义handler的类销毁前记得调用
     */
    public void releaseMessageQueue() {
        removeCallbacksAndMessages(null);
    }

    /**
     * 注意：
     * 传进来的Runnable不能使用匿名实现的变量，必须与使用这个Handle的对象的生命周期一致，否则会被立即释放掉了
     */
    public static class WeakRefRunnable implements Runnable {

        WeakReference<Runnable> reference;

        public WeakRefRunnable(Runnable runnable) {
            reference = new WeakReference<>(runnable);
        }

        @Override
        public void run() {
            Runnable runnable = reference.get();
            if (runnable != null) {
                runnable.run();
            }
        }
    }
}