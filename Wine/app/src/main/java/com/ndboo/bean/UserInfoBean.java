package com.ndboo.bean;

/**
 * Created by Li on 2017/1/8.
 * 用户信息实体
 */

public class UserInfoBean {

    /**
     * memberSex : 性别
     * memberNickname : 昵称
     * memberAccount : 手机账号
     * memberBirthday : 生日
     * shopLogo : logo
     */

    private String memberSex;
    private String memberNickname;
    private String memberAccount;
    private String memberBirthday;
    private String shopLogo;

    public String getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(String memberSex) {
        this.memberSex = memberSex;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getMemberBirthday() {
        return memberBirthday;
    }

    public void setMemberBirthday(String memberBirthday) {
        this.memberBirthday = memberBirthday;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "memberSex='" + memberSex + '\'' +
                ", memberNickname='" + memberNickname + '\'' +
                ", memberAccount='" + memberAccount + '\'' +
                ", memberBirthday='" + memberBirthday + '\'' +
                ", shopLogo='" + shopLogo + '\'' +
                '}';
    }
}
