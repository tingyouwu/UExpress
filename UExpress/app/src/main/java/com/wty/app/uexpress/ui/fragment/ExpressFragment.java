package com.wty.app.uexpress.ui.fragment;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.ui.BaseFragment;

/**
 * @author wty
 * 快递公司列表
 */
public class ExpressFragment extends BaseFragment {

    public static final String TAG = "ExpressFragment";

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView() {

    }

    @Override
    protected void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.app_name))
                .getLeftButton()
                .hide();
    }
}
