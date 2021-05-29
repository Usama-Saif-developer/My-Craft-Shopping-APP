package com.example.craft_shoping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.craft_shoping.MainActivity.showCart;
import static com.example.craft_shoping.RegisterActivity.setSignUpFragment;

public class ProductDetailActivity extends AppCompatActivity {

    private ViewPager productDetailsviewpager;
    private TabLayout productDetailTablayout;
    private Button buyNowbtn;
    private LinearLayout addToCartBtn;

    private LinearLayout coupenRedemptionLayout;
    private Button coupenRedeemBtn;
    private TextView ProductTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private TextView codindicator;
    private TextView tvCODindicator;
    private TextView rewardTitle;
    private TextView rewardBody;


    ////Product description///

    private ConstraintLayout productDeatailsOnlyContainer;
    private ConstraintLayout productDeatailsTabsContainer;
    private ViewPager productImageviewpager;
    private TabLayout viewpagerindicator;

    private String productDescription;
    private String productOtherDetails;

    private TextView productOnlyDescriptionBody;
    ////Product description///

    ////Rating Layout
    private LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingNoContainer;
    private LinearLayout ratingProgressbarContainer;
    private TextView totalRatingsFigure;
    private TextView averageRating;
    ////Rating Layout

    private static boolean ALREADY_ADDED_TO_WISHLIST;
    private FloatingActionButton addtowishlistbtn;


    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    ////Coupen Dailog


    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;
    private static RecyclerView coupensRecyclerview;
    private static LinearLayout selectedCoupen;
    ////
    private Dialog signInDailog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentuser;

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
        coupenRedemptionLayout=findViewById(R.id.coupen_redemption_layout);

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

        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();
        firebaseFirestore.collection("PRODUCTS").document(getIntent().getStringExtra("PRODUCT_ID"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                        productImages.add((String) documentSnapshot.get("product_image_" + x));
                    }
                    ProductimagesAdapter productimagesAdapter = new ProductimagesAdapter(productImages);
                    productImageviewpager.setAdapter(productimagesAdapter);

                    ProductTitle.setText((String) documentSnapshot.get("product_title"));
                    averageRatingMiniView.setText((String) documentSnapshot.get("average_rating"));
                    totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                    productPrice.setText("Rs." + (String) documentSnapshot.get("product_price") + "/-");
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

                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + "ratings");
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
                } else {
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
                    if (ALREADY_ADDED_TO_WISHLIST) {
                        ALREADY_ADDED_TO_WISHLIST = false;
                        addtowishlistbtn.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D3D6")));
                    } else {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addtowishlistbtn.setSupportBackgroundTintList(getResources().getColorStateList(R.color.purple_500));
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
                        setRating(startposition);
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
                    Intent deliveryIntent = new Intent(ProductDetailActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser == null) {
                    signInDailog.show();
                } else {
                    //todo:Add to cart
                }
            }
        });

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

        TextView orignalPrice = checkCoupenPriceDailog.findViewById(R.id.orignal_price);
        TextView discountedPrice = checkCoupenPriceDailog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerview.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cash Back", "Valid before 2th May 2021", "Using this Coupen You can Get 20% OFF at any product above at Rs.500/- and below at Rs.2000/-"));
        rewardModelList.add(new RewardModel("Combo Coupen", "Valid before 13th Jan 2021", "Using this Coupen You can Get 30% OFF at any product above at Rs.3000/- "));
        rewardModelList.add(new RewardModel("Buy 1 get 1 free", "Till 22th May 2021", "Using this Coupen You can Get 50% OFF at any product above at Rs.500/- and below at Rs.2000/-"));
        rewardModelList.add(new RewardModel("Combo Coupen", "Valid before 5th April 2021", "70% Discount"));
        rewardModelList.add(new RewardModel("Cash Back", "Valid before 2th May 2021", "Using this Coupen You can Get 20% OFF at any product above at Rs.500/- and below at Rs.2000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 3 Free", "Only Till Ramzan", "Large Deal in Small price"));

        MyRewardAdapter myRewardAdapter = new MyRewardAdapter(rewardModelList, true);
        coupensRecyclerview.setAdapter(myRewardAdapter);
        myRewardAdapter.notifyDataSetChanged();

        toggleRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDailogRecyclerview();
            }
        });

        /////Coupen dailog code end
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
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser==null){
            coupenRedemptionLayout.setVisibility(View.GONE);
        }else {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }
    }

    public static void ShowDailogRecyclerview() {
        if (coupensRecyclerview.getVisibility() == View.GONE) {
            coupensRecyclerview.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerview.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    private void setRating(int startposition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView startbtn = (ImageView) rateNowContainer.getChildAt(x);
            startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
            if (x <= startposition) {
                startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentuser == null) {
                signInDailog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}