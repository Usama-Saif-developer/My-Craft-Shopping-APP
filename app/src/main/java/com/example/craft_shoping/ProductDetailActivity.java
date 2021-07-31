package com.example.craft_shoping;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;
    public static Activity productDetailActivity;
    public static boolean fromSearch=false;

    ////Rating Layout
    public static LinearLayout rateNowContainer;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static FloatingActionButton addtowishlistbtn;
    public static String productID;
    ////Coupen Dailog
    public static int initialRating;
    public static boolean setSignUpFragment = false;
    public static boolean showCart = false;
    public static MenuItem cartItem;
    private TextView coupenTitle;
    private TextView coupenExpiryDate;
    private TextView coupenBody;
    private TextView orignalPrice;
    private TextView discountedPrice;
    private RecyclerView coupensRecyclerview;
    private LinearLayout selectedCoupen;
    private LinearLayout ratingNoContainer;
    private ViewPager productDetailsviewpager;
    private TabLayout productDetailTablayout;
    private Button buyNowbtn;
    private LinearLayout addToCartBtn;
    private LinearLayout coupenRedemptionLayout;
    private Button coupenRedeemBtn;
    ////Product description///
    private TextView ProductTitle;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private String productOrignalPrice;
    private TextView cuttedPrice;
    private TextView codindicator;
    private TextView tvCODindicator;
    ////Product description///
    private TextView rewardTitle;
    private TextView rewardBody;
    private ConstraintLayout productDeatailsOnlyContainer;
    private ConstraintLayout productDeatailsTabsContainer;
    private ViewPager productImageviewpager;
    private TabLayout viewpagerindicator;
    ////Rating Layout
    private String productDescription;
    private String productOtherDetails;
    private TextView productOnlyDescriptionBody;
    private TextView totalRatings;
    private LinearLayout ratingProgressbarContainer;
    private TextView totalRatingsFigure;
    private TextView averageRating;
    private TextView averageRatingMiniView;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    ////
    private boolean inStock = false;
    private Dialog signInDailog;
    private Dialog loadingDialog;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentuser;
    private DocumentSnapshot documentSnapshot;
    private TextView badgecount;

    public static void setRating(int startposition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView startbtn = (ImageView) rateNowContainer.getChildAt(x);
            startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
            if (x <= startposition) {
                startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }


    }

    private void ShowDailogRecyclerview() {
        if (coupensRecyclerview.getVisibility() == View.GONE) {
            coupensRecyclerview.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerview.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    private String calculateAverageRatings(long currentUserRatings, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong((String) ratingNo.getText()) * x);
        }
        totalStars = totalStars + currentUserRatings;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong((String) totalRatingsFigure.getText())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong((String) totalRatingsFigure.getText()) + 1)).substring(0, 3);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Toolbar CODE IS hERE
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productImageviewpager = findViewById(R.id.product_images_viewpager);
        viewpagerindicator = findViewById(R.id.viewpager_indicator);
        addtowishlistbtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsviewpager = findViewById(R.id.product_details_viewpager);
        productDetailTablayout = findViewById(R.id.product_details_tabLayout);
        buyNowbtn = findViewById(R.id.buy_now_btn);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);

        coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
        coupenRedemptionLayout = findViewById(R.id.coupen_redemption_layout);

        ProductTitle = findViewById(R.id.product_title_at_wishlist);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_at_wishlist);
        totalRatingMiniView = findViewById(R.id.total_rating_miniview);
        productPrice = findViewById(R.id.product_price_at_wishlist);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCODindicator = findViewById(R.id.tv_cod_indicator);
        codindicator = findViewById(R.id.cod_indicator_textview);
        rewardTitle = findViewById(R.id.rewards_title);
        rewardBody = findViewById(R.id.rewards_body);
        productDeatailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDeatailsOnlyContainer = findViewById(R.id.product_detail_container);
        productOnlyDescriptionBody = findViewById(R.id.product_detail_body);
        totalRatings = findViewById(R.id.total_rating);
        ratingNoContainer = findViewById(R.id.rating_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingProgressbarContainer = findViewById(R.id.linear_layout_rating);
        averageRating = findViewById(R.id.average_rating);
        initialRating = -1;
        ///loading Dailog//////
        loadingDialog = new Dialog(ProductDetailActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dailog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ///loading Dailog//////


        //////Coupen Dailog Code start from here
        Dialog checkCoupenPriceDailog = new Dialog(ProductDetailActivity.this);
        checkCoupenPriceDailog.setContentView(R.layout.coupen_redeem_dailog);
        checkCoupenPriceDailog.setCancelable(true);
        checkCoupenPriceDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerview = checkCoupenPriceDailog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerview = checkCoupenPriceDailog.findViewById(R.id.coupen_recyclerView);
        selectedCoupen = checkCoupenPriceDailog.findViewById(R.id.selected_coupen_container);
        coupenTitle = checkCoupenPriceDailog.findViewById(R.id.coupen_title_at_reward);
        coupenExpiryDate = checkCoupenPriceDailog.findViewById(R.id.coupen_validity);
        coupenBody = checkCoupenPriceDailog.findViewById(R.id.coupen_body_at_reward);

        orignalPrice = checkCoupenPriceDailog.findViewById(R.id.orignal_price);
        discountedPrice = checkCoupenPriceDailog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerview.setLayoutManager(layoutManager);

        toggleRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDailogRecyclerview();
            }
        });

        /////Coupen dailog code end

        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                            productImages.add((String) documentSnapshot.get("product_image_" + x));
                                        }
                                        ProductimagesAdapter productimagesAdapter = new ProductimagesAdapter(productImages);
                                        productImageviewpager.setAdapter(productimagesAdapter);
                                        ProductTitle.setText((String) documentSnapshot.get("product_title"));
                                        averageRatingMiniView.setText((String) documentSnapshot.get("average_rating"));
                                        totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                                        productPrice.setText("Rs." + (String) documentSnapshot.get("product_price") + "/-");

                                        ///////for coupen Dialog
                                        orignalPrice.setText(productPrice.getText());
                                        productOrignalPrice = (String) documentSnapshot.get("product_price");
                                        MyRewardAdapter myRewardAdapter = new MyRewardAdapter(DBqueries.rewardModelList, true, coupensRecyclerview, selectedCoupen, productOrignalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice);
                                        coupensRecyclerview.setAdapter(myRewardAdapter);
                                        myRewardAdapter.notifyDataSetChanged();
                                        ///////for coupen Dialog

                                        cuttedPrice.setText("Rs." + (String) documentSnapshot.get("cutted_price") + "/-");
                                        if ((boolean) documentSnapshot.get("COD")) {
                                            codindicator.setVisibility(View.VISIBLE);
                                            tvCODindicator.setVisibility(View.VISIBLE);
                                        } else {
                                            codindicator.setVisibility(View.INVISIBLE);
                                            tvCODindicator.setVisibility(View.INVISIBLE);
                                        }
                                        rewardTitle.setText((long) documentSnapshot.get("free_coupen") + (String) documentSnapshot.get("free_coupen_title"));
                                        rewardBody.setText((String) documentSnapshot.get("free_coupen_body"));

                                        if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                            productDeatailsTabsContainer.setVisibility(View.VISIBLE);
                                            productDeatailsOnlyContainer.setVisibility(View.GONE);

                                            productDescription = (String) documentSnapshot.get("product_description");
                                            productOtherDetails = (String) documentSnapshot.get("product_other_detail");
                                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                                productSpecificationModelList.add(new ProductSpecificationModel(0, (String) documentSnapshot.get("spec_title_" + x)));
                                                for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                                    productSpecificationModelList.add(new ProductSpecificationModel(1,
                                                            (String) documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name")
                                                            , (String) documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value")));
                                                }
                                            }
                                        } else {

                                            productDeatailsTabsContainer.setVisibility(View.GONE);
                                            productDeatailsOnlyContainer.setVisibility(View.VISIBLE);
                                            productOnlyDescriptionBody.setText((String) documentSnapshot.get("product_description"));
                                        }

                                        totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                                        for (int x = 0; x < 5; x++) {
                                            TextView rating = (TextView) ratingNoContainer.getChildAt(x);
                                            rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                            ProgressBar progressBar = (ProgressBar) ratingProgressbarContainer.getChildAt(x);
                                            int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                            progressBar.setMax(maxProgress);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                        }
                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));


                                        averageRating.setText((String) documentSnapshot.get("average_rating"));
                                        productDetailsviewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailTablayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                                        if (currentuser != null) {

                                            if (DBqueries.myRating.size() == 0) {
                                                DBqueries.loadRatingList(ProductDetailActivity.this);
                                            }

                                            if (DBqueries.cartList.size() == 0) {
                                                DBqueries.loadCartList(ProductDetailActivity.this, loadingDialog, false, badgecount, new TextView(ProductDetailActivity.this));
                                            }

                                            if (DBqueries.wishList.size() == 0) {
                                                DBqueries.loadWishlist(ProductDetailActivity.this, loadingDialog, false);
                                                DBqueries.loadRatingList(ProductDetailActivity.this);
                                            }
                                            if (DBqueries.rewardModelList.size() == 0) {
                                                DBqueries.loadRewards(ProductDetailActivity.this, loadingDialog, false);
                                            }
                                            if (DBqueries.cartList.size() != 0 && DBqueries.wishList.size() != 0 && DBqueries.rewardModelList.size() != 0) {
                                                loadingDialog.dismiss();
                                            }
                                        } else {
                                            loadingDialog.dismiss();
                                        }

                                        if (DBqueries.myRatedIds.contains(productID)) {
                                            int index = DBqueries.myRatedIds.indexOf(productID);
                                            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
                                            setRating(initialRating);
                                        }
                                        if (DBqueries.cartList.contains(productID)) {
                                            ALREADY_ADDED_TO_CART = true;
                                        } else {
                                            ALREADY_ADDED_TO_CART = false;
                                        }
                                        if (DBqueries.wishList.contains(productID)) {
                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.purple_500));
                                        } else {
                                            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
                                            ALREADY_ADDED_TO_WISHLIST = false;
                                        }
                                        if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                            inStock = true;
                                            buyNowbtn.setVisibility(View.VISIBLE);
                                            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (currentuser == null) {
                                                        signInDailog.show();
                                                    } else {
                                                        if (!running_cart_query) {
                                                            running_cart_query = true;
                                                            if (ALREADY_ADDED_TO_CART) {
                                                                running_cart_query = false;
                                                                Toast.makeText(ProductDetailActivity.this, "Already Add to Cart!!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Map<String, Object> addProduct = new HashMap<>();
                                                                addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                                addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));

                                                                firebaseFirestore.collection("USERS").document(currentuser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                        .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            if (DBqueries.cartitemModelList.size() != 0) {
                                                                                DBqueries.cartitemModelList.add(0, new CartitemModel(documentSnapshot.getBoolean("COD"),CartitemModel.CART_ITEM, productID, (String) documentSnapshot.get("product_image_1"),
                                                                                        (String) documentSnapshot.get("product_title"),
                                                                                        (long) documentSnapshot.get("free_coupen"),
                                                                                        (String) documentSnapshot.get("product_price"),
                                                                                        (String) documentSnapshot.get("cutted_price"),
                                                                                        (long) 1,
                                                                                        (long) documentSnapshot.get("offers_applied"),
                                                                                        (long) 0,
                                                                                        inStock,
                                                                                        (long) documentSnapshot.get("max-quantity"),
                                                                                        (long) documentSnapshot.get("stock_quantity")
                                                                                ));

                                                                            }
                                                                            ALREADY_ADDED_TO_CART = true;
                                                                            DBqueries.cartList.add(productID);
                                                                            Toast.makeText(ProductDetailActivity.this, "Add to CART successfully!", Toast.LENGTH_SHORT).show();
                                                                            invalidateOptionsMenu();
                                                                            running_cart_query = false;
                                                                        } else {
                                                                            running_cart_query = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        } else {
                                            inStock = false;
                                            buyNowbtn.setVisibility(View.GONE);
                                            TextView outofStock = (TextView) addToCartBtn.getChildAt(0);
                                            outofStock.setText("Out of Stock");
                                            outofStock.setTextColor(getResources().getColor(R.color.purple_500));
                                            outofStock.setCompoundDrawables(null, null, null, null);
                                        }
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ///Connect tablayout and viewpager
        viewpagerindicator.setupWithViewPager(productImageviewpager, true);

        //////wishbtncode

        addtowishlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser == null) {
                    signInDailog.show();
                } else {
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishlist(index, ProductDetailActivity.this);
                            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
                        } else {
                            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.purple_500));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentuser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBqueries.wishlistModelList.size() != 0) {
                                            DBqueries.wishlistModelList.add(new WishlistModel(productID, (String) documentSnapshot.get("product_image_1"),
                                                    (String) documentSnapshot.get("product_title"),
                                                    (long) documentSnapshot.get("free_coupen"),
                                                    (String) documentSnapshot.get("average_rating"),
                                                    (long) documentSnapshot.get("total_ratings"),
                                                    (String) documentSnapshot.get("product_price"),
                                                    (String) documentSnapshot.get("cutted_price"),
                                                    (boolean) documentSnapshot.get("COD"),
                                                    inStock));
                                        }
                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.purple_500));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(ProductDetailActivity.this, "Add to wishlist successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                }
                            });
                        }
                    }
                }
            }
        });

        //product Details viewpager and tab layoout code here

        productDetailsviewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailTablayout));

        productDetailTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsviewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        ////Rating Layout
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int startposition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentuser == null) {
                        signInDailog.show();
                    } else {
                        if (startposition != initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;
                                setRating(startposition);

                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {

                                    TextView oldRating = (TextView) ratingNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingNoContainer.getChildAt(5 - startposition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong((String) oldRating.getText()) - 1);
                                    updateRating.put(startposition + 1 + "_star", Long.parseLong((String) finalRating.getText()) + 1);
                                    updateRating.put("average_rating", calculateAverageRatings((long) startposition - initialRating, true));

                                } else {
                                    updateRating.put(startposition + 1 + "_star", (long) documentSnapshot.get(startposition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRatings((long) startposition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }

                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myRating = new HashMap<>();

                                            if (DBqueries.myRatedIds.contains(productID)) {
                                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) startposition + 1);
                                            } else {
                                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) startposition + 1);
                                            }

                                            firebaseFirestore.collection("USERS").document(currentuser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DBqueries.myRatedIds.contains(productID)) {
                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) startposition + 1);

                                                            TextView oldRating = (TextView) ratingNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingNoContainer.getChildAt(5 - startposition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt((String) oldRating.getText()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt((String) finalRating.getText()) + 1));

                                                        } else {
                                                            DBqueries.myRatedIds.add(productID);
                                                            DBqueries.myRating.add((long) startposition + 1);

                                                            TextView rating = (TextView) ratingNoContainer.getChildAt(5 - startposition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt((String) rating.getText()) + 1));
                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                            Toast.makeText(ProductDetailActivity.this, "Thanks for rating us!", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingNoContainer.getChildAt(x);
                                                            ProgressBar progressBar = (ProgressBar) ratingProgressbarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt((String) totalRatingsFigure.getText());
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt((String) ratingfigures.getText()));
                                                        }
                                                        initialRating = startposition;
                                                        averageRating.setText(calculateAverageRatings(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRatings(0, true));

                                                        if (DBqueries.wishList.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                            int index = DBqueries.wishList.indexOf(productID);
                                                            DBqueries.wishlistModelList.get(index).setRating((String) averageRating.getText());
                                                            DBqueries.wishlistModelList.get(index).setTotalrating(Long.parseLong((String) totalRatingsFigure.getText()));
                                                        }

                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });
                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
        ////Rating Layout

        buyNowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser == null) {
                    signInDailog.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailActivity = ProductDetailActivity.this;
                    DeliveryActivity.cartitemModelList = new ArrayList<>();
                    DeliveryActivity.cartitemModelList.add(new CartitemModel(documentSnapshot.getBoolean("COD"),CartitemModel.CART_ITEM, productID, (String) documentSnapshot.get("product_image_1"),
                            (String) documentSnapshot.get("product_title"),
                            (long) documentSnapshot.get("free_coupen"),
                            (String) documentSnapshot.get("product_price"),
                            (String) documentSnapshot.get("cutted_price"),
                            (long) 1,
                            (long) documentSnapshot.get("offers_applied"),
                            (long) 0,
                            inStock,
                            (long) documentSnapshot.get("max-quantity"),
                            (long) documentSnapshot.get("stock_quantity")));
                    DeliveryActivity.cartitemModelList.add(new CartitemModel(CartitemModel.TOTAL_AMOUNT));
                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(ProductDetailActivity.this, loadingDialog,true);
                    } else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });


        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCoupenPriceDailog.show();
            }
        });

        ////signin dailog///

        signInDailog = new Dialog(ProductDetailActivity.this);
        signInDailog.setContentView(R.layout.sign_in_dialog);
        signInDailog.setCancelable(true);
        signInDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dailogSignInbtn = signInDailog.findViewById(R.id.cancel_btn);
        Button dailogSignUpbtn = signInDailog.findViewById(R.id.ok_btn);
        final Intent registerIntent = new Intent(ProductDetailActivity.this, RegisterActivity.class);

        dailogSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDailog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dailogSignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDailog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);

            }
        });

        ////signin dailog///

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
        if (currentuser == null) {
            coupenRedemptionLayout.setVisibility(View.GONE);
        } else {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }

        if (currentuser != null) {

            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingList(ProductDetailActivity.this);
            }
            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishlist(ProductDetailActivity.this, loadingDialog, false);
                DBqueries.loadRatingList(ProductDetailActivity.this);
            }
            if (DBqueries.rewardModelList.size() == 0) {
                DBqueries.loadRewards(ProductDetailActivity.this, loadingDialog, false);
            }
            if (DBqueries.cartList.size() != 0 && DBqueries.wishList.size() != 0 && DBqueries.rewardModelList.size() != 0) {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)) {
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
            setRating(initialRating);
        }

        if (DBqueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }

        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addtowishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.purple_500));
        } else {
            addtowishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem = menu.findItem(R.id.main_cart_icon);
        cartItem.setActionView(R.layout.badge_layout);
        ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_white_24);
        badgecount = cartItem.getActionView().findViewById(R.id.badge_count);
        if (currentuser != null) {
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetailActivity.this, loadingDialog, false, badgecount, new TextView(ProductDetailActivity.this));
            } else {
                badgecount.setVisibility(View.VISIBLE);
                if (DBqueries.cartList.size() < 99) {
                    badgecount.setText(String.valueOf(DBqueries.cartList.size()));
                } else {
                    badgecount.setText("99+");
                }
            }
        }

        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser == null) {
                    signInDailog.show();
                } else {
                    //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    //  fragmentTransaction.replace(R.id.main_container, new MyCartFragment()).commit();
                    Intent cartIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                    showCart = true;
                    startActivity(cartIntent);
                }
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            productDetailActivity = null;
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            if (currentuser == null) {
                signInDailog.show();
            } else {
                if (fromSearch){
                    finish();
                }else {
                    Intent searchIntent=new Intent(this,SearchActivity.class);
                    startActivity(searchIntent);
                }
            }
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentuser == null) {
                signInDailog.show();
            } else {

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch=false;
    }

    @Override
    public void onBackPressed() {
        productDetailActivity = null;
        super.onBackPressed();
    }
}




