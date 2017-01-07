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
import android.widget.TextView;
import android.widget.Toast;

import com.ndboo.base.BaseFragment;
import com.ndboo.bean.CarWine;
import com.ndboo.interfaces.ShoppingCarOnItemClickListener;
import com.ndboo.wine.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Li on 2016/12/26.
 * “商家”界面
 */

public class ShoppingCarFragment extends BaseFragment {


    @BindView(R.id.list_view_car_wines)
    ListView mListViewCarWines;
    @BindView(R.id.tv_edit_complete)
    TextView mTvEditComplete;

    private List<CarWine> mCarWines;
    private ShoppingCarOnItemClickListener mListener;
    private CarWineAdapter mCarWineAdapter;

    private String mEdit;
    private String mComplete;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shopping_car;
    }

    @Override
    public void firstVisibleDeal() {
        super.firstVisibleDeal();
        mEdit = getResources().getString(R.string.car_edit);
        mComplete = getResources().getString(R.string.car_complete);
        mCarWines = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                mCarWines.add(new CarWine(true));
                continue;
            }
            mCarWines.add(new CarWine(false));
        }
        mCarWineAdapter = new CarWineAdapter(mCarWines);
        mListener = new ShoppingCarOnItemClickListener() {
            @Override
            public void delete(int position, View view) {
                Toast.makeText(getContext(), "delete:" + position, Toast.LENGTH_SHORT).show();
            }

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
        mListViewCarWines.setAdapter(mCarWineAdapter);
    }


    @Override
    protected void visibleDeal() {
        super.visibleDeal();
        switch ("12") {
            case "1":

                break;
        }
    }


    @OnClick(R.id.tv_edit_complete)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_complete:

                if (mTvEditComplete.getText().toString().equals(mEdit)) {
                    mTvEditComplete.setText(mComplete);
                    editCar();
                } else {
                    mTvEditComplete.setText(mEdit);
                    editCarComplete();
                }

                break;
        }
    }

    /**
     * 编辑购物车
     */
    private void editCar() {

        Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();
    }

    /**
     * 编辑完成
     */
    private void editCarComplete() {

        Toast.makeText(getActivity(), "editComplete", Toast.LENGTH_SHORT).show();
    }


    static class CarWineHolder {
        @BindView(R.id.image_delete)
        ImageView mImageDelete;
        @BindView(R.id.check_box_select)
        CheckBox mCheckBoxSelect;
        @BindView(R.id.image_goods)
        ImageView mImageGoods;
        @BindView(R.id.text_goods_description)
        TextView mTextGoodsDescription;
        @BindView(R.id.text_price)
        TextView mTextPrice;
        @BindView(R.id.image_decrease)
        ImageView mImageDecrease;
        @BindView(R.id.text_number)
        TextView mTextNumber;
        @BindView(R.id.image_add)
        ImageView mImageAdd;
        @BindView(R.id.layout_edit)
        LinearLayout mLayoutEdit;

        CarWineHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class CarWineAdapter extends BaseAdapter {

        private List<CarWine> mWines;

        public CarWineAdapter(List<CarWine> wines) {
            mWines = wines;
        }

        @Override
        public int getCount() {
            return mWines.size();
        }

        @Override
        public Object getItem(int position) {
            return mWines.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CarWine wine = mWines.get(position);
            CarWineHolder carWineHolder;
            if (convertView != null) {
                carWineHolder = (CarWineHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shopping_car, null);
                carWineHolder = new CarWineHolder(convertView);
                convertView.setTag(carWineHolder);
            }
            carWineHolder.mCheckBoxSelect.setChecked(wine.isSelected());
            if (mListener != null) {
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
                carWineHolder.mImageDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.delete(position, v);
                    }
                });
            }

            return convertView;
        }


    }


}
