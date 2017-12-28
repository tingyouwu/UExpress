package com.wty.app.uexpress.widget.xrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * 通用recyclerview viewholder
 * @author wty
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;

    public View convertView;

    public BaseRecyclerViewHolder(View view) {
        super(view);
        this.views = new SparseArray<View>();
        convertView = view;
    }

    public static BaseRecyclerViewHolder get(Context context, ViewGroup parent, int layoutId)
    {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(itemView);
        return holder;
    }

    public View getConvertView() {
        return convertView;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The BaseViewHolder for chaining.
     */
    public BaseRecyclerViewHolder setAdapter(int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    @SuppressWarnings("unchecked")
    public void removeView(int viewId) {
        View view = views.get(viewId);
        if (view != null) {
            views.remove(viewId);
        }
    }
}
