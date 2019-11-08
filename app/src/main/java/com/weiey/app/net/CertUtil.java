package com.weiey.app.net;

import android.annotation.SuppressLint;
import android.content.Context;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * @Date
 * @Description
 * https证书工具
1、支持 默认信任所有的证书
2、支持 字符串流的证书
3、支持 文件形式的证书
 */
public class CertUtil {
    private final static String CER_0 = "";


    private final static String CER_1 =" ";

    private final static String CER_2 ="-----BEGIN CERTIFICATE-----\n" +
            "MIIEizCCA3OgAwIBAgIQBUb+GCP34ZQdo5/OFMRhczANBgkqhkiG9w0BAQsFADBh\n" +
            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
            "d3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBD\n" +
            "QTAeFw0xNzExMDYxMjIzNDVaFw0yNzExMDYxMjIzNDVaMF4xCzAJBgNVBAYTAlVT\n" +
            "MRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5j\n" +
            "b20xHTAbBgNVBAMTFEdlb1RydXN0IFJTQSBDQSAyMDE4MIIBIjANBgkqhkiG9w0B\n" +
            "AQEFAAOCAQ8AMIIBCgKCAQEAv4rRY03hGOqHXegWPI9/tr6HFzekDPgxP59FVEAh\n" +
            "150Hm8oDI0q9m+2FAmM/n4W57Cjv8oYi2/hNVEHFtEJ/zzMXAQ6CkFLTxzSkwaEB\n" +
            "2jKgQK0fWeQz/KDDlqxobNPomXOMJhB3y7c/OTLo0lko7geG4gk7hfiqafapa59Y\n" +
            "rXLIW4dmrgjgdPstU0Nigz2PhUwRl9we/FAwuIMIMl5cXMThdSBK66XWdS3cLX18\n" +
            "4ND+fHWhTkAChJrZDVouoKzzNYoq6tZaWmyOLKv23v14RyZ5eqoi6qnmcRID0/i6\n" +
            "U9J5nL1krPYbY7tNjzgC+PBXXcWqJVoMXcUw/iBTGWzpwwIDAQABo4IBQDCCATww\n" +
            "HQYDVR0OBBYEFJBY/7CcdahRVHex7fKjQxY4nmzFMB8GA1UdIwQYMBaAFAPeUDVW\n" +
            "0Uy7ZvCj4hsbw5eyPdFVMA4GA1UdDwEB/wQEAwIBhjAdBgNVHSUEFjAUBggrBgEF\n" +
            "BQcDAQYIKwYBBQUHAwIwEgYDVR0TAQH/BAgwBgEB/wIBADA0BggrBgEFBQcBAQQo\n" +
            "MCYwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRpZ2ljZXJ0LmNvbTBCBgNVHR8E\n" +
            "OzA5MDegNaAzhjFodHRwOi8vY3JsMy5kaWdpY2VydC5jb20vRGlnaUNlcnRHbG9i\n" +
            "YWxSb290Q0EuY3JsMD0GA1UdIAQ2MDQwMgYEVR0gADAqMCgGCCsGAQUFBwIBFhxo\n" +
            "dHRwczovL3d3dy5kaWdpY2VydC5jb20vQ1BTMA0GCSqGSIb3DQEBCwUAA4IBAQAw\n" +
            "8YdVPYQI/C5earp80s3VLOO+AtpdiXft9OlWwJLwKlUtRfccKj8QW/Pp4b7h6QAl\n" +
            "ufejwQMb455OjpIbCZVS+awY/R8pAYsXCnM09GcSVe4ivMswyoCZP/vPEn/LPRhH\n" +
            "hdgUPk8MlD979RGoUWz7qGAwqJChi28uRds3thx+vRZZIbEyZ62No0tJPzsSGSz8\n" +
            "nQ//jP8BIwrzBAUH5WcBAbmvgWfrKcuv+PyGPqRcc4T55TlzrBnzAzZ3oClo9fTv\n" +
            "O9PuiHMKrC6V6mgi0s2sa/gbXlPCD9Z24XUMxJElwIVTDuKB0Q4YMMlnpN/QChJ4\n" +
            "B0AFsQ+DU0NCO+f78Xf7\n" +
            "-----END CERTIFICATE-----";


    // 证书数据
    private static List<byte[]> CERTIFICATES_DATA = new ArrayList<>();
    /**
     * 添加https证书
     * @param inputStream
     */
    private synchronized static void addCertificate(InputStream inputStream) {
//        Log.i(TAG,"#addCertificate inputStream = " + inputStream);
        if (inputStream != null) {
            try {
                int ava = 0;// 数据当次可读长度
                int len = 0;// 数据总长度
                ArrayList<byte[]> data = new ArrayList<>();
                while ((ava = inputStream.available()) > 0) {
                    byte[] buffer = new byte[ava];
                    inputStream.read(buffer);
                    data.add(buffer);
                    len += ava;
                }
                byte[] buff = new byte[len];
                int dstPos = 0;
                for (byte[] bytes:data) {
                    int length = bytes.length;
                    System.arraycopy(bytes, 0, buff, dstPos, length);
                    dstPos += length;
                }
                CERTIFICATES_DATA.add(buff);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    // 添加https证书 字符串形式
    public  static  void loadCertificate(){
        try {
            String[]  certs=new String[]{CER_0,CER_1,CER_2};
            if (certs != null) {
                for (String cert:certs) {
                    addCertificate(new ByteArrayInputStream(cert.getBytes())); // 这里将证书读取出来，，放在配置中byte[]里
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
    // 添加https证书 文件形式
    public static void loadCertificate(Context context){
        try {
            String[]  certFiles=context.getAssets().list("certs");
            if (certFiles != null) {
                for (String cert:certFiles) {
                    InputStream is = context.getAssets().open("certs/" + cert);
                    addCertificate(is); // 这里将证书读取出来，，放在配置中byte[]里
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
    /**
     * https证书
     * @return
     */
    private static List<byte[]> getCertificatesData() {
        return CERTIFICATES_DATA;
    }

    /**
     * 加载证书
     * @return
     */
    private static List<InputStream> getCertificates(){
        List<InputStream> certificates = new ArrayList<>();
//        List<String> certs = CertUtil.getCertificates();
        List<byte[]> certs_data = getCertificatesData();
        // 将字节数组转为数组输入流
        if (certs_data != null && !certs_data.isEmpty()) {
            for (byte[] bytes:certs_data) {
                certificates.add(new ByteArrayInputStream(bytes));
            }
        }
        return certificates;
    }

    public static SSLSocketFactory getSocketFactory() {
        return getSocketFactory(getCertificates());
    }
    /**
     * 添加证书
     *
     * @param certificates
     */
    private static SSLSocketFactory getSocketFactory(List<InputStream> certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            try {
                for (int i = 0, size = certificates.size(); i < size; ) {
                    InputStream certificate = certificates.get(i);
                    String certificateAlias = Integer.toString(i++);
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    if (certificate != null)
                        certificate.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null,trustManagerFactory.getTrustManagers(),new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 默认信任所有的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    public static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static HostnameVerifier getHostnameVerifier(){
        return new TrustAllHostnameVerifier();
    }
}
