package com.weiey.app.adapter;

import android.util.SparseArray;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public RecyclerHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <V extends View> V getView(int res) {
        View v = mViews.get(res);
        if (v == null) {
            v = itemView.findViewById(res);
            mViews.put(res, v);
        }
        return (V) v;
    }
}