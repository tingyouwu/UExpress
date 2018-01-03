package com.wty.app.uexpress.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.base.BroadcastConstants;
import com.wty.app.uexpress.base.FragmentObserver;
import com.wty.app.uexpress.base.UExpressConstant;
import com.wty.app.uexpress.data.entity.BaseResponseEntity;
import com.wty.app.uexpress.data.entity.GetCompanyByExpressNumEntity;
import com.wty.app.uexpress.data.entity.GetExpressInfoEntity;
import com.wty.app.uexpress.data.model.CompanyModel;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.task.SimpleDialogTask;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.OnActivityResultListener;
import com.wty.app.uexpress.ui.activity.ExpressCompanySelectActivity;
import com.wty.app.uexpress.ui.activity.ExpressInfoActivity;
import com.wty.app.uexpress.ui.activity.ExpressSearchListActivity;
import com.wty.app.uexpress.ui.adapter.TabFragmentAdapter;
import com.wty.app.uexpress.util.CoreCommonUtil;
import com.wty.app.uexpress.util.CoreRegexUtil;
import com.wty.app.uexpress.util.CoreScreenUtil;
import com.wty.app.uexpress.widget.common.ClearEditText;
import com.wty.app.uexpress.widget.sweet.OnDismissCallbackListener;
import com.wty.app.uexpress.widget.sweet.SweetAlertDialog;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.xudaojie.qrcodelib.CaptureActivity;

