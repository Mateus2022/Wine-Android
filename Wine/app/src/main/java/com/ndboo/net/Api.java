package com.ndboo.net;

import com.ndboo.bean.UserInfoBean;
import com.ndboo.bean.WineBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface Api {

    /**
     * 通过密码登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @return 结果
     */
    @POST(Urls.URL_LOGIN_BY_PASSWORD)
    Observable<String> loginByPassword(@Query("memberAccount") String userAccount,
                                       @Query("memberPassword") String userPassword);

    /**
     * 注册
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 结果
     */
    @POST(Urls.URL_REGISTER)
    Observable<String> register(@Query("human_account") String userAccount,
                                @Query("human_password") String userPassword);

    /**
     * 获取用户信息
     *
     * @param userId 用户编号
     * @return 用户信息
     */
    @POST(Urls.URL_GET_USER_INFO)
    Observable<UserInfoBean> getUserInfo(@Query("memberId") String userId);

    @FormUrlEncoded
    @POST(Urls.URL_MODIFY_USER_INFO)
    Observable<String> modifyUserInfo(@Field("memberId") String userId,
                                      @Field("memberNickname") String userNickName,
                                      @Field("memberBirthday") String userBirthDay,
                                      @Field("memberSex") String userSex);

    /**
     * 修改头像
     *
     * @param userId        用户编号
     * @param userHeadImage 用户头像（Base64字符串）
     * @return 成功/失败
     */
    @GET(Urls.URL_MODIFY_USER_HEAD_IMAGE)
    Observable<String> modifyUserHead(@Query("memberId") String userId,
                                      @Query("shopLogo") String userHeadImage);

    /**
     * 根据商品总类显示商品
     * @param wineType  酒类
     * @param userId    用户编号
     * @return  酒类信息
     */
    @POST(Urls.URL_SHOW_WINES_BY_TYPE)
    Observable<List<WineBean>> showWinesByType(@Query("typeIndexId") String wineType,
                                               @Query("memberId") String userId);

    /**
     * 获取购物车商品列表
     * @param memberId 用户id
     * @return 购物车信息
     */
    @POST(Urls.URL_CART_LIST)
    Observable<String> getCartProductsList(@Query("memberId") String memberId);
}