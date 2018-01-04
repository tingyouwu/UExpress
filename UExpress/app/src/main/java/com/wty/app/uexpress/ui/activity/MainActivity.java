package com.wty.app.uexpress.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.base.BroadcastConstants;
import com.wty.app.uexpress.base.UExpressConstant;
import com.wty.app.uexpress.data.entity.BaseResponseEntity;
import com.wty.app.uexpress.data.entity.GetCompanyListEntity;
import com.wty.app.uexpress.db.ORMManager;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.task.TaskManager;
import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.fragment.CompanyFragment;
import com.wty.app.uexpress.ui.fragment.HomeFragment;
import com.wty.app.uexpress.ui.fragment.SearchFragment;
import com.wty.app.uexpress.ui.fragment.SettingFragment;
import com.wty.app.uexpress.util.CoreCommonUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * @author wty
 *  主页面
 **/
public class MainActivity extends BaseActivity {

    @BindView(R.id.home_tab_layout)
    LinearLayout homeTabLayout;

    Map<String,BaseFragment> fragments = new HashMap<>(4);
    Map<String,HomeTab> homeTabMap = new LinkedHashMap<>(4);
    private HomeTab lastTab;
    private SimpleTask datatask;
    BroadcastReceiver receiver;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        if(datatask!=null && datatask.getStatus() == AsyncTask.Status.RUNNING){
            datatask.cancel(true);
        }
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        fragments.clear();
        TaskManager.getInstance().clear();
        ORMManager.getInstance().closeDB();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            // 准备退出
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 取消退出
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);

        }
    }

    @Override
    protected void initView(){
        initExpressList();
        homeTabLayout.removeAllViews();
        fragments.clear();
        homeTabMap.clear();
        homeTabMap.put(HomeFragment.TAG,addHomeFragment(true));
        homeTabMap.put(SearchFragment.TAG,addSearchFragment(false));
        homeTabMap.put(CompanyFragment.TAG,addCompanyFragment(false));
        homeTabMap.put(SettingFragment.TAG,addSetingFragment(false));
        initHomeTab();
        if(receiver == null){
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String fragmentTag = intent.getStringExtra(UExpressConstant.TAG_FRAGMENT);
                    HomeTab homeTab = homeTabMap.get(fragmentTag);
                    if(lastTab == homeTab){
                        return;
                    }
                    handleChangeTab(homeTab);
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CHANGE_HOME_TAB);
            registerReceiver(receiver, filter);
        }
    }

    /**
     * 功能描述：构建底部tab
     */
    public void initHomeTab() {
        if(homeTabMap.size() == 0){
            throw new RuntimeException("请初始化 HomeTab 列表");
        }
        for (final HomeTab tab : homeTabMap.values()) {
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
                    handleChangeTab(tab);
                }
            });
        }
    }

    /**
     * 切换tab时处理事件
     **/
    private void handleChangeTab(HomeTab tab){
        if(tab == null || lastTab == tab){
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

    /**
     * 首次安装app需要初次加载 快递公司数据到数据表中
     **/
    private void initExpressList(){
        if(datatask != null && datatask.getStatus() == AsyncTask.Status.RUNNING){
            return;
        }

        datatask = new SimpleTask(){

            @Override
            protected Object doInBackground(String... params) {
                if(EntityCompanyDALEx.get().countSize()==0){
                    //先处理本地的文件  导入到数据库
                    return CoreCommonUtil.loadJsonFile(MainActivity.this, "express_company.json");
                }
                return "";
            }

            @Override
            protected void onPostExecute(Object o) {
                String json = (String) o;
                if(!TextUtils.isEmpty(json)){
                    new GetCompanyListEntity().handleResponse(json, new BaseResponseEntity.OnResponseListener<GetCompanyListEntity>() {
                        @Override
                        public void onSuccess(String json, GetCompanyListEntity response) {
                            EntityCompanyDALEx.get().saveOrUpdateQuick(response.data);
                        }

                        @Override
                        public void onTimeout() {
                        }
                    });
                }
            }
        };
        datatask.startTask();
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
     * 添加 CompanyFragment
     * @param isSelect 是否默认显示
     **/
    private HomeTab addCompanyFragment(boolean isSelect){
        BaseFragment fragment = new CompanyFragment();
        fragment.setActivity(this);
        fragments.put(CompanyFragment.TAG,fragment);
        HomeTab tab_crm = new HomeTab(R.mipmap.bottom_notice_normal, R.mipmap.bottom_notice_click,
                "寄快递", CompanyFragment.TAG,isSelect);
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
            ((BaseFragment)fragment).handleOnShow();
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
