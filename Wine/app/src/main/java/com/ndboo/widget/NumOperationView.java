package com.ndboo.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ndboo.interfaces.NumOperationListener;
import com.ndboo.wine.R;

/**
 * Created by Li on 2017/1/7.
 * 商城以及购物车界面修改数量控件
 */

public class NumOperationView extends LinearLayout {

    private static final String NUM_EMPTY = "0";
    private ImageView mImageViewAdd;
    private ImageView mImageViewReduce;
    private TextView mTextViewNumber;
    private NumOperationListener mNumOperationListener;
    private LinearLayout mLayout;

    public NumOperationView(Context context) {
        super(context);
    }

    public NumOperationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initView();
    }

    public void setNumOperationListener(NumOperationListener numOperationListener) {
        mNumOperationListener = numOperationListener;
    }

    private void initView() {
        mImageViewAdd = (ImageView) findViewById(R.id.item_cart_goods_acccunt_add);
        mImageViewReduce = (ImageView) findViewById(R.id.item_cart_goods_acccunt_reduce);
        mTextViewNumber = (TextView) findViewById(R.id.item_cart_goods_acccunt_number);
        mLayout = (LinearLayout) findViewById(R.id.item_cart_goods_acccunt);

        mImageViewAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumOperationListener != null) {
                    mNumOperationListener.numAdd(mNumOperationListener.position(), v);
                }

            }
        });
        mImageViewReduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumOperationListener != null) {
                    mNumOperationListener.numReduce(mNumOperationListener.position(), v);
                }

            }
        });


    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_num_operation, this);
    }

    public void hide() {
        mLayout.setBackgroundColor(Color.WHITE);
        mImageViewReduce.setVisibility(View.GONE);
        mTextViewNumber.setVisibility(View.GONE);
    }

    public void show() {
        mLayout.setBackgroundResource(R.drawable.rectangle);
        mImageViewReduce.setVisibility(View.VISIBLE);
        mTextViewNumber.setVisibility(View.VISIBLE);
    }

    public void numAdd() {
        setNumber(Integer.parseInt(getNumber()) + 1 + "");
        hideOrShow();
    }

    public void numReduce() {
        setNumber(Integer.parseInt(getNumber()) - 1 + "");
        hideOrShow();
    }

    public String getNumber() {
        return mTextViewNumber.getText().toString();
    }

    public void setNumber(String number) {
        if (Integer.parseInt(number)<=Integer.parseInt(NUM_EMPTY)) {
            hide();
        }else {
            show();
        }
        mTextViewNumber.setText(number);
    }

    private void hideOrShow() {
        if (getNumber().equals(NUM_EMPTY)) {
            hide();
        } else {
            show();
        }
    }

}
