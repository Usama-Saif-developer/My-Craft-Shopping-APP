package com.example.craft_shoping;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private boolean fromSearch;
    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastposition = -1;

    public WishlistAdapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public List<WishlistModel> getWishlistModelList() {
        return wishlistModelList;
    }

    public void setWishlistModelList(List<WishlistModel> wishlistModelList) {
        this.wishlistModelList = wishlistModelList;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        String productId = wishlistModelList.get(position).getProductId();
        String resource = wishlistModelList.get(position).getProductimage();
        String title = wishlistModelList.get(position).getProductTitle();
        long freecoupens = wishlistModelList.get(position).getFreeCoupens();
        String rating = wishlistModelList.get(position).getRating();
        long totalRating = wishlistModelList.get(position).getTotalrating();
        String productPrice = wishlistModelList.get(position).getProductprice();
        String cuttedPrice = wishlistModelList.get(position).getCuttedPrice();
        boolean paymentMethod = wishlistModelList.get(position).isCOD();
        boolean inStock = wishlistModelList.get(position).isInStock();
        holder.setDataAtwishlist(productId, resource, title, freecoupens, rating, totalRating, productPrice, cuttedPrice, paymentMethod, position, inStock);

        if (lastposition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastposition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView producutimage;
        private TextView productTitle;
        private TextView freeCoupens;
        private ImageView coupenIcon;
        private TextView rating;
        private TextView totalRatings;
        private View pricecut;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageButton deletedBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            producutimage = itemView.findViewById(R.id.product_image_at_wishlist);
            productTitle = itemView.findViewById(R.id.product_title_at_wishlist);
            freeCoupens = itemView.findViewById(R.id.free_coupen_at_wishlist);
            coupenIcon = itemView.findViewById(R.id.coupen_icon_at_wishlist);
            rating = itemView.findViewById(R.id.tv_product_rating_at_wishlist);
            totalRatings = itemView.findViewById(R.id.total_ratings_at_wishlist);
            pricecut = itemView.findViewById(R.id.price_cut_at_wishlist);
            productPrice = itemView.findViewById(R.id.product_price_at_wishlist);
            cuttedPrice = itemView.findViewById(R.id.cutted_price_at_wishlist);
            paymentMethod = itemView.findViewById(R.id.payment_method_at_wishlist);
            deletedBtn = itemView.findViewById(R.id.delete_btn_at_wishlist);
        }

        private void setDataAtwishlist(String productId, String resource, String title, long freecoupensNo, String averagerate, long totalRatingNo, String price, String cuttedPricevalue, boolean COD, int index, boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.categoryicon)).into(producutimage);
            productTitle.setText(title);
            if (freecoupensNo != 0 && inStock) {
                coupenIcon.setVisibility(View.VISIBLE);
                if (freecoupensNo == 1) {
                    freeCoupens.setText("free " + freecoupensNo + " Coupens");
                } else {
                    freeCoupens.setText("free " + freecoupensNo + " Coupens");
                }
            } else {
                coupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }
            LinearLayout linearLayoutrating = (LinearLayout) rating.getParent();
            if (inStock) {
                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setVisibility(View.VISIBLE);
                linearLayoutrating.setVisibility(View.VISIBLE);
                rating.setText(averagerate);
                totalRatings.setText("(" + totalRatingNo + ")ratings");
                productPrice.setText("Rs." + price + "/-");
                cuttedPrice.setText("Rs." + cuttedPricevalue + "/-");
                if (COD) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            } else {
                linearLayoutrating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.purple_500));
                cuttedPrice.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);
            }

            if (wishlist) {
                deletedBtn.setVisibility(View.VISIBLE);
            } else {
                deletedBtn.setVisibility(View.GONE);
            }
            deletedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailActivity.running_wishlist_query) {
                        ProductDetailActivity.running_wishlist_query = true;
                        DBqueries.removeFromWishlist(index, itemView.getContext());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fromSearch) {
                        ProductDetailActivity.fromSearch = true;
                    }
                    Intent productDetailIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    productDetailIntent.putExtra("PRODUCT_ID", productId);
                    itemView.getContext().startActivity(productDetailIntent);
                }
            });
        }
    }
}
