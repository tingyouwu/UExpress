package com.wty.app.uexpress.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.adapter.ExpressCompanyListAdapter;
import com.wty.app.uexpress.util.CoreCommonUtil;
import com.wty.app.uexpress.widget.common.SearchView;
import com.wty.app.uexpress.widget.common.SideBar;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author wty
 *         快递公司列表
 */
public class CompanyFragment extends BaseFragment {

    public static final String TAG = "CompanyFragment";
    @BindView(R.id.listview)
    XRecyclerView listview;
    @BindView(R.id.tv_letter)
    TextView overlay;
    @BindView(R.id.filter_letters)
    SideBar filter_letters;
    @BindView(R.id.searchview)
    SearchView searchview;

    ExpressCompanyListAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_company;
    }

    @Override
    protected void onInitView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(false);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(getActivity(),2,false));
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
        adapter = new ExpressCompanyListAdapter(getActivity(),data);
        listview.setAdapter(adapter);
        adapter.refreshList(EntityCompanyDALEx.get().queryAllCompany(""));
        searchview.setHint("快速搜索快递公司");
        searchview.setOnSearchListener(searchListener);
        searchview.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    CoreCommonUtil.keyboardControl(getActivity(), false, v);
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
    }

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.phone_send_express))
                .getLeftButton()
                .hide();
        activity.getDefaultNavigation().getRightButton().hide();
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
