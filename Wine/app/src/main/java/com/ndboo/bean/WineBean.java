package com.ndboo.bean;

/**
 * Created by Li on 2017/1/5.
 * 酒水实体
 */

public class WineBean {
    /**
     * picPath : 图片地址
     * cartProductCount : 商品数量
     * productPrice : 商品价格
     * productName : 商品名称
     * productId : 商品编号
     */

    private String picPath;
    private String cartProductCount;
    private String productPrice;
    private String productName;
    private String productId;

    public WineBean(String productId, String productName, String picPath,
                    String cartProductCount, String productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.picPath = picPath;
        this.cartProductCount = cartProductCount;
        this.productPrice = productPrice;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getCartProductCount() {
        return cartProductCount;
    }

    public void setCartProductCount(String cartProductCount) {
        this.cartProductCount = cartProductCount;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


//    private String mImgUrl;
//    private String mPrice;
//    private String mOriginalPrice;
//    private String mWineName;
//
//    public String getNumber() {
//        return mNumber;
//    }
//
//    public void setNumber(String number) {
//        mNumber = number;
//    }
//
//    private String mNumber;
//
//    public WineBean() {
//    }
//
//    public WineBean(String imgUrl, String price, String originalPrice, String wineName, String number) {
//        mImgUrl = imgUrl;
//        mPrice = price;
//        mOriginalPrice = originalPrice;
//        mWineName = wineName;
//        mNumber=number;
//    }
//
//    public String getImgUrl() {
//        return mImgUrl;
//    }
//
//    public void setImgUrl(String imgUrl) {
//        mImgUrl = imgUrl;
//    }
//
//    public String getPrice() {
//        return mPrice;
//    }
//
//    public void setPrice(String price) {
//        mPrice = price;
//    }
//
//    public String getOriginalPrice() {
//        return mOriginalPrice;
//    }
//
//    public void setOriginalPrice(String originalPrice) {
//        mOriginalPrice = originalPrice;
//    }
//
//    public String getWineName() {
//        return mWineName;
//    }
//
//    public void setWineName(String wineName) {
//        mWineName = wineName;
//    }
}
