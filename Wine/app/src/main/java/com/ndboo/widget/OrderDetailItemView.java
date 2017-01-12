package com.ndboo.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ndboo.bean.OrderDetailBean;
import com.ndboo.wine.R;

/**
 * Created by ZhangHang on 2016/9/13.
 */
public class OrderDetailItemView extends LinearLayout {
    private View mView;
    private ImageView mImageView;
    private TextView mNameTextView, mPerPrice1TextView, mCountTextView;
    private TextRightTextView mTextRightTextView;

    private Context mContext;

    public OrderDetailItemView(Context context) {
        this(context, null);
    }

    public OrderDetailItemView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public OrderDetailItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.item_orderdetail, null);
        mView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mImageView = (ImageView) mView.findViewById(R.id.item_orderdetail_img);
        mNameTextView = (TextView) mView.findViewById(R.id.item_orderdetail_name);
        mPerPrice1TextView = (TextView) mView.findViewById(R.id.item_orderdetail_perprice);
        mTextRightTextView = (TextRightTextView) mView.findViewById(R.id.item_orderdetail_item);
        mCountTextView = (TextView) mView.findViewById(R.id.item_orderdetail_count);
        addView(mView);
    }

    public void setData(OrderDetailBean orderDetailBean) {
        mNameTextView.setText(orderDetailBean.getName());
        mPerPrice1TextView.setText("¥" + orderDetailBean.getPerPrice());
        mCountTextView.setText("x" + orderDetailBean.getCount());
        mTextRightTextView.setLabelText("已选" + orderDetailBean.getCount());
        mTextRightTextView.setDataText("¥" + orderDetailBean.getTotal());
        Glide.with(mContext).load(orderDetailBean.getImagePath()).into(mImageView);
    }
}