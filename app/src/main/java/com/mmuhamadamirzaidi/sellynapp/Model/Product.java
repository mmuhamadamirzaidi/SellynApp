package com.mmuhamadamirzaidi.sellynapp.Model;

public class Product {

    private String productName, productImage, productDescription, productPrice, productDiscount, productHolder, notificationNo, categoryId;

    public Product() {
    }

    public Product(String productName, String productImage, String productDescription, String productPrice, String productDiscount, String productHolder, String notificationNo, String categoryId) {
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productHolder = productHolder;
        this.notificationNo = notificationNo;
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductHolder() {
        return productHolder;
    }

    public void setProductHolder(String productHolder) {
        this.productHolder = productHolder;
    }

    public String getNotificationNo() {
        return notificationNo;
    }

    public void setNotificationNo(String notificationNo) {
        this.notificationNo = notificationNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
