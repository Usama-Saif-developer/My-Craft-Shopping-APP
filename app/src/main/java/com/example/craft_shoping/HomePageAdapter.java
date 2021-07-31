package com.example.craft_shoping;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int lastposition = -1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.Banner_Slider;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.Banner_Slider:
                View bannersliderview = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewHolder(bannersliderview);

            case HomePageModel.STRIP_AD_BANNER:
                View stripAdview = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdBannerViewHolder(stripAdview);

            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalproductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewHolder(horizontalproductview);

            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridproductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewHolder(gridproductview);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.Banner_Slider:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannersliderviewpager(sliderModelList);
                break;
            case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResourse();
                String color = homePageModelList.get(position).getBackgroundColor();
                ((StripAdBannerViewHolder) holder).setStripAd(resource, color);
                break;
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String layoutcolor = homePageModelList.get(position).getBackgroundColor();
                String horizontalLayoutTitle = homePageModelList.get(position).getTitle();
                List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((HorizontalProductViewHolder) holder).setHorizontalproductlayout(horizontalProductScrollModelList, horizontalLayoutTitle, layoutcolor, viewAllProductList);
                break;
            case HomePageModel.GRID_PRODUCT_VIEW:
                String gridlayoutColor = homePageModelList.get(position).getBackgroundColor();
                String gridLayoutTitle = homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewHolder) holder).setGridproductLayout(gridProductScrollModelList, gridLayoutTitle, gridlayoutColor);
                break;
            default:
                return;
        }
        if (lastposition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastposition = position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    ////////Banner Slider view holder class with its function
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        final private long DELAY_TIMER = 3000;
        final private long PERIOD_TIMER = 3000;
        /////banner slider
        private ViewPager bannersliderviewpager;
        private int currentpage;
        private Timer timer;
        private List<SliderModel> arrangeList;
        ////banner slider

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannersliderviewpager = itemView.findViewById(R.id.banner_slider_view_pager);

        }

        private void setBannersliderviewpager(List<SliderModel> sliderModelList) {
            currentpage = 2;
            if (timer != null) {
                timer.cancel();
            }
            arrangeList = new ArrayList<>();
            for (int x = 0; x < sliderModelList.size(); x++) {
                arrangeList.add(x, sliderModelList.get(x));
            }
            arrangeList.add(0, sliderModelList.get(sliderModelList.size() - 2));
            arrangeList.add(1, sliderModelList.get(sliderModelList.size() - 1));
            arrangeList.add(sliderModelList.get(0));
            arrangeList.add(sliderModelList.get(1));


            SliderAdapter sliderAdapter = new SliderAdapter(arrangeList);
            bannersliderviewpager.setAdapter(sliderAdapter);
            bannersliderviewpager.setClipToPadding(false);
            bannersliderviewpager.setPageMargin(20);
            bannersliderviewpager.setCurrentItem(currentpage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentpage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrangeList);
                    }
                }

            };
            bannersliderviewpager.addOnPageChangeListener(onPageChangeListener);

            startbannerslideshow(arrangeList);

            bannersliderviewpager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLooper(arrangeList);
                    stopbannerslideshow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startbannerslideshow(arrangeList);
                    }
                    return false;
                }
            });
            //banner slider code
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentpage == sliderModelList.size() - 2) {
                currentpage = 2;
                bannersliderviewpager.setCurrentItem(currentpage, false);
            }
            if (currentpage == 1) {
                currentpage = sliderModelList.size() - 3;
                bannersliderviewpager.setCurrentItem(currentpage, false);
            }
        }

        private void startbannerslideshow(List<SliderModel> sliderModelList) {
            Handler handler = new Handler();
            Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentpage >= sliderModelList.size()) {
                        currentpage = 1;
                    }
                    bannersliderviewpager.setCurrentItem(currentpage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIMER, PERIOD_TIMER);
        }

        private void stopbannerslideshow() {
            timer.cancel();
        }
    }

    ////////Strip Ad class with its function
    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder {
        //Strip ad variable
        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        //strip ad
        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);

            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);

        }

        private void setStripAd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.slidericon)).into(stripAdImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    ////////Horizontal product view Holder
    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder {
        //Horizontal scroll variables
        private TextView horizontallayoutTitle;
        private Button horizontalviewAllbtn;
        private RecyclerView horizontalRecyclerview;
        private ConstraintLayout container;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_layout);
            horizontallayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalviewAllbtn = itemView.findViewById(R.id.horizontal_scroll_view_all_btn);
            horizontalRecyclerview = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalRecyclerview.setRecycledViewPool(recycledViewPool);
        }

        private void setHorizontalproductlayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, String title, String color, List<WishlistModel> viewAllProductList) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontallayoutTitle.setText(title);
            for (HorizontalProductScrollModel model : horizontalProductScrollModelList) {
                if (!model.getProductID().isEmpty() && model.getProducttitle().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                model.setProducttitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProductprice(task.getResult().getString("product_price"));

                                WishlistModel wishlistModel = viewAllProductList
                                        .get(horizontalProductScrollModelList.indexOf(model));
                                wishlistModel.setTotalrating(task.getResult().getLong("total_ratings"));
                                wishlistModel.setRating(task.getResult().getString("average_rating"));
                                wishlistModel.setProductTitle(task.getResult().getString("product_title"));
                                wishlistModel.setProductprice(task.getResult().getString("product_price"));
                                wishlistModel.setProductimage(task.getResult().getString("product_image_1"));
                                wishlistModel.setFreeCoupens(task.getResult().getLong("free_coupen"));
                                wishlistModel.setCuttedPrice(task.getResult().getString("cutted_price"));
                                wishlistModel.setCOD(task.getResult().getBoolean("COD"));
                                wishlistModel.setInStock(task.getResult().getLong("stock_quantity") > 0);

                                if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size() - 1) {
                                    if (horizontalRecyclerview.getAdapter() != null){
                                        horizontalRecyclerview.getAdapter().notifyDataSetChanged();
                                    }
                                }
                            } else {
                                ////do nothing
                            }
                        }
                    });
                }
            }
            if (horizontalProductScrollModelList.size() > 8) {
                horizontalviewAllbtn.setVisibility(View.VISIBLE);
                horizontalviewAllbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishlistModelList = viewAllProductList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 0);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            } else {
                horizontalviewAllbtn.setVisibility(View.INVISIBLE);
            }
            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerview.setLayoutManager(linearLayoutManager);
            horizontalRecyclerview.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }
    }

    //////Grid product view holde
    public class GridProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout grid_container_layout;
        private TextView gridLayoutTitle;
        private Button gridLayoutviewallbtn;
        private GridLayout gridproductlayout;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            grid_container_layout = itemView.findViewById(R.id.grid_container_layout);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutviewallbtn = itemView.findViewById(R.id.grid_product_layout_viewall_btn);
            gridproductlayout = itemView.findViewById(R.id.grid_layout);
        }

        private void setGridproductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, String title, String color) {
            grid_container_layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);

            for (HorizontalProductScrollModel model : horizontalProductScrollModelList) {
                if (!model.getProductID().isEmpty() && model.getProducttitle().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                model.setProducttitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProductprice(task.getResult().getString("product_price"));

                                if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size() - 1) {
                                    setGrideData(title,horizontalProductScrollModelList);
                                    if(!title.equals("")){
                                        gridLayoutviewallbtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ViewAllActivity.horizontalProductScrollModelList=horizontalProductScrollModelList;
                                                Intent gridviewAllIntent=new Intent(itemView.getContext(),ViewAllActivity.class);
                                                gridviewAllIntent.putExtra("title",title);
                                                gridviewAllIntent.putExtra("layout_code",1);
                                                itemView.getContext().startActivity(gridviewAllIntent);
                                            }
                                        });
                                    }
                                }
                            } else {
                                ////do nothing
                            }
                        }
                    });
                }
            }
            setGrideData(title,horizontalProductScrollModelList);

        }
        private void setGrideData(String title,List<HorizontalProductScrollModel> horizontalProductScrollModelList){

            for (int x = 0; x < 4; x++) {
                ImageView productImage = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_image);
                TextView productTitle = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_title);
                TextView productDescription = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_description);
                TextView productPrice = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_price);

                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.categoryicon)).into(productImage);
                productTitle.setText(horizontalProductScrollModelList.get(x).getProducttitle());
                productDescription.setText(horizontalProductScrollModelList.get(x).getProductdescription());
                productPrice.setText("Rs." + horizontalProductScrollModelList.get(x).getProductprice() + "/-");
                gridproductlayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                if (!title.equals("")) {
                    int finalX = x;
                    gridproductlayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                            productDetailIntent.putExtra("PRODUCT_ID", horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailIntent);
                        }
                    });
                }
            }
        }
    }
}
