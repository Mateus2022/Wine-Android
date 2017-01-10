package com.ndboo.wine;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ndboo.base.BaseActivity;
import com.ndboo.bean.AddressBean;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.TopBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddressActivity extends BaseActivity {


    @BindView(R.id.top_bar)
    TopBar mTopBar;
    @BindView(R.id.tv_add_address)
    TextView mTvAddAddress;
    @BindView(R.id.lv_address)
    ListView mLvAddress;

    private String mUserId;
    private AddressAdapter mAddressAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    public void init() {
        mUserId = SharedPreferencesUtil.getUserId(getApplicationContext());
        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });

    }


    @OnClick(R.id.tv_add_address)
    public void onClick() {
        startActivity(new Intent(this, AddAddressActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddressAdapter = new AddressAdapter();
        mLvAddress.setAdapter(mAddressAdapter);
        mLvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressBean addressBean = (AddressBean) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("addressId", addressBean.getAddressId());
                intent.putExtra("addressName", addressBean.getAddresseeName());
                intent.putExtra("addressPhone", addressBean.getAddresseePhone());
                intent.putExtra("addressArea", addressBean.getAddresseeArea() +
                        addressBean.getDetailAddress());

                setResult(14, intent);
                finish();
            }
        });
        Subscription subscription = RetrofitHelper.getApi()
                .queryAddress(mUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AddressBean>>() {
                    @Override
                    public void call(List<AddressBean> addressBeen) {
                        Log.e("tag", addressBeen.size() + "");
                        mAddressAdapter.setAddressBeen(addressBeen);
                        mAddressAdapter.setEditDelete(new EditDelete() {
                            @Override
                            public void edit() {
                                ToastUtil.showToast(AddressActivity.this,"edit");
                            }

                            @Override
                            public void delete() {
                                ToastUtil.showToast(AddressActivity.this,"delete");
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("tag", throwable.getMessage());
                    }
                });

        addSubscription(subscription);
    }

    public interface EditDelete {
        void edit();

        void delete();
    }

    static class AddressViewHolder {
        @BindView(R.id.item_delivery_name)
        TextView mItemDeliveryName;
        @BindView(R.id.item_delivery_phone)
        TextView mItemDeliveryPhone;
        @BindView(R.id.item_delivery_address)
        TextView mItemDeliveryAddress;
        @BindView(R.id.item_delivery_delete)
        TextView mItemDeliveryDelete;
        @BindView(R.id.item_delivery_edit)
        TextView mItemDeliveryEdit;

        AddressViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class AddressAdapter extends BaseAdapter {

        private List<AddressBean> mAddressBeen;
        private EditDelete mEditDelete;

        public void setEditDelete(EditDelete editDelete) {
            mEditDelete = editDelete;
        }

        public void setAddressBeen(List<AddressBean> addressBeen) {
            mAddressBeen = addressBeen;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mAddressBeen == null ? 0 : mAddressBeen.size();
        }

        @Override
        public Object getItem(int position) {
            return mAddressBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(mAddressBeen.get(position).getAddressId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AddressViewHolder addressViewHolder;
            convertView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_address, null);
            addressViewHolder = new AddressViewHolder(convertView);
            AddressBean addressBean = mAddressBeen.get(position);
            addressViewHolder.mItemDeliveryName.setText(addressBean.getAddresseeName());
            addressViewHolder.mItemDeliveryPhone.setText(addressBean.getAddresseePhone());
            addressViewHolder.mItemDeliveryAddress.setText(
                    addressBean.getAddresseeArea() + addressBean.getDetailAddress()
            );
            if (mEditDelete != null) {
                addressViewHolder.mItemDeliveryEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditDelete.edit();
                    }
                });
                addressViewHolder.mItemDeliveryDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditDelete.delete();
                    }
                });
            }
            return convertView;
        }


    }
}
