package com.wty.app.uexpress.widget.xrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用recyclerview adapter(单种类型)
 * @author wty
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected int mLayoutResId;//布局文件id
    protected List<T> mData;
    private View mContentView;
    protected OnRecyclerItemClickListener listener;
    protected OnItemLongClickLitener onItemLongClickLitener = null;

    public BaseRecyclerViewAdapter(Context context) {
        this(context,0, null);
    }

    public BaseRecyclerViewAdapter(Context context, List<T> data) {
        this(context,0, data);
    }

    public BaseRecyclerViewAdapter(Context context, int layoutResId) {
        this(context,layoutResId,null);
    }

    public BaseRecyclerViewAdapter(Context context, View contentView, List<T> data) {
        this(context,0, data);
        mContentView = contentView;
    }

    public BaseRecyclerViewAdapter(Context context, int layoutResId, List<T> data) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder BaseRecyclerViewHolder = onCreateDefViewHolder(parent, viewType);
        return BaseRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        convert((BaseRecyclerViewHolder)holder,mData.get(position),position);
    }

    protected BaseRecyclerViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected BaseRecyclerViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (mContentView == null) {
            return BaseRecyclerViewHolder.get(mContext,parent,layoutResId);
        }
        return new BaseRecyclerViewHolder(mContentView);
    }

    public void remove(int position) {
        /**
         * 先remove,再notifyItemRemoved， 最后再notifyItemRangeChanged
         remove：把数据从list中remove掉，
         notifyItemRemoved：显示动画效果
         notifyItemRangeChanged：对于被删掉的位置及其后range大小范围内的view进行重新onBindViewHolder
         **/
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener){
        this.listener = listener;
    }

    /**
     * @Desc 在末尾新增一个item
     **/
    public void addOne(T item) {
        mData.add(item);
        notifyItemInserted(mData.size());
        notifyItemRangeChanged(mData.size()-1,1);
    }

    /**
     * @Desc  在某个位置上插入一个item
     **/
    public void addOne(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * @Desc 在末尾插入一系列item
     **/
    public void addList(List<T> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * @Desc 在某个位置插入一系列item
     **/
    public void addList(int position,List<T> data) {
        this.mData.addAll(position,data);
        notifyDataSetChanged();
    }

    /**
     * @Desc 重新刷新列表 - 注意传入的data不可为关联的 mData
     **/
    public void refreshList(List<T> data){
        this.mData.clear();
        addList(data);
    }

    /**
     * @Desc 清除列表
     **/
    public void clearList(){
        this.mData.clear();
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 功能描述：绑定数据
     **/
    protected abstract void convert(BaseRecyclerViewHolder holder, T item,int position);

    public interface OnRecyclerItemClickListener<T>{
        void OnItemClick(T item, int position);
    }

    //设置回调接口
    public interface OnItemLongClickLitener<T> {
        void onItemLongClick(View view, T item);
    }

    public void setOnItemLongClickLitener(OnItemLongClickLitener onItemLongClickLitener) {
        this.onItemLongClickLitener = onItemLongClickLitener;
    }

}
