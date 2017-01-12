package com.ndboo.net;

/**
 * Created by Li on 2017/1/6.
 * 服务端接口地址
 */

public class Urls {
    public static final String URL_BASE = "http://www.ndboo.com/wine/";
//    public static final String URL_BASE = "http://192.168.1.103:88/wine/";

    //轮播图
    public static final String URL_CAROUSEL="carouse/selectCarouselPicture";
    //通过密码登录
    public static final String URL_LOGIN_BY_PASSWORD = "member/loginByPassword";
    //注册
    public static final String URL_REGISTER = "member/register";
    //重置密码
    public static final String URL_RESET_PASSWORD="member/resetPassword";
    //获取个人信息
    public static final String URL_GET_USER_INFO = "member/getMemberInformationById";
    //修改个人信息
    public static final String URL_MODIFY_USER_INFO = "member/updateUserInformationById";
    //修改头像
    public static final String URL_MODIFY_USER_HEAD_IMAGE = "member/updateMemberShopLogo";
    //分类下酒的信息
    public static final String URL_SHOW_WINES_BY_TYPE = "ws/showAllWineByWineClass";

    //添加至购物车
    public static final String URL_ADD_TO_CAR = "ws /addProductToCart";
    //从购物车删除
    public static final String URL_CART_DELETE_PRODUCT = "ws/delProductFromCart";
    //修改商品数量
    public static final String URL_MODIFY_PRODUCT_NUM = "ws/updateProductNumFromCart";
    //商品详情
    public static final String URL_WINE_DETAIL = "ws/showWineInformationById";
    //去结算后跳转
    public static final String URL_SUBMIT_ORDER = "ws/summitOrderView";
    //提交订单
    public static final String URL_ENSURE_ORDER = "ws/addIndentFromCart";
    //获取单个订单详情
    public static final String URL_ORDER_DETAIL = "ws/selectIndentDetail";
    //根据订单状态获取订单列表
    public static final String URL_GET_ORDER_BY_STATUS = "ws/selectIndentByStatus";
    //支付宝支付
    public static final String URL_PAY_ALIPAY = "ws/doPayForCart";
    //购物车列表
    public static final String URL_CART_GET_PRODUCT_LIST = "ws/getProductsFromCart";
    //查询收货地址
    public static final String URL_QUERY_ADDRESS = "ws/showAddressByHumanId";
    //添加收货地址
    public static final String URL_ADD_ADDRESS = "ws/addAddress";
    //修改收货地址
    public static final String URL_UPDATE_ADDRESS = "ws/updateAddress";
    //删除收货地址
    public static final String URL_DELETE_ADDRESS = "ws/deleteAddress";
    //通过地址编号查询地址信息
    public static final String URL_QUERY_ADDRESS_BY_ID = "ws/selectAddressById";
    //立即支付
    public static final String URL_PAY_RIGHTAWAY = "ws/addIndentFromProductDetail";
    //通过用户编号查询购物车数量
    public static final String URL_QUERY_CAR_NUM="ws/selectProCountFromCart";
    //确认收货
    public static final String URL_CONFIRM_RECEIPT = "ws/updateIndentStatus";

}
