package com.mmuhamadamirzaidi.sellynapp.Model;

public class BannedProduct {

    private String notificationNo, productDescription, productHolder, productImage, productManufacturer, productName, prohibitedIngredient;

    public BannedProduct() {
    }

    public BannedProduct(String notificationNo, String productDescription, String productHolder, String productImage, String productManufacturer, String productName, String prohibitedIngredient) {
        this.notificationNo = notificationNo;
        this.productDescription = productDescription;
        this.productHolder = productHolder;
        this.productImage = productImage;
        this.productManufacturer = productManufacturer;
        this.productName = productName;
        this.prohibitedIngredient = prohibitedIngredient;
    }

    public String getNotificationNo() {
        return notificationNo;
    }

    public void setNotificationNo(String notificationNo) {
        this.notificationNo = notificationNo;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductHolder() {
        return productHolder;
    }

    public void setProductHolder(String productHolder) {
        this.productHolder = productHolder;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public void setProductManufacturer(String productManufacturer) {
        this.productManufacturer = productManufacturer;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProhibitedIngredient() {
        return prohibitedIngredient;
    }

    public void setProhibitedIngredient(String prohibitedIngredient) {
        this.prohibitedIngredient = prohibitedIngredient;
    }
}
