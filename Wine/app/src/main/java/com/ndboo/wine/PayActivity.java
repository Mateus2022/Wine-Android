package com.ndboo.wine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ndboo.base.BaseActivity;
import com.ndboo.extra.PayResult;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.TopBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PayActivity extends BaseActivity {

    @BindView(R.id.pay_header)
    TopBar mTopBar;

    //付款金额
    @BindView(R.id.pay_money)
    TextView mMoneyTextView;

    //订单编号、价格
    private String mOrderId = "";
    private String mOrderPrice = "";

    @OnClick({R.id.pay_alipay})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.pay_alipay:
                doAlipay();
                break;

        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = (String) msg.obj;
            if (TextUtils.equals(message, "9000")) {
                //订单支付成功
                ToastUtil.showToast(PayActivity.this, "支付成功");
                finish();
            } else if (TextUtils.equals(message, "8000")) {
                // 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                ToastUtil.showToast(PayActivity.this, "支付结果待确认,请稍后查询订单");
                finish();
            } else if (TextUtils.equals(message, "4000")) {
                // 订单支付失败
                ToastUtil.showToast(PayActivity.this, "支付失败");
            } else if (TextUtils.equals(message, "5000")) {
                //重复请求
                ToastUtil.showToast(PayActivity.this, "请勿重复请求");
            } else if (TextUtils.equals(message, "6001")) {
                //用户中途取消
                ToastUtil.showToast(PayActivity.this, "已取消支付");
            } else if (TextUtils.equals(message, "6002")) {
                //网络连接出错
                ToastUtil.showToast(PayActivity.this, "网络连接出错");
            } else if (TextUtils.equals(message, "6004")) {
                //支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                ToastUtil.showToast(PayActivity.this, "支付结果待确认,请稍后查询订单");
            } else {
                //其他支付错误
                ToastUtil.showToast(PayActivity.this, "支付错误");
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        mOrderId = intent.getStringExtra("orderId");
        mOrderPrice = intent.getStringExtra("orderPrice");
        mMoneyTextView.setText("¥" + mOrderPrice + "元");
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
                                    PayTask payTask = new PayTask(PayActivity.this);
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
                        ToastUtil.showToast(PayActivity.this, "error:" + throwable.getMessage());
                        Log.e("ndb", "error:" + throwable.getMessage());
                    }
                });
    }

}
