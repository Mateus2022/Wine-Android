package com.ndboo.net;

import com.ndboo.bean.AddressBean;
import com.ndboo.bean.CarouselBean;
import com.ndboo.bean.UserInfoBean;
import com.ndboo.bean.WineBean;
import com.ndboo.bean.WineDetailBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface Api {


    /**
     * 获取轮播图
     *
     * @return 结果
     */
    @POST(Urls.URL_CAROUSEL)
    Observable<List<CarouselBean>> getCarousel();

    /**
     * 通过密码登录
     *
     * @param userAccount  ic_login_account
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
     * 重置密码
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return
     */
    @POST(Urls.URL_RESET_PASSWORD)
    Observable<String> resetPassword(@Query("memberAccount") String userAccount,
                                     @Query("memberPassword") String userPassword);

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
    @FormUrlEncoded
    @POST(Urls.URL_MODIFY_USER_HEAD_IMAGE)
    Observable<String> modifyUserHead(@Field("memberId") String userId,
                                      @Field("shopLogo") String userHeadImage);

    /**
     * 根据商品总类显示商品
     *
     * @param wineType 酒类
     * @param userId   用户编号
     * @return 酒类信息
     */
    @POST(Urls.URL_SHOW_WINES_BY_TYPE)
    Observable<List<WineBean>> showWinesByType(@Query("typeIndexId") String wineType,
                                               @Query("memberId") String userId);

    /**
     * 添加商品至购物车
     *
     * @param userId       用户编号
     * @param productId    商品编号
     * @param productCount 商品数量
     * @return 添加结果
     */
    @POST(Urls.URL_ADD_TO_CAR)
    Observable<String> addWineToCar(@Query("memberId") String userId,
                                    @Query("productId") String productId,
                                    @Query("productCount") String productCount);

    /**
     * 删除购物车商品数量
     *
     * @param memberId   用户编号
     * @param productIds 商品编号，用逗号隔开
     * @return 删除结果
     */
    @POST(Urls.URL_CART_DELETE_PRODUCT)
    Observable<String> deleteFromCart(@Query("memberId") String memberId,
                                      @Query("productIds") String productIds);

    /**
     * 修改商品数量
     *
     * @param userId       用户编号
     * @param productId    商品编号
     * @param productCount 商品数量
     * @return 修改结果
     */
    @POST(Urls.URL_MODIFY_PRODUCT_NUM)
    Observable<String> modifyProductNum(@Query("memberId") String userId,
                                        @Query("productId") String productId,
                                        @Query("productCount") String productCount);

    /**
     * 商品详情
     *
     * @param productId 商品编号
     * @return 详情信息
     */
    @POST(Urls.URL_WINE_DETAIL)
    Observable<WineDetailBean> showWineDetail(@Query("productId") String productId);

    /**
     * 获取购物车商品列表
     *
     * @param memberId 用户id
     * @return 商品列表信息
     */
    @POST(Urls.URL_CART_GET_PRODUCT_LIST)
    Observable<String> getCartProductsList(@Query("memberId") String memberId);

    /**
     * 购物车内商品去结算
     *
     * @param memberId   用户id
     * @param productIds 商品id集合
     * @return 操作成功或失败信息
     */
    @POST(Urls.URL_SUBMIT_ORDER)
    Observable<String> submitOrder(@Query("memberId") String memberId,
                                   @Query("productIds") String productIds);

    /**
     * 确认订单
     *
     * @param memberId   用户id
     * @param productIds 商品id集合
     * @param addressId  地址id
     * @param payId      支付方式id
     * @return 确认订单结果
     */
    @POST(Urls.URL_ENSURE_ORDER)
    Observable<String> ensureOrder(@Query("memberId") String memberId,
                                   @Query("productIds") String productIds,
                                   @Query("addressId") String addressId,
                                   @Query("payId") String payId);

    /**
     * 获取订单详情
     *
     * @param memberId 用户id
     * @param orderId  订单id
     * @return 订单信息
     */
    @POST(Urls.URL_ORDER_DETAIL)
    Observable<String> getOrderDetail(@Query("memberId") String memberId,
                                      @Query("orderId") String orderId);

    /**
     * 根据订单状态获取订单列表
     *
     * @param memberId    用户id
     * @param orderStatus 订单状态
     * @param pageNum     页码，第几页
     * @param numPerPage  每页显示的数量
     * @return 订单列表
     */
    @POST(Urls.URL_GET_ORDER_BY_STATUS)
    Observable<String> getOrderByStatus(@Query("memberId") String memberId,
                                        @Query("orderStatus") String orderStatus,
                                        @Query("pageNum") int pageNum,
                                        @Query("numPerPage ") int numPerPage);

    /**
     * 支付宝支付
     *
     * @param memberId 用户id
     * @param orderId  订单id
     * @return 支付路径等信息
     */
    @POST(Urls.URL_PAY_ALIPAY)
    Observable<String> doAlipay(@Query("memberId") String memberId,
                                @Query("orderId") String orderId);

    /**
     * 微信支付
     *
     * @param memberId 用户id
     * @param orderId  订单id
     * @return 微信支付信息
     */
    @POST(Urls.URL_PAY_WECHAT)
    Observable<String> doWeChatPay(@Query("memberId") String memberId,
                                   @Query("orderId") String orderId);

    /**
     * 查询收货地址
     *
     * @param userId 用户编号
     * @return 收货地址
     */
    @POST(Urls.URL_QUERY_ADDRESS)
    Observable<List<AddressBean>> queryAddress(@Query("humanId") String userId);

    /**
     * 添加地址
     *
     * @param userId        用户编号
     * @param addressName   联系人
     * @param detailAddress 送货地址
     * @param addressPhone  联系电话
     * @return 添加结果
     */
    @POST(Urls.URL_ADD_ADDRESS)
    Observable<String> addAddress(@Query("humanId") String userId,
                                  @Query("addresseeName") String addressName,
                                  @Query("detailAddress") String detailAddress,
                                  @Query("addresseePhone") String addressPhone);

    /**
     * 修改地址信息
     *
     * @param addressId     地址编号
     * @param addressName   收货人
     * @param addressPhone  收货电话
     * @param addressDetail 收货地址
     * @return 修改结果
     */
    @FormUrlEncoded
    @POST(Urls.URL_UPDATE_ADDRESS)
    Observable<String> updateAddress(@Query("addressId") String addressId,
                                     @Field("addresseeName") String addressName,
                                     @Query("addresseePhone") String addressPhone,
                                     @Field("detailAddress") String addressDetail);

    /**
     * 删除地址
     *
     * @param addressId 地址编号
     * @return 删除结果
     */
    @POST(Urls.URL_DELETE_ADDRESS)
    Observable<String> deleteAddress(@Query("addressId") String addressId);

    /**
     * 通过地址编号查看地址
     *
     * @param addressId 地址编号
     * @return 地址信息
     */
    @POST(Urls.URL_QUERY_ADDRESS_BY_ID)
    Observable<String> queryAddressById(@Query("addressId") String addressId);

    /**
     * 确认订单(立即购买)
     *
     * @param memberId     用户id
     * @param productId    商品id集合
     * @param addressId    地址id
     * @param payId        支付方式id
     * @param productCount 商品数量
     * @return 确认订单结果
     */
    @POST(Urls.URL_PAY_RIGHTAWAY)
    Observable<String> submitOrderByCash(@Query("memberId") String memberId,
                                         @Query("productId") String productId,
                                         @Query("addressId") String addressId,
                                         @Query("payId") String payId,
                                         @Query("productCount") String productCount);

    /**
     * 透过用户编号查询购物车商品数量
     *
     * @param memberId 用户编号
     * @return 数量
     */
    @POST(Urls.URL_QUERY_CAR_NUM)
    Observable<String> queryCarNum(@Query("memberId") String memberId);

    /**
     * 确认收货
     *
     * @param orderId     订单编号
     * @param memberId    用户编号
     * @param orderStatus 订单状态
     * @return 确认结果
     */
    @POST(Urls.URL_CONFIRM_RECEIPT)
    Observable<String> confirmReceipt(@Query("orderId") String orderId,
                                      @Query("memberId") String memberId,
                                      @Query("orderStatus") String orderStatus);
}