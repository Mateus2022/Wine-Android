package com.ndboo.wine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ndboo.base.BaseActivity;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.TopBar;

import butterknife.BindView;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {
    private static final int WHAT_PAY_ALIPAY = 1;//支付宝支付
    private static final int WHAT_PAY_WECHAT = 2;//微信支付

    protected static final int WHAT_PAY_SUCCESS = 0;//支付成功
    protected static final int WHAT_PAY_FAILED = 1;//支付失败
    protected static final int WHAT_PAY_NOTSURE = 2;//支付结果待确认

    @BindView(R.id.pay_header)
    TopBar mTopBar;

    //付款金额
    @BindView(R.id.pay_money)
    TextView mMoneyTextView;

    //订单编号、价格
    private String mOrderId = "";
    private String mOrderPrice = "";

    @OnClick({R.id.pay_alipay, R.id.pay_wechat})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.pay_alipay:
                doAlipay();
                break;
            case R.id.pay_wechat:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == WHAT_PAY_SUCCESS) {
                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (what == WHAT_PAY_FAILED) {
                Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            } else if (what == WHAT_PAY_NOTSURE) {
                Toast.makeText(PayActivity.this, "支付结果待确认,请稍后查看", Toast.LENGTH_SHORT).show();
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
        if (mOrderId == null || mOrderId.equals("")) {
            ToastUtil.showToast(this, "订单错误，请重试");
            finish();
            return;
        }
    }
}
