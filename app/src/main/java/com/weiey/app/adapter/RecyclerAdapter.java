package com.weiey.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {
    public Context context;
    public int layoutRes;
    public List<T> items;

    public RecyclerAdapter(Context context, int layoutRes) {
        this.context = context;
        this.layoutRes = layoutRes;
        items = new ArrayList<T>();
    }

//    public RecyclerAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<T> data) {
//        this.context = context;
//        this.items = data == null ? new ArrayList<T>() : data;
//        if (layoutResId != 0) {
//            this.layoutRes = layoutResId;
//        }
//    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(inflateItemView(parent, viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final T item = getItem(position);
        convert(holder, position, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        if (items == null) {
            return null;
        }
        return items.get(position);
    }


    public void setData(List<T> items) {
        this.items = items == null ? new ArrayList<T>() : items;
        notifyDataSetChanged();
    }

    public void addData(@IntRange(from = 0) int position,@NonNull T data) {
        items.add(position, data);
        notifyItemInserted(position);
        compatibilityDataSizeChanged(1);
    }

    public void addData(@NonNull T data) {
        items.add(data);
        notifyItemInserted(items.size());
    }


    public void remove(int position) {
        items.remove(position);
        int internalPosition = position;
        notifyItemRemoved(internalPosition);
        notifyItemRangeChanged(internalPosition, items.size() - internalPosition);
    }

    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = items == null ? 0 : items.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }


    /**
     * 根据Type返回布局资源
     *
     * @param type
     * @return
     */
    protected int getItemLayout(int type) {
        return layoutRes;
    }

    /**
     * 解析布局资源
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    protected View inflateItemView(ViewGroup viewGroup, int viewType) {
        int itemLayout = getItemLayout(viewType);
        Context context = viewGroup.getContext();
        return LayoutInflater.from(context).inflate(itemLayout,
                viewGroup, false);
    }

    /**
     * 需要重写的方法
     *
     * @param holder
     * @param t
     */
    public abstract void convert(RecyclerHolder holder, int position, T t);
}