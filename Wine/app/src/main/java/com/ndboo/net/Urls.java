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
    public static final String URL_MODIFY_PRODUCT_NUM="ws/updateProductNumFromCart";
}
