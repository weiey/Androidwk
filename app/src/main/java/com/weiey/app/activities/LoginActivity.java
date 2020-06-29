package com.weiey.app.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.weiey.app.R;
import com.weiey.app.activities.base.BaseActivity;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.cb_visibiliy_pwd)
    ImageButton cbVisibiliyPwd;
    @BindView(R.id.tv_logo)
    TextView tvLogo;
    @BindView(R.id.tv_ver)
    TextView tvVer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    /**
     * 设置当前页面不可侧滑
     *
     * @return
     */
    @Override
    protected boolean isSwipeBackEnable() {
        return false;
    }


    @Override
    protected void init() {

    }

    private void login() {


    }

    @Override
    protected View getLoaingTargetView() {
        return container;
    }

    private void toNext() {
        to(HomeActivity.class);
        finish();
    }

//    private void checkVersion() {
//        Map<String, Object> body = new LinkedHashMap<>();
//        CallManager.getInstance().postForm(this, Urls.version, null, body, new CallManager.ResponseCallback() {
//            @Override
//            public void onSuccess(Result res) {
//                dismissProgressLoading();
//                if (res.isOk()) {
//                    try {
//                        JSONObject object = new JSONObject(GsonUtils.toJson(res.getData()));
//                        int version = object.optInt("version");
//                        String install_url = object.optString("install_url");
//                        if (version > BuildConfig.VERSION_CODE) {
//                            showUpdate(install_url);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    //ToastUtil.showSafe(res.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                dismissProgressLoading();
//                ToastUtil.showSafe(msg);
//            }
//        });

        //
//        String url = "http://api.bq04.com/apps/latest/5e1ed421b2eb4661eb0f21f0";
//        Map<String, Object> map = new LinkedHashMap<>();
////        map.put("api_token", "0214fc0579955412e26ac9c298c2538e");
//        OkHttpUtil.get(Urls.version, null, map, new OkHttpUtil.ReqCallBack() {
//            @Override
//            public void onSuccess(String result) {
//                try {
//                    JSONObject object = new JSONObject(result);
//                    int version = object.optInt("version");
//                    String install_url = object.optString("install_url");
//                    if (version > BuildConfig.VERSION_CODE) {
//                        showUpdate(install_url);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailed(String errorMsg) {
//
//            }
//        });
//
//    }

    private void showUpdate(final String install_url) {
        check(install_url, true, "发现新版本,是否更新？");
//        AlertUtil.showAlert(mContext, "提示", "发现新版本,是否更新？", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                openBrowser(install_url);
//            }
//        }, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });

    }


    /**
     * 调用第三方浏览器打开
     *
     * @param url 要浏览的资源地址
     */
    public void openBrowser(String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
// 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
// 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(getPackageManager()); // 打印Log   ComponentName到底是什么 L.d("componentName = " + componentName.getClassName());
            startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void check(String apkUrl, boolean isForceUpdate, String content) {
//        DownloadBuilder downloadBuilder = AllenVersionChecker.getInstance().downloadOnly(UIData.create()
//                .setDownloadUrl(apkUrl).setContent(content)
//                .setTitle("提示"));
//        downloadBuilder.setForceRedownload(isForceUpdate);
//        downloadBuilder.setShowNotification(false);
//        if (isForceUpdate) {
//            downloadBuilder.setForceUpdateListener(new ForceUpdateListener() {
//                @Override
//                public void onShouldForceUpdate() {
//                    finish();
//                }
//            });
//        }
//        downloadBuilder.executeMission(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AllenVersionChecker.getInstance().cancelAllMission(this);
    }
}
