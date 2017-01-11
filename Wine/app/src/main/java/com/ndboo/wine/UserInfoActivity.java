package com.ndboo.wine;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ndboo.base.BaseActivity;
import com.ndboo.bean.UserInfoBean;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.TopBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 个人资料页面
 */
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.userinfo_topbar)
    TopBar mTopBar;
    @BindView(R.id.userinfo_nickname)
    EditText mNickNameEditText;
    @BindView(R.id.userinfo_birthday)
    TextView mBirthdayTextView;
    @BindView(R.id.userinfo_sex)
    RadioGroup mSexRadioGroup;

    //生日
    private String mBirthday = "1995-01-06";
    //时间选择弹框
    private DatePickerDialog mDatePickerDialog;
    private String mHumanId;
    private ProgressDialog mProgressDialog;

    @OnClick({R.id.userinfo_birthday, R.id.userinfo_submit})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_birthday:
                showDatePickerDialog();
                break;
            case R.id.userinfo_submit:
                String sex = "";
                for (int i = 0; i < mSexRadioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) mSexRadioGroup.getChildAt(i);
                    if (radioButton.isChecked()) {
                        sex = radioButton.getText().toString();
                    }
                }
                modifyUserInfo(mNickNameEditText.getText().toString(), mBirthdayTextView.getText().toString()
                        , sex);
                break;
        }
    }


    /**
     * 生日选择弹框
     */
    private void showDatePickerDialog() {

        //将生日字符串拆分成年月日
        String[] dates = mBirthday.split("-");
        mDatePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        String month = (monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : ("" + (monthOfYear + 1));
                        String day = (dayOfMonth < 10) ? ("0" + dayOfMonth) : ("" + dayOfMonth);
                        mBirthday = year + "-" + month + "-" + day;
                        mBirthdayTextView.setText(mBirthday);
                    }
                },
                Integer.parseInt(dates[0]),
                Integer.parseInt(dates[1]) - 1,
                Integer.parseInt(dates[2]));
        mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        mDatePickerDialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void init() {
        mHumanId = SharedPreferencesUtil.getUserId(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("修改信息");
        getUserInfo();
        addListener();
    }
    private void addListener() {
        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
    }

    private void modifyUserInfo(String nickName, String birthday, String sex) {



        mProgressDialog.show();
        Subscription subscription = RetrofitHelper.getApi()
                .modifyUserInfo(mHumanId, nickName, birthday, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mProgressDialog.cancel();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String result = jsonObject.getString("result");
                            if (result.equals("success")) {
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mProgressDialog.cancel();
                        ToastUtil.showToast(UserInfoActivity.this, "网络错误");
                    }
                });

        addSubscription(subscription);

    }

    private void getUserInfo() {


        Subscription subscription = RetrofitHelper.getApi()
                .getUserInfo(mHumanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfoBean>() {
                    @Override
                    public void call(UserInfoBean userInfoBean) {
                        mNickNameEditText.setText(userInfoBean.getMemberNickname());
                        mBirthdayTextView.setText(userInfoBean.getMemberBirthday());
                        mBirthday=userInfoBean.getMemberBirthday();
                        if (userInfoBean.getMemberSex().equals("男")) {
                            RadioButton radioButton = (RadioButton) mSexRadioGroup.getChildAt(0);
                            radioButton.setChecked(true);
                        } else if (userInfoBean.getMemberSex().equals("女")) {
                            RadioButton radioButton = (RadioButton) mSexRadioGroup.getChildAt(1);
                            radioButton.setChecked(true);
                        } else {
                            RadioButton radioButton = (RadioButton) mSexRadioGroup.getChildAt(2);
                            radioButton.setChecked(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(UserInfoActivity.this, "网络连接错误");
                    }
                });

        addSubscription(subscription);
    }
}
