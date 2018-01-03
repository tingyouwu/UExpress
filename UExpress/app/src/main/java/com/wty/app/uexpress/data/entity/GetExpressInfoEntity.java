package com.wty.app.uexpress.data.entity;

import com.wty.app.uexpress.base.UExpressConstant;
import com.wty.app.uexpress.data.model.ExpressInfoItemModel;
import com.wty.app.uexpress.http.HttpUtilCore;

import java.util.List;

/**
 * 获取快递物流信息
 */
public class GetExpressInfoEntity extends BaseResponseEntity {

    //消息内容 返回ok表示查询到该快递单 或者返回具体错误信息
    public List<ExpressInfoItemModel> data;
    //查询结果状态：201物流单暂无结果，200查询成功
    public String status;
    /**
     * 快递单当前的状态 ：　
     0：在途，即货物处于运输过程中；
     1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
     2：疑难，货物寄送过程出了问题；
     3：签收，收件人已签收；
     4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
     5：派件，即快递正在进行同城派件；
     6：退回，货物正处于退回发件人的途中；
     **/
    public String state;
    //物流公司编号
    public String com;
    //物流单号
    public String nu;
    public String remark;

    @Override
    protected String createArgs(Object... params) {
        //?type=[快递公司名]&postid=快递单号
        return "?type="+params[0]+"&"+"postid="+params[1];
    }

    @Override
    protected String makeUrl() {
        return UExpressConstant.API_GET_EXPRESS_INFO;
    }

    @Override
    public String requestJson(Object... params) {
        method = HttpUtilCore.Method.Get;
        return super.requestJson(params);
    }
}
