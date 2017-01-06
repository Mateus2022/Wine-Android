package com.ndboo.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ndboo.base.BaseFragment;
import com.ndboo.wine.R;

import butterknife.BindView;

/**
 * Created by Li on 2016/12/26.
 * “商家”界面
 */

public class ShoppingCarFragment extends BaseFragment {


    @BindView(R.id.list_view_car_wines)
    ListView mListViewCarWines;
    private BaseAdapter mBaseAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shopping_car;
    }

    @Override
    public void firstVisibleDeal() {
        super.firstVisibleDeal();
        mBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return View.inflate(getContext(),R.layout.item_car_wine,null);
            }
        };
    }

    @Override
    protected void visibleDeal() {
        super.visibleDeal();

        mListViewCarWines.setAdapter(mBaseAdapter);
    }
}
