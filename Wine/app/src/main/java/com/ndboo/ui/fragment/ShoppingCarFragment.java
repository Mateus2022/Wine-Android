package com.ndboo.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.CartBean;
import com.ndboo.interfaces.ShoppingCarOnItemClickListener;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.adapter.CartAdapter;
import com.ndboo.utils.ToastUtil;
import com.ndboo.wine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Li on 2016/12/26.
 * “购物车”界面
 */

public class ShoppingCarFragment extends BaseFragment {
    private static final boolean TYPE_EDIT = false;
    private static final boolean TYPE_COMPLETE = true;

    @BindView(R.id.list_view_car_wines)
    ListView mListViewCarWines;
    @BindView(R.id.tv_edit_complete)
    TextView mTvEditComplete;

    private List<CartBean> mCartBeanList;
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
        mCartAdapter = new CartAdapter(getActivity(), mCartBeanList);
        mCartAdapter.setListener(new ShoppingCarOnItemClickListener() {
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
        });
        mListViewCarWines.setAdapter(mCartAdapter);
    }

    @Override
    protected void visibleDeal() {
        super.visibleDeal();
        requestData();
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
                        Log.e("ndb", "result:" + string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            //总价
                            String totalMoney = jsonObject.optString("totalMoney", "0.00");
                            mTotalPriceTextView.setText("总价：" + totalMoney + "元");
                            //商品信息
                            JSONArray jsonArray = jsonObject.optJSONArray("productInformation");
                            Type type = new TypeToken<ArrayList<CartBean>>() {
                            }.getType();
                            List<CartBean> cartBeanList = new Gson().fromJson(jsonArray.toString(), type);
                            mCartBeanList.addAll(cartBeanList);
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
}
