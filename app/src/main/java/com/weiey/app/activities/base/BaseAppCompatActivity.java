package com.weiey.app.activities.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.weiey.app.AppManager;
import com.weiey.app.R;
import com.weiey.app.utils.KeyboardUtils;


public abstract class BaseAppCompatActivity extends AppCompatActivity {
    /**
     * Log tag
     */
    protected static String TAG_LOG = null;
    /**
     * context
     */
    protected Context mContext = null;

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        DEFAULT, LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }


    /**
     * 绑定界面布局
     */
    protected abstract int getLayoutId();

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overrideTransition();
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getInstance().addActivity(this);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        TAG_LOG = this.getClass().getSimpleName();
        unbinder = ButterKnife.bind(this);
        init();
    }

    protected abstract void init();

    @Override
    public void finish() {
        KeyboardUtils.hideSoftInput(this);
        super.finish();
        AppManager.getInstance().removeActivity(this);
        overrideTransition();
    }

    private void overrideTransition() {
        switch (getOverridePendingTransitionMode()) {
            case LEFT:
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case RIGHT:
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case TOP:
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            case BOTTOM:
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                break;
            case FADE:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case DEFAULT:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    /**
     * 跳转动画类型
     */
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.DEFAULT;
    }

    ;


    /**
     * startActivity
     *
     * @param clazz
     */
    protected void to(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void to(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void toForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void toForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
