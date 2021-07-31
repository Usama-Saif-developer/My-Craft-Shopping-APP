package com.example.craft_shoping;

import java.util.ArrayList;
import java.util.List;

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
    private String productID;
    private String productimage;
    private String productTitle;
    private Long freeCoupens;
    private String productPrice;
    private String cuttedPrice;
    private Long productQuantity;
    private Long offersApplied;
    private Long coupedApplied;
    private boolean inStock;
    private Long maxQuantity;
    private Long stockQuantity;
    private List<String> qtyIDs;
    private boolean qtyError;
    private String selectedCoupenId;
    private String discountedPrice;
    private boolean COD;

    public CartitemModel(boolean COD,int type,String productID, String productimage, String productTitle, Long freeCoupens, String productPrice, String cuttedPrice, Long productQuantity, Long offersApplied, Long coupedApplied,boolean inStock,Long maxQuantity,Long stockQuantity) {
        this.type = type;
        this.productID=productID;
        this.productimage = productimage;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
        this.coupedApplied = coupedApplied;
        this.inStock=inStock;
        this.maxQuantity=maxQuantity;
        this.stockQuantity=stockQuantity;
        qtyIDs=new ArrayList<>();
        qtyError=false;
        this.COD=COD;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getSelectedCoupenId() {
        return selectedCoupenId;
    }

    public void setSelectedCoupenId(String selectedCoupenId) {
        this.selectedCoupenId = selectedCoupenId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public Long getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Long maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductimage() {
        return productimage;
    }
    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }
    public String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public Long getFreeCoupens() {
        return freeCoupens;
    }
    public void setFreeCoupens(Long freeCoupens) {
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
    public Long getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }
    public Long getOffersApplied() {
        return offersApplied;
    }
    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }
    public Long getCoupedApplied() {
        return coupedApplied;
    }
    public void setCoupedApplied(Long coupedApplied) {
        this.coupedApplied = coupedApplied;
    }
    //////CART ITEM


    ////cart Total
    private int totalItems,totalItemPrice,totalAmount,savedAmount;
    private String deliveryPrice;
    public CartitemModel(int type) {
        this.type = type;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(int totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
////cart Total
}
