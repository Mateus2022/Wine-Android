package com.ndboo.wine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ndboo.adapter.EditOrderAdapter;
import com.ndboo.base.BaseActivity;
import com.ndboo.bean.CartBean;
import com.ndboo.bean.WineDetailBean;
import com.ndboo.extra.MyLinearLayoutManager;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.SizeUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.ItemDecoration;
import com.ndboo.widget.TopBar;

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
 * 填写订单界面
 */
public class EditOrderActivity extends BaseActivity {
    private static final int PAYMENT_CASH = 0;//货到付款
    private static final int PAYMENT_ALYPAY = 1;//支付宝
    private static final String ERROR_ADDRESS_ID = "-20";
    private static final int REQUEST_ADDRESS_CODE = 1;
    //头部
    @BindView(R.id.editorder_header)
    TopBar mTopBar;
    //收货信息
    @BindView(R.id.editorder_delivery_info)
    TextView mDeliveryInfoTextView;
    //收货地址
    @BindView(R.id.editorder_delivery_address)
    TextView mDeliveryAddressTextView;
    //    //配送时间
//    @BindView(R.id.editorder_delivery_time)
//    TextView mDeliveryTimeTextView;
    //付款方式
    @BindView(R.id.editorder_delivery_payment)
    TextView mPayentTextView;
    //总价
    @BindView(R.id.editorder_footer_price)
    TextView mTotalPriceTextView;
    //RecyclerView
    @BindView(R.id.editorder_recyclerview)
    RecyclerView mRecyclerView;
    //订单中商品id
    private String mProductIds;
    private List<CartBean> mBeanList = new ArrayList<>();
    private EditOrderAdapter mAdapter;
    private String mAddressId = ERROR_ADDRESS_ID;
    //当前支付方式
    private int mCurrentPayment = PAYMENT_ALYPAY;
    private String[] payWay = new String[]{"货到付款", "支付宝"};
    private WineDetailBean mWineDetailBean;

    @OnClick({R.id.editorder_footer_submit, R.id.editorder_delivery,
            R.id.editorder_delivery_payment})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.editorder_footer_submit:
                submitOrder();
                break;
            case R.id.editorder_delivery:
                //选择收货地址
                chooseDeliveryAddress();
                break;
            case R.id.editorder_delivery_payment:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("请选择付款方式")
                        .setItems(payWay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPayentTextView.setText(payWay[which]);
                                switch (which) {
                                    case 0:
                                        mCurrentPayment = PAYMENT_CASH;
                                        break;
                                    case 1:
                                        mCurrentPayment = PAYMENT_ALYPAY;
                                        break;
                                }
                            }
                        });
                builder.create().show();
                //选择支付方式
                break;

        }
    }

    /**
     * 选择收货地址
     */
    private void chooseDeliveryAddress() {
        startActivityForResult(new Intent(this, AddressActivity.class), REQUEST_ADDRESS_CODE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_order;
    }


    @Override
    public void init() {
        initView();
        int type = getIntent().getIntExtra("type", 0);
        if (type == 2) {
            mWineDetailBean = (WineDetailBean) getIntent().getSerializableExtra("wine");
            CartBean cartBean = new CartBean("1", mWineDetailBean.getProductPrice(),
                    mWineDetailBean.getProductPrice(), mWineDetailBean.getProductName(),
                    mWineDetailBean.getProductId(), mWineDetailBean.getPricturePath());
            mBeanList.add(cartBean);
            mTotalPriceTextView.setText(mWineDetailBean.getProductPrice() + "元");
            mAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            mProductIds = getIntent().getStringExtra("productIds");
            getData();
        }
    }


    private void getData() {
        Subscription subscription = RetrofitHelper.getApi()
                .submitOrder(SharedPreferencesUtil.getUserId(this), mProductIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        Log.e("ndb", "result:" + string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            //总价
                            String totalMoney = jsonObject.optString("orderTotal", "0.00");
                            //商品信息
                            JSONArray jsonArray = jsonObject.getJSONArray("productInformation");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cartObject = jsonArray.getJSONObject(i);
                                CartBean cartBean = new Gson().fromJson(cartObject.toString(), CartBean.class);
                                mBeanList.add(cartBean);
                            }
                            mTotalPriceTextView.setText(totalMoney + "元");
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(EditOrderActivity.this, "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
        addSubscription(subscription);
    }

    private void submitOrder() {
        if (mAddressId.equals(ERROR_ADDRESS_ID)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("请选择收货地址")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(EditOrderActivity.this, AddressActivity.class), REQUEST_ADDRESS_CODE);
                        }
                    });
            builder.create().show();
            return;
        }
        Subscription subscription = RetrofitHelper.getApi()
                .ensureOrder(SharedPreferencesUtil.getUserId(this),
                        mProductIds, mAddressId, "" + mCurrentPayment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            //总价
                            String result = jsonObject.optString("result", "");
                            if (result.equals("")) {
                                ToastUtil.showToast(EditOrderActivity.this, "提交订单失败,请稍后再试");
                                return;
                            }
                            String orderId = jsonObject.optString("orderId", "");
                            Intent intent = new Intent(EditOrderActivity.this, OrderDetailActivity.class);
                            intent.putExtra("orderId", orderId);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(EditOrderActivity.this, "网络错误");

                    }
                });
        addSubscription(subscription);
    }

    private void initView() {
        mAdapter = new EditOrderAdapter(this, mBeanList);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //分割线
        ItemDecoration itemDecoration = new ItemDecoration(this,
                LinearLayoutManager.VERTICAL,
                (int) SizeUtil.dp2px(1, this),
                Color.LTGRAY);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mAdapter);

        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 14) {
            if (data != null) {

                String addressName = data.getStringExtra("addressName");
                String addressPhone = data.getStringExtra("addressPhone");
                String addressArea = data.getStringExtra("addressArea");
                mDeliveryInfoTextView.setText("收货人："+addressName+"  "+addressPhone);
                mDeliveryAddressTextView.setText(addressArea);
                mAddressId = data.getStringExtra("addressId");

            }else {
                mDeliveryInfoTextView.setText("请添加收货地址");
                mDeliveryAddressTextView.setText("");
                mAddressId=ERROR_ADDRESS_ID;
            }
        }
        if (resultCode==15) {
            if (data != null) {
                String addressId = data.getStringExtra("addressId");
                if (addressId.equals(mAddressId)) {
                    mDeliveryInfoTextView.setText("请添加收货地址");
                    mDeliveryAddressTextView.setText("");
                    mAddressId=ERROR_ADDRESS_ID;
                }
            }
        }
    }
}
