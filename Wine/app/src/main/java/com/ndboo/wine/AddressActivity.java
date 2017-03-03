package com.ndboo.wine;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

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
    private boolean mIsClickable=true;
    private AddressAdapter mAddressAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    public void init() {
        if (getIntent().getExtras() != null) {
            mIsClickable=getIntent().getExtras().getBoolean("isClickable");
        }
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
        if (mIsClickable) {
            mLvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AddressBean addressBean = (AddressBean) parent.getItemAtPosition(position);
                    Intent intent = new Intent();
                    intent.putExtra("addressId", addressBean.getAddressId());
                    setResult(14, intent);
                    finish();
                }
            });
        }
        getAddressList();

    }

    private void getAddressList() {
        Subscription subscription = RetrofitHelper.getApi()
                .queryAddress(mUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AddressBean>>() {
                    @Override
                    public void call(final List<AddressBean> addressBeen) {
                        mAddressAdapter.setAddressBeen(addressBeen);
                        mAddressAdapter.setEditDelete(new EditDelete() {
                            @Override
                            public void edit(int pos) {
                                AddressBean addressBean = addressBeen.get(pos);
                                String addressId = addressBean.getAddressId();
                                String addressName = addressBean.getAddresseeName();
                                String addressPhone = addressBean.getAddresseePhone();
                                String addressDetail = addressBean.getDetailAddress();
                                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                                intent.putExtra("addressId", addressId);
                                intent.putExtra("addressName", addressName);
                                intent.putExtra("addressPhone", addressPhone);
                                intent.putExtra("addressDetail", addressDetail);
                                startActivity(intent);
                            }

                            @Override
                            public void delete(final int pos) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this)
                                        .setTitle("温馨提示")
                                        .setMessage("是否删除地址")
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                AddressBean addressBean = addressBeen.get(pos);
                                                final String addressId = addressBean.getAddressId();
                                                Subscription subscription1 = RetrofitHelper.getApi()
                                                        .deleteAddress(addressId)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(new Action1<String>() {
                                                            @Override
                                                            public void call(String s) {
                                                                try {
                                                                    JSONObject object = new JSONObject(s);
                                                                    if (object.getString("result").equals("success")) {
                                                                        Intent intent=new Intent();
                                                                        intent.putExtra("addressId",addressId);
                                                                        setResult(15,intent);
                                                                        getAddressList();
                                                                    }else {
                                                                        ToastUtil.showToast(AddressActivity.this,"删除失败");
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    ToastUtil.showToast(AddressActivity.this,"网络连接错误");
                                                                }
                                                            }
                                                        }, new Action1<Throwable>() {
                                                            @Override
                                                            public void call(Throwable throwable) {
                                                                Log.e("tag", throwable.getMessage());
                                                            }
                                                        });
                                                addSubscription(subscription1);
                                            }
                                        });
                                builder.create().show();

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
        void edit(int position);

        void delete(int position);
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
            final int pos = position;
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
                        mEditDelete.edit(pos);
                    }
                });
                addressViewHolder.mItemDeliveryDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditDelete.delete(pos);
                    }
                });
            }
            return convertView;
        }


    }
}
