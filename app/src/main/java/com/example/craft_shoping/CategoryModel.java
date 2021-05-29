package com.example.craft_shoping;

public class CategoryModel {

    //their are recyclerviews data come from firebase
    private String CategoryIconLink;
    private String Categoryname;

    public CategoryModel(String categoryIconLink, String categoryname) {
        CategoryIconLink = categoryIconLink;
        Categoryname = categoryname;
    }

    public String getCategoryIconLink() {
        return CategoryIconLink;
    }

    public void setCategoryIconLink(String categoryIconLink) {
        CategoryIconLink = categoryIconLink;
    }

    public String getCategoryname() {
        return Categoryname;
    }

    public void setCategoryname(String categoryname) {
        Categoryname = categoryname;
    }
}
