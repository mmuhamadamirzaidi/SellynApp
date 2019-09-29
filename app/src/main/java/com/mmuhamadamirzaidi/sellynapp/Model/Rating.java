package com.mmuhamadamirzaidi.sellynapp.Model;

public class Rating {

    public String productId, userPhone, userImage, userName, ratingTime, ratingValue, ratingReview;

    public Rating() {
    }

    public Rating(String productId, String userPhone, String userImage, String userName, String ratingTime, String ratingValue, String ratingReview) {
        this.productId = productId;
        this.userPhone = userPhone;
        this.userImage = userImage;
        this.userName = userName;
        this.ratingTime = ratingTime;
        this.ratingValue = ratingValue;
        this.ratingReview = ratingReview;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRatingTime() {
        return ratingTime;
    }

    public void setRatingTime(String ratingTime) {
        this.ratingTime = ratingTime;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRatingReview() {
        return ratingReview;
    }

    public void setRatingReview(String ratingReview) {
        this.ratingReview = ratingReview;
    }
}
