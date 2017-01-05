package com.ndboo.wine;

import com.ndboo.base.BaseActivity;
import com.ndboo.widget.TopBar;

import butterknife.BindView;

public class CollectionActivity extends BaseActivity {
    @BindView(R.id.collection_topbar)
    TopBar mTopBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collection;
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
