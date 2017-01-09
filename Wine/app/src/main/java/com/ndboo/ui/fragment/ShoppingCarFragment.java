package com.ndboo.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ndboo.base.BaseFragment;
import com.ndboo.bean.CartBean;
import com.ndboo.interfaces.ShoppingCarOnItemClickListener;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.wine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Li on 2016/12/26.
 * “商家”界面
 */

public class ShoppingCarFragment extends BaseFragment {
    private static final boolean TYPE_EDIT = false;
    private static final boolean TYPE_COMPLETE = true;

    @BindView(R.id.list_view_car_wines)
    ListView mListViewCarWines;
    @BindView(R.id.tv_edit_complete)
    TextView mTvEditComplete;

    private List<CartBean> mCartBeanList;
    private ShoppingCarOnItemClickListener mListener;
    private CartAdapter mCartAdapter;

    private String mEdit;
    private String mComplete;

    // 当前操作类型
    private boolean mCurrentType = TYPE_COMPLETE;
    //选中的复选框集合
    private List<String> mSelectedList = new ArrayList<>();

    //底部-结算
    @BindView(R.id.cart_bottom_pay)
    RelativeLayout mBottomLayout1;
    @BindView(R.id.cart_bottom_pay_totalprice)
    TextView mTotalPriceTextView;

    //底部-删除
    @BindView(R.id.cart_bottom_delete)
    RelativeLayout mBottomLayout2;
    @BindView(R.id.cart_bottom_delete_checkbox)
    CheckBox mCheckBox;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shopping_car;
    }

    @Override
    public void firstVisibleDeal() {
        super.firstVisibleDeal();
        mEdit = getResources().getString(R.string.car_edit);
        mComplete = getResources().getString(R.string.car_complete);

        /**
         * 全选按钮
         */
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCartBeanList.size() == 0) {
                    mCheckBox.setChecked(false);
                    ToastUtil.showToast(getActivity(), "暂无商品");
                }
            }
        });
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    for (int i = 0; i < mCartBeanList.size(); i++) {
                        mSelectedList.add("" + i);
                    }
                } else {
                    mSelectedList.clear();
                }
                mCartAdapter.notifyDataSetChanged();
            }
        });

        mCartBeanList = new ArrayList<>();
        mCartAdapter = new CartAdapter(mCartBeanList);
        mListener = new ShoppingCarOnItemClickListener() {
            @Override
            public void numAdd(int position, View view) {
                Toast.makeText(getContext(), "add:" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void numReduce(int position, View view) {
                Toast.makeText(getContext(), "reduce:" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void viewClick(int position, View view) {
                Toast.makeText(getContext(), "click:" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckedChanged(int position, CompoundButton buttonView) {
                Toast.makeText(getContext(), "check:" + position + " " +
                        buttonView.isChecked(), Toast.LENGTH_SHORT).show();
            }
        };
        mListViewCarWines.setAdapter(mCartAdapter);
    }

    @Override
    protected void visibleDeal() {
        super.visibleDeal();
        switch ("12") {
            case "1":
                requestData();
                break;
        }
    }

    /**
     * 获取购物车列表
     */
    private void requestData() {
        Subscription subscription = RetrofitHelper.getApi()
                .getCartProductsList(SharedPreferencesUtil.getUserId(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            //总价
                            String totalMoney = jsonObject.optString("totalMoney", "0.00");
                            mTotalPriceTextView.setText("总价：" + totalMoney + "元");
                            //商品信息
                            JSONArray jsonArray = jsonObject.optJSONArray("productInformation");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject goodsObject = jsonArray.optJSONObject(i);
                                String productId = goodsObject.optString("productId");
                                String productName = goodsObject.optString("productName");
                                String productPicture = goodsObject.optString("productPicture");
                                String productPrice = goodsObject.optString("productPrice");
                                String productCount = goodsObject.optString("productCount");
                                String productMoney = goodsObject.optString("productMoney");
                                CartBean cartBean = new CartBean(productPicture, productCount,
                                        productPrice, productName, productId, productMoney);
                                mCartBeanList.add(cartBean);
                            }
                            mCartAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(getActivity(), "error");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(getActivity(), "error:" + throwable.getMessage());
                    }
                });

    }

    @OnClick(R.id.tv_edit_complete)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_complete:
                if (mCartBeanList.size() == 0) {
                    ToastUtil.showToast(getActivity(), "暂无商品");
                    return;
                }
                mCurrentType = !mCurrentType;
                if (mCurrentType == TYPE_EDIT) {
                    mTvEditComplete.setText(mComplete);
                    mBottomLayout1.setVisibility(View.VISIBLE);
                    mBottomLayout2.setVisibility(View.GONE);
                } else {
                    mTvEditComplete.setText(mEdit);
                    mBottomLayout2.setVisibility(View.VISIBLE);
                    mBottomLayout1.setVisibility(View.GONE);
                }
                mCartAdapter.notifyDataSetChanged();
                break;
        }
    }

    static class CarWineHolder {
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

        CarWineHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class CartAdapter extends BaseAdapter {

        private List<CartBean> mCartBeens;

        public CartAdapter(List<CartBean> cartBeens) {
            mCartBeens = cartBeens;
        }

        @Override
        public int getCount() {
            return mCartBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return mCartBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CartBean cartBean = mCartBeens.get(position);
            final CarWineHolder carWineHolder;
            if (convertView != null) {
                carWineHolder = (CarWineHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shopping_car, null);
                carWineHolder = new CarWineHolder(convertView);
                convertView.setTag(carWineHolder);
            }
            //是否显示复选框
            if (mCurrentType == TYPE_EDIT) {
                carWineHolder.mCheckBoxSelect.setVisibility(View.VISIBLE);
            } else {
                carWineHolder.mCheckBoxSelect.setVisibility(View.GONE);
            }
            //checkbox  复用问题
            carWineHolder.mCheckBoxSelect.setTag("" + position);//设置tag 否则划回来时选中消失
            if (mSelectedList != null) {
                carWineHolder.mCheckBoxSelect.setChecked((
                        mSelectedList.contains("" + position) ? true : false));
            } else {
                carWineHolder.mCheckBoxSelect.setChecked(false);
            }
            if (mListener != null) {
                //复选框点击
                carWineHolder.mCheckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            if (!mSelectedList.contains(carWineHolder.mCheckBoxSelect.getTag())) {
                                mSelectedList.add("" + position);
                            }
                        } else {
                            if (mSelectedList.contains(carWineHolder.mCheckBoxSelect.getTag())) {
                                mSelectedList.remove("" + position);
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
            }
            return convertView;
        }
    }
}
