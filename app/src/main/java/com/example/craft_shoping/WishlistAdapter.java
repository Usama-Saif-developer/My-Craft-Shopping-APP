package com.example.craft_shoping;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;

    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist=wishlist;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        String resource=wishlistModelList.get(position).getProductimage();
        String title=wishlistModelList.get(position).getProductTitle();
        long freecoupens=wishlistModelList.get(position).getFreeCoupens();
        String rating=wishlistModelList.get(position).getRating();
        long totalRating=wishlistModelList.get(position).getTotalrating();
        String productPrice=wishlistModelList.get(position).getProductprice();
        String cuttedPrice=wishlistModelList.get(position).getCuttedPrice();
        boolean paymentMethod=wishlistModelList.get(position).isCOD();
        holder.setDataAtwishlist(resource,title,freecoupens,rating,totalRating,productPrice,cuttedPrice,paymentMethod);
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

        private void setDataAtwishlist(String resource, String title, long freecoupensNo,String averagerate,long totalRatingNo,String price,String cuttedPricevalue,boolean COD) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_home_24)).into(producutimage);
            productTitle.setText(title);
            if (freecoupensNo != 0) {
                coupenIcon.setVisibility(View.VISIBLE);
                if (freecoupensNo == 1) {
                    freeCoupens.setText("free "+freecoupensNo+" Coupens");
                } else {
                    freeCoupens.setText("free "+freecoupensNo+" Coupens");
                }
            } else {
                coupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }
            rating.setText(averagerate);
            totalRatings.setText("("+totalRatingNo+")ratings");
            productPrice.setText("Rs."+price+"/-");
            cuttedPrice.setText("Rs."+cuttedPricevalue+"/-");
            if(COD){
                paymentMethod.setVisibility(View.VISIBLE);
            }else{
                paymentMethod.setVisibility(View.INVISIBLE);
            }

            if (wishlist){
                deletedBtn.setVisibility(View.VISIBLE);
            }else{
                deletedBtn.setVisibility(View.GONE);
            }
            deletedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "deletebtn work properly", Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent=new Intent(itemView.getContext(),ProductDetailActivity.class);
                    itemView.getContext().startActivity(productDetailIntent);
                }
            });
        }
    }
}
