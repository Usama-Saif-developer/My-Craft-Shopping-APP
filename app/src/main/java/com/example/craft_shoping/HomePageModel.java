package com.example.craft_shoping;

import java.util.List;

public class HomePageModel {

    public static final int Banner_Slider=0;
    public static final int STRIP_AD_BANNER=1;
    public static final int HORIZONTAL_PRODUCT_VIEW=2;
    public static final int GRID_PRODUCT_VIEW=3;
    private int type;
    private String backgroundColor;

    //////Banner Slider
    private List<SliderModel> sliderModelList;
    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    //////Banner Slider



    /////Strip Ad Banner

    private String resourse;
    public HomePageModel(int type, String resourse, String backgroundColor) {
        this.type = type;
        this.resourse = resourse;
        this.backgroundColor = backgroundColor;
    }
    public String getResourse() {
        return resourse;
    }
    public void setResourse(String resourse) {
        this.resourse = resourse;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /////Strip Ad Banner



    private String title;
    List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    ////////Horizontal Product views

    List<WishlistModel> viewAllProductList;
    public HomePageModel(int type, String title,String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList,List<WishlistModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor=backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList=viewAllProductList;
    }

    public List<WishlistModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishlistModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }

    ////////Horizontal Product views

    ////////Grid Product views
    public HomePageModel(int type, String title, String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColor=backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }


    ////////Grid Product views

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }
    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }
    ////////Horizontal Product views

}
