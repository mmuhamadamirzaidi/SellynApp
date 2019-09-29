package com.mmuhamadamirzaidi.sellynapp.Model;

import java.util.List;

public class OrderRequest {

    public String userPhone;
    public String userName;
    public String userAddress;
    public String grandTotal;

    public String status;

    public String grandSubTotal;
    public String grandDeliveryCharge;
    public String grandOthersCharge;
    public String grandDiscount;

    public String notes;

    public List<Order> product;

    public OrderRequest() {
    }

    public OrderRequest(String userPhone, String userName, String userAddress, String grandTotal, String grandSubTotal, String grandDeliveryCharge, String grandOthersCharge, String grandDiscount, String notes, List<Order> product) {
        this.userPhone = userPhone;
        this.userName = userName;
        this.userAddress = userAddress;
        this.grandTotal = grandTotal;
        this.status = "0"; //Default 0:Processing, 1:Shipped, 2:Delivered
        this.grandSubTotal = grandSubTotal;
        this.grandDeliveryCharge = grandDeliveryCharge;
        this.grandOthersCharge = grandOthersCharge;
        this.grandDiscount = grandDiscount;
        this.notes = notes;
        this.product = product;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrandSubTotal() {
        return grandSubTotal;
    }

    public void setGrandSubTotal(String grandSubTotal) {
        this.grandSubTotal = grandSubTotal;
    }

    public String getGrandDeliveryCharge() {
        return grandDeliveryCharge;
    }

    public void setGrandDeliveryCharge(String grandDeliveryCharge) {
        this.grandDeliveryCharge = grandDeliveryCharge;
    }

    public String getGrandOthersCharge() {
        return grandOthersCharge;
    }

    public void setGrandOthersCharge(String grandOthersCharge) {
        this.grandOthersCharge = grandOthersCharge;
    }

    public String getGrandDiscount() {
        return grandDiscount;
    }

    public void setGrandDiscount(String grandDiscount) {
        this.grandDiscount = grandDiscount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Order> getProduct() {
        return product;
    }

    public void setProduct(List<Order> product) {
        this.product = product;
    }
}
