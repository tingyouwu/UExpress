package com.wty.app.uexpress.util;

/**
 * @author wty
 * 正则表达式工具
 */
public class CoreRegexUtil {

    /**
     * 正则匹配是否为纯数字
     */
    public static boolean matchNum(String str){
        if(str.matches("^\\d+$")){
            return true;
        }
        return  false;
    }
}
