package com.weiey.app.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import com.weiey.app.AppContext;

/**
 * ToastUtil
 *
 */
public class ToastUtil {
    private static Handler sHandler = new Handler(Looper.getMainLooper());


    private ToastUtil() {
    }


    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }

    public static void runInUIThread(Runnable task) {
        sHandler.post(task);
    }

    public static void runInUIThread(Runnable task, long delayMillis) {
        sHandler.postDelayed(task, delayMillis);
    }




    /**
     * 显示吐司
     *
     * @param text
     */
    public static void show(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.length() < 10) {
                Toast.makeText(AppContext.getContext(), text, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppContext.getContext(), text, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 显示吐司
     *
     * @param resId
     */
    public static void show(int resId) {
        show(AppContext.getContext().getString(resId));
    }

    /**
     * 在子线程中显示吐司时使用该方法
     *
     * @param text
     */
    public static void showSafe(final CharSequence text) {
        runInUIThread(new Runnable() {
            @Override
            public void run() {
                show(text);
            }
        });
    }

    /**
     * 在子线程中显示吐司时使用该方法
     *
     * @param resId
     */
    public static void showSafe(int resId) {
        showSafe(AppContext.getContext().getString(resId));
    }
}