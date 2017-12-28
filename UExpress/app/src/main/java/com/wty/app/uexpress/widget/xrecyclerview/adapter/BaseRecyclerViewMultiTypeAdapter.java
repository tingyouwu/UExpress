package com.wty.app.uexpress.widget.xrecyclerview.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用recyclerview adapter（多布局）
 */
public abstract class BaseRecyclerViewMultiTypeAdapter<T> extends BaseRecyclerViewAdapter<T> {

    /**
     * Map用于存储  类型以及 对应的布局文件  调用addItemType吧少年
     */
    private SparseArray<Integer> layouts;

    public BaseRecyclerViewMultiTypeAdapter(Context context) {
        super(context,null);
    }

    public BaseRecyclerViewMultiTypeAdapter(Context context, List<T> data) {
        super(context,data);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemMultiViewType(position);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    /**
     * @Decription 添加多布局文件
     **/
    protected void addItemType(int type, int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }


    @Override
    protected void convert(BaseRecyclerViewHolder holder, T item,int position) {
        bindView(holder, item, position);
    }

    /**
     * @Decription 绑定数据
     **/
    protected abstract void bindView(BaseRecyclerViewHolder holder, T item,int position);

    /**
     * @Description 布局类型
     **/
    protected abstract int getItemMultiViewType(int position);

}


