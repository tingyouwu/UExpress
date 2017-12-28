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
 *         首页
 */
public class HomeFragment extends BaseFragment {

    public static final String TAG = "HomeFragment";
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
        Map<String,BaseFragment> fragments = new LinkedHashMap<>();
        fragments.put("全部",new ExpressAllFragment());
        fragments.put("未签收",new ExpressUnCheckFragment());
        fragments.put("已签收",new ExpressCheckFragment());
        fragments.put("回收站",new ExpressDeleteFragment());
        for(BaseFragment fragment:fragments.values()){
            fragment.setActivity(activity);
        }
        TabFragmentAdapter adapter = new TabFragmentAdapter(fragments,this.getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabTextColors(getResources().getColor(R.color.bottom_normal), getResources().getColor(R.color.bottom_click));
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
