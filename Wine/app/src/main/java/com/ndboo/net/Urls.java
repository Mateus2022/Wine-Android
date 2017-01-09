package com.ndboo.net;

/**
 * Created by Li on 2017/1/6.
 * 服务端接口地址
 */

public class Urls {
    public static final String URL_BASE = "http://www.ndboo.com/wine/";
//    public static final String URL_BASE = "http://192.168.1.103:88/wine/";

    //通过密码登录
    public static final String URL_LOGIN_BY_PASSWORD = "member/loginByPassword";
    //注册
    public static final String URL_REGISTER = "member/register";
    //获取个人信息
    public static final String URL_GET_USER_INFO = "member/getMemberInformationById";
    //修改个人信息
    public static final String URL_MODIFY_USER_INFO = "member/updateUserInformationById";
    //修改头像
    public static final String URL_MODIFY_USER_HEAD_IMAGE = "member/updateMemberShopLogo";
    //分类下酒的信息
    public static final String URL_SHOW_WINES_BY_TYPE = "ws/showAllWineByWineClass";
    //修改商品数量
    public static final String URL_MODIFY_PRODUCT_NUM = "ws/updateProductNumFromCart";
    //购物车列表
    public static final String URL_CART_GET_PRODUCT_LIST = "ws/getProductsFromCart";
    //删除购物车商品
    public static final String URL_CART_DELETE_PRODUCT = "ws/delProductFromCart";
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
}
