package com.wty.app.uexpress.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.ui.adapter.ExpressCompanySelectListAdapter;
import com.wty.app.uexpress.widget.common.ExpressInfoEmptyLayout;
import com.wty.app.uexpress.widget.common.ExpressInfoHeaderLayout;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author wty
 *  选择快递公司页面
 **/
public class ExpressInfoActivity extends BaseActivity {

    public static final String TAG_EXPRESSID = "expressid";

    @BindView(R.id.listview)
    XRecyclerView listview;

    ExpressCompanySelectListAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static void startActivity(Activity activity, String expressid){
        Intent intent = new Intent(activity,ExpressInfoActivity.class);
        intent.putExtra(TAG_EXPRESSID,expressid);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.translate_right_in, R.anim.translate_fix);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_expressinfo;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.translate_fix, R.anim.translate_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView(){
        getDefaultNavigation().setTitle(getString(R.string.search_result));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(true);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(this,2,true));
        List<EntityCompanyDALEx> data = new ArrayList<>();
        adapter = new ExpressCompanySelectListAdapter(this,data);
        final ExpressInfoHeaderLayout headerLayout = new ExpressInfoHeaderLayout(this);
        listview.addHeaderView(headerLayout);
        final ExpressInfoEmptyLayout emptyLayout = new ExpressInfoEmptyLayout(this);
        listview.addHeaderEmptyView(emptyLayout);
        listview.setAdapter(adapter);

        emptyLayout.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) emptyLayout.getLayoutParams();
                layoutParams.height = ((RecyclerView)(emptyLayout.getParent())).getHeight()-headerLayout.getHeight();
                layoutParams.width = ((RecyclerView)(emptyLayout.getParent())).getWidth();
                emptyLayout.setLayoutParams(layoutParams);
            }
        });
    }
}
