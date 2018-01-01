package com.wty.app.uexpress.ui.fragment;


import android.content.Context;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;

import java.util.List;

/**
 * @author wty
 *         全部
 */
public class ExpressAllFragment extends BaseExpressFragment {

    public static final String TAG = "全部";

    public ExpressAllFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        emptytext_up = getString(R.string.express_record_empty);
        emptytext_down = getString(R.string.click_add);
    }

    @Override
    protected List<EntityExpressDALEx> queryList() {
        return EntityExpressDALEx.get().queryAllWithoutDelete();
    }
}
