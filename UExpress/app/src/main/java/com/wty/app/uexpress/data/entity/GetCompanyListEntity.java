package com.wty.app.uexpress.data.entity;

import com.orhanobut.logger.Logger;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;

import java.util.List;

/**
 * 获取快递公司列表
 */
public class GetCompanyListEntity extends BaseResponseEntity {

    public List<EntityCompanyDALEx> data;

    @Override
    protected String createArgs(Object... params) {
        return "";
    }

    @Override
    protected String makeUrl() {
        return "";
    }

    public String request() {

        String json = requestJson();
        //内部消化
        return handleResponse(json, new OnResponseListener<GetCompanyListEntity>() {

            @Override
            public void onSuccess(String json, GetCompanyListEntity response) {
                EntityCompanyDALEx.get().saveOrUpdateQuick(response.data);
            }

            @Override
            public void onTimeout() {
                Logger.d("请求超时");
            }
        });
    }
}
