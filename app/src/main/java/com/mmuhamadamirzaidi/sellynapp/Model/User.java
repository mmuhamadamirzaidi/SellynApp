package com.mmuhamadamirzaidi.sellynapp.Model;

public class User {
    private String userName, userPassword, userIdentityCard, userPhone, userAddress, userImage, isStaff, userSecureCode, userHolderId;

    public User() {
    }

    public User(String userName, String userPassword, String userIdentityCard, String userPhone, String userAddress, String userImage, String isStaff, String userSecureCode, String userHolderId) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userIdentityCard = userIdentityCard;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userImage = userImage;
        this.isStaff = isStaff;
        this.userSecureCode = userSecureCode;
        this.userHolderId = userHolderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserIdentityCard() {
        return userIdentityCard;
    }

    public void setUserIdentityCard(String userIdentityCard) {
        this.userIdentityCard = userIdentityCard;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getUserSecureCode() {
        return userSecureCode;
    }

    public void setUserSecureCode(String userSecureCode) {
        this.userSecureCode = userSecureCode;
    }

    public String getUserHolderId() {
        return userHolderId;
    }

    public void setUserHolderId(String userHolderId) {
        this.userHolderId = userHolderId;
    }
}
