package com.wty.app.uexpress.http;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import okhttp3.MediaType;
import okhttp3.Response;

/**
 * 使用okhttp取代Apache httpClient
 */

public class HttpUtil {

    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF-8");

    public static String postToService(String url, String args) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip, deflate");
        Response response = HttpUtilCore.postToService(JSON, url, args, headers);
        return execute(response);
    }

    public static String getToService(String url) throws Exception {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip, deflate");
        Response response = HttpUtilCore.getToService(url, headers);
        return execute(response);
    }

    public static String execute(Response response) throws Exception {
        if (null != response) {
            boolean isGzip;
            String header = response.header("Content-Encoding");
            isGzip = header != null && header.equals("gzip");
            String responentity;
            if(isGzip){
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(4096);
                GZIPInputStream gis = new GZIPInputStream(response.body().byteStream());
                int lenth;
                byte[] tmp = new byte[4096];
                while ((lenth = gis.read(tmp)) != -1) {
                    buffer.write(tmp, 0, lenth);
                }
                responentity = new String(buffer.toByteArray(), "utf-8");
            }else{
                responentity = response.body().string();
            }
            if ((response.code() >= 200 && response.code() < 400) || (response.code() == 500)) {
                return responentity;
            } else if (response.code() > 500) {
                // 服务器错误
                throw new WebServiceException();
            } else {
                return responentity;
            }
        }
        return null;
    }
}
