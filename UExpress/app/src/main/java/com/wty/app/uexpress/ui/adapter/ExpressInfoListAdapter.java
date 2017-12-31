package com.wty.app.uexpress.ui.adapter;


import android.content.Context;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityExpressDALEx;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewHolder;

import java.util.List;

/**
 * @author wty
 *         快递物流信息适配器
 */
public class ExpressInfoListAdapter extends BaseRecyclerViewAdapter<EntityExpressDALEx> {

    public ExpressInfoListAdapter(Context context, List<EntityExpressDALEx> list) {
        super(context, R.layout.item_expressinfo_list, list);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder holder, final EntityExpressDALEx item, int position) {
        TextView tvDate = holder.getView(R.id.tv_date);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView imgState = holder.getView(R.id.img_state);
        TextView tvDetail = holder.getView(R.id.tv_detail);
    }
}
