package com.ndboo.bean;

/**
 * Created by Li on 2017/1/5.
 * 酒水实体
 */

public class Wine {


    private String mImgUrl;
    private String mPrice;
    private String mOriginalPrice;
    private String mWineName;

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    private String mNumber;

    public Wine() {
    }

    public Wine(String imgUrl, String price, String originalPrice, String wineName,String number) {
        mImgUrl = imgUrl;
        mPrice = price;
        mOriginalPrice = originalPrice;
        mWineName = wineName;
        mNumber=number;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getOriginalPrice() {
        return mOriginalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        mOriginalPrice = originalPrice;
    }

    public String getWineName() {
        return mWineName;
    }

    public void setWineName(String wineName) {
        mWineName = wineName;
    }
}
