package com.weiey.app.dialog.floatwindow;

import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public abstract class AbsDraggable implements View.OnTouchListener {

    private FloatWindow mToast;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private View mRootView;

    /**
     * Toast 显示后回调这个类
     */
    public void start(FloatWindow toast) {
        mToast = toast;
        mWindowManager = toast.getWindowManager();
        mWindowParams = toast.getWindowParams();
        mRootView = toast.getView();

        mRootView.setOnTouchListener(this);
    }

    protected FloatWindow getXToast() {
        return mToast;
    }

    protected WindowManager getWindowManager() {
        return mWindowManager;
    }

    protected WindowManager.LayoutParams getWindowParams() {
        return mWindowParams;
    }

    protected View getRootView() {
        return mRootView;
    }

    /**
     * 获取状态栏的高度
     */
    protected int getStatusBarHeight() {
        Rect frame = new Rect();
        getRootView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 更新 WindowManager 所在的位置
     */
    public void updateViewLayout(int x, int y) {
        mWindowParams.x = x;
        mWindowParams.y = y;
        // 一定要先设置重心位置为左上角
        mWindowParams.gravity = Gravity.TOP | Gravity.START;
        mWindowManager.updateViewLayout(mRootView, mWindowParams);
    }
}
