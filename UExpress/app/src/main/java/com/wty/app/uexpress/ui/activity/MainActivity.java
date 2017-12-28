package com.wty.app.uexpress.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.fragment.ExpressFragment;
import com.wty.app.uexpress.ui.fragment.HomeFragment;
import com.wty.app.uexpress.ui.fragment.SearchFragment;
import com.wty.app.uexpress.ui.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author wty
 *  主页面
 **/
public class MainActivity extends BaseActivity {

    @BindView(R.id.home_tab_layout)
    LinearLayout homeTabLayout;

    Map<String,BaseFragment> fragments = new HashMap<>(4);
    private HomeTab lastTab;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragments.clear();
    }

    @Override
    protected void initView(){
    }

    @Override
    protected void onStart() {
        super.onStart();
        homeTabLayout.removeAllViews();
        fragments.clear();
        List<HomeTab> tabs = new ArrayList<>();
        tabs.add(addHomeFragment(true));
        tabs.add(addSearchFragment(false));
        tabs.add(addExpressFragment(false));
        tabs.add(addSetingFragment(false));
        initHomeTab(tabs);
    }

    /**
     * 功能描述：构建底部tab
     */
    public void initHomeTab(List<HomeTab> tabs) {
        if(tabs == null || tabs.size() == 0){
            throw new RuntimeException("请初始化 HomeTab 列表");
        }
        boolean hasSelect =false;
        for (final HomeTab tab : tabs) {
            View tabView = LayoutInflater.from(this).inflate(R.layout.item_home_tab, null);
            TextView tabTitle = (TextView) tabView.findViewById(R.id.home_tab_title);
            ImageView tabIcon = (ImageView)tabView.findViewById(R.id.home_tab_icon);
            tab.setView(tabView);
            tabIcon.setImageResource(tab.iconResource);
            tabTitle.setText(tab.title);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            tabView.setTag(tab.tag);
            homeTabLayout.addView(tabView, params);

            if (tab.isSelect) {
                hasSelect =true;
                tab.onSelect();
                if(lastTab != null){
                    handleFragment(tab.tag,lastTab.tag);
                    lastTab.onUnSelect();
                }else {
                    //第一个选中的tab
                    handleFragment(tab.tag,"");
                }
                lastTab = tab;
                if(fragments.get(tab.tag).isAdded()){
                    fragments.get(tab.tag).handleActionBar();
                }
            }

            tabView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(lastTab == tab){
                        //相同不需要处理
                        return;
                    }
                    tab.onSelect();
                    handleFragment(tab.tag,lastTab.tag);
                    lastTab.onUnSelect();
                    lastTab=tab;
                    if(fragments.get(tab.tag).isAdded()){
                        fragments.get(tab.tag).handleActionBar();
                    }
                }
            });
        }

        if(!hasSelect){
            //如果一个都没有选中的话  默认选中第一个
            lastTab = tabs.get(0);
            lastTab.onSelect();
            handleFragment(lastTab.tag,"");
            fragments.get(lastTab.tag).handleActionBar();
        }
    }

    /**
     * 添加 CRMFragment
     * @param isSelect 是否默认显示
     **/
    private HomeTab addHomeFragment(boolean isSelect){
        BaseFragment fragment = new HomeFragment();
        fragment.setActivity(this);
        fragments.put(HomeFragment.TAG,fragment);
        HomeTab tab_crm = new HomeTab(R.mipmap.bottom_home_normal, R.mipmap.bottom_home_click,
                "首页",HomeFragment.TAG,isSelect);
        return tab_crm;
    }

    /**
     * 添加 CRMFragment
     * @param isSelect 是否默认显示
     **/
    private HomeTab addSearchFragment(boolean isSelect){
        BaseFragment fragment = new SearchFragment();
        fragment.setActivity(this);
        fragments.put(SearchFragment.TAG,fragment);
        HomeTab tab_crm = new HomeTab(R.mipmap.bottom_bill_normal, R.mipmap.bottom_bill_click,
                "查快递",SearchFragment.TAG,isSelect);
        return tab_crm;
    }

    /**
     * 添加 ExpressFragment
     * @param isSelect 是否默认显示
     **/
    private HomeTab addExpressFragment(boolean isSelect){
        BaseFragment fragment = new ExpressFragment();
        fragment.setActivity(this);
        fragments.put(ExpressFragment.TAG,fragment);
        HomeTab tab_crm = new HomeTab(R.mipmap.bottom_notice_normal, R.mipmap.bottom_notice_click,
                "寄快递",ExpressFragment.TAG,isSelect);
        return tab_crm;
    }

    /**
     * 添加 SetingFragment
     * @param isSelect 是否默认显示
     **/
    private HomeTab addSetingFragment(boolean isSelect){
        BaseFragment fragment = new SettingFragment();
        fragment.setActivity(this);
        fragments.put(SettingFragment.TAG,fragment);
        HomeTab tab_crm = new HomeTab(R.mipmap.bottom_me_normal, R.mipmap.bottom_me_click,
                "我的",SettingFragment.TAG,isSelect);
        return tab_crm;
    }

    /**
     * 处理新旧fragment
     * @param newTag 需要显示的fragment
     * @param lastTag 需要隐藏的fragment
     **/
    private void handleFragment(String newTag,String lastTag){
        if(TextUtils.isEmpty(newTag)){
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(!TextUtils.isEmpty(lastTag)){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(lastTag);
            if(fragment!= null){
                transaction.hide(fragment);
            }
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(newTag);
        if (fragment == null) {
            fragment = fragments.get(newTag);
            transaction.add(R.id.main_container, fragment, newTag);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    /**
     * @author wty
     * 对页签对象的封装
     */
    static class HomeTab {
        int iconResource;
        int selectResource;
        String title;
        ImageView tabImg;
        TextView tabTitle;
        String tag;
        boolean isSelect;

        public HomeTab(int iconResource, int selectResource,
                       String title, String tag,boolean isSelect) {
            this.selectResource = selectResource;
            this.iconResource = iconResource;
            this.title = title;
            this.tag = tag;
            this.isSelect = isSelect;
        }

        /**
         * 功能描述：绑定对象也对应的View
         */
        public void setView(View tabView){
            tabImg =  (ImageView)tabView.findViewById(R.id.home_tab_icon);
            tabTitle  = (TextView)tabView.findViewById(R.id.home_tab_title);
        }

        /**
         * 功能描述：页签选中状态事件
         */
        public void onSelect(){
            tabTitle.setTextColor(tabTitle.getContext().getResources().getColor(R.color.bottom_click));
            tabImg.setImageResource(selectResource);
        }

        /**
         * 功能描述：页签没选中状态事件
         */
        public void onUnSelect(){
            tabTitle.setTextColor(tabTitle.getContext().getResources().getColor(R.color.bottom_normal));
            tabImg.setImageResource(iconResource);
        }
    }
}
