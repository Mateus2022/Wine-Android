package com.ndboo.wine;

import android.widget.EditText;

import com.ndboo.base.BaseActivity;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.RemindUtil;
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

public class AddAddressActivity extends BaseActivity {


    @BindView(R.id.top_bar)
    TopBar mTopBar;
    @BindView(R.id.edit_address_name)
    EditText mEditAddressName;
    @BindView(R.id.edit_address_phone)
    EditText mEditAddressPhone;
    @BindView(R.id.edit_address_detail)
    EditText mEditAddressDetail;

    private String mUserId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
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


    @OnClick(R.id.edit_address_save)
    public void onClick() {
        if (checkInput()) {
            Subscription subscription = RetrofitHelper.getApi()
                    .addAddress(mUserId, mEditAddressName.getText().toString(),
                            mEditAddressDetail.getText().toString(), mEditAddressPhone.getText().toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            try {
                                JSONObject object=new JSONObject(s);
                                if (object.getString("result").equals("success")) {
                                    finish();
                                }else {
                                    ToastUtil.showToast(AddAddressActivity.this,"添加失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            ToastUtil.showToast(AddAddressActivity.this,"网络连接错误");
                        }
                    });
            addSubscription(subscription);
        }

    }

    /**
     * 判断输入内容是否正确
     *
     * @return 文本内容是否符合要求
     */
    private boolean checkInput() {
        String name = mEditAddressName.getText().toString();
        String phone = mEditAddressPhone.getText().toString();
        String detail = mEditAddressDetail.getText().toString();


        if (name.length() < 2 || name.length() > 15) {
            RemindUtil.dialogRemind(AddAddressActivity.this, R.string.warn_contacts_name);
            return false;
        }
        if (!phone.matches(getResources().getString(R.string.phone_check))) {
            RemindUtil.dialogRemind(AddAddressActivity.this, R.string.warn_contacts_phone);
            return false;
        }

        if (detail.length() < 5 || detail.length() > 60) {
            RemindUtil.dialogRemind(AddAddressActivity.this, R.string.warn_contacts_address);
            return false;
        }
        return true;
    }
}
