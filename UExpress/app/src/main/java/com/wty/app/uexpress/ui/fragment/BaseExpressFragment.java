package com.wty.app.uexpress.ui.fragment;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.adapter.ExpressListAdapter;
import com.wty.app.uexpress.util.CoreTimeUtils;
import com.wty.app.uexpress.widget.common.ListViewEmptyLayout;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author wty
 *         全部
 */
public abstract class BaseExpressFragment extends BaseFragment {

    @BindView(R.id.listview)
    protected XRecyclerView listview;

    protected ExpressListAdapter adapter;
    protected SimpleTask refreshtask;

    protected String emptytext_up;
    protected String emptytext_down;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_express;
    }

    @Override
    protected void onInitView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(true);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(activity,2,false));
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }

            @Override
            public void onLoadMore() {

            }
        });
        List<EntityExpressDALEx> data = new ArrayList<>();
        adapter = new ExpressListAdapter(getActivity(),data);
        listview.setAdapter(adapter);

        ListViewEmptyLayout emptylayout = new ListViewEmptyLayout(getActivity());
        emptylayout.setEmptyText(getString(R.string.express_record_empty),getString(R.string.click_add));
        listview.addHeaderEmptyView(emptylayout);
        refreshList();
    }

    @Override
    public void doWorkOnResume() {
    }

    @Override
    public void handleOnShow() {
        refreshList();
    }

    @Override
    public void onDestroy() {
        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
            refreshtask.cancel(true);
        }
        super.onDestroy();
    }

    /**
     * 刷新列表
     **/
    private void refreshList(){
        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
            return;
        }
        refreshtask = new SimpleTask() {
            @Override
            protected Object doInBackground(String... params) {
                return queryList();
            }

            @Override
            protected void onPostExecute(Object o) {
                listview.refreshComplete(CoreTimeUtils.getNowTime());
                List<EntityExpressDALEx> result = (List<EntityExpressDALEx>) o;
                adapter.refreshList(result);
            }
        };
        refreshtask.startTask();
    }

    abstract protected List<EntityExpressDALEx> queryList();
}
