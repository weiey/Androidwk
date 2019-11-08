package com.weiey.app.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.weiey.app.BuildConfig;
import com.weiey.app.Urls;
import com.weiey.app.utils.GsonUtils;
import com.weiey.app.utils.LogUtil;
import okhttp3.*;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import java.io.*;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zw
 * 基于OKHttp,异步请求工具
 * 1.支持JSON （get post) 的https,http请求
 * 2.支持表单 （get post) 的https,http请求
 * 文件上传进度回调
 */
public class OkHttpUtil {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");
    private static final String TAG = OkHttpUtil.class.getSimpleName();
    /**全局处理子线程和M主线程通信*/
    private static Handler okHttpHandler = new Handler(Looper.getMainLooper());


    private static boolean isDebug = BuildConfig.DEBUG;

    static OkHttpClient mHttpClient;
    static OkHttpClient mHttpsClient;


    /**获得网络工具的对象*/
    private static OkHttpClient getHttpUtils() {
        if (null == mHttpClient) {
            mHttpClient = getHttpClient();
        }
        return mHttpClient;
    }

    private static OkHttpClient getHttpsUtils() {
        if (null == mHttpsClient) {
            mHttpsClient = getHttpsClient();
        }

        return mHttpsClient;
    }

    private static OkHttpClient getClient(boolean isHttps) {
        if (isHttps) {
            return getHttpsUtils();
        }
        return getHttpUtils();
    }

    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            //包含header、body数据
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }

    /**
     * // 信任所有证书
     * builder.sslSocketFactory(CertUtil.createSSLSocketFactory(), Platform.get().trustManager(CertUtil.createSSLSocketFactory()));
     * builder.hostnameVerifier(CertUtil.getHostnameVerifier());
     * // 信任指定证书
     * SSLSocketFactory sslSocketFactory = CertUtil.getSocketFactory();
     * if (sslSocketFactory != null) {
     * builder.sslSocketFactory(sslSocketFactory);
     * }
     */
    private static OkHttpClient getHttpsClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            //包含header、body数据
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // 信任所有证书
        builder.sslSocketFactory(CertUtil.createSSLSocketFactory(), Platform.get().trustManager(CertUtil.createSSLSocketFactory()));
        builder.hostnameVerifier(CertUtil.getHostnameVerifier());

        return builder.build();
    }

    /**
     * okHttp get异步请求
     *
     * @param url       接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @return
     */
    public static Call get(String url, Map<String, Object> header, Map<String, Object> paramsMap, final ReqCallBack callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            boolean isHttps = false;
            if (url.startsWith(Urls.HTTPS)) {
                isHttps = true;
            }
            String urlStr = url;
            if (paramsMap != null) {
                int i = 0;
                for (String key : paramsMap.keySet()) {
                    if (i > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key).toString(), "utf-8")));
                    i++;
                }
            }

            String requestUrl = String.format("%s?%s", urlStr, tempParams.toString());

            Request.Builder headerBuilder = addHeaders();
            if (header != null) {
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    headerBuilder.addHeader(entry.getKey(), entry.getValue().toString());
                }
            }

            final Request request = headerBuilder.url(requestUrl).build();
            final Call call = getClient(isHttps).newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failure(e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    response(response, callBack);
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求
     *
     * @param url       接口地址
     * @param header    请求头参数
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @return
     */
    public static Call post(String url, Map<String, Object> header, Map<String, Object> paramsMap, final ReqCallBack callBack) {
        try {

            boolean isHttps = false;
            if (url.startsWith(Urls.HTTPS)) {
                isHttps = true;
            }
            String urlStr = url;
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, GsonUtils.toJson(paramsMap));
            String requestUrl = urlStr;
            Request.Builder headerBuilder = addHeaders();
            if (header != null) {
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    headerBuilder.addHeader(entry.getKey(), entry.getValue().toString());
                }
            }
            final Request request = headerBuilder.url(requestUrl).post(requestBody).build();
            final Call call = getClient(isHttps).newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if(call.isCanceled()){
                        return;
                    }
                    failure(e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(call.isCanceled()){
                        return;
                    }
                    response(response, callBack);
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * okHttp post异步请求表单提交
     *
     * @param url       接口地址
     * @param paramsMap 请求参数
     * @param header    请求头参数
     * @param callBack  请求返回数据回调
     * @return
     */
    public static Call postForm(String url, Map<String, Object> header, Map<String, Object> paramsMap, final ReqCallBack callBack) {
        try {
            boolean isHttps = false;
            if (url.startsWith(Urls.HTTPS)) {
                isHttps = true;
            }
            String urlStr = url;
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key).toString());
            }
            RequestBody formBody = builder.build();

            String requestUrl = urlStr;
            Request.Builder headerBuilder = addHeaders();
            if (header != null) {
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    headerBuilder.addHeader(entry.getKey(), entry.getValue().toString());
                }
            }
            final Request request = headerBuilder.url(requestUrl).post(formBody).build();
            final Call call = getClient(isHttps).newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failure(e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    response(response, callBack);
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param url       接口地址
     * @param paramsMap 参数
     * @param callBack  回调
     * @param
     */
    public static void uploadFile(String url, Map<String, Object> header, Map<String, Object> paramsMap, final ReqCallBack callBack) {
        try {
            //补全请求地址
            String requestUrl = url;
            boolean isHttps = false;
            if (requestUrl.startsWith(Urls.HTTPS)) {
                isHttps = true;
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_OBJECT_STREAM, file));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();

            Request.Builder headerBuilder = addHeaders();
            if (header != null) {
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    headerBuilder.addHeader(entry.getKey(), entry.getValue().toString());
                }
            }

            //创建Request
            final Request request = headerBuilder.url(requestUrl).post(body).build();
            //单独设置参数 比如读取超时时间
            final Call call = getClient(isHttps).newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failure(e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    response(response, callBack);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 上传文件有进度回调
     *
     * @param url       接口地址
     * @param paramsMap 参数
     * @param callBack  回调
     * @param
     */
    public static void uploadFile(String url, Map<String, Object> header, Map<String, Object> paramsMap, final ReqProgressCallBack callBack) {
        try {
            //补全请求地址
            String requestUrl = url;
            boolean isHttps = false;
            if (requestUrl.startsWith(Urls.HTTPS)) {
                isHttps = true;
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file, callBack));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            Request.Builder headerBuilder = addHeaders();
            if (header != null) {
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    headerBuilder.addHeader(entry.getKey(), entry.getValue().toString());
                }
            }
            //创建Request
            final Request request = headerBuilder.url(requestUrl).post(body).build();
            final Call call = getClient(isHttps).newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failure(e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    response(response, callBack);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /***
     * 同步上传
     * @param url
     * @param header
     * @param paramsMap
     * @param callBack
     */
    public static void uploadFileSyn(String url, Map<String, Object> header, Map<String, Object> paramsMap, final ReqProgressCallBack callBack) {
        try {
            //补全请求地址
            String requestUrl = url;
            boolean isHttps = false;
            if (requestUrl.startsWith(Urls.HTTPS)) {
                isHttps = true;
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file, callBack));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            Request.Builder headerBuilder = addHeaders();
            if (header != null) {
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    headerBuilder.addHeader(entry.getKey(), entry.getValue().toString());
                }
            }
            //创建Request
            final Request request = headerBuilder.url(requestUrl).post(body).build();
            final Call call = getClient(isHttps).newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()) {
                if(callBack != null){
                    callBack.onSuccess(response.body().string());
                }
                LogUtil.d(TAG, "response ----->" + response.body().string());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称
     * @param listener     下载监听
     */

    public static void download(final String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) {

        boolean isHttps = false;
        if (url.startsWith(Urls.HTTPS)) {
            isHttps = true;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        //异步请求
        getClient(isHttps).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                //储存下载文件的目录
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);

                try {

                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    //下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    listener.onDownloadFailed(e);
                }finally {

                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }

                }


            }
        });
    }


    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称
     * @param listener     下载监听
     */

    public static String downloadSycn(final String url, final String destFileDir, final String destFileName, final OnDownloadListener listener) throws IOException {

        boolean isHttps = false;
        if (url.startsWith(Urls.HTTPS)) {
            isHttps = true;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = getClient(isHttps).newCall(request).execute();
       return getFile(response,destFileDir,destFileName,listener);

    }

    private static String getFile(Response response, String destFileDir, String destFileName, OnDownloadListener listener) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;

        //储存下载文件的目录
        File dir = new File(destFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, destFileName);

        try {

            is = response.body().byteStream();
            long total = response.body().contentLength();
            fos = new FileOutputStream(file);
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
                //下载中更新进度条
                listener.onDownloading(progress);
            }
            fos.flush();
            //下载完成
            listener.onDownloadSuccess(file);
        } catch (Exception e) {
            listener.onDownloadFailed(e);
        }finally {

            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {

            }

        }
        return file.getAbsolutePath();
    }

    public interface OnDownloadListener{

        /**
         * 下载成功之后的文件
         * @param file
         */
        void onDownloadSuccess(File file);

        /**
         * 下载进度
         * @param progress
         */
        void onDownloading(int progress);

        /**
         * 下载异常信息
         * @param e
         */
        void onDownloadFailed(Exception e);
    }


    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @param
     * @return
     */
    public static RequestBody createProgressRequestBody(final MediaType contentType, final File file, final ReqProgressCallBack callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    long byteCount = 2048;
                    for (long readCount; (readCount = source.read(buf, byteCount)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public interface ReqCallBack {
        /**
         * 响应成功
         * @param result
         */
        void onSuccess(String result);

        /**
         * 响应失败
         * @param errorMsg
         */
        void onFailed(String errorMsg);
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private static Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder();
        builder.addHeader("osType", "Android");

        return builder;
    }

    private static void failure(IOException e, ReqCallBack callBack) {
        failedCallBack("网络连接失败", callBack);
    }

    private static void response(Response response, ReqCallBack callBack) {
        try {
            if (response.isSuccessful()) {
                String string = response.body().string();
                successCallBack(string, callBack);
            } else if(response.code() == 401) {
                String string = response.body().string();
                successCallBack(string, callBack);
            }
            else {
                failedCallBack(response.message(), callBack);
            }
        } catch (IOException e) {
            failedCallBack("服务器错误", callBack);
        }
    }
    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param
     */
    private static void successCallBack(final String result, final ReqCallBack callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     * @param
     */
    private static void failedCallBack(final String errorMsg, final ReqCallBack callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFailed(errorMsg);
                }
            }
        });
    }

    public interface ReqProgressCallBack extends ReqCallBack {
        /**
         * 响应进度更新
         * @param total
         * @param current
         */
        void onProgress(long total, long current);
    }

    /**
     * 统一处理进度信息
     *
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     * @param
     */
    private static void progressCallBack(final long total, final long current, final ReqProgressCallBack callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onProgress(total, current);
                }
            }
        });
    }


    public static Map<Object,List<Call>> callsMap = new ConcurrentHashMap<>();

    /**
     * 保存请求集合
     * @param call
     */
    public static void putCall(Object tag, Call call){
        if(null != tag){
            List<Call> callList = callsMap.get(tag);
            if(null == callList){
                callList = new LinkedList<>();
                callList.add(call);
                callsMap.put(tag,callList);
            }else{
                callList.add(call);
            }
            LogUtil.d("callsMapputCall==>",tag.toString());
        }
    }
    /**
     * 取消请求
     * @param clazz
     */
    public static void cancelCall(Object clazz){
        LogUtil.d("callsMapcancelCall==>",clazz.toString());
        List<Call> callList = callsMap.get(clazz);
        if(null != callList){
            for(Call call : callList){
                if(!call.isCanceled())
                    call.cancel();
            }
            callsMap.remove(clazz);
        }
    }
}
