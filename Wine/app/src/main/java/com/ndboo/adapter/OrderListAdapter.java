package com.ndboo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ndboo.bean.MyOrderFirstBean;
import com.ndboo.bean.MyOrderSecondBean;
import com.ndboo.widget.OrderItemView;
import com.ndboo.wine.R;

import java.util.List;
import java.util.Map;

/**
 * Created by ZhangHang on 2017/1/4.
 */

public class OrderListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    //Item1
    private List<MyOrderFirstBean> mFirstBeanList;
    //Item2
    private Map<Integer, List<MyOrderSecondBean>> mSecondBeanList;

    public OrderListAdapter(Context context, Map<Integer, List<MyOrderSecondBean>> secondBeanList,
                            List<MyOrderFirstBean> firstBeanList) {
        this.mContext = context;
        this.mSecondBeanList = secondBeanList;
        this.mFirstBeanList = firstBeanList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mFirstBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFirstBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_orderlist_first, null);

            holder.mLinearLayout = (LinearLayout) view.findViewById(R.id.item_myorder_line);
            holder.mTimeTextView = (TextView) view.findViewById(R.id.item_myorder_time);
            holder.mStatusTextView = (TextView) view.findViewById(R.id.item_myorder_status);
            holder.mTotalCountTextView = (TextView) view.findViewById(R.id.item_myorder_totalcount);
            holder.mTotalPriceTextView = (TextView) view.findViewById(R.id.item_myorder_totalprice);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MyOrderFirstBean firstBean = mFirstBeanList.get(i);
        //嵌套的RecyclerView
        List<MyOrderSecondBean> list = mSecondBeanList.get(new Integer(i));
        if (list != null && list.size() > 0) {
            holder.mTimeTextView.setText(firstBean.getPlaceTime());
            holder.mStatusTextView.setText(firstBean.getStatus());
            //设置样式
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
            //数量
            String text1 = "共" + firstBean.getCount() + "种商品";
            SpannableString spannableString1 = new SpannableString(text1);
            spannableString1.setSpan(colorSpan, 1, text1.length() - 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.mTotalCountTextView.setText(spannableString1);
            //价格
            String text2 = "订单金额" + firstBean.getPrice() + "元";
            SpannableString spannableString2 = new SpannableString(text2);
            spannableString2.setSpan(colorSpan, 4, text2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.mTotalPriceTextView.setText(spannableString2);
            //添加内部商品Item
            holder.mLinearLayout.removeAllViews();
            //显示中间和底部的分割线
            holder.mLinearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE
                    | LinearLayout.SHOW_DIVIDER_END);
            holder.mLinearLayout.setDividerDrawable(mContext.getResources()
                    .getDrawable(R.drawable.shape_divider_horizontal));
            for (int j = 0; j < list.size(); j++) {
                MyOrderSecondBean secondBean = list.get(j);

                OrderItemView item2 = new OrderItemView(mContext);
                item2.setImage(secondBean.getImageUrl());
                item2.setNameText(secondBean.getName());
                item2.setCountText("x" + secondBean.getCount());
                item2.setPerPriceText("¥" + secondBean.getPerPrice() + "/" + secondBean.getUnit());
                item2.setTotalText("小计：" + secondBean.getTotalPrice() + "元");
                holder.mLinearLayout.addView(item2);
            }
        }
        return view;
    }

    private class ViewHolder {
        private TextView mTimeTextView;
        private TextView mStatusTextView;
        private TextView mTotalCountTextView;
        private TextView mTotalPriceTextView;
        private LinearLayout mLinearLayout;
    }
}
