package com.example.qrcode.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    /**
     * Log tag
     */
    protected static String TAG_LOG = null;
    /**
     * Screen information
     */
//    protected int mScreenWidth = 0;
//    protected int mScreenHeight = 0;
//    protected float mScreenDensity = 0.0f;
    /**
     * context
     */
    protected Context mContext = null;

    /**
     * overridePendingTransition mode
     * */
    public enum TransitionMode {

        DEFAULT,LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE, RIGHT_IN_NO_ANIM_OUT
    }

    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }



    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        overrideTransition();
        super.onCreate(savedInstanceState);
        mContext = this;
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        TAG_LOG = this.getClass().getSimpleName();
        initView();
    }

    protected abstract void initView();

    @Override
    public void finish() {
        super.finish();
//        overrideTransition();
    }

//    private void overrideTransition() {
//        switch (getOverridePendingTransitionMode()) {
//            case LEFT:
//                overridePendingTransition(R.anim.left_in, R.anim.left_out);
//                break;
//            case RIGHT:
//                overridePendingTransition(R.anim.right_in, R.anim.right_out);
//                break;
//            case TOP:
//                overridePendingTransition(R.anim.top_in, R.anim.top_out);
//                break;
//            case BOTTOM:
//                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
//                break;
//            case SCALE:
//                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
//                break;
//            case FADE:
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                break;
//            case RIGHT_IN_NO_ANIM_OUT:
//                overridePendingTransition(R.anim.right_in, 0);
//                break;
//            case DEFAULT:
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 跳转动画类型
     *
     */
    protected TransitionMode getOverridePendingTransitionMode(){
        return TransitionMode.DEFAULT;
    };





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
     * startActivity then finish
     *
     * @param clazz
     */
    protected void toThenKill(Class<?> clazz) {

        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void toThenKill(Class<?> clazz, Bundle bundle) {

        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
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
