package com.wty.app.uexpress.ui.fragment;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.ui.BaseFragment;

/**
 * @author wty
 * 查快递
 */
public class SearchFragment extends BaseFragment {

    public static final String TAG = "SearchFragment";

    @Override
    protected void doWorkOnResume() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitView() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.app_name))
                .getLeftButton()
                .hide();
    }

}
