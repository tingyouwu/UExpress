package com.wty.app.uexpress.base;

/**
 * @author wty
 * 应用常量
 **/
public class UExpressConstant{

    public static final String TAG_FRAGMENT = "fragment_tag";

    /** 物流单暂无结果 */
    public static final String EXPRESS_STATUS_NOEXIST = "201";
    /** 物流单查询成功 */
    public static final String EXPRESS_STATUS_SUCESS = "200";

    /** 快递100 API */
    public static final String BASE_URL = "https://www.kuaidi100.com";

    /** 根据单号判断快递公司 */
    public static final String API_GET_FINDCOMPANY_BYCOMNUM = BASE_URL + "/autonumber/autoComNum";
    /** 获取快递物流信息 */
    public static final String API_GET_EXPRESS_INFO = BASE_URL + "/query";
}
