package com.ndboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndboo.bean.Wine;
import com.ndboo.interfaces.NumOperationListener;
import com.ndboo.widget.NumOperationView;
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
    private List<Wine> mWines;

    public WineAdapter(Context context, List<Wine> wines) {
        mContext = context;
        mWines = wines;
    }

    public void setWines(List<Wine> wines) {
        mWines = wines;
        notifyDataSetChanged();
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
        final int pos = position;
        Wine wine = mWines.get(position);
        final WineViewHolder wineViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wine, null);
            wineViewHolder = new WineViewHolder(convertView);
            convertView.setTag(wineViewHolder);
        } else {
            wineViewHolder = (WineViewHolder) convertView.getTag();
        }
//        wineViewHolder.mIv.setImageResource(Integer.parseInt(wine.getImgUrl()));
        wineViewHolder.mTvWineName.setText(wine.getWineName());
        wineViewHolder.mTvPrice.setText("¥：" + wine.getPrice());
        wineViewHolder.mViewNumOperation.setNumber(wine.getNumber());
        wineViewHolder.mViewNumOperation.setNumOperationListener(new NumOperationListener() {
            @Override
            public void numAdd(int position, View view) {

                wineViewHolder.mViewNumOperation.numAdd();
                mWines.get(position).setNumber(wineViewHolder.mViewNumOperation.getNumber());
                notifyDataSetChanged();
            }

            @Override
            public void numReduce(int position, View view) {
                wineViewHolder.mViewNumOperation.numReduce();
                mWines.get(position).setNumber(wineViewHolder.mViewNumOperation.getNumber());
                notifyDataSetChanged();
            }

            @Override
            public int position() {
                return pos;
            }
        });
        return convertView;
    }


    static class WineViewHolder {
        @BindView(R.id.iv)
        ImageView mIv;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.view_num_operation)
        NumOperationView mViewNumOperation;
        @BindView(R.id.tv_wine_name)
        TextView mTvWineName;

        WineViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
