package com.ndboo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ndboo.bean.OrderDetailBean;
import com.ndboo.widget.TextRightTextView;
import com.ndboo.wine.R;

import java.util.List;

/**
 * Created by ZhangHang on 2016/8/25.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<OrderDetailBean> mBeanList;

    public OrderDetailAdapter(Context context, List<OrderDetailBean> beanList) {
        mContext = context;
        mBeanList = beanList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_orderdetail, parent, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.measure(0, 0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetailBean bean = mBeanList.get(position);
        Glide.with(mContext).load(bean.getImagePath()).into(holder.mImageView);
        holder.mNameTextView.setText(bean.getName());
        holder.mTextRightTextView.setLabelText("已选" + bean.getCount());
        holder.mTextRightTextView.setDataText("¥" + bean.getTotal());
        holder.mPerPrice1TextView.setText("¥" + bean.getPerPrice());
        holder.mCountTextView.setText("x" + bean.getCount());
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mNameTextView, mPerPrice1TextView, mCountTextView;
        private TextRightTextView mTextRightTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.item_orderdetail_img);
            mNameTextView = (TextView) itemView.findViewById(R.id.item_orderdetail_name);
            mPerPrice1TextView = (TextView) itemView.findViewById(R.id.item_orderdetail_perprice);
            mTextRightTextView = (TextRightTextView) itemView.findViewById(R.id.item_orderdetail_item);
            mCountTextView = (TextView) itemView.findViewById(R.id.item_orderdetail_count);
        }
    }
}
