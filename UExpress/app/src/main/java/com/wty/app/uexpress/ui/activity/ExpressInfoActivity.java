package com.wty.app.uexpress.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.entity.BaseResponseEntity;
import com.wty.app.uexpress.data.entity.GetExpressInfoEntity;
import com.wty.app.uexpress.data.model.ExpressInfoItemModel;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.ui.adapter.ExpressInfoListAdapter;
import com.wty.app.uexpress.util.CoreCommonUtil;
import com.wty.app.uexpress.util.CoreTimeUtils;
import com.wty.app.uexpress.widget.common.ClearEditText;
import com.wty.app.uexpress.widget.common.ExpressInfoEmptyLayout;
import com.wty.app.uexpress.widget.common.ExpressInfoHeaderLayout;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static com.wty.app.uexpress.base.UExpressConstant.EXPRESS_STATUS_SUCESS;

/**
 * @author wty
 *  快递详情页面
 **/
public class ExpressInfoActivity extends BaseActivity {

    public static final String TAG_COMPANYCODE = "companycode";
    public static final String TAG_POSTID = "postid";

    @BindView(R.id.listview)
    XRecyclerView listview;

    ExpressInfoHeaderLayout headerLayout;
    ExpressInfoEmptyLayout emptyLayout;
    ExpressInfoListAdapter adapter;
    private SimpleTask task;
    private String companycode;
    private String postid;
    EntityExpressDALEx express;

    /**
     * @param companycode 快递公司编码
     * @param postid 快递单号
     **/
    public static void startActivity(Activity activity, String companycode,String postid){
        Intent intent = new Intent(activity,ExpressInfoActivity.class);
        intent.putExtra(TAG_COMPANYCODE,companycode);
        intent.putExtra(TAG_POSTID,postid);
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
        if(task != null && task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void initView(){
        handleIntent();
        express = EntityExpressDALEx.get().findById(companycode+postid);
        getDefaultNavigation().setTitle(getString(R.string.express_detail))
                .setRightButton(R.mipmap.img_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRemarkDialog(express.getRemark());
                    }
                });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(true);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(this,2,true));
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadExpressDetail();
            }

            @Override
            public void onLoadMore() {

            }
        });
        List<ExpressInfoItemModel> data = new ArrayList<>();
        adapter = new ExpressInfoListAdapter(this,data);
        headerLayout = new ExpressInfoHeaderLayout(this);
        listview.addHeaderView(headerLayout);
        emptyLayout = new ExpressInfoEmptyLayout(this);
        listview.addHeaderEmptyView(emptyLayout);
        listview.setAdapter(adapter);

        refreshHeaderLayout(express);
        refreshEmptyLayout(express);
        refreshList(express);
        loadExpressDetail();
    }

    private void handleIntent(){
        companycode = getIntent().getStringExtra(TAG_COMPANYCODE);
        postid = getIntent().getStringExtra(TAG_POSTID);
    }

    /**
     * 刷新header
     **/
    private void refreshHeaderLayout(EntityExpressDALEx express){
        headerLayout.setIcon(express.getCompanyicon());
        if(!TextUtils.isEmpty(express.getRemark())){
            headerLayout.setName(express.getRemark());
            headerLayout.setRemark(express.getCompanyname()+" "+express.getExpressnum());
        }else {
            headerLayout.setName(express.getCompanyname()+" "+express.getExpressnum());
            headerLayout.setRemark(express.getRemark());
        }
    }

    /**
     * 刷新空布局
     **/
    private void refreshEmptyLayout(EntityExpressDALEx express){
        if(express.getStepsize()==0){
            EntityCompanyDALEx companyDALEx = EntityCompanyDALEx.get().findById(companycode);
            emptyLayout.setEmptyText(companyDALEx.getContact());
        }
    }

    /**
     * 刷新快递跟踪信息列表
     **/
    private void refreshList(EntityExpressDALEx express){
        if(express.getStepsize()==0){
            adapter.clearList();
        }else {
            new GetExpressInfoEntity().handleResponse(express.getLastjson(), new BaseResponseEntity.OnResponseListener<GetExpressInfoEntity>() {
                @Override
                public void onSuccess(String json, GetExpressInfoEntity response) {
                    adapter.refreshList(response.data);
                }

                @Override
                public void onTimeout() {

                }
            });
        }
    }

    /**
     * 从服务获取快递信息
     **/
    private void loadExpressDetail(){
        if(task !=null && task.getStatus()== AsyncTask.Status.RUNNING){
            return;
        }
        task = new SimpleTask() {

            GetExpressInfoEntity entity = new GetExpressInfoEntity();

            @Override
            protected Object doInBackground(String... params) {
                return entity.requestJson(companycode,postid);
            }

            @Override
            protected void onPostExecute(Object obj) {
                listview.refreshComplete(CoreTimeUtils.getNowTime());
                String json = (String) obj;
                entity.handleResponse(json, new BaseResponseEntity.OnResponseListener<GetExpressInfoEntity>() {
                    @Override
                    public void onSuccess(final String json, final GetExpressInfoEntity response) {
                        if(EXPRESS_STATUS_SUCESS.equals(response.status)){
                            if(response.data !=null && response.data.size()!= 0 && response.data.size()>express.getStepsize()){
                                Toast.makeText(ExpressInfoActivity.this,String.format(Locale.US,"查询结束，%d条更新!",response.data.size()-express.getStepsize()),Toast.LENGTH_LONG).show();
                                express.setLastjson(json);
                                express.setStatus(response.status);
                                express.setState(response.state);
                                express.setUnreadsize(0);
                                express.setLaststeptime(response.data.get(0).time);
                                express.setLaststepcontext(response.data.get(0).context);
                                express.setFirststeptime(response.data.get(response.data.size()-1).time);
                                express.setFirststepcontext(response.data.get(response.data.size()-1).context);
                                express.setStepsize(response.data.size());
                                express.saveOrUpdate();
                                refreshList(express);
                            }else {
                                Toast.makeText(ExpressInfoActivity.this,String.format(Locale.US,"查询结束，%d条更新!",0),Toast.LENGTH_SHORT).show();
                                //更新未读条数
                                express.setUnreadsize(0);
                                express.saveOrUpdate();
                            }
                        }
                    }

                    @Override
                    public void onTimeout() {
                        Toast.makeText(ExpressInfoActivity.this,"查询超时，请检查网络!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        task.startTask();
    }

    /**
     * 显示备注页面
     **/
    private void showRemarkDialog(String remark){
        // 以下代码实现动态加载xml布局文件
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  contentview = inflater.inflate(R.layout.layout_remark_edit,null);
        final ClearEditText et_remark = (ClearEditText) contentview.findViewById(R.id.et_remark);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(TextUtils.isEmpty(remark)){
            builder.setTitle(getResources().getString(R.string.add_remark));
        }else {
            et_remark.setText(remark);
            et_remark.setSelection(remark.length());
            builder.setTitle(getResources().getString(R.string.edit_remark));
        }

        builder.setView(contentview);
        ////禁止点击 dialog 外部取消弹窗
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                express.setRemark(et_remark.getText().toString());
                express.saveOrUpdate();
                refreshHeaderLayout(express);
                et_remark.post(new Runnable() {
                    @Override
                    public void run() {
                        CoreCommonUtil.keyboardControl(ExpressInfoActivity.this,false,et_remark);
                    }
                });
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#176ce3"));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#F27474"));
        et_remark.post(new Runnable() {
            @Override
            public void run() {
                et_remark.requestFocus();
                CoreCommonUtil.keyboardControl(ExpressInfoActivity.this,true,et_remark);
            }
        });
    }
}
