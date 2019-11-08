package com.weiey.app.utils;

import android.app.Application;
import android.util.Log;
import android.util.TypedValue;

/**
 * app工具类
 *
 */
public class AppUtil {
    private static final Application sApp;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(AppUtil.class.getSimpleName(), "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(AppUtil.class.getSimpleName(), "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            sApp = app;
        }
    }

    private AppUtil() {
    }

    public static Application getApp() {
        return sApp;
    }


    public static int getDimensionPixelOffset(int resId) {
        return getApp().getResources().getDimensionPixelOffset(resId);
    }

    public static int getColor(int resId) {
        return getApp().getResources().getColor(resId);
    }
    public static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getApp().getResources().getDisplayMetrics());
    }

    public static int sp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpValue, getApp().getResources().getDisplayMetrics());
    }
}