package com.wty.app.uexpress.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import java.util.List;

/**
 * @author wty
 *         已签收
 */
public class ExpressCheckFragment extends BaseExpressFragment {

    public static final String TAG = "已签收";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        emptytext_up = getString(R.string.express_check_empty);
        emptytext_down = getString(R.string.click_add);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected List<EntityExpressDALEx> queryList() {
        return EntityExpressDALEx.get().queryCheck();
    }

    @Override
    protected List<EntityExpressDALEx> queryServiceList() {
        return queryList();
    }
}
