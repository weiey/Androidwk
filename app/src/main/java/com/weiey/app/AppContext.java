package com.weiey.app;
import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

public class AppContext extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppManager.init(this);
        initLeakCanary();
        initBugly();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initBugly() {
        String id = BuildConfig.DEBUG?"27e515dd46":"f8cc92746c";
        CrashReport.initCrashReport(getApplicationContext(), id, true);
    }
    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }
}
