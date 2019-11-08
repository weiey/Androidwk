package com.weiey.app.utils;

import android.util.Log;
import com.weiey.app.AppConfig;
/**
 *
 * @Description:
 *
 * @author zw
 * @date 2019/11/5 11:31
 */
public class LogUtil {
    private static final String TAG = "Logger";
    private static boolean DEBUG = AppConfig.DEBUG;

    public static void i(String tag, String msg) {
        if (DEBUG) {
            try {
                Log.i(tag, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            try {
                Log.d(tag, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void e(String tag, String msg) {
        if (DEBUG) {
            try {
                Log.e(tag, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void e(String tag,Throwable e) {
        if (DEBUG) {
            try {
                Log.e(tag, Log.getStackTraceString(e));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    public static void e(String msg) {
        if (DEBUG) {
            try {
                Log.e(getCurrentClassName(), msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 默认当前类名作为TAG
     * @param e
     */
    public static void e(Throwable e) {
        if (DEBUG) {
            try {
                Log.e(getCurrentClassName(), Log.getStackTraceString(e));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }





    /**
     * 获取打印信息所在方法名，行号等信息
     *
     * @return
     */
    private static String getCurrentClassName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 5) {
            Log.e(TAG, "Stack is too shallow!!!");
            return TAG;
        }
        try {
            String className = elements[4].getClassName().substring(
                    elements[4].getClassName().lastIndexOf(".") + 1);
            return className;
        } catch (Exception e) {
//            e.printStackTrace();
//            e(Log.getStackTraceString(e));
        }
        return TAG;
    }
}
