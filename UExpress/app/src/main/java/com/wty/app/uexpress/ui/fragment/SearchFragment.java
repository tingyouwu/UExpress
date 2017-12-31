package com.wty.app.uexpress.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.base.BroadcastConstants;
import com.wty.app.uexpress.data.entity.BaseResponseEntity;
import com.wty.app.uexpress.data.entity.GetCompanyByExpressNumEntity;
import com.wty.app.uexpress.data.model.CompanyModel;
import com.wty.app.uexpress.data.entity.GetExpressInfoEntity;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.task.SimpleDialogTask;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.OnActivityResultListener;
import com.wty.app.uexpress.ui.activity.ExpressCompanySelectActivity;
import com.wty.app.uexpress.ui.activity.ExpressInfoActivity;
import com.wty.app.uexpress.util.CoreRegexUtil;
import com.wty.app.uexpress.widget.common.ClearEditText;
import com.wty.app.uexpress.widget.sweet.OnDismissCallbackListener;
import com.wty.app.uexpress.widget.sweet.SweetAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.xudaojie.qrcodelib.CaptureActivity;

import static com.wty.app.uexpress.base.UExpressConstant.EXPRESS_STATUS_SUCESS;
import static com.wty.app.uexpress.base.UExpressConstant.TAG_FRAGMENT;

/**
 * @author wty
 *         查快递
 */
public class SearchFragment extends BaseFragment {

    public static final String TAG = "SearchFragment";
    @BindView(R.id.tv_label_num)
    TextView tvLabelNum;
    @BindView(R.id.et_expressnum)
    ClearEditText etExpressnum;
    @BindView(R.id.img_scan)
    ImageView imgScan;
    @BindView(R.id.tv_label_company)
    TextView tvLabelCompany;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private SimpleTask task;
    private SimpleDialogTask expressinfotask;
    GetCompanyByExpressNumEntity entity;
    String companycode;

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void onDestroy() {
        if(task != null && task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        if(expressinfotask != null && expressinfotask.getStatus() == AsyncTask.Status.RUNNING){
            expressinfotask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void handleOnShow() {
        etExpressnum.setText("");
        tvLabelNum.setVisibility(View.GONE);
        tvCompany.setText("");
        companycode = "";
        handleCompanyLayout("");
        handleConfirmButton();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_search;
    }

    @Override
    protected void onInitView() {
        etExpressnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tvLabelNum.setVisibility(View.VISIBLE);
                } else {
                    tvLabelNum.setVisibility(View.GONE);
                    handleCompanyLayout("");
                    handleConfirmButton();
                }
                if (s.length() >= 8) {
                    //开始请求网络 查找公司
                    countDowntimer.cancel();
                    countDowntimer.start();
                }
            }
        });
        handleConfirmButton();
    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.search_express))
                .getLeftButton()
                .hide();
    }

    @OnClick({R.id.img_scan, R.id.img_next,R.id.img_delete,R.id.tv_company,R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_scan:
                //扫描二维码
                Intent intent_scan = new Intent(activity, CaptureActivity.class);
                activity.startActivityForListener(intent_scan, new OnActivityResultListener() {
                    @Override
                    public void onResult(Intent data) {
                        String result = data.getStringExtra("result");
                        //正则匹配数字
                        if (CoreRegexUtil.matchNum(result)) {
                            etExpressnum.setText(result);
                            handleConfirmButton();
                        }
                    }
                });
                break;
            case R.id.tv_company:
            case R.id.img_next:
                //选择快递公司
                Intent intent = new Intent(activity, ExpressCompanySelectActivity.class);
                activity.startActivityForListener(intent, new OnActivityResultListener() {
                    @Override
                    public void onResult(Intent data) {
                        CompanyModel companyModel = (CompanyModel) data.getSerializableExtra(ExpressCompanySelectActivity.TAG_RESULT);
                        companycode = companyModel.getCode();
                        handleCompanyLayout(companyModel.getName());
                        handleConfirmButton();
                    }
                });
                break;
            case R.id.img_delete:
                handleCompanyLayout("");
                handleConfirmButton();
                break;
            case R.id.btn_confirm:
                getExpressInfo();
                break;
            default:
                break;
        }
    }

    CountDownTimer countDowntimer = new CountDownTimer(300,100) {
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
                    postid = etExpressnum.getText().toString();
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
                                handleCompanyLayout(company.getName());
                                handleConfirmButton();
                            }else {
                                companycode = "";
                                handleCompanyLayout("");
                                handleConfirmButton();
                            }
                        }else {
                            companycode = "";
                            handleCompanyLayout("");
                            handleConfirmButton();
                        }
                    }
                }
            };
            task.startTask();
        }
    };

    /**
     * 处理一下选择公司布局的显示效果
     **/
    private void handleCompanyLayout(String content){
        if(TextUtils.isEmpty(content)){
            tvCompany.setText("");
            tvLabelCompany.setVisibility(View.GONE);
            imgDelete.setVisibility(View.GONE);
        }else {
            tvCompany.setText(content);
            tvLabelCompany.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 处理 查询按钮
     **/
    private void handleConfirmButton(){
        if(TextUtils.isEmpty(etExpressnum.getText().toString())||TextUtils.isEmpty(tvCompany.getText().toString())){
            btnConfirm.setEnabled(false);
        }else {
            btnConfirm.setEnabled(true);
        }
    }

    /**
     * 获取物流信息
     **/
    private void getExpressInfo(){
        if(expressinfotask != null && expressinfotask.getStatus()== AsyncTask.Status.RUNNING){
            return;
        }
        expressinfotask = new SimpleDialogTask(activity) {

            String postid = etExpressnum.getText().toString();
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
                            EntityExpressDALEx.saveExpressInfo(json,response);
                            ExpressInfoActivity.startActivity(activity,postid);
                        }else {
                            //订单不存在
                            activity.onToastSelect(new OnDismissCallbackListener("查询无结果，是否保存单号?", SweetAlertDialog.WARNING_TYPE){
                                @Override
                                public void onCallback() {
                                    //保存单号
                                    response.com = companycode;
                                    response.nu = postid;
                                    EntityExpressDALEx.saveExpressInfo(json,response);

                                    //切回到首页
                                    Intent intentForHome = new Intent(BroadcastConstants.CHANGE_HOME_TAB);
                                    intentForHome.putExtra(TAG_FRAGMENT,HomeFragment.TAG);
                                    activity.sendBroadcast(intentForHome);
                                    //同时切回到首页的未签收
                                    Intent intentForUncheck = new Intent(BroadcastConstants.CHANGE_FRAGMENT_TAB);
                                    intentForUncheck.putExtra(TAG_FRAGMENT,ExpressUnCheckFragment.TAG);
                                    activity.sendBroadcast(intentForUncheck);
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
