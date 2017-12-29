package com.wty.app.uexpress.http;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 使用okhttp取代Apache httpClient
 */

public class HttpUtilCore {

    public static Response postToService(MediaType mediaType, String url, String args, HashMap<String, String> headers) throws Exception {

        OkHttpClient hClient;
        if (url.startsWith("https")) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(createSSLSocketFactory());
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            hClient = builder.readTimeout(30,TimeUnit.SECONDS).
                    connectTimeout(30, TimeUnit.SECONDS).build();
        }else{
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            hClient = builder.readTimeout(30,TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS).build();
        }
        RequestBody body = RequestBody.create(mediaType, args);
        Request.Builder requestBuilder = new Request.Builder();
        initHeaders(requestBuilder, headers);

        requestBuilder.url(url).post(body);
        Request request = requestBuilder.build();

        return hClient.newCall(request).execute();
    }

    public static Response getToService(String url, HashMap<String, String> headers) throws Exception {
        OkHttpClient hClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        initHeaders(requestBuilder, headers);
        requestBuilder.url(url);
        Request request = requestBuilder.build();

        return hClient.newCall(request).execute();
    }

    private static void initHeaders(Request.Builder builder, HashMap<String, String> headers) {
        Iterator<Map.Entry<String,String>> iter = headers.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            builder.addHeader(key, val);
        }
    }

    public enum Method {
        Get, Post
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }
}