package com.wty.app.uexpress.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.CompanyModel;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.ui.adapter.ExpressCompanyListAdapter;
import com.wty.app.uexpress.ui.adapter.ExpressCompanySelectListAdapter;
import com.wty.app.uexpress.util.CoreCommonUtil;
import com.wty.app.uexpress.widget.common.SearchView;
import com.wty.app.uexpress.widget.common.SideBar;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author wty
 *  选择快递公司页面
 **/
public class ExpressCompanySelectActivity extends BaseActivity {

    public static final String TAG_RESULT = "result";

    @BindView(R.id.listview)
    XRecyclerView listview;
    @BindView(R.id.tv_letter)
    TextView overlay;
    @BindView(R.id.filter_letters)
    SideBar filter_letters;
    @BindView(R.id.searchview)
    SearchView searchview;

    ExpressCompanySelectListAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView(){
        getDefaultNavigation().setTitle(getString(R.string.select_express_company));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(false);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(this,2,false));
        filter_letters.setTextView(overlay);
        filter_letters.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (s != null && s.trim().length() > 0) {
                    overlay.setText(s);
                    if (adapter.getAlphaIndexer().get(s) != null) {
                        int position = adapter.getAlphaIndexer().get(s);
                        layoutManager.scrollToPositionWithOffset(position+1, 0);
                    }
                }
            }
        });
        List<EntityCompanyDALEx> data = new ArrayList<>();
        adapter = new ExpressCompanySelectListAdapter(this,data);
        listview.setAdapter(adapter);
        adapter.refreshList(EntityCompanyDALEx.get().queryAllCompany(""));
        searchview.setHint("快速搜索快递公司");
        searchview.setOnSearchListener(searchListener);
        searchview.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    CoreCommonUtil.keyboardControl(ExpressCompanySelectActivity.this, false, v);
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
        filter_letters.setLettersList(adapter.getAlphaList());
        adapter.setOnRecyclerItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerItemClickListener<EntityCompanyDALEx>() {
            @Override
            public void OnItemClick(EntityCompanyDALEx item, int position) {
                Intent intent = new Intent();
                CompanyModel model = new CompanyModel();
                model.setName(item.getName());
                model.setCode(item.getCode());
                intent.putExtra(TAG_RESULT, model);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    SearchView.OnSearchListener searchListener = new SearchView.OnSearchListener() {

        @Override
        public void onSearchEmpty() {
            adapter.refreshList(EntityCompanyDALEx.get().queryAllCompany(""));
            filter_letters.setLettersList(adapter.getAlphaList());
        }

        @Override
        public void onSearchChange(String content) {
            adapter.refreshList(EntityCompanyDALEx.get().queryAllCompany(content));
            filter_letters.setLettersList(adapter.getAlphaList());
        }
    };
}
