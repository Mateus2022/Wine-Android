package com.ndboo.wine;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ndboo.base.BaseActivity;
import com.ndboo.bean.OrderDetailBean;
import com.ndboo.extra.PayResult;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.DeletePopupWindow;
import com.ndboo.widget.ImageTextTextView;
import com.ndboo.widget.OrderDetailItemView;
import com.ndboo.widget.TopBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 订单详情页面
 */
public class OrderDetailActivity extends BaseActivity {
    //支付方式
    private static final String PAYMENT_ALIPAY = "支付宝";
    private static final String PAYMENT_WECHAT = "微信";
    private static final String PAYMENT_CASH = "货到付款";
    //订单状态
    private static final String STATUS_WAIT_TO_PAY = "未付款";
    private static final String STATUS_PAIED = "已付款";
    private static final String STATUS_DELIVERYING = "派送中";
    private static final String STATUS_fINISH = "已完成";

    //确认收货
    protected static final String STATUS_CONFIRM_RECEIPT = "4";

    @BindView(R.id.orderdetail_header)
    TopBar mTopBar;
    @BindView(R.id.orderdetail_products_layout)
    LinearLayout mLinearLayout;

    //订单id、状态、支付方式、价格
    private String mOrderId;
    private String mOrderStaus;

    //支付状态
    @BindView(R.id.orderdetail_status)
    TextView mPayentStatusTextView;
    //订单号
    @BindView(R.id.orderdetail_order_data)
    TextView mOrderNumberTextView;
    //收货人
    @BindView(R.id.orderdetail_consignee)
    ImageTextTextView mConsigneeView;
    //收货地址
    @BindView(R.id.orderdetail_address)
    ImageTextTextView mAddressView;
    //支付方式
    @BindView(R.id.orderdetail_payway)
    ImageTextTextView mPayWayView;
    //下单时间
    @BindView(R.id.orderdetail_placetime)
    ImageTextTextView mPlaceTimeView;
    //订单金额
    @BindView(R.id.orderdetail_ordermoney)
    TextView mOrderMoneyTextView;

    //去支付
    @BindView(R.id.orderdetail_gotopay)
    Button mPayButton;
    private String mOrderPayWay;

    //确认收货弹框
    private DeletePopupWindow mConfirmPopupWindow;

