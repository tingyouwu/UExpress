package com.wty.app.uexpress.ui.fragment;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.ui.BaseFragment;

/**
 * @author wty
 * 首页
 */
public class SettingFragment extends BaseFragment {

    public static final String TAG = "SettingFragment";

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView() {

    }

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.mine))
                .getLeftButton()
                .hide();
    }

}
