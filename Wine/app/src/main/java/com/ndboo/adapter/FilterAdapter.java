package com.ndboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ndboo.wine.R;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public class FilterAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;
    private int mCheckPosition=0;


    public FilterAdapter(List<String> list, Context context) {
        mList = list;
        mContext = context;
    }

    public void setChecked(int checkPosition) {
        mCheckPosition = checkPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.item_filter,null);
        TextView mTv= (TextView) convertView.findViewById(R.id.tv_filter);
        mTv.setText(mList.get(position));
        if (mCheckPosition == position) {
            mTv.setTextColor(mContext.getResources().getColor(R.color.Yellow));
        }else {
            mTv.setTextColor(mContext.getResources().getColor(R.color.themeColor));
        }
        return convertView;
    }
}
