package com.ndboo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ndboo.bean.CartBean;
import com.ndboo.wine.R;

import java.util.List;

/**
 * Created by ZhangHang on 2016/8/25.
 */
public class EditOrderAdapter extends RecyclerView.Adapter<EditOrderAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<CartBean> mBeanList;

    public EditOrderAdapter(Context context, List<CartBean> beanList) {
        mContext = context;
        mBeanList = beanList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_editorder, parent, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.measure(0, 0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartBean bean = mBeanList.get(position);
        Glide.with(mContext).load(bean.getProductPicture()).into(holder.mImageView);
        holder.mNameTextView.setText(bean.getProductName());
        holder.mNumPriceTextView.setText("¥" + bean.getProductMoney());
        holder.mPriceTextView.setText("¥" + bean.getProductPrice());
        holder.mCountTextView.setText("x" + bean.getProductCount());
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mNameTextView, mPriceTextView,
                mNumPriceTextView, mCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.item_editorder_img);
            mNameTextView = (TextView) itemView.findViewById(R.id.item_editorder_name);
            mPriceTextView = (TextView) itemView.findViewById(R.id.item_editorder_price);
            mNumPriceTextView = (TextView) itemView.findViewById(R.id.item_editorder_numprice);
            mCountTextView = (TextView) itemView.findViewById(R.id.item_editorder_count);
        }
    }
}
