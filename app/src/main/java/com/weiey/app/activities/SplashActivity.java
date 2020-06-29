package com.weiey.app.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import butterknife.BindView;
import com.weiey.app.R;
import com.weiey.app.activities.base.BaseActivity;

/**
 *
 * @Description: 启动页
 *
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_root)
    RelativeLayout splashRoot;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    /**
     * 设置当前页面不可侧滑
     * @return
     */
    @Override
    protected boolean isSwipeBackEnable() {  return false; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //判断SplashActivity是否位于栈底
        if (!isTaskRoot()) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashRoot.startAnimation(animation);

    }

    @Override
    protected View getLoaingTargetView() {
        return splashRoot;
    }

    private void toNext() {
        to(HomeActivity.class);
        finish();
    }

}