import static com.wty.app.uexpress.base.UExpressConstant.EXPRESS_STATUS_SUCESS;
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

    BroadcastReceiver receiver;
    Map<String, BaseFragment> fragments = new LinkedHashMap<>();
    private String companycode;
    private SimpleTask task;
    private SimpleDialogTask expressinfotask;
    GetCompanyByExpressNumEntity entity;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String fragmentTag = intent.getStringExtra(UExpressConstant.TAG_FRAGMENT);
                    int index = 0;
                    for (String tag : fragments.keySet()) {
                        if (tag.equals(fragmentTag)) {
                            if(viewpager.getCurrentItem()==index){
                                //当前页面就是要跳转的页面
                                fragments.get(tag).handleOnShow();
                            }else {
                                viewpager.setCurrentItem(index);
                                tablayout.getTabAt(index).select();
                            }
                            break;
                        }
                        index++;
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadcastConstants.CHANGE_FRAGMENT_TAB);
            activity.registerReceiver(receiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            activity.unregisterReceiver(receiver);
            receiver = null;
        }
        fragments.clear();
        if(task != null && task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        if(expressinfotask != null && expressinfotask.getStatus() == AsyncTask.Status.RUNNING){
            expressinfotask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onInitView() {
        fragments.put(ExpressAllFragment.TAG, new ExpressAllFragment());
        fragments.put(ExpressUnCheckFragment.TAG, new ExpressUnCheckFragment());
        fragments.put(ExpressCheckFragment.TAG, new ExpressCheckFragment());
        fragments.put(ExpressDeleteFragment.TAG, new ExpressDeleteFragment());
        for (BaseFragment fragment : fragments.values()) {
            fragment.setActivity(activity);
        }
        TabFragmentAdapter adapter = new TabFragmentAdapter(fragments, this.getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabTextColors(getResources().getColor(R.color.bottom_normal), getResources().getColor(R.color.bottom_click));
    }

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.app_name))
                .getLeftButton()
                .hide();
        activity.getDefaultNavigation().setRightButton(R.mipmap.actionbar_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EasyPopup menupop = new EasyPopup(activity)
                        .setContentView(R.layout.layout_addexpress_menu)
                        .setAnimationStyle(R.style.QQPopAnim)
                        .setFocusAndOutsideEnable(true)
                        .createPopup();
                menupop.getView(R.id.scan_express).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menupop.dismiss();
                        Intent intent_scan = new Intent(activity, CaptureActivity.class);
                        activity.startActivityForListener(intent_scan, new OnActivityResultListener() {
                            @Override
                            public void onResult(Intent data) {
                                String result = data.getStringExtra("result");
                                //正则匹配数字
                                if (CoreRegexUtil.matchNum(result)) {
                                    showAddDialog(result);
                                }
                            }
                        });
                    }
                });
                menupop.getView(R.id.add_express).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddDialog("");
                        menupop.dismiss();

                    }
                });
                menupop.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.LEFT, CoreScreenUtil.dip2px(activity,25), -30);
            }
        });
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        ExpressSearchListActivity.startActivity(activity);
    }

    /**
     * 显示添加布局
     **/
    private void showAddDialog(final String content){
        // 以下代码实现动态加载xml布局文件
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  contentview = inflater.inflate(R.layout.layout_express_add,null);

        final ClearEditText et_expressnum = (ClearEditText) contentview.findViewById(R.id.et_expressnum);
        ImageView img_scan = (ImageView) contentview.findViewById(R.id.img_scan);
        final TextView tv_company = (TextView) contentview.findViewById(R.id.tv_company);
        final ImageView img_delete = (ImageView) contentview.findViewById(R.id.img_delete);
        ImageView img_next = (ImageView) contentview.findViewById(R.id.img_next);
        final ClearEditText et_remark = (ClearEditText) contentview.findViewById(R.id.et_remark);

        final CountDownTimer countDowntimer = new CountDownTimer(300,100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if(task != null && task.getStatus()== AsyncTask.Status.RUNNING){
                    task.cancel(true);
                }
                task = new SimpleTask() {

                    String postid;

                    @Override
                    protected void onPreExecute() {
                        postid = et_expressnum.getText().toString();
                        entity = new GetCompanyByExpressNumEntity();
                    }

                    @Override
                    protected Object doInBackground(String... params) {
                        return entity.request(postid);
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        String result = (String) o;
                        if("200".equals(result)){
                            if(entity.auto != null && entity.auto.size()!=0){
                                EntityCompanyDALEx company = EntityCompanyDALEx.get().findById(entity.auto.get(0).comCode);
                                if(company != null){
                                    companycode = company.getCode();
                                    tv_company.setText(company.getName());
                                    img_delete.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                            companycode = "";
                            tv_company.setText("");
                            img_delete.setVisibility(View.GONE);
                        }
                    }
                };
                task.startTask();
            }
        };

        et_expressnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 5) {
                    //开始请求网络 查找公司
                    countDowntimer.cancel();
                    countDowntimer.start();
                }
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_company.setText("");
                companycode = "";
                img_delete.setVisibility(View.GONE);
            }
        });

        tv_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择快递公司
                Intent intent = new Intent(activity, ExpressCompanySelectActivity.class);
                activity.startActivityForListener(intent, new OnActivityResultListener() {
                    @Override
                    public void onResult(Intent data) {
                        CompanyModel companyModel = (CompanyModel) data.getSerializableExtra(ExpressCompanySelectActivity.TAG_RESULT);
                        companycode = companyModel.getCode();
                        tv_company.setText(companyModel.getName());
                        img_delete.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //扫描二维码
                Intent intent_scan = new Intent(activity, CaptureActivity.class);
                activity.startActivityForListener(intent_scan, new OnActivityResultListener() {
                    @Override
                    public void onResult(Intent data) {
                        String result = data.getStringExtra("result");
                        //正则匹配数字
                        if (CoreRegexUtil.matchNum(result)) {
                            et_expressnum.setText(result);
                        }
                    }
                });
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择快递公司
                Intent intent = new Intent(activity, ExpressCompanySelectActivity.class);
                activity.startActivityForListener(intent, new OnActivityResultListener() {
                    @Override
                    public void onResult(Intent data) {
                        CompanyModel companyModel = (CompanyModel) data.getSerializableExtra(ExpressCompanySelectActivity.TAG_RESULT);
                        companycode = companyModel.getCode();
                        tv_company.setText(companyModel.getName());
                        img_delete.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(contentview);
        ////禁止点击 dialog 外部取消弹窗
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(TextUtils.isEmpty(et_expressnum.getText().toString())){
                    Toast.makeText(activity,et_expressnum.getHint(),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(tv_company.getText().toString())){
                    Toast.makeText(activity,tv_company.getHint(),Toast.LENGTH_SHORT).show();
                }
                et_remark.post(new Runnable() {
                    @Override
                    public void run() {
                        CoreCommonUtil.keyboardControl(activity,false,et_remark);
                    }
                });
                getExpressInfo(et_expressnum.getText().toString(),et_remark.getText().toString());

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
        if(!TextUtils.isEmpty(content)){
            et_expressnum.setText(content);
        }
        et_expressnum.post(new Runnable() {
            @Override
            public void run() {
                et_expressnum.setSelection(content.length());
                et_expressnum.requestFocus();
                CoreCommonUtil.keyboardControl(activity,true,et_expressnum);
            }
        });
    }

    /**
     * 获取物流信息
     **/
    private void getExpressInfo(final String postid, final String remark){
        if(expressinfotask != null && expressinfotask.getStatus()== AsyncTask.Status.RUNNING){
            return;
        }
        expressinfotask = new SimpleDialogTask(activity) {

            GetExpressInfoEntity entity = new GetExpressInfoEntity();

            @Override
            public Object onAsync() {
                return entity.requestJson(companycode,postid);
            }

            @Override
            public void onResult(Object obj) {
                String json = (String) obj;
                entity.handleResponse(json, new BaseResponseEntity.OnResponseListener<GetExpressInfoEntity>() {
                    @Override
                    public void onSuccess(final String json, final GetExpressInfoEntity response) {
                        if(EXPRESS_STATUS_SUCESS.equals(response.status)){
                            //保存单号
                            response.remark = remark;
                            if(EntityExpressDALEx.get().isExist(response.com+response.nu)){
                                EntityExpressDALEx.updateExpressInfo(json,response);
                            }else {
                                EntityExpressDALEx.saveExpressInfo(json,response);
                            }
                            ExpressInfoActivity.startActivity(activity,companycode,postid);
                            handleOnShow();
                        }else {
                            setDimissAskCallback(new OnDismissCallbackListener("查询无结果，是否保存单号?", SweetAlertDialog.WARNING_TYPE){
                                @Override
                                public void onCallback() {
                                    //保存单号
                                    response.com = companycode;
                                    response.nu = postid;
                                    response.remark = remark;
                                    if(EntityExpressDALEx.get().isExist(response.com+response.nu)){
                                        EntityExpressDALEx.updateExpressInfo(json,response);
                                    }else {
                                        EntityExpressDALEx.saveExpressInfo(json,response);
                                    }

                                    EntityExpressDALEx expressDALEx = EntityExpressDALEx.get().findById(response.com+response.nu);
                                    if(expressDALEx.getRecstatus()==0){
                                        FragmentObserver.goToHomeDeletePage(activity);
                                    }else if("3".equals(expressDALEx.getState())){
                                        FragmentObserver.goToHomeCheckPage(activity);
                                    }else {
                                        FragmentObserver.goToHomeUnCheckPage(activity);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onTimeout() {
                        activity.onToastErrorMsg("查询超时，请检查网络!");
                    }
                });
            }
        };
        expressinfotask.startTask(activity.getString(R.string.searching_wait));
    }

}
