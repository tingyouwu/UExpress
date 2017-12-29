package com.wty.app.uexpress.http;

/**
 * @author wty
 * 请求失败提示
 */
public class WebServiceException extends Exception{

    @Override
    public String getMessage() {
        return "连接服务器失败，请稍后再试！";
    }
}
