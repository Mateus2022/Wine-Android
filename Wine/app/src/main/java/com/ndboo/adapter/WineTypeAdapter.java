package com.ndboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ndboo.wine.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Li on 2016/12/27.
 * 二级分类适配器
 */

public class WineTypeAdapter extends BaseAdapter {
    private List<String> mTypes;
    private int mCurrentPosition;
    private Context mContext;

    public WineTypeAdapter(List<String> types, int currentPosition, Context context) {
        mTypes = types;
        mCurrentPosition = currentPosition;
        mContext = context;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
        notifyDataSetChanged();
    }

    public void setTypesAndPosition(List<String> types, int currentPosition) {
        mTypes = types;
        mCurrentPosition = currentPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return mTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WineTypeHolder wineTypeHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stage_two, null);
            wineTypeHolder = new WineTypeHolder(convertView);
            convertView.setTag(wineTypeHolder);
        } else {
            wineTypeHolder = (WineTypeHolder) convertView.getTag();
        }


        if (mCurrentPosition == position) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.stageTwoSelectedColor));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.stageTwoColor));
        }
        wineTypeHolder.mTvStageTwo.setText(mTypes.get(position));
        return convertView;
    }

    static class WineTypeHolder {


        @BindView(R.id.tv_stage_two)
        TextView mTvStageTwo;

        WineTypeHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
