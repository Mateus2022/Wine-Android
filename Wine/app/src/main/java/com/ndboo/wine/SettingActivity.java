package com.ndboo.wine;

import com.ndboo.base.BaseActivity;
import com.ndboo.widget.TopBar;

import butterknife.BindView;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.setting_topbar)
    TopBar mTopBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
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
