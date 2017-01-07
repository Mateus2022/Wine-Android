package com.ndboo.wine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ndboo.base.BaseActivity;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.TopBar;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

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

    @OnClick({R.id.userinfo_birthday, R.id.userinfo_submit})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_birthday:
                showDatePickerDialog();
                break;
            case R.id.userinfo_submit:
                submitUserInfo();
                break;
        }
    }

    /**
     * 提交信息
     */
    private void submitUserInfo() {
        ToastUtil.showToast(this, "已保存");
    }

    /**
     * 生日选择弹框
     */
    private void showDatePickerDialog() {
        if (mBirthday == null || mBirthday.equals("")) {
            ToastUtil.showToast(this, "请刷新重试");
            return;
        }
        //将生日字符串拆分成年月日
        String[] dates = mBirthday.split("-");
        mDatePickerDialog = new DatePickerDialog(this,
                AlertDialog.THEME_HOLO_LIGHT,
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
        addListener();
        mBirthdayTextView.setText(mBirthday);
    }

    private void addListener() {
        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });
    }
}
