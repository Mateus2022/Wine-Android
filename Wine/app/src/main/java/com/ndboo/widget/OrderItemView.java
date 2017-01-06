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
import com.ndboo.wine.R;

/**
 * Created by ZhangHang on 2016/9/13.
 */
public class OrderItemView extends LinearLayout {
    private View mView;
    private ImageView mImageView;
    private TextView mNameTextView;
    private TextView mCountTextView;
    private TextView mPerPriceTextView;
    private TextView mTotalTextView;

    private Context mContext;

    public OrderItemView(Context context) {
        this(context, null);
    }

    public OrderItemView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public OrderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.layout_orderlist_second, null);
        mView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mImageView = (ImageView) mView.findViewById(R.id.item_myorder_two_info_img);
        mNameTextView = (TextView) mView.findViewById(R.id.item_myorder_two_info_name);
        mCountTextView = (TextView) mView.findViewById(R.id.item_myorder_two_info_count);
        mPerPriceTextView = (TextView) mView.findViewById(R.id.item_myorder_two_info_perprice);
        mTotalTextView = (TextView) mView.findViewById(R.id.item_myorder_two_info_totalprice);
        addView(mView);
    }

    public void setNameText(String nameText) {
        mNameTextView.setText(nameText);
    }

    public void setCountText(String countText) {
        mCountTextView.setText(countText);
    }

    public void setPerPriceText(String perPriceText) {
        mPerPriceTextView.setText(perPriceText);
    }

    public void setTotalText(String totalText) {
        mTotalTextView.setText(totalText);
    }

    public void setImage(String imgPath) {
        Glide.with(mContext).load(imgPath).into(mImageView);
    }
}