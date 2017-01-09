package com.ndboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ndboo.bean.CartBean;
import com.ndboo.interfaces.ShoppingCarOnItemClickListener;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<CartBean> mCartBeanList;
    //选中的item
    private List<String> mCheckedPositionList;
    //是否显示复选框
    private boolean mShowCheckBox;

    private ShoppingCarOnItemClickListener mListener;

    public CartAdapter(Context context, List<CartBean> beanList) {
        mCartBeanList = beanList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mCheckedPositionList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mCartBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CarWineHolder carWineHolder = null;
        if (convertView != null) {
            carWineHolder = (CarWineHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_car, null);
            carWineHolder = new CarWineHolder(convertView);
            convertView.setTag(carWineHolder);
        }
        CartBean cartBean = mCartBeanList.get(position);
        Glide.with(mContext)
                .load(cartBean.getProductPicture())
                .into(carWineHolder.mImageGoods);
        carWineHolder.mTextGoodsDescription.setText(cartBean.getProductName());
        carWineHolder.mTextPrice.setText(cartBean.getProductPrice() + "元");
        carWineHolder.mTextNumber.setText(cartBean.getProductCount());
        carWineHolder.mNumPriceTextView.setText("小计：" + cartBean.getProductMoney() + "元");
        //是否显示复选框
        if (mShowCheckBox) {
            carWineHolder.mCheckBoxSelect.setVisibility(View.VISIBLE);
        } else {
            carWineHolder.mCheckBoxSelect.setVisibility(View.GONE);
        }
        //checkbox  复用问题
        carWineHolder.mCheckBoxSelect.setTag("" + position);//设置tag 否则划回来时选中消失
        if (mCheckedPositionList != null) {
            carWineHolder.mCheckBoxSelect.setChecked((
                    mCheckedPositionList.contains("" + position) ? true : false));
        } else {
            carWineHolder.mCheckBoxSelect.setChecked(false);
        }
        //复选框点击
        final CarWineHolder finalCarWineHolder = carWineHolder;
        carWineHolder.mCheckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (!mCheckedPositionList.contains(finalCarWineHolder.mCheckBoxSelect.getTag())) {
                        mCheckedPositionList.add("" + position);
                    }
                } else {
                    if (mCheckedPositionList.contains(finalCarWineHolder.mCheckBoxSelect.getTag())) {
                        mCheckedPositionList.remove("" + position);
                    }
                }
            }
        });
        carWineHolder.mCheckBoxSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckedChanged(position, (CompoundButton) v);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.viewClick(position, v);
            }
        });
        carWineHolder.mImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.numAdd(position, v);
            }
        });
        carWineHolder.mImageDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.numReduce(position, v);
            }
        });
        return convertView;
    }

    class CarWineHolder {
        @BindView(R.id.item_cart_checkbox)
        CheckBox mCheckBoxSelect;
        @BindView(R.id.item_cart_goods_image)
        ImageView mImageGoods;
        @BindView(R.id.item_cart_goods_name)
        TextView mTextGoodsDescription;
        @BindView(R.id.item_cart_goods_price)
        TextView mTextPrice;
        @BindView(R.id.item_cart_goods_acccunt_reduce)
        ImageView mImageDecrease;
        @BindView(R.id.item_cart_goods_acccunt_number)
        TextView mTextNumber;
        @BindView(R.id.item_cart_goods_acccunt_add)
        ImageView mImageAdd;
        @BindView(R.id.item_cart_goods_acccunt)
        LinearLayout mLayoutEdit;
        @BindView(R.id.item_cart_goods_acccunt_numprice)
        TextView mNumPriceTextView;

        CarWineHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setListener(ShoppingCarOnItemClickListener listener) {
        mListener = listener;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        mShowCheckBox = showCheckBox;
        notifyDataSetChanged();
    }

    public List<String> getCheckedPositionList() {
        return mCheckedPositionList;
    }

    public void setCheckedPositionList(List<String> checkedPositionList) {
        mCheckedPositionList = checkedPositionList;
    }
}