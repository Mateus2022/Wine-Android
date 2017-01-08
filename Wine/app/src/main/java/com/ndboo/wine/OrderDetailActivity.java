package com.ndboo.wine;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ndboo.adapter.OrderDetailAdapter;
import com.ndboo.base.BaseActivity;
import com.ndboo.bean.OrderDetailBean;
import com.ndboo.extra.MyLinearLayoutManager;
import com.ndboo.widget.ImageTextTextView;
import com.ndboo.widget.ItemDecoration;
import com.ndboo.widget.LoadingDialog;
import com.ndboo.widget.TopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单详情页面
 */
public class OrderDetailActivity extends BaseActivity {
    private static final int WHAT_ORDER_DETAIL = 1;//获取订单信息
    private static final int WHAT_CANCLE_ORDER = 2;//取消订单

    @BindView(R.id.orderdetail_header)
    TopBar mTopBar;

    @BindView(R.id.orderdetail_recyclerview)
    RecyclerView mRecyclerView;
    private OrderDetailAdapter mAdapter;
    private List<OrderDetailBean> mList = new ArrayList<>();

    //订单id、价格、状态
    private String mOrderId;
    private String mOrderPrice;
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
    //配送时间
    @BindView(R.id.orderdetail_sendtime)
    ImageTextTextView mSendTimeView;
    //订单金额
    @BindView(R.id.orderdetail_ordermoney)
    TextView mOrderMoneyTextView;

    //加载框
    private LoadingDialog mLoadingDialog;
    //取消订单
    @BindView(R.id.orderdetail_cancelorder)
    Button mCancleButton;
    //去支付
    @BindView(R.id.orderdetail_gotopay)
    Button mPayButton;

    @OnClick({R.id.orderdetail_gotopay, R.id.orderdetail_cancelorder,
            R.id.orderdetail_service_phone})
    void doClick(View v) {
        switch (v.getId()) {
            case R.id.orderdetail_gotopay:
//                if (mOrderStaus.equals("未付款")) {
                    Intent payIntent = new Intent(this, PayActivity.class);
                    payIntent.putExtra("orderId", mOrderId);
                    payIntent.putExtra("orderPrice", mOrderPrice);
                    startActivity(payIntent);
//                }
                break;
            case R.id.orderdetail_cancelorder:
                cancleOrder();
                break;
            case R.id.orderdetail_service_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "051266155111");
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void init() {
        mOrderId = getIntent().getStringExtra("orderId");
        mLoadingDialog = new LoadingDialog(this);

        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });

        initRecyclerView();
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
    }

    /**
     * 取消订单
     */
    public void cancleOrder() {
    }

    private void initRecyclerView() {
        mList.add(new OrderDetailBean("", "aaaaaaaaa", "10", "瓶", "20", "90"));

        RecyclerView.ItemDecoration itemDecoration = new ItemDecoration(this,
                LinearLayoutManager.VERTICAL,
                1, Color.parseColor("#d3d3d3"));
        mRecyclerView.addItemDecoration(itemDecoration);
        MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new OrderDetailAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
