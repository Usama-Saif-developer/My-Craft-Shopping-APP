package com.example.craft_shoping;

public class HorizontalProductScrollModel {
    private String productID;
    private String productImage;
    private String producttitle;
    private String productdescription;
    private String productprice;

    public HorizontalProductScrollModel(String productID,String productImage, String producttitle, String productdescription, String productprice) {
        this.productID=productID;
        this.productImage = productImage;
        this.producttitle = producttitle;
        this.productdescription = productdescription;
        this.productprice = productprice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }
}
