package com.wty.app.uexpress.ui.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.db.entity.EntityCompanyDALEx;
import com.wty.app.uexpress.util.AppImageLoader;
import com.wty.app.uexpress.util.CoreImageURLUtils;
import com.wty.app.uexpress.util.CorePinYinUtil;
import com.wty.app.uexpress.widget.roundedimageview.RoundedImageView;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wty
 * 快递公司适配器
 */
public class ExpressCompanySelectListAdapter extends BaseRecyclerViewAdapter<EntityCompanyDALEx> {

    //字母列表
    private Map<String, Integer> alphaIndexer;

    public ExpressCompanySelectListAdapter(Context context, List<EntityCompanyDALEx> list){
        super(context, R.layout.item_company_list,list);
        init();
    }

    @Override
    public void refreshList(List<EntityCompanyDALEx> data) {
        super.refreshList(data);
        init();
    }

    @Override
    protected void convert(BaseRecyclerViewHolder holder, final EntityCompanyDALEx item, final int position) {
        RoundedImageView iv_icon = holder.getView(R.id.item_icon);
        TextView tv_name = holder.getView(R.id.item_name);
        TextView tv_index = holder.getView(R.id.item_index);
        TextView tv_phone = holder.getView(R.id.item_phonenum);

        tv_name.setText(item.getName());
        tv_index.setText(item.getPinyin());
        tv_phone.setText(item.getContact());
        tv_phone.setVisibility(TextUtils.isEmpty(item.getContact())?View.GONE:View.VISIBLE);

        AppImageLoader.displayImage(mContext,CoreImageURLUtils.ImageScheme.HEADIMG.wrap(item.getCompanyIcon()),iv_icon);

        if (position > 0) {
            // 不是第一行，控制pinyin栏
            EntityCompanyDALEx lastItem = getItem(position - 1);
            String lastAlpha = CorePinYinUtil.getPinyinFirstAlpha(lastItem.getPinyin());
            String nowAlpha = CorePinYinUtil.getPinyinFirstAlpha(item.getPinyin());
            if (lastAlpha.equals(nowAlpha)) {
                tv_index.setVisibility(View.GONE);
            } else {
                tv_index.setVisibility(View.VISIBLE);
                tv_index.setText(nowAlpha);
            }
        } else {
            tv_index.setVisibility(View.VISIBLE);
            if("*".equals(item.getPinyin())){
                tv_index.setText("常用");
            }else {
                tv_index.setText(CorePinYinUtil.getPinyinFirstAlpha(item.getPinyin()));
            }
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.OnItemClick(item,position);
                }
            }
        });
    }

    public void init() {
        //构建字母列表索引
        if (alphaIndexer == null) {
            alphaIndexer = new LinkedHashMap<>();
        } else {
            alphaIndexer.clear();
        }

        for (int index = 0; index < mData.size(); index++) {
            String alpha = CorePinYinUtil.getPinyinFirstAlpha(mData.get(index).getPinyin());
            if (!alphaIndexer.containsKey(alpha)) {
                alphaIndexer.put(alpha, index);
            }
        }
    }

    public Map<String, Integer> getAlphaIndexer() {
        if (alphaIndexer == null) {
            alphaIndexer = new HashMap<String, Integer>();
        }
        return alphaIndexer;
    }

    public List<String> getAlphaList() {
        List<String> list = new ArrayList<>();
        if (alphaIndexer == null) {
            init();
        }
        list.addAll(alphaIndexer.keySet());
        return list;
    }
}
