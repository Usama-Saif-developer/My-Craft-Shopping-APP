package com.example.craft_shoping;

public class CartitemModel {
    public static final int CART_ITEM=0;
    public static final int TOTAL_AMOUNT=1;

    private int type;
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    //////CART ITEM
    private int productimage;
    private String productTitle;
    private int freeCoupens;
    private String productPrice;
    private String cuttedPrice;
    private int productQuantity;
    private int offersApplied;
    private int coupedApplied;
    public CartitemModel(int type, int productimage, String productTitle, int freeCoupens, String productPrice, String cuttedPrice, int productQuantity, int offersApplied, int coupedApplied) {
        this.type = type;
        this.productimage = productimage;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
        this.coupedApplied = coupedApplied;
    }
    public int getProductimage() {
        return productimage;
    }
    public void setProductimage(int productimage) {
        this.productimage = productimage;
    }
    public String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public int getFreeCoupens() {
        return freeCoupens;
    }
    public void setFreeCoupens(int freeCoupens) {
        this.freeCoupens = freeCoupens;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getCuttedPrice() {
        return cuttedPrice;
    }
    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }
    public int getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
    public int getOffersApplied() {
        return offersApplied;
    }
    public void setOffersApplied(int offersApplied) {
        this.offersApplied = offersApplied;
    }
    public int getCoupedApplied() {
        return coupedApplied;
    }
    public void setCoupedApplied(int coupedApplied) {
        this.coupedApplied = coupedApplied;
    }
    //////CART ITEM


    ////cart Total
    private String totalItems;
    private String totalItemPrice;
    private String deliveryPrice;
    private String saveAmount;
    private String totalAmount;

    public CartitemModel(int type, String totalItems, String totalItemPrice, String deliveryPrice, String totalAmount, String saveAmount) {
        this.type = type;
        this.totalItems = totalItems;
        this.totalItemPrice = totalItemPrice;
        this.deliveryPrice = deliveryPrice;
        this.totalAmount = totalAmount;
        this.saveAmount = saveAmount;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(String totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getSaveAmount() {
        return saveAmount;
    }

    public void setSaveAmount(String saveAmount) {
        this.saveAmount = saveAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    ////cart Total
}
