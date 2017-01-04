package com.ndboo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndboo.bean.Type;
import com.ndboo.interfaces.OnItemClickListener;
import com.ndboo.wine.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Li on 2016/12/27.
 * 三级分类适配器
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Type> mTypes;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public ProductAdapter(List<Type> types, Context context) {
        mTypes = types;
        mContext = context;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_index_wine_type, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        final int pos=position;
        Type type = mTypes.get(position);
        holder.mTextView.setText(type.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnItemClick(v,pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTypes.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView mImageView;
        @BindView(R.id.text)
        TextView mTextView;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

