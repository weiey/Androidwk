package com.weiey.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**通用的ListView的Adapter
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> list;
    protected LayoutInflater mInflater;
    protected int layoutId;

    public CommonAdapter(Context context, int layoutId) {
        this.mContext = context;
        this.layoutId = layoutId;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<T> list) {
        if(this.list == null) {
            this.list = new ArrayList();
        }
        this.list.clear();
        this.list.addAll(new ArrayList(list));
        this.notifyDataSetChanged();
    }
    public void addData(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if(this.list == null){
            this.list = new ArrayList<>();
        }
        this.list.addAll(new ArrayList(list));
        this.notifyDataSetChanged();
    }

    public T remove(int position) {
        T t = this.list.remove(position);
        this.notifyDataSetChanged();
        return t;
    }

    public void add(T t) {
        if(this.list == null) {
            this.list = new ArrayList();
        }
        this.list.add(t);
        this.notifyDataSetChanged();
    }

    public void insert(int location, T t) {
        if(this.list == null) {
            this.list = new ArrayList();
            this.list.add(t);
            this.notifyDataSetChanged();
        } else if(location >= 0 && location <= this.list.size()) {
            this.list.add(location, t);
            this.notifyDataSetChanged();
        }

    }

    public void clear() {
        if(this.list != null) {
            this.list.clear();
            this.notifyDataSetChanged();
        }
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,  layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder, T t);

    public void updataView(int posi, ListView listView, T entity) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            ViewHolder holder = (ViewHolder) view.getTag();
            convert(holder,entity);
        }
    }
}
