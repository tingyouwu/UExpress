package com.wty.app.uexpress.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.ui.adapter.ExpressSearchListAdapter;
import com.wty.app.uexpress.util.CoreCommonUtil;
import com.wty.app.uexpress.widget.common.SearchView;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author wty
 *  快递搜索页面
 **/
public class ExpressSearchListActivity extends BaseActivity {

    @BindView(R.id.listview)
    XRecyclerView listview;
    @BindView(R.id.searchview)
    SearchView searchview;

    ExpressSearchListAdapter adapter;

    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity,ExpressSearchListActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.translate_right_in, R.anim.translate_fix);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_express_search;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.translate_fix, R.anim.translate_right_out);
    }

    @Override
    protected void initView(){
        getDefaultNavigation().setTitle(getString(R.string.search_express));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(false);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(this,2,false));

        List<EntityExpressDALEx> data = new ArrayList<>();
        adapter = new ExpressSearchListAdapter(this,data);
        listview.setAdapter(adapter);
        adapter.refreshList(EntityExpressDALEx.get().queryAll(""));
        searchview.setHint("输入单号/备注");
        searchview.setOnSearchListener(searchListener);
        searchview.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    CoreCommonUtil.keyboardControl(ExpressSearchListActivity.this, false, v);
                }
            }
        });

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchview.getEditText().clearFocus();
                return false;
            }
        });
    }

    SearchView.OnSearchListener searchListener = new SearchView.OnSearchListener() {

        @Override
        public void onSearchEmpty() {
            adapter.refreshList(EntityExpressDALEx.get().queryAll(""));
        }

        @Override
        public void onSearchChange(String content) {
            adapter.refreshList(EntityExpressDALEx.get().queryAll(content));
        }
    };
}
