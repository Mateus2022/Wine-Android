package com.ndboo.bean;

/**
 * Created by Li on 2017/1/10.
 * 收货地址
 */

public class AddressBean {

    /**
     * addresseeName : 收货人姓名
     * addresseeArea : 收货地区
     * addresseePhone : 收货电话
     * addressId : 地址编号
     * detailAddress : 详细地址
     */

    private String addresseeName;
    private String addresseeArea;
    private String addresseePhone;
    private String addressId;
    private String detailAddress;

    public String getAddresseeName() {
        return addresseeName;
    }

    public void setAddresseeName(String addresseeName) {
        this.addresseeName = addresseeName;
    }

    public String getAddresseeArea() {
        return addresseeArea;
    }

    public void setAddresseeArea(String addresseeArea) {
        this.addresseeArea = addresseeArea;
    }

    public String getAddresseePhone() {
        return addresseePhone;
    }

    public void setAddresseePhone(String addresseePhone) {
        this.addresseePhone = addresseePhone;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
