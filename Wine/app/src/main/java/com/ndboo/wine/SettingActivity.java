package com.ndboo.wine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ndboo.base.BaseActivity;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.utils.ToastUtil;
import com.ndboo.widget.TopBar;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置页面
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.setting_topbar)
    TopBar mTopBar;
    @BindView(R.id.setting_push_switch)
    Switch mSwitch;
    @BindView(R.id.setting_cache_size)
    TextView mCacheTextView;
    @BindView(R.id.setting_version_current)
    TextView mVersionTextView;
    @BindView(R.id.setting_logout)
    Button mSettingLogout;

    //保存推送开启的关闭
    private SharedPreferences mSharedPreferences;

    @OnClick({R.id.setting_push, R.id.setting_cache, R.id.setting_version,R.id.setting_logout})
    void doClick(View view) {
        switch (view.getId()) {
            case R.id.setting_push:
                mSwitch.setChecked(!mSwitch.isChecked());
                break;
            case R.id.setting_cache:
                clearAllCache();
                break;
            case R.id.setting_logout:
                SharedPreferencesUtil.userLogout(SettingActivity.this);
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.setting_version:
                ToastUtil.showToast(this, "已是最新版本");
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        if (SharedPreferencesUtil.isUserLoginIn(SettingActivity.this)) {
            mSettingLogout.setVisibility(View.VISIBLE);
        }else {
            mSettingLogout.setVisibility(View.GONE);
        }
        mSharedPreferences = getSharedPreferences("push_mode", MODE_PRIVATE);

        //是否开启推送
        boolean isEnablePush = mSharedPreferences.getBoolean("isEnablePush", true);
        mSwitch.setChecked(isEnablePush);
        //缓存大小
        mCacheTextView.setText(getTotalCacheSize());
        //当前版本
        mVersionTextView.setText("v " + getVersion());
        addListener();
    }

    private void addListener() {
        mTopBar.setOnTopBarClickListener(new TopBar.OnTopBarClickListener() {
            @Override
            public void onBackClicked() {
                finish();
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("isEnablePush", checked);
                editor.commit();
                String text = checked ? "开启" : "关闭";
                ToastUtil.showToast(SettingActivity.this, "已" + text + "推送");
            }
        });
    }

    /**
     * 获取缓存大小
     */
    public String getTotalCacheSize() {
        long cacheSize = getFolderSize(getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     */
    public void clearAllCache() {
        deleteDir(getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            boolean isSuccess = deleteDir(getExternalCacheDir());
            ToastUtil.showToast(SettingActivity.this, "清除缓存" + (isSuccess ? "成功" : "失败"));
            mCacheTextView.setText(getTotalCacheSize());
        }
    }

    /**
     * 删除缓存文件
     */
    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件大小
    public long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     */
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + " B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " T";
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
