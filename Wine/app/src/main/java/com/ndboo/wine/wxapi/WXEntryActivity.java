package com.ndboo.wine.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ndboo.utils.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付完成回调界面
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String APPID = "wx9d46d4b37cb95bdd";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    //支付前调用
    @Override
    public void onReq(BaseReq req) {

    }

    // 支付完成后调用
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //支付成功
                ToastUtil.showToast(WXEntryActivity.this, "支付成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //取消支付
                ToastUtil.showToast(WXEntryActivity.this, "已取消支付");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                ToastUtil.showToast(WXEntryActivity.this, "认证失败");
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                ToastUtil.showToast(WXEntryActivity.this, "支付失败");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                ToastUtil.showToast(WXEntryActivity.this, "不支持");
                break;
            default:
                break;
        }
    }
}