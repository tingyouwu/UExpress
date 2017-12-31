package com.wty.app.uexpress.data.entity;

import com.wty.app.uexpress.base.UExpressConstant;
import com.wty.app.uexpress.http.HttpUtilCore;

import java.util.List;

/**
 * 根据单号判断快递公司
 */
public class GetCompanyByExpressNumEntity extends BaseResponseEntity {

    public List<CompanyModel> auto;

    public static class CompanyModel{
        //快递公司代码
        public String comCode;
    }

    @Override
    protected String createArgs(Object... params) {
        //?text=[快递单号]
        return "?text="+params[0];
    }

    @Override
    protected String makeUrl() {
        return UExpressConstant.API_GET_FINDCOMPANY_BYCOMNUM;
    }

    public String request(String postid){
        method = HttpUtilCore.Method.Get;
        String json =  requestJson(postid);
        return handleResponse(json, new OnResponseListener<GetCompanyByExpressNumEntity>() {
            @Override
            public void onSuccess(String json, GetCompanyByExpressNumEntity response) {
                auto = response.auto;
            }

            @Override
            public void onTimeout() {

            }
        });
    }
}
