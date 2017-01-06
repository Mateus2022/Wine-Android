package com.ndboo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ndboo.adapter.OrderAdapter;
import com.ndboo.bean.MyOrderFirstBean;
import com.ndboo.bean.MyOrderSecondBean;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ZhangHang on 2016/9/21.
 */

public class OrderFragment extends Fragment {
    protected static final int TYPE_LOAD = 0;//加载
    protected static final int TYPE_REFRESH = 1;//刷新

    private View mView;

    //刷新
    private SwipeRefreshLayout mRefreshLayout;

    //列表
    private ListView mListView;
    private OrderAdapter mAdapter;
    //订单的集合
    private List<MyOrderFirstBean> mFirstBeanList = new ArrayList<>();
    //每个订单中商品的集合
    private Map<Integer, List<MyOrderSecondBean>> mSecondBeanList = new HashMap<>();

    //加载类型
    private int mCurrentType = TYPE_REFRESH;
    //当前页数
    private int mCurrentPage = 1;
    //是否正在加载数据
    private boolean mIsLoading = true;

    //当前Fragment是哪个分类
    private int mCurrentSort;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (mCurrentType == TYPE_REFRESH) {
                    mFirstBeanList.clear();
                    mSecondBeanList.clear();
                }
                MyOrderFirstBean firstBean = new MyOrderFirstBean("111", "2017-01-05", "10", "23.6", "1");
                Map<Integer, List<MyOrderSecondBean>> secondList = new HashMap<>();
                List<MyOrderSecondBean> secondBeanList = new ArrayList<>();
                MyOrderSecondBean secondBean = new MyOrderSecondBean("", "aaaa", "10", "10.0", "袋", "2222");
                secondBeanList.add(secondBean);
                secondList.put(new Integer(mFirstBeanList.size()), secondBeanList);
                mFirstBeanList.add(firstBean);
                mSecondBeanList.putAll(secondList);
                mAdapter.notifyDataSetChanged();
                mIsLoading = false;
                mRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mCurrentSort = getArguments().getInt("type");
        mView = inflater.inflate(R.layout.fragment_order, null);

        initView();
        addListener();
        requestData();
        return mView;
    }

    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.order_swipe);
        mListView = (ListView) mView.findViewById(R.id.order_listview);
        mAdapter = new OrderAdapter(getActivity(), mSecondBeanList, mFirstBeanList);
        mListView.setAdapter(mAdapter);

        //设置刷新圆圈的颜色
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void addListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //屏蔽ListView的Header和Footer
//                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
//                intent.putExtra("orderId", mFirstBeanList.get(i).getOrderId());
//                startActivity(intent);
            }
        });

        //判断ListView是否滑到最后
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastVisibleItemIndex;//当前ListView中最后一个可见的Item的索引

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                //当ListView不再滚动，并且最后一项的索引等于项数减一时，表示已经加载到最后一个
                if (scrollState == SCROLL_STATE_IDLE &&
                        lastVisibleItemIndex == mAdapter.getCount() - 1) {
                    //判断最后一个是否完全显示(参数是可见的Item中的index)
                    View lastVisibleItem = mListView.getChildAt(
                            lastVisibleItemIndex - mListView.getFirstVisiblePosition());
                    if (lastVisibleItem.getBottom() <= mListView.getBottom()) {
                        if (!mIsLoading) {
                            //加载
                            mCurrentType = TYPE_LOAD;
                            //页数增加
                            mCurrentPage++;
                            //获取数据
                            requestData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                lastVisibleItemIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                mCurrentType = TYPE_REFRESH;
                //页数重置为1
                mCurrentPage = 1;
                //获取数据
                requestData();
            }
        });
    }

    private void requestData() {
        mIsLoading = true;
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }
}
