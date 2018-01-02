package com.wty.app.uexpress.ui.adapter;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.ui.activity.ExpressInfoActivity;
import com.wty.app.uexpress.util.AppImageLoader;
import com.wty.app.uexpress.util.CoreImageURLUtils;
import com.wty.app.uexpress.util.CoreTimeUtils;
import com.wty.app.uexpress.widget.common.UnreadView;
import com.wty.app.uexpress.widget.roundedimageview.RoundedImageView;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewHolder;
import java.util.List;

import static com.wty.app.uexpress.base.UExpressConstant.EXPRESS_STATUS_SUCESS;

/**
 * @author wty
 *         快递列表适配器
 */
public class ExpressListAdapter extends BaseRecyclerViewAdapter<EntityExpressDALEx> {

    public ExpressListAdapter(Context context, List<EntityExpressDALEx> list) {
        super(context, R.layout.item_express_list, list);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder holder, final EntityExpressDALEx item, int position) {
        RoundedImageView itemIcon = holder.getView(R.id.item_icon);
        TextView itemName = holder.getView(R.id.item_name);
        TextView itemRemark = holder.getView(R.id.item_remark);
        TextView itemStep = holder.getView(R.id.item_step);
        TextView itemCheckTime = holder.getView(R.id.item_check_time);
        UnreadView unreadView = holder.getView(R.id.item_notice);
        LinearLayout llayout = holder.getView(R.id.llayout_time);

        if(item.getUnreadsize()!=0){
            unreadView.setVisibility(View.GONE);
        }else {
            unreadView.setVisibility(View.VISIBLE);
            unreadView.setUnread(item.getUnreadsize());
        }

        if(!TextUtils.isEmpty(item.getCompanyicon())){
            AppImageLoader.displayImage(mContext, CoreImageURLUtils.ImageScheme.HEADIMG.wrap(item.getCompanyicon()),itemIcon);
        }else {
            AppImageLoader.displayImage(mContext, R.mipmap.ic_launcher,itemIcon);
        }

        switch (item.getStatus()){
            case EXPRESS_STATUS_SUCESS:
                if(TextUtils.isEmpty(item.getRemark())){
                    itemRemark.setVisibility(View.GONE);
                }else {
                    itemRemark.setText(item.getRemark());
                }
                itemStep.setVisibility(View.VISIBLE);
                llayout.setVisibility(View.VISIBLE);
                itemCheckTime.setText(CoreTimeUtils.dateToMMdd(item.getSteptime()));
                itemStep.setText(item.getStepcontext());
                break;
            default:
                itemStep.setVisibility(View.GONE);
                llayout.setVisibility(View.GONE);
                itemRemark.setVisibility(View.GONE);
                break;
        }

        itemName.setText(item.getCompanyname()+" "+item.getExpressnum());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressInfoActivity.startActivity((Activity) mContext,item.getCompanycode(),item.getExpressnum());
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickLitener != null){
                    onItemLongClickLitener.onItemLongClick(v,item);
                }
                return true;
            }
        });
    }

}
