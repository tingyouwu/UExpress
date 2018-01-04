package com.wty.app.uexpress.ui.fragment;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.adapter.ExpressListAdapter;
import com.wty.app.uexpress.util.CoreTimeUtils;
import com.wty.app.uexpress.widget.common.ListViewEmptyLayout;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author wty
 *  快递列表基类fragment
 *  刷新数据分两种
 *  1.初次进来获取本地数据列表 并调用服务获取最近更新的快递信息
 *  2.下拉刷新调用服务获取最近更新的快递信息 并且更新本地数据表
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
                refreshServiceList();
            }

            @Override
            public void onLoadMore() {

            }
        });
        List<EntityExpressDALEx> data = new ArrayList<>();
        adapter = new ExpressListAdapter(getActivity(),data);
        listview.setAdapter(adapter);
        adapter.setOnItemLongClickLitener(new BaseRecyclerViewAdapter.OnItemLongClickLitener<EntityExpressDALEx>() {
            @Override
            public void onItemLongClick(View view, final EntityExpressDALEx item) {
                if(item.getRecstatus()==1){
                    //启用状态
                    final EasyPopup popup = new EasyPopup(activity)
                            .setContentView(R.layout.layout_express_menu)
                            .setFocusAndOutsideEnable(true)
                            .createPopup();
                    popup.getView(R.id.tv_top).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            item.setCreatetime(CoreTimeUtils.getNowTime());
                            item.saveOrUpdate();
                            refreshLocalList();
                        }
                    });
                    popup.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            item.setRecstatus(0);
                            item.saveOrUpdate();
                            refreshLocalList();
                        }
                    });
                    popup.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.CENTER, 0, 0);
                }else {
                    //删除状态  置顶 恢复 彻底删除
                    final EasyPopup popup = new EasyPopup(activity)
                            .setContentView(R.layout.layout_express_menu2)
                            .setFocusAndOutsideEnable(true)
                            .createPopup();
                    popup.getView(R.id.tv_top).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            item.setCreatetime(CoreTimeUtils.getNowTime());
                            item.saveOrUpdate();
                            refreshLocalList();
                        }
                    });
                    popup.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            item.deleteById(item.getExpressid());
                            refreshLocalList();
                        }
                    });
                    popup.getView(R.id.tv_restore).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                            item.setRecstatus(1);
                            item.saveOrUpdate();
                            refreshLocalList();
                        }
                    });
                    popup.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.CENTER, 0, 0);
                }
            }
        });

        ListViewEmptyLayout emptylayout = new ListViewEmptyLayout(getActivity());
        emptylayout.setEmptyText(emptytext_up,emptytext_down);
        listview.addHeaderEmptyView(emptylayout);
    }

    @Override
    public void doWorkOnResume() {
        refreshLocalList();
    }

    @Override
    public void onDestroy() {
        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
            refreshtask.cancel(true);
        }
        super.onDestroy();
    }

    /**
     * 刷新本地列表
     **/
    private void refreshLocalList(){
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

    @Override
    public void handleOnShow() {
        refreshLocalList();
    }

    /**
     * 获取最新服务列表
     **/
    private void refreshServiceList(){
        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
            return;
        }
        refreshtask = new SimpleTask() {
            @Override
            protected Object doInBackground(String... params) {
                return queryServiceList();
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
    abstract protected List<EntityExpressDALEx> queryServiceList();
}
