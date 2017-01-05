package com.ndboo.wine;

import com.ndboo.base.BaseActivity;
import com.ndboo.widget.TopBar;

import butterknife.BindView;

/**
 * 个人资料页面
 */
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.userinfo_topbar)
    TopBar mTopBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
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
