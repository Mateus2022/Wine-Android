package com.ndboo.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ndboo.base.BaseFragment;
import com.ndboo.bean.UserInfoBean;
import com.ndboo.net.RetrofitHelper;
import com.ndboo.utils.SharedPreferencesUtil;
import com.ndboo.widget.CircleImageView;
import com.ndboo.widget.ImgTextView;
import com.ndboo.widget.PortraitPopupWindow;
import com.ndboo.wine.AboutUsActivity;
import com.ndboo.wine.AddressActivity;
import com.ndboo.wine.LoginActivity;
import com.ndboo.wine.OrderListActivity;
import com.ndboo.wine.R;
import com.ndboo.wine.RegisterActivity;
import com.ndboo.wine.SettingActivity;
import com.ndboo.wine.UserInfoActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Li on 2016/12/26.
 * “我的”界面
 */

public class MineFragment extends BaseFragment {
    protected static final int CHOOSE_PICTURE = 0;//选择本地图片
    protected static final int TAKE_PICTURE = 1;//照相
    private static final int CROP_SMALL_PICTURE = 2;//裁剪
    protected static Uri tempUri;

    @BindView(R.id.mine_portrait)
    CircleImageView mPortraitImageView;//头像
    @BindView(R.id.mine_nickname)
    TextView mNickNameTextView;//昵称

    @BindView(R.id.mine_order)
    ImgTextView mOrderImgTextView;//我的订单
    @BindView(R.id.mine_address)
    ImgTextView mMineAddress;//收货地址
    @BindView(R.id.mine_service)
    ImgTextView mServiceImgTextView;//联系客服
    @BindView(R.id.mine_aboutus)
    ImgTextView mAboutUsImgTextView;//关于我们
    //    @BindView(R.id.mine_suggestion)
//    ImgTextView mSuggestionImgTextView;//意见反馈
    @BindView(R.id.mine_setting)
    ImgTextView mSettingImgTextView;//设置
    @BindView(R.id.layout_user_not_exist)
    LinearLayout mLayoutUserNotExist;
    @BindView(R.id.layout_user_exist)
    LinearLayout mLayoutUserExist;
    @BindView(R.id.mine_main)
    View mMineView;
    //修改头像
    private PortraitPopupWindow mPortraitPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    private void showChangePicDialog() {
        if (mPortraitPopupWindow == null) {
            mPortraitPopupWindow = new PortraitPopupWindow(getActivity());
            mPortraitPopupWindow.setOnPopClickListener(new PortraitPopupWindow.OnPopClickListener() {
                @Override
                public void onTakePicClicked() {
                    // 拍照
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                            new File(getActivity().getExternalCacheDir(), "image.jpg")));
//                    tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
//                            "image.jpg"));
//                    // 指定照片保存路径（SD卡），image.jpg为一个临时文件，
//                    // 每次拍照后这个图片都会被替换
//                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                }

                @Override
                public void onChoosePicClicked() {
                    // 选择本地照片

                    Intent intentFromLocal = new Intent(Intent.ACTION_PICK);
                    intentFromLocal.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intentFromLocal, CHOOSE_PICTURE);
//                    Intent openAlbumIntent = new Intent(Intent.ACTION_PICK,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    openAlbumIntent.setType("image/*");
//                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                }
            });
        }
        mPortraitPopupWindow.showAtLocation(mMineView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    File tmpFile = new File(getActivity().getExternalCacheDir(), "image.jpg");
                    // 开始对图片进行裁剪处理
                    startPhotoZoom(Uri.fromFile(tmpFile));
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);

    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            //将Bitmap转换成字节流
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            try {

                byte[] buffer = byteArrayOutputStream.toByteArray();
                //将图片的字节流数据加密成base64字符输出
                String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
                uploadLogo(photo);
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadLogo(String photo) {
        Subscription subscription = RetrofitHelper.getApi()
                .modifyUserHead(SharedPreferencesUtil.getUserId(getContext()), photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        showUserInfo();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("tag", throwable.getMessage());
                    }
                });
        addSubscription(subscription);
    }


    /**
     * 弹框是否已经弹出
     */
    public boolean isShow() {
        if (mPortraitPopupWindow == null) {
            return false;
        } else {
            return mPortraitPopupWindow.isShowing();
        }
    }

    /**
     * 关闭PopupWindow
     */
    public void closePop() {
        mPortraitPopupWindow.dismiss();
    }


    public void showHeadByUserIsExist() {
        if (SharedPreferencesUtil.isUserLoginIn(getContext())) {
            mLayoutUserExist.setVisibility(View.VISIBLE);
            mLayoutUserNotExist.setVisibility(View.GONE);
            showUserInfo();
        } else {
            mLayoutUserExist.setVisibility(View.GONE);
            mLayoutUserNotExist.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.btn_login, R.id.btn_register, R.id.mine_portrait, R.id.mine_nickname, R.id.mine_order,
            R.id.mine_service, R.id.mine_aboutus, R.id.mine_setting,
            R.id.mine_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.mine_portrait:
                //修改头像
                showChangePicDialog();
                break;
            case R.id.mine_nickname:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.mine_order:
                if (!SharedPreferencesUtil.isUserLoginIn(getContext())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), OrderListActivity.class));
                }

                break;
            case R.id.mine_service:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "051266155111");
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.mine_aboutus:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
//            case R.id.mine_suggestion:
//                startActivity(new Intent(getActivity(), SuggestionActivity.class));
//                break;
            case R.id.mine_address:
                if (!SharedPreferencesUtil.isUserLoginIn(getContext())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), AddressActivity.class));
                }

                break;
            case R.id.mine_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeadByUserIsExist();
//        showUserInfo();
    }

    /**
     * 显示用户信息
     */
    private void showUserInfo() {
//        if (!SharedPreferencesUtil.isUserLoginIn(getContext())) {
//            return;
//        }
        String humanId = SharedPreferencesUtil.getUserId(getActivity());
        Subscription subscription = RetrofitHelper.getApi()
                .getUserInfo(humanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfoBean>() {
                    @Override
                    public void call(UserInfoBean userInfoBean) {
                        String memberAccount = userInfoBean.getMemberAccount();
                        if (memberAccount.equals("null") || memberAccount.equals("") || memberAccount == null) {
                            SharedPreferencesUtil.userLogout(getContext());
                            showHeadByUserIsExist();
                            return;
                        }

                        Glide.with(getActivity())
                                .load(userInfoBean.getShopLogo())
//                                .placeholder(R.drawable.ic_tab_mine_nm)
                                .error(R.drawable.ic_tab_mine_nm)
                                .into(mPortraitImageView);
                        mNickNameTextView.setText(userInfoBean.getMemberNickname());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mNickNameTextView.setText(getResources().getText(R.string.app_name));
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onStop() {
        super.onStop();
        unSubscribe();
    }
}
