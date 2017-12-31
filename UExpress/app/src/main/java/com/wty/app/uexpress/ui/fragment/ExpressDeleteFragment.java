package com.wty.app.uexpress.ui.fragment;

import android.content.Context;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import java.util.List;

/**
 * @author wty
 *         回收站
 */
public class ExpressDeleteFragment extends BaseExpressFragment {

    public static final String TAG = "回收站";

    public ExpressDeleteFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        emptytext_up = getString(R.string.recyclerbin_empty);
        emptytext_down = "";
    }

    @Override
    protected List<EntityExpressDALEx> queryList() {
        return EntityExpressDALEx.get().queryDelete();
    }
}
