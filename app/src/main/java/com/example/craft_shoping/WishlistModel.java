package com.example.craft_shoping;

public class WishlistModel {

    private String productimage;
    private String productTitle;
    private long freeCoupens;
    private String rating;
    private long totalrating;
    private String productprice;
    private String cuttedPrice;
    private boolean COD;

    public WishlistModel(String productimage, String productTitle, long freeCoupens, String rating, long totalrating, String productprice, String cuttedPrice, boolean COD) {
        this.productimage = productimage;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.rating = rating;
        this.totalrating = totalrating;
        this.productprice = productprice;
        this.cuttedPrice = cuttedPrice;
        this.COD = COD;
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

    public long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(long totalrating) {
        this.totalrating = totalrating;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }
}
