package com.ndboo.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ndboo.adapter.CartAdapter;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.CartBean;
import com.ndboo.interfaces.ShoppingCarOnItemClickListener;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.wine.EditOrderActivity;
import com.ndboo.wine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                mCartAdapter.setCheckedPositionList(mSelectedList);
                mCartAdapter.notifyDataSetChanged();
            }
        });

        mCartBeanList = new ArrayList<>();
        mCartAdapter = new CartAdapter(getActivity(), mCartBeanList);
        mCartAdapter.setListener(new ShoppingCarOnItemClickListener() {
            @Override
            public void numAdd(int position, View view) {
                CartBean cartBean = mCartBeanList.get(position);
                int productCount = Integer.parseInt(cartBean.getProductCount());
                updateProductCount(cartBean.getProductId(), ++productCount, position);
            }

            @Override
            public void numReduce(int position, View view) {
                CartBean cartBean = mCartBeanList.get(position);
                int productCount = Integer.parseInt(cartBean.getProductCount());
                if (productCount == 1) {
                    deleteProduct(cartBean.getProductId());
                } else {
                    updateProductCount(cartBean.getProductId(), --productCount, position);
                }
            }

            @Override
            public void viewClick(int position, View view) {
            }

            @Override
            public void onCheckedChanged(int position, CompoundButton buttonView) {
            }
        });
        mListViewCarWines.setAdapter(mCartAdapter);
    }

    /**
     * 修改商品数量
     */
    private void updateProductCount(final String productId, final int productCount, final int position) {
        Subscription subscription = RetrofitHelper.getApi()
                .modifyProductNum(SharedPreferencesUtil.getUserId(getActivity()), productId, "" + productCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        Log.e("ndb", "result:" + string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            String result = jsonObject.optString("result");
                            if (result.equals("true")) {
                                mCartBeanList.get(position).setProductCount(productCount + "");
                                mCartAdapter.notifyDataSetChanged();
                            } else {
                                ToastUtil.showToast(getActivity(), "修改数量失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(getActivity(), "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
    }

    @Override
    protected void visibleDeal() {
        super.visibleDeal();
        if (SharedPreferencesUtil.isUserLoginIn(getActivity())) {
            mCartBeanList.clear();
            mSelectedList.clear();
            requestData();
        }
    }

    /**
     * 获取购物车列表
     */
    private void requestData() {
        mCartBeanList.clear();
        mCheckBox.setChecked(false);
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
                            //商品信息
                            JSONArray jsonArray = jsonObject.optJSONArray("productInfromation");
                            if (jsonArray == null) {
                                mCurrentType = TYPE_EDIT;
                                changeEditMode();
//                                ToastUtil.showToast(getActivity(), "暂无商品");
                                return;
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cartObject = jsonArray.getJSONObject(i);
                                mCartBeanList.add(new Gson().fromJson(cartObject.toString(), CartBean.class));
                            }
                            mTotalPriceTextView.setText("总价：" + totalMoney + "元");
                            mCartAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(getActivity(), "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
    }

    @OnClick({R.id.tv_edit_complete, R.id.cart_bottom_delete_delete,
            R.id.cart_bottom_pay_topay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_complete:
                if (mCartBeanList.size() == 0) {
                    ToastUtil.showToast(getActivity(), "暂无商品");
                    return;
                }
                changeEditMode();
                mCartAdapter.notifyDataSetChanged();
                break;
            case R.id.cart_bottom_delete_delete:
                //删除商品
                if (mCartBeanList.size() == 0) {
                    ToastUtil.showToast(getActivity(), "暂无商品");
                    return;
                }
                mSelectedList = mCartAdapter.getCheckedPositionList();
                if (mSelectedList.size() == 0) {
                    ToastUtil.showToast(getActivity(), "请选择商品");
                    return;
                }
                deleteDialog();
                break;
            case R.id.cart_bottom_pay_topay:
                //去结算
                if (mCartBeanList.size() == 0) {
                    ToastUtil.showToast(getActivity(), "暂无商品");
                    return;
                }
                Intent intent = new Intent(getActivity(), EditOrderActivity.class);
                //获取所有id
                String ids = "";
                for (int i = 0; i < mCartBeanList.size(); i++) {
                    ids += mCartBeanList.get(i).getProductId();
                    if (i != mCartBeanList.size() - 1) {
                        ids += ",";
                    }
                }
                intent.putExtra("productIds", ids);
                startActivity(intent);
                break;
        }
    }

    private void deleteDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("温馨提示")
                .setMessage("确认删除选中的商品吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //表示删除选中的
                        String ids = "";
                        for (int i = 0; i < mSelectedList.size(); i++) {
                            String position = mSelectedList.get(i);
                            CartBean cartBean = mCartBeanList.get(Integer.parseInt(position));
                            ids += cartBean.getProductId();
                            if (i != mSelectedList.size() - 1) {
                                ids += ",";
                            }
                        }
                        deleteProduct(ids);
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 删除购物车商品
     *
     * @param ids 要删除的商品id
     */
    private void deleteProduct(String ids) {
        Subscription subscription = RetrofitHelper.getApi()
                .deleteFromCart(SharedPreferencesUtil.getUserId(getActivity()), ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        Log.e("ndb", "result:" + string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            String result = jsonObject.optString("result");
                            if (result.equals("true")) {
                                ToastUtil.showToast(getActivity(), "删除成功");
                                requestData();
                            } else {
                                ToastUtil.showToast(getActivity(), "删除失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(getActivity(), "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
    }

    private void changeEditMode() {
        if (mCurrentType == TYPE_EDIT) {
            mTvEditComplete.setText(mEdit);
            mBottomLayout1.setVisibility(View.VISIBLE);
            mBottomLayout2.setVisibility(View.GONE);
        } else {
            mTvEditComplete.setText(mComplete);
            mBottomLayout2.setVisibility(View.VISIBLE);
            mBottomLayout1.setVisibility(View.GONE);
        }
        mCartAdapter.setShowCheckBox(mCurrentType);
        mCurrentType = !mCurrentType;
    }
}
