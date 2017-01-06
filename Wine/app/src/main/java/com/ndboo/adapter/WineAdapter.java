package com.ndboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndboo.bean.Wine;
import com.ndboo.wine.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Li on 2017/1/5.
 * “酒”适配器
 */

public class WineAdapter extends BaseAdapter {
    private Context mContext;

    public void setWines(List<Wine> wines) {
        mWines = wines;
        notifyDataSetChanged();
    }

    private List<Wine> mWines;

    public WineAdapter(Context context, List<Wine> wines) {
        mContext = context;
        mWines = wines;
    }

    @Override
    public int getCount() {

        return mWines.size();
    }

    @Override
    public Object getItem(int position) {
        return mWines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Wine wine=mWines.get(position);
        WineViewHolder wineViewHolder;
        if (convertView == null) {
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_wine,null);
            wineViewHolder=new WineViewHolder(convertView);
            convertView.setTag(wineViewHolder);
        }else {
            wineViewHolder= (WineViewHolder) convertView.getTag();
        }
//        wineViewHolder.mIv.setImageResource(Integer.parseInt(wine.getImgUrl()));
        wineViewHolder.mTvWineName.setText(wine.getWineName());
        wineViewHolder.mTvPrice.setText("¥："+wine.getPrice());
        return convertView;
    }



    static class WineViewHolder {
        @BindView(R.id.iv)
        ImageView mIv;
        @BindView(R.id.tv_wine_name)
        TextView mTvWineName;
        @BindView(R.id.tv_price)
        TextView mTvPrice;

        WineViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
