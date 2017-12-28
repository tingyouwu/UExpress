package com.wty.app.uexpress.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.adapter.TabFragmentAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * @author wty
 *         回收站
 */
public class ExpressDeleteFragment extends BaseFragment {

    @BindView(R.id.fragment_home_tablayout)
    TabLayout tablayout;
    @BindView(R.id.fragment_home_viewpager)
    ViewPager viewpager;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onInitView() {

    }

    @Override
    protected void doWorkOnResume() {

    }
}
