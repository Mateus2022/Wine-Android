package com.ndboo.wine;

import android.view.View;
import android.widget.EditText;

import com.ndboo.base.BaseActivity;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.TopBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 建议意见页面
 */
public class SuggestionActivity extends BaseActivity {
    @BindView(R.id.suggestion_topbar)
    TopBar mTopBar;
    @BindView(R.id.suggestion_message)
    EditText mEditText;

    @OnClick({R.id.suggestion_send})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_submit:
                submitSuggestion();
                break;
        }
    }

    /**
     * 提交
     */
    private void submitSuggestion() {
        String message = mEditText.getText().toString();
        if (message == null | message.equals("")) {
            ToastUtil.showToast(this, "请填写建议");
            return;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_suggestion;
    }

    @Override
    public void init() {
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
}
