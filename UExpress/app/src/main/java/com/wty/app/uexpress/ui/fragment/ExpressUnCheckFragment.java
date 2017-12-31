package com.wty.app.uexpress.ui.fragment;


import android.content.Context;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import java.util.List;

/**
 * @author wty
 *         未签收
 */
public class ExpressUnCheckFragment extends BaseExpressFragment {

    public static final String TAG = "未签收";

    public ExpressUnCheckFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        emptytext_up = getString(R.string.express_uncheck_empty);
        emptytext_down = getString(R.string.click_add);
    }

    @Override
    protected List<EntityExpressDALEx> queryList() {
        return EntityExpressDALEx.get().queryUnCheck();
    }
}
