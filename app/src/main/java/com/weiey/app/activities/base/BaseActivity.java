package com.weiey.app.activities.base;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.weiey.app.AppConstants;
import com.weiey.app.AppManager;
import com.weiey.app.R;
import com.weiey.app.activities.swipeback.BaseSwipeBackActivity;
import com.weiey.app.utils.SPUtil;
import com.weiey.app.view.loading.VaryViewHelperController;
import pub.devrel.easypermissions.EasyPermissions;


public abstract class BaseActivity extends BaseSwipeBackActivity {

    //通用loading页error页等的控制器
    private VaryViewHelperController mVaryViewHelperController;

    protected Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.setStatusBarTranslucent(this, false);
        if (null == mVaryViewHelperController && null != getLoaingTargetView() )
            mVaryViewHelperController = new VaryViewHelperController(getLoaingTargetView());

    }
    protected abstract View getLoaingTargetView();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setSwipeBackEnable(isSwipeBackEnable());
        mToolbar = $(R.id.common_toolbar);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }





    /**
     * 是否可以滑动关闭
     * @return
     */
    protected boolean isSwipeBackEnable() {
        return AppManager.getInstance().getActivityStack().size() > 1 && AppConstants.isSwipeBackEnable;
    }
//
    protected void navBack(View v) {
        if (isSwipeBackEnable()) {
            onSwipeBackFinish();
        } else {
            finish();
        }
    }


    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
//        if (mToolbar != null) {
//            TextView textView = mToolbar.findViewById(R.id.tv_title);
//            if (textView != null) {
//                textView.setText(title);
//            }
//        }
    }
    int mScreenHeight = 0;
    int mScreenWidth = 0;

    private void getScreenWidthHeight() {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            mScreenHeight = displayMetrics.heightPixels;
            mScreenWidth = displayMetrics.widthPixels;
        } catch (Exception e) {
        }
    }


    /**
     * 处理输入框弹出遮挡输入框问题
     *
     * @param main
     * @param scroll
     */
    protected void addLayoutListener(final View main, final View scroll) {
        getScreenWidthHeight();
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //1、获取main在窗体的可视区域
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                //获得当前焦点所在的view
                View view = getCurrentFocus();
                if (view == null) {
                    return;
                }
                final int[] wh = new int[2];
                view.getLocationOnScreen(wh);
                int w = wh[0];
                int h = wh[1];
                int vh = view.getHeight();
                int mHeight2 = mScreenHeight / 2;
                int mHeight4 = mScreenHeight / 4;
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (h > mHeight2 && (mainInvisibleHeight > mHeight4)) {
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    //5､让界面整体上移键盘的高度
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }


            }
        });
    }




    public void showLoading() {
        if (mVaryViewHelperController == null) {
            return;
        }
        mVaryViewHelperController.showLoading();
    }

    public void refreshView() {
        if (mVaryViewHelperController == null) {
            return;
        }
        mVaryViewHelperController.restore();
    }

    public void showNetError() {
        if (mVaryViewHelperController == null) {
            return;
        }
        mVaryViewHelperController.showNetworkError(v -> {
            showLoading();
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
