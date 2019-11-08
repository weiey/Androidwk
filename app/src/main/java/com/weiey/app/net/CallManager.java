package com.weiey.app.net;

import android.os.Handler;
import android.os.Looper;
import com.weiey.app.utils.GsonUtils;
import com.weiey.app.utils.LogUtil;
import okhttp3.Call;
import java.util.*;

/**
 *
 */
public class CallManager {

    public interface ResponseCallback{
        void onSuccess(Result body);
        void onFailure(String msg);
    }
    public interface ResponseProgressCallBack extends ResponseCallback{
        /**
         * 响应进度更新
         * @param total
         * @param current
         */
        void onProgress(long total, long current);
    }

    /**全局处理子线程和M主线程通信*/
    public static Handler callHandler = new Handler(Looper.getMainLooper());


    private CallManager() {
    }


    private static CallManager instance;
    public static CallManager getInstance() {
        if (instance == null) {
            synchronized (CallManager.class) {
                if (instance == null) {
                    instance = new CallManager();
                }
            }
        }
        return instance;
    }

    private Map<String, Object> addBaseHeaders(Map<String, Object> header){
        if(header == null){
            header = new HashMap<>();
        }
        header.put("","");
        return header;
    }

    private Map<String, Object> addBaseParams(Map<String, Object> body){
        if(body == null){
            body = new HashMap<>();
        }
        body.put("","");
        return body;
    }


    /**
     * post 异步
     * @param tag
     * @param url
     * @param header
     * @param body
     * @param callback
     */
    public void post(Object tag,String url,Map<String, Object> header,Map<String, Object> body,final ResponseCallback callback){
        header = addBaseHeaders(header);
        body  = addBaseParams(body);

        Call call = OkHttpUtil.post(url, header, body, new OkHttpUtil.ReqCallBack() {
            @Override
            public void onSuccess(String result) {
                Result t = handleResult(result);
                success(callback,t);


            }

            @Override
            public void onFailed(String errorMsg) {
                failed(callback,errorMsg);
            }
        });
        addTag(tag, call);
    }
    /**
     * post 异步
     * @param tag
     * @param url
     * @param header
     * @param body
     * @param callback
     */
    public void postForm(Object tag,String url,Map<String, Object> header,Map<String, Object> body,final ResponseCallback callback){
        header = addBaseHeaders(header);
        body  = addBaseParams(body);

        Call call = OkHttpUtil.postForm(url, header, body, new OkHttpUtil.ReqCallBack() {
            @Override
            public void onSuccess(String result) {
                Result t = handleResult(result);
                success(callback,t);


            }

            @Override
            public void onFailed(String errorMsg) {
                failed(callback,errorMsg);
            }
        });
        addTag(tag, call);
    }

    /**
     * get 异步
     * @param tag
     * @param url
     * @param header
     * @param body
     * @param callback
     */
    public void get(Object tag,String url,Map<String, Object> header,Map<String, Object> body,final ResponseCallback callback){
        header = addBaseHeaders(header);
        body  = addBaseParams(body);

        Call call = OkHttpUtil.get(url, header, body, new OkHttpUtil.ReqCallBack() {
            @Override
            public void onSuccess(String result) {
                Result t = handleResult(result);
                success(callback,t);


            }

            @Override
            public void onFailed(String errorMsg) {
                failed(callback,errorMsg);
            }
        });
        addTag(tag, call);

    }

    /**
     * 上传文件
     * @param url
     * @param header
     * @param body  直接传 File类型
     * @param callback
     */
    public void uploadFile(String url,Map<String, Object> header,Map<String, Object> body,final ResponseProgressCallBack callback){
        header = addBaseHeaders(header);
        body  = addBaseParams(body);

        OkHttpUtil.uploadFile(url, header, body, new OkHttpUtil.ReqProgressCallBack() {
            @Override
            public void onSuccess(String result) {
                Result t = handleResult(result);
                success(callback,t);

            }

            @Override
            public void onFailed(String errorMsg) {
                failed(callback,errorMsg);
            }

            @Override
            public void onProgress(long total, long current) {
                progress(callback,total,current);
            }
        });
    }














    private void success(ResponseCallback c, Result t){
        callHandler.post(new Runnable() {
            @Override
            public void run() {
                c.onSuccess(t);
            }
        });
    }
    private void failed(ResponseCallback c,String errorMsg){
        callHandler.post(new Runnable() {
            @Override
            public void run() {
                c.onFailure(errorMsg);
            }
        });
    }
    private void progress(ResponseProgressCallBack c,long total, long current){
        callHandler.post(new Runnable() {
            @Override
            public void run() {
                c.onProgress(total,current);
            }
        });
    }
    private Result handleResult(String result) {
        Result res = GsonUtils.toObj(result,Result.class);
        return res;
    }


    private static void addTag(Object tag, Call call) {
        if (call != null && tag != null) {
            OkHttpUtil.putCall(tag, call);
        }
    }
    public static void cancel(Object tag) {
        try {
            OkHttpUtil.cancelCall(tag);
        } catch (Exception e) {
            LogUtil.e(e);
        }

    }



}
