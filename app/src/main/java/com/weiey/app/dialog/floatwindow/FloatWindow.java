package com.weiey.app.dialog.floatwindow;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.*;

import java.util.Timer;
import java.util.TimerTask;

public class FloatWindow implements Application.ActivityLifecycleCallbacks{


    // 监听
    public interface FloatWindowListener{
        void onShow(FloatWindow floatWindow);
        void onDismiss(FloatWindow floatWindow);
    };

    FloatWindowListener mListener;

    // 当前是否已经显示
    private volatile boolean isShow;

    // 自定义拖动处理
    private AbsDraggable mDraggable;

    // 显示时长
    private int mDuration;

    private WindowManager mWindowManager;

    private WindowManager.LayoutParams mWindowParams;

    private View mRootView;

    private Context mContext;

    private Activity mActivity;

    public FloatWindow(Activity activity) {
        this((Context) activity);
        mActivity = activity;
        // 跟随 Activity 的生命周期
        activity.getApplication().registerActivityLifecycleCallbacks(this);
    }

    public FloatWindow(Application application) {
        this((Context) application);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
    }

    private FloatWindow(Context context) {
        mContext = context;
        mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));

        mWindowParams = new WindowManager.LayoutParams();
        // 配置一些默认的参数
        mWindowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = android.R.style.Animation_Toast;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWindowParams.packageName = context.getPackageName();
        mWindowParams.gravity = Gravity.CENTER;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    /**
     * 设置标志位
     */
    public FloatWindow setFlags(int flags) {
        mWindowParams.flags = flags;
        return  this;
    }

    /**
     * 设置显示的类型
     */
    public FloatWindow setType(int type) {
        mWindowParams.type = type;
        return  this;
    }

    /**
     * 设置动画样式
     */
    public FloatWindow setAnimStyle(int resId) {
        mWindowParams.windowAnimations = resId;
        return  this;
    }

    /**
     * 设置拖动
     */
    public FloatWindow setDraggable() {
        return setDraggable(new MovingDraggable());
    }

    public FloatWindow setDraggable(AbsDraggable draggable) {
        // 设置触摸范围为当前的 RootView，而不是整个WindowManager
        mWindowParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mDraggable = draggable;
        return  this;
    }

    /**
     * 设置宽度
     */
    public FloatWindow setWidth(int width) {
        mWindowParams.width = width;
        return this;
    }

    /**
     * 设置高度
     */
    public FloatWindow setHeight(int height) {
        mWindowParams.height = height;
        return  this;
    }

    /**
     * 限定显示时长
     */
    public FloatWindow setDuration(int duration) {
        mDuration = duration;
        return this;
    }



    /**
     * 设置重心
     */
    public FloatWindow setGravity(int gravity) {
        mWindowParams.gravity = gravity;
        return   this;
    }

    /**
     * 设置 X 轴偏移量
     */
    public FloatWindow setXOffset(int x) {
        mWindowParams.x = x;
        return  this;
    }

    /**
     * 设置 Y 轴偏移量
     */
    public FloatWindow setYOffset(int y) {
        mWindowParams.y = y;
        return this;
    }

    /**
     * 设置 WindowManager 参数集
     */
    public FloatWindow setWindowParams(WindowManager.LayoutParams params) {
        mWindowParams = params;
        return this;
    }

    /**
     * 当前是否已经显示
     */
    public boolean isShow() {
        return isShow;
    }

    /**
     * 获取上下文对象
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 跳转 Activity
     */
    public void startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(mContext, cls));
    }

    public void startActivity(Intent intent) {
        if (!(mContext instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }

    /**
     * 显示
     */
    public FloatWindow show() {
        if (mRootView == null || mWindowParams == null) {
            throw new IllegalArgumentException("WindowParams and view cannot be empty");
        }

        // 如果当前已经显示取消上一次显示
        if (isShow) {
            cancel();
        }
        try {
            // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
            // java.lang.IllegalStateException: View android.widget.TextView{3d2cee7 V.ED..... ......ID 0,0-312,153} has already been added to the window manager.
            mWindowManager.addView(mRootView, mWindowParams);
            // 当前已经显示
            isShow = true;
            // 如果当前限定了显示时长
            if (mDuration != 0) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (isShow()) {
                            cancel();
                        }
                    }
                }, mDuration);
            }
            // 如果当前设置了拖拽
            if (mDraggable != null) {
                mDraggable.start(this);
            }

            // 回调监听
            if (mListener != null) {
                mListener.onShow(this);
            }
        } catch (NullPointerException | IllegalStateException | WindowManager.BadTokenException ignored) {}

        return this;
    }



    /**
     * 取消
     */
    public FloatWindow cancel() {
        if (isShow) {
            try {
                // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
                // java.lang.IllegalArgumentException: View=android.widget.TextView{3d2cee7 V.ED..... ........ 0,0-312,153} not attached to window manager
                mWindowManager.removeView(mRootView);
                // 回调监听
                if (mListener != null) {
                    mListener.onDismiss(this);
                }
            } catch (NullPointerException | IllegalArgumentException ignored) {}
            // 当前没有显示
            isShow = false;
        }

        return  this;
    }

    /**
     * 获取 WindowManager 参数集
     */
    public WindowManager.LayoutParams getWindowParams() {
        return mWindowParams;
    }

    /**
     * 设置布局
     */
    public FloatWindow setView(int layoutId) {
        return setView(LayoutInflater.from(mContext).inflate(layoutId, null));
    }
    public FloatWindow setView(View view) {
        cancel();
        mRootView = view;
        return this;
    }

    /**
     * 获取布局
     */
    public View getView() {
        return mRootView;
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (mActivity != null && mActivity == activity && activity.isFinishing() && isShow()) {
            cancel();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
