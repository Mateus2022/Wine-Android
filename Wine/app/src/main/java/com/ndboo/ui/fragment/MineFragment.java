package com.ndboo.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ndboo.base.BaseFragment;
import com.ndboo.widget.CircleImageView;
import com.ndboo.widget.ImgTextView;
import com.ndboo.widget.PortraitPopupWindow;
import com.ndboo.widget.TopBar;
import com.ndboo.wine.AboutUsActivity;
import com.ndboo.wine.CollectionActivity;
import com.ndboo.wine.OrderListActivity;
import com.ndboo.wine.R;
import com.ndboo.wine.SettingActivity;
import com.ndboo.wine.SuggestionActivity;
import com.ndboo.wine.UserInfoActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Li on 2016/12/26.
 * “我的”界面
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    protected static final int CHOOSE_PICTURE = 0;//选择本地图片
    protected static final int TAKE_PICTURE = 1;//照相
    private static final int CROP_SMALL_PICTURE = 2;//裁剪
    protected static Uri tempUri;

    @BindView(R.id.mine_topbar)
    TopBar mTopBar;//头部

    @BindView(R.id.mine_portrait)
    CircleImageView mPortraitImageView;//头像
    @BindView(R.id.mine_nickname)
    TextView mNickNameTextView;//昵称

    @BindView(R.id.mine_order)
    ImgTextView mOrderImgTextView;//我的订单
    @BindView(R.id.mine_collection)
    ImgTextView mCollectionImgTextView;//我的收藏
    @BindView(R.id.mine_service)
    ImgTextView mServiceImgTextView;//联系客服
    @BindView(R.id.mine_aboutus)
    ImgTextView mAboutUsImgTextView;//关于我们
    @BindView(R.id.mine_suggestion)
    ImgTextView mSuggestionImgTextView;//意见反馈
    @BindView(R.id.mine_setting)
    ImgTextView mSettingImgTextView;//设置

    //修改头像
    private PortraitPopupWindow mPortraitPopupWindow;
    @BindView(R.id.mine_main)
    View mMineView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void showContent() {
        super.showContent();
        addListener();
    }

    private void addListener() {
        mPortraitImageView.setOnClickListener(this);
        mNickNameTextView.setOnClickListener(this);
        mOrderImgTextView.setOnClickListener(this);
        mCollectionImgTextView.setOnClickListener(this);
        mServiceImgTextView.setOnClickListener(this);
        mAboutUsImgTextView.setOnClickListener(this);
        mSuggestionImgTextView.setOnClickListener(this);
        mSettingImgTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_portrait:
                //修改头像
                showChangePicDialog();
                break;
            case R.id.mine_nickname:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.mine_order:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.mine_collection:
                startActivity(new Intent(getActivity(), CollectionActivity.class));
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
            case R.id.mine_suggestion:
                startActivity(new Intent(getActivity(), SuggestionActivity.class));
                break;
            case R.id.mine_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    private void showChangePicDialog() {
        if (mPortraitPopupWindow == null) {
            mPortraitPopupWindow = new PortraitPopupWindow(getActivity());
            mPortraitPopupWindow.setOnPopClickListener(new PortraitPopupWindow.OnPopClickListener() {
                @Override
                public void onTakePicClicked() {
                    // 拍照
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "image.jpg"));
                    // 指定照片保存路径（SD卡），image.jpg为一个临时文件，
                    // 每次拍照后这个图片都会被替换
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                }

                @Override
                public void onChoosePicClicked() {
                    // 选择本地照片
                    Intent openAlbumIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                }
            });
            mPortraitPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setBackgroundAlpha(1f);
                }
            });
        }
        setBackgroundAlpha(0.4f);
        mPortraitPopupWindow.showAtLocation(mMineView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
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
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Log.e("my", "uri=" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
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
            mPortraitImageView.setImageBitmap(bitmap);
            //将Bitmap转换成字节流
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            try {
                byteArrayOutputStream.close();
                byte[] buffer = byteArrayOutputStream.toByteArray();
                //将图片的字节流数据加密成base64字符输出
                String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
//                uploadLogo(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置屏幕的背景透明度
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
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
}
