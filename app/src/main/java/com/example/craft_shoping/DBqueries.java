package com.example.craft_shoping;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static String email, fullname, profile;

    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoryNames = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartitemModel> cartitemModelList = new ArrayList<>();

    public static int selectedAddress = -1;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    public static List<RewardModel> rewardModelList = new ArrayList<>();

    public static List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();

    public static List<NotificationModel> notificationModelList = new ArrayList<>();
    private static ListenerRegistration registration;

    public static void loadCategories(RecyclerView categoryrecyclerview, Context context) {
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel((String) documentSnapshot.get("icon"), (String) documentSnapshot.get("categoryName")));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
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

                                    ArrayList<String> productIds=(ArrayList<String>) documentSnapshot.get("products");
                                    for (String productId: productIds) {
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(productId
                                                        ,""
                                                        ,""
                                                        ,""
                                                        ,""));

                                        viewAllProductList.add(new WishlistModel(productId
                                                , ""
                                                ,""
                                                ,0
                                                ,""
                                                ,0
                                                ,""
                                                ,""
                                                ,false
                                                ,false));
                                    }
                                    lists.get(index).add(new HomePageModel(2, (String) documentSnapshot.get("layout_title"), (String) documentSnapshot.get("layout_background"), horizontalProductScrollModelList, viewAllProductList));


                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> grideLayoutModelList = new ArrayList<>();
                                    ArrayList<String> productIds=(ArrayList<String>) documentSnapshot.get("products");
                                    for (String productId: productIds) {
                                        grideLayoutModelList.add(new HorizontalProductScrollModel(productId
                                                ,""
                                                ,""
                                                ,""
                                                ,""));

                                    }
                                    lists.get(index).add(new HomePageModel(3, (String) documentSnapshot.get("layout_title"), (String) documentSnapshot.get("layout_background"), grideLayoutModelList));
                                }
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
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

    public static void loadWishlist(Context context, Dialog dialog, boolean loadProductData) {
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        wishList.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.wishList.contains(ProductDetailActivity.productID)) {
                            ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = true;
                            if (ProductDetailActivity.addtowishlistbtn != null) {
                                ProductDetailActivity.addtowishlistbtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.purple_500));
                            }
                        } else {
                            if (ProductDetailActivity.addtowishlistbtn != null) {
                                ProductDetailActivity.addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
                            }
                            ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }

                        if (loadProductData) {
                            wishlistModelList.clear();
                            String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                                                wishlistModelList.add(new WishlistModel(productId, (String) documentSnapshot.get("product_image_1"),
                                                                        (String) documentSnapshot.get("product_title"),
                                                                        (long) documentSnapshot.get("free_coupen"),
                                                                        (String) documentSnapshot.get("average_rating"),
                                                                        (long) documentSnapshot.get("total_ratings"),
                                                                        (String) documentSnapshot.get("product_price"),
                                                                        (String) documentSnapshot.get("cutted_price"),
                                                                        (boolean) documentSnapshot.get("COD"),
                                                                        true));
                                                            } else {
                                                                wishlistModelList.add(new WishlistModel(productId, (String) documentSnapshot.get("product_image_1"),
                                                                        (String) documentSnapshot.get("product_title"),
                                                                        (long) documentSnapshot.get("free_coupen"),
                                                                        (String) documentSnapshot.get("average_rating"),
                                                                        (long) documentSnapshot.get("total_ratings"),
                                                                        (String) documentSnapshot.get("product_price"),
                                                                        (String) documentSnapshot.get("cutted_price"),
                                                                        (boolean) documentSnapshot.get("COD"),
                                                                        false));
                                                            }
                                                            MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                                        } else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromWishlist(int index, Context context) {
        String removeProductId = wishList.get(index);
        wishList.remove(index);
        Map<String, Object> updateWishlist = new HashMap<>();

        for (int x = 0; x < wishList.size(); x++) {
            updateWishlist.put("product_ID_" + x, wishList.get(x));
        }
        updateWishlist.put("list_size", (long) wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (wishlistModelList.size() != 0) {
                        wishlistModelList.remove(index);
                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context, "Remove Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    if (ProductDetailActivity.addtowishlistbtn != null) {
                        ProductDetailActivity.addtowishlistbtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.purple_500));
                    }
                    wishList.add(index, removeProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailActivity.running_wishlist_query = false;
            }
        });
    }

    public static void loadRatingList(Context context) {
        if (!ProductDetailActivity.running_rating_query) {
            ProductDetailActivity.running_rating_query = true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> orderProductIds = new ArrayList<>();
                        for (int x = 0; x < myOrderItemModelList.size(); x++) {
                            orderProductIds.add(myOrderItemModelList.get(x).getProductId());
                        }
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add((String) task.getResult().get("product_ID_" + x));
                            myRating.add((long) task.getResult().get("rating_" + x));
                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailActivity.productID)) {
                                ProductDetailActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductDetailActivity.rateNowContainer != null) {
                                    ProductDetailActivity.setRating(ProductDetailActivity.initialRating);
                                }
                            }
                            if (orderProductIds.contains((String) task.getResult().get("product_ID_" + x))) {
                                myOrderItemModelList.get(orderProductIds.indexOf((String) task.getResult().get("product_ID_" + x))).setRating(Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1);
                            }
                        }
                        if (MyOrderFragment.myOrderAdapter != null) {
                            MyOrderFragment.myOrderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailActivity.running_rating_query = false;
                }
            });
        }
    }

    public static void loadCartList(Context context, Dialog dialog, boolean loadProductData, final TextView badgecount, TextView cartTotalAmount) {
        cartList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        cartList.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.cartList.contains(ProductDetailActivity.productID)) {
                            ProductDetailActivity.ALREADY_ADDED_TO_CART = true;
                        } else {
                            ProductDetailActivity.ALREADY_ADDED_TO_CART = false;
                        }

                        if (loadProductData) {
                            cartitemModelList.clear();
                            String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            int index = 0;
                                                            if (cartList.size() >= 2) {
                                                                index = cartList.size() - 2;
                                                            }
                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {

                                                                cartitemModelList.add(index, new CartitemModel(documentSnapshot.getBoolean("COD"), CartitemModel.CART_ITEM, productId, (String) documentSnapshot.get("product_image_1"),
                                                                        (String) documentSnapshot.get("product_title"),
                                                                        (long) documentSnapshot.get("free_coupen"),
                                                                        (String) documentSnapshot.get("product_price"),
                                                                        (String) documentSnapshot.get("cutted_price"),
                                                                        (long) 1,
                                                                        (long) documentSnapshot.get("offers_applied"),
                                                                        (long) 0,
                                                                        true,
                                                                        (long) documentSnapshot.get("max-quantity"),
                                                                        (long) documentSnapshot.get("stock_quantity")));
                                                            } else {
                                                                cartitemModelList.add(index, new CartitemModel(documentSnapshot.getBoolean("COD"), CartitemModel.CART_ITEM, productId, (String) documentSnapshot.get("product_image_1"),
                                                                        (String) documentSnapshot.get("product_title"),
                                                                        (long) documentSnapshot.get("free_coupen"),
                                                                        (String) documentSnapshot.get("product_price"),
                                                                        (String) documentSnapshot.get("cutted_price"),
                                                                        (long) 1,
                                                                        (long) documentSnapshot.get("offers_applied"),
                                                                        (long) 0,
                                                                        false,
                                                                        (long) documentSnapshot.get("max-quantity"),
                                                                        (long) documentSnapshot.get("stock_quantity")));
                                                            }
                                                            if (cartList.size() == 1) {
                                                                cartitemModelList.add(new CartitemModel(CartitemModel.TOTAL_AMOUNT));
                                                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                                parent.setVisibility(View.VISIBLE);
                                                            }
                                                            if (cartList.size() == 0) {
                                                                cartitemModelList.clear();
                                                            }
                                                            MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                        } else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    if (cartList.size() != 0) {
                        badgecount.setVisibility(View.VISIBLE);
                    } else {
                        badgecount.setVisibility(View.INVISIBLE);
                    }
                    if (DBqueries.cartList.size() < 99) {
                        badgecount.setText(String.valueOf(DBqueries.cartList.size()));
                    } else {
                        badgecount.setText("99+");
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromCart(int index, Context context, TextView cartTotalAmount) {
        String removeProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartlist = new HashMap<>();

        for (int x = 0; x < cartList.size(); x++) {
            updateCartlist.put("product_ID_" + x, cartList.get(x));
        }
        updateCartlist.put("list_size", (long) cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (cartitemModelList.size() != 0) {
                        cartitemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size() == 0) {
                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cartitemModelList.clear();
                    }
                    Toast.makeText(context, "Remove Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    cartList.add(index, removeProductId);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailActivity.running_cart_query = false;
            }
        });
    }

    public static void loadAddresses(Context context, Dialog loadingDialog, boolean gotoDeliveryActivity) {
        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if ((long) task.getResult().get("list_size") == 0) {
                        Intent deliveryintent = new Intent(context, AddressActivity.class);
                        deliveryintent.putExtra("INTENT", "deliveryIntent");
                        context.startActivity(deliveryintent);
                    } else {
                        for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {
                            addressesModelList.add(new AddressesModel(task.getResult().getBoolean("selected_" + x)
                                    , task.getResult().getString("city_" + x)
                                    , task.getResult().getString("locality_" + x)
                                    , task.getResult().getString("flat_no_" + x)
                                    , task.getResult().getString("pincode_" + x)
                                    , task.getResult().getString("landmark_" + x)
                                    , task.getResult().getString("name_" + x)
                                    , task.getResult().getString("mobile_no_" + x)
                                    , task.getResult().getString("alternate_mobile_no_" + x)
                                    , task.getResult().getString("state_" + x)
                            ));

                            if ((boolean) task.getResult().get("selected_" + x)) {
                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        if (gotoDeliveryActivity) {
                            Intent deliveryintent = new Intent(context, DeliveryActivity.class);
                            context.startActivity(deliveryintent);
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    public static void loadRewards(Context context, Dialog loadingDialog, Boolean onRewardsFragment) {
        rewardModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
//                            Date lastseenDate=task.getResult().getDate("Last seen");
//                            String checkdate=lastseenDate.before(MyRewardAdapter.datentime);
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                    if (documentSnapshot.get("type").toString().equals("Discount")) {
                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(), (String) documentSnapshot.get("type")
                                                                , (String) documentSnapshot.get("lower_limit")
                                                                , (String) documentSnapshot.get("upper_limit")
                                                                , (String) documentSnapshot.get("percentage")
                                                                , (String) documentSnapshot.get("body")
                                                                , (String) documentSnapshot.get("validity")
                                                                , (boolean) documentSnapshot.get("already_used")
                                                        ));
                                                    } else if (documentSnapshot.get("type").toString().equals("Flat Rs.* OFF")) {
                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(), (String) documentSnapshot.get("type")
                                                                , (String) documentSnapshot.get("lower_limit")
                                                                , (String) documentSnapshot.get("upper_limit")
                                                                , (String) documentSnapshot.get("amount")
                                                                , (String) documentSnapshot.get("body")
                                                                , (String) documentSnapshot.get("validity")
                                                                , (boolean) documentSnapshot.get("already_used")
                                                        ));
                                                    }
                                                }
                                                if (onRewardsFragment) {
                                                    MyRewardsFragment.myRewardAdapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                            }
                                            loadingDialog.dismiss();
                                        }
                                    });
                        } else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void loadOrders(Context context, @Nullable MyOrderAdapter myOrderAdapter, Dialog loadingDialog) {
        myOrderItemModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").orderBy("time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                firebaseFirestore.collection("ORDERS").document(documentSnapshot.getString("order_id")).collection("OrderItems").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot orderItems : task.getResult().getDocuments()) {
                                                        MyOrderItemModel myOrderItemModel = new MyOrderItemModel(orderItems.getString("Product Id"), orderItems.getString("Order Status"), orderItems.getString("Address"), orderItems.getString("Coupen Id"), orderItems.getString("Cutted Price"), orderItems.getDate("Ordered date"), orderItems.getDate("Packed date"), orderItems.getDate("Shipped date"), orderItems.getDate("Delivered date"), orderItems.getDate("Cancelled date"), orderItems.getString("Discounted Price"), orderItems.getLong("Free Coupens"), orderItems.getString("FullName"), orderItems.getString("ORDER ID"), orderItems.getString("Payment Method"), orderItems.getString("Pincode"), orderItems.getString("Product Price"), orderItems.getLong("Product Quantity"), orderItems.getString("User Id"), orderItems.getString("Product Image"), orderItems.getString("Product Title"), orderItems.getString("Delivery Price"), orderItems.getBoolean("Cancellation requested"));
                                                        myOrderItemModelList.add(myOrderItemModel);
                                                    }
                                                    loadRatingList(context);
                                                    if (myOrderAdapter != null) {
                                                        myOrderAdapter.notifyDataSetChanged();
                                                    }
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });
                            }
                        } else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void checkNotification(boolean remove, @Nullable TextView notifyCount) {
        if (remove) {
            registration.remove();
        } else {
            registration = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null && value.exists()) {
                                notificationModelList.clear();
                                int unread = 0;
                                for (long x = 0; x < (long) value.get("list_size"); x++) {
                                    notificationModelList.add(0,new NotificationModel((String) value.get("Image_" + x)
                                            , (String) value.get("Body_" + x)
                                            , value.getBoolean("Readed_" + x)));
                                    if (!value.getBoolean("Readed_" + x)) {
                                        unread++;
                                        if (notifyCount != null) {
                                            if (unread > 0) {
                                                notifyCount.setVisibility(View.VISIBLE);
                                                if (unread < 99) {
                                                    notifyCount.setText(String.valueOf(unread));
                                                } else {
                                                    notifyCount.setText("99+");
                                                }
                                            }else{
                                                notifyCount.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                }
                                if (NotificationActivity.adapter != null) {
                                    NotificationActivity.adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }

    public static void clearData() {
        categoryModelList.clear();
        lists.clear();
        loadedCategoryNames.clear();
        wishList.clear();
        wishlistModelList.clear();
        cartList.clear();
        cartitemModelList.clear();
        myRatedIds.clear();
        myRating.clear();
        addressesModelList.clear();
        rewardModelList.clear();
        myOrderItemModelList.clear();
    }
}




