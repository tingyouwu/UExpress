package com.wty.app.uexpress.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.base.BroadcastConstants;
import com.wty.app.uexpress.base.UExpressConstant;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.activity.ExpressSearchListActivity;
import com.wty.app.uexpress.ui.adapter.TabFragmentAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wty
 *         首页
 */
public class HomeFragment extends BaseFragment {

    public static final String TAG = "HomeFragment";
    @BindView(R.id.fragment_home_tablayout)
    TabLayout tablayout;
    @BindView(R.id.fragment_home_viewpager)
    ViewPager viewpager;

    BroadcastReceiver receiver;
    Map<String, BaseFragment> fragments = new LinkedHashMap<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String fragmentTag = intent.getStringExtra(UExpressConstant.TAG_FRAGMENT);
                    int index = 0;
                    for (String tag : fragments.keySet()) {
                        if (tag.equals(fragmentTag)) {
                            viewpager.setCurrentItem(index);
                            tablayout.getTabAt(index).select();
                            break;
                        }
                        index++;
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CHANGE_FRAGMENT_TAB);
            activity.registerReceiver(receiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
            receiver = null;
        }
        fragments.clear();
    }

    @Override
    protected void onInitView() {
        fragments.put(ExpressAllFragment.TAG, new ExpressAllFragment());
        fragments.put(ExpressUnCheckFragment.TAG, new ExpressUnCheckFragment());
        fragments.put(ExpressCheckFragment.TAG, new ExpressCheckFragment());
        fragments.put(ExpressDeleteFragment.TAG, new ExpressDeleteFragment());
        for (BaseFragment fragment : fragments.values()) {
            fragment.setActivity(activity);
        }
        TabFragmentAdapter adapter = new TabFragmentAdapter(fragments, this.getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabTextColors(getResources().getColor(R.color.bottom_normal), getResources().getColor(R.color.bottom_click));
    }

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.app_name))
                .getLeftButton()
                .hide();
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        ExpressSearchListActivity.startActivity(activity);
    }
}
