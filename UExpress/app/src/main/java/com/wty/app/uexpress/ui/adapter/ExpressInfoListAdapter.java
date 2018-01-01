package com.wty.app.uexpress.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.ExpressInfoItemModel;
import com.wty.app.uexpress.util.AppImageLoader;
import com.wty.app.uexpress.util.CoreTimeUtils;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @author wty
 *         快递物流信息适配器
 */
public class ExpressInfoListAdapter extends BaseRecyclerViewAdapter<ExpressInfoItemModel> {

    public ExpressInfoListAdapter(Context context, List<ExpressInfoItemModel> list) {
        super(context, R.layout.item_expressinfo_list, list);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder holder, final ExpressInfoItemModel item, int position) {
        TextView tvDate = holder.getView(R.id.tv_date);
        TextView tvTime = holder.getView(R.id.tv_time);
        ImageView imgState = holder.getView(R.id.img_state);
        TextView tvDetail = holder.getView(R.id.tv_detail);

        tvDate.setText(CoreTimeUtils.dateToYYYYMMdd(item.time));
        tvTime.setText(CoreTimeUtils.dateToHHmm(item.time));
        tvDetail.setText(item.context);

        if(position==0){
            //第一个位置需要显示不同颜色
            AppImageLoader.displayImage(mContext,R.mipmap.img_checked,imgState);
            tvDate.setTextColor(mContext.getResources().getColor(R.color.bottom_click));
            tvTime.setTextColor(mContext.getResources().getColor(R.color.bottom_click));
            tvDetail.setTextColor(mContext.getResources().getColor(R.color.bottom_click));
        }else {
            AppImageLoader.displayImage(mContext,R.mipmap.image_exp_status_wait,imgState);
            tvDate.setTextColor(mContext.getResources().getColor(R.color.bottom_normal));
            tvTime.setTextColor(mContext.getResources().getColor(R.color.bottom_normal));
            tvDetail.setTextColor(mContext.getResources().getColor(R.color.bottom_normal));
        }
    }
}
