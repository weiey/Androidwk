package com.weiey.app;

import android.text.TextUtils;
import com.weiey.app.utils.LogUtil;
import com.weiey.app.utils.SPUtil;
import java.io.Serializable;


/**
 *
 * @Description: API配置
 *
 * @author zw
 * @date 2019/11/5 13:38
 */
public final class Urls implements Serializable {
    /*** 常量***/
    public final static String HTTP = "http";
    public final static String HTTPS = "https";

    /***RELEASE环境***/
    private final static String BASE_URL_RELEASE = "http://127.0.0.1:8080/release/";
    /***DEBUG环境**/
    private final static String BASE_URL_DEBUG = "http://127.0.0.1:8080/debug/";


    /**
     * 根据环境设置地址
     * @return
     */
    public static String getBaseServiceUrl() {
        if(TextUtils.isEmpty(baseService)){
            loadUrlConfig();
        }
        return baseService;
    }

    /***
     * 实际使用的地址
     */
    public static String baseService = null;


    /**
     * 设置环境
     *
     * @param type
     */
    public static void setUrlConfig(int type) {
        SPUtil.getInstance().putInt(AppConfig.URL_CONFIG, type);
        loadUrlConfig();
    }
    /**
     *  从配置文件读取，接口地址
     */
    private static void loadUrlConfig() {
        int type = AppConfig.APP_EVN;
        if(AppConfig.DEBUG){
            type = SPUtil.getInstance().getInt(AppConfig.URL_CONFIG, AppConfig.APP_EVN);
        }
        switch (type) {
            //debug
            case 0:
                baseService = Urls.BASE_URL_DEBUG;
                break;
            //release
            case 1:
                baseService = Urls.BASE_URL_RELEASE;
                break;
            default:
                baseService = Urls.BASE_URL_RELEASE;
                break;
        }
        LogUtil.d("baseService==>", baseService);
    }


}