    @OnClick({R.id.orderdetail_gotopay,
            R.id.orderdetail_service_phone})
    void doClick(View v) {
        switch (v.getId()) {
            case R.id.orderdetail_gotopay:
                if (mOrderStaus.equals(STATUS_DELIVERYING)) {
                    //确认订单弹框
                    showReceivedProduct();
                } else if (mOrderStaus.equals(STATUS_WAIT_TO_PAY)) {
                    if (mOrderPayWay.equals(PAYMENT_ALIPAY)) {
                        doAlipay();
                    } else if (mOrderPayWay.equals(PAYMENT_WECHAT)) {
                        doWeChatPay();
                    }
                }
                break;
            case R.id.orderdetail_service_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "051266155111");
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    /**
     * 处理支付宝付款
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = (String) msg.obj;
            if (TextUtils.equals(message, "9000")) {
                //订单支付成功
                ToastUtil.showToast(OrderDetailActivity.this, "支付成功");
                finish();
            } else if (TextUtils.equals(message, "8000")) {
                // 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                ToastUtil.showToast(OrderDetailActivity.this, "支付结果待确认,请稍后查询订单");
                finish();
            } else if (TextUtils.equals(message, "4000")) {
                // 订单支付失败
                ToastUtil.showToast(OrderDetailActivity.this, "支付失败");
            } else if (TextUtils.equals(message, "5000")) {
                //重复请求
                ToastUtil.showToast(OrderDetailActivity.this, "请勿重复请求");
            } else if (TextUtils.equals(message, "6001")) {
                //用户中途取消
                ToastUtil.showToast(OrderDetailActivity.this, "已取消支付");
            } else if (TextUtils.equals(message, "6002")) {
                //网络连接出错
                ToastUtil.showToast(OrderDetailActivity.this, "网络连接出错");
            } else if (TextUtils.equals(message, "6004")) {
                //支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                ToastUtil.showToast(OrderDetailActivity.this, "支付结果待确认,请稍后查询订单");
            } else {
                //其他支付错误
                ToastUtil.showToast(OrderDetailActivity.this, "支付错误");
            }
        }
    };

    /**
     * 微信支付
     */
    private void doWeChatPay() {

    }

    /**
     * 支付宝支付
     */
    private void doAlipay() {
        Subscription subscription = RetrofitHelper.getApi()
                .doAlipay(SharedPreferencesUtil.getUserId(this), mOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        Log.e("ndb", "result:" + string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            final String payURL = jsonObject.optString("payURL");
                            //调起支付
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask payTask = new PayTask(OrderDetailActivity.this);
                                    String result = payTask.pay(payURL, true);
                                    PayResult payResult = new PayResult(result);
                                    //同步返回的结果必须放置到服务端进行验证
                                    String resultStatus = payResult.getResultStatus();
                                    Message message = new Message();
                                    message.obj = resultStatus;
                                    mHandler.sendMessage(message);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(OrderDetailActivity.this, "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 确认订单
     */
    private void showReceivedProduct() {
        if (mConfirmPopupWindow == null) {
            mConfirmPopupWindow = new DeletePopupWindow(this);
            mConfirmPopupWindow.setMessageText("确认已收到商品吗？");
            mConfirmPopupWindow.setOnPopupWindowClickListener(new DeletePopupWindow.OnPopupWindowClickListener() {
                @Override
                public void cancleClicked(View view) {
                }

                @Override
                public void ensureClicked(View view) {
                    Subscription subscription = RetrofitHelper.getApi()
                            .confirmReceipt(mOrderId,
                                    SharedPreferencesUtil.getUserId(OrderDetailActivity.this),
                                    STATUS_CONFIRM_RECEIPT)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String string) {
                                    Log.e("tag", "result:" + string);
                                    try {
                                        JSONObject jsonObject = new JSONObject(string);
                                        String value = jsonObject.optString("result", "");
                                        if (value.equals("success")) {
                                            getOrderDetail();
                                        } else {
                                            ToastUtil.showToast(OrderDetailActivity.this, "确认收货失败，请刷新重试");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    ToastUtil.showToast(OrderDetailActivity.this, "error:" + throwable.getMessage());
                                    Log.e("ndb", "error:" + throwable.getMessage());
                                }
                            });
                    addSubscription(subscription);
                }
            });
        }
        mConfirmPopupWindow.showAtLocation(mTopBar, Gravity.CENTER, 0, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void init() {
        mOrderId = getIntent().getStringExtra("orderId");

        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderDetail();
    }

    /**
     * 获取订单信息
     */
    private void getOrderDetail() {
        Subscription subscription = RetrofitHelper.getApi()
                .getOrderDetail(SharedPreferencesUtil.getUserId(this), mOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        Log.e("tag", "result:" + string);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            //总价
                            JSONArray jsonArray = jsonObject.optJSONArray("productInformation");
                            mLinearLayout.removeAllViews();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.optJSONObject(i);

                                String productCount = object.optString("productCount", "");
                                String productName = object.optString("productName", "");
                                String productPrice = object.optString("productPrice", "");
                                String productPicture = object.optString("productPicture", "");
                                String productMoney = object.optString("productMoney", "");

                                OrderDetailBean orderDetailBean = new OrderDetailBean(productPicture, productName,
                                        productCount, productPrice, productMoney);
                                OrderDetailItemView itemView = new OrderDetailItemView(OrderDetailActivity.this);
                                itemView.setData(orderDetailBean);
                                mLinearLayout.addView(itemView);
                            }

                            String orderTotal = jsonObject.optString("orderTotal", "");
                            String orderTime = jsonObject.optString("orderTime", "");
                            String orderNum = jsonObject.optString("orderNum", "");
                            String orderStatus = jsonObject.optString("orderStatus", "");
                            mOrderPayWay = jsonObject.optString("orderPayWay", "");
                            String consignee = jsonObject.optString("consignee", "");
                            String addressDetail = jsonObject.optString("addressDetail", "");
                            String area = jsonObject.optString("area", "");

                            mOrderStaus = orderStatus;
                            mPayentStatusTextView.setText(mOrderStaus);
                            mOrderMoneyTextView.setText("¥" + orderTotal);
                            mOrderNumberTextView.setText(orderNum);
                            mConsigneeView.setDataString(consignee);
                            mAddressView.setDataString(area + addressDetail);
                            mPayentStatusTextView.setText(orderStatus);
                            mPlaceTimeView.setDataString(orderTime);
                            mPayWayView.setDataString(mOrderPayWay);

                            if (mOrderPayWay.equals(PAYMENT_CASH)) {
                                mPayButton.setText(PAYMENT_CASH);
                            } else {
                                if (mOrderStaus.equals(STATUS_WAIT_TO_PAY)) {
                                    mPayButton.setText("去支付");
                                } else if (mOrderStaus.equals(STATUS_DELIVERYING)) {
                                    mPayButton.setText("确认收货");
                                } else {
                                    mPayButton.setText(mOrderStaus);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(OrderDetailActivity.this, "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
        addSubscription(subscription);
    }
}
