package com.weiey.app.activities.swipeback;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.weiey.app.activities.base.BaseAppCompatActivity;


/**
 * @Description  侧滑关闭页面
 * Thx https://github.com/ikew0ng/SwipeBackLayout
 *
 */
@SuppressLint("NewApi")
public abstract class BaseSwipeBackActivity extends BaseAppCompatActivity {
    private SwipeBackLayout mSwipeBackLayout;
    private int mDefaultFragmentBackground = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onActivityCreate();
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.attachToActivity(this);
    }

    @Override
    public View findViewById(int id) {
        View view = super.findViewById(id);
        if (view == null && mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return view;
    }

    void onActivityCreate() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = new SwipeBackLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeBackLayout.setLayoutParams(params);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackLayout.setEnableGesture(enable);
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity可以滑动退出, 并且总是优先; false: Activity不允许滑动退出
     */
    public boolean swipeBackPriority() {
        return getSupportFragmentManager().getBackStackEntryCount() <= 1;
    }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * 库默认使用Theme的android:windowbackground作为Fragment的背景,
     * 如果不像使用windowbackground作为背景, 可以通过该方法改变Fragment背景。
     */
    protected void setDefaultFragmentBackground(int backgroundRes) {
        mDefaultFragmentBackground = backgroundRes;
    }

    int getDefaultFragmentBackground() {
        return mDefaultFragmentBackground;
    }
    
    protected void onSwipeBackFinish(){
      	 finish();
    }
}
