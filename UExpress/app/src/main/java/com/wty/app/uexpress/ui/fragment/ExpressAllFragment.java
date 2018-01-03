package com.wty.app.uexpress.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.base.FragmentObserver;
import com.wty.app.uexpress.data.entity.BaseResponseEntity;
import com.wty.app.uexpress.data.entity.GetExpressInfoEntity;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.ui.activity.ExpressInfoActivity;
import com.wty.app.uexpress.widget.sweet.OnDismissCallbackListener;
import com.wty.app.uexpress.widget.sweet.SweetAlertDialog;

import java.util.List;

import static com.wty.app.uexpress.base.UExpressConstant.EXPRESS_STATUS_SUCESS;

/**
 * @author wty
 *         全部
 */
public class ExpressAllFragment extends BaseExpressFragment {

    public static final String TAG = "全部";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        emptytext_up = getString(R.string.express_record_empty);
        emptytext_down = getString(R.string.click_add);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected List<EntityExpressDALEx> queryList() {
        return EntityExpressDALEx.get().queryAllWithoutDelete();
    }

    @Override
    protected List<EntityExpressDALEx> queryServiceList() {
        List<EntityExpressDALEx> unchecklist = EntityExpressDALEx.get().queryUnCheck();
        if(unchecklist.size()!=0){
            GetExpressInfoEntity entity = new GetExpressInfoEntity();
            for(EntityExpressDALEx express:unchecklist){
                String json = entity.requestJson(express.getCompanycode(),express.getExpressnum());
                entity.handleResponse(json, new BaseResponseEntity.OnResponseListener<GetExpressInfoEntity>() {
                    @Override
                    public void onSuccess(String json, GetExpressInfoEntity response) {
                        if (EXPRESS_STATUS_SUCESS.equals(response.status)) {
                            EntityExpressDALEx.updateExpressInfo(json, response);
                        }
                    }

                    @Override
                    public void onTimeout() {

                    }
                });
            }
        }
        return queryList();
    }
}
