package com.ndboo.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.ndboo.interfaces.OnItemClickListener;

/**
 * Created by Administrator on 2016/12/27.
 */

public class RecyclerView<T extends android.support.v7.widget.RecyclerView.ViewHolder> extends android.support.v7.widget.RecyclerView.Adapter<T>{

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T t, int position) {

        final int pos = position;
        t.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnItemClick(v, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
