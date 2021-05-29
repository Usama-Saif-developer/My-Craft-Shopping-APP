package com.example.craft_shoping;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBqueries {


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoryNames = new ArrayList<>();

    public static void loadCategories(RecyclerView categoryrecyclerview, Context context) {

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel((String) documentSnapshot.get("icon"), (String) documentSnapshot.get("categoryName")));
                            }
                            CategoryAdapter categoryAdapter=new CategoryAdapter(categoryModelList);
                            categoryrecyclerview.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadFragmentData(RecyclerView homepageRecyclerView, final Context context, final int index, String categoryName) {

        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS")
                .orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add(new SliderModel((String) documentSnapshot.get("banner_" + x)
                                                , (String) documentSnapshot.get("banner_" + x + "_background")));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));


                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1
                                            , (String) documentSnapshot.get("strip_ad_banner")
                                            , (String) documentSnapshot.get("background")));


                                } else if ((long) documentSnapshot.get("view_type") == 2) {

                                    List<WishlistModel> viewAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel
                                                ((String) documentSnapshot.get("product_ID_" + x),
                                                        (String) documentSnapshot.get("product_image_" + x),
                                                        (String) documentSnapshot.get("product_title_" + x),
                                                        (String) documentSnapshot.get("product_subtitle_" + x),
                                                        (String) documentSnapshot.get("product_price_" + x)));
                                        viewAllProductList.add(new WishlistModel((String) documentSnapshot.get("product_image_" + x),
                                                (String) documentSnapshot.get("product_full_title_" + x),
                                                (long) documentSnapshot.get("free_coupens_" + x),
                                                (String) documentSnapshot.get("average_rating_" + x),
                                                (long) documentSnapshot.get("total_rating_" + x),
                                                (String) documentSnapshot.get("product_price_" + x),
                                                (String) documentSnapshot.get("cutted_price_" + x),
                                                (boolean) documentSnapshot.get("COD_" + x)));
                                    }
                                    lists.get(index).add(new HomePageModel(2, (String) documentSnapshot.get("layout_title"), (String) documentSnapshot.get("layout_background"), horizontalProductScrollModelList, viewAllProductList));


                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> grideLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        grideLayoutModelList.add(new HorizontalProductScrollModel
                                                ((String) documentSnapshot.get("product_ID_" + x),
                                                        (String) documentSnapshot.get("product_image_" + x),
                                                        (String) documentSnapshot.get("product_title_" + x),
                                                        (String) documentSnapshot.get("product_subtitle_" + x),
                                                        (String) documentSnapshot.get("product_price_" + x)));
                                    }
                                    lists.get(index).add(new HomePageModel(3, (String) documentSnapshot.get("layout_title"), (String) documentSnapshot.get("layout_background"), grideLayoutModelList));
                                }
                            }
                            HomePageAdapter homePageAdapter=new HomePageAdapter(lists.get(index));
                            homepageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            MyMallFragment.swipeRefreshLayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
