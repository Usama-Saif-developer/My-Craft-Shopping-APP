package com.example.craft_shoping;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartitemModel> cartitemModelList;

    public CartAdapter(List<CartitemModel> cartitemModelList) {
        this.cartitemModelList = cartitemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartitemModelList.get(position).getType()) {
            case 0:
                return CartitemModel.CART_ITEM;
            case 1:
                return CartitemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartitemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(cartItemView);
            case CartitemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewHolder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartitemModelList.get(position).getType()) {
            case CartitemModel.CART_ITEM:
                int resource=cartitemModelList.get(position).getProductimage();
                String title=cartitemModelList.get(position).getProductTitle();
                int freeCoupens=cartitemModelList.get(position).getFreeCoupens();
                String productPrice=cartitemModelList.get(position).getProductPrice();
                String cuttedPrice=cartitemModelList.get(position).getCuttedPrice();
                int offersApplied=cartitemModelList.get(position).getOffersApplied();
                int productQuantity=cartitemModelList.get(position).getProductQuantity();
                int coupenApplied=cartitemModelList.get(position).getCoupedApplied();
                ((CartItemViewHolder)holder).setItemDetails(resource,title,freeCoupens,productPrice,cuttedPrice,offersApplied);
                break;
            case CartitemModel.TOTAL_AMOUNT:
                String totalItems=cartitemModelList.get(position).getTotalItems();
                String totalItemPrice=cartitemModelList.get(position).getTotalItemPrice();
                String deliveryPrice=cartitemModelList.get(position).getDeliveryPrice();
                String totalAmount=cartitemModelList.get(position).getTotalAmount();
                String saveAmount=cartitemModelList.get(position).getSaveAmount();
                ((CartTotalAmountViewHolder)holder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,saveAmount);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return cartitemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private ImageView freeCoupenIcon;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offerApplied;
        private TextView coupenApplied;
        private TextView productQuantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_at_wishlist);
            freeCoupenIcon = itemView.findViewById(R.id.free_coupen_icon);
            productTitle = itemView.findViewById(R.id.product_title_at_cart);
            freeCoupens = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.product_price_at_cart);
            cuttedPrice = itemView.findViewById(R.id.cutted_price_at_cart);
            offerApplied = itemView.findViewById(R.id.offers_applied);
            coupenApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }

        private void setItemDetails(int resource, String title, int freeCoupensNo, String productPriceText, String cuttedPriceText, int offersAppliedNo) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (freeCoupensNo > 0) {
                freeCoupenIcon.setVisibility(View.VISIBLE);
                freeCoupens.setVisibility(View.VISIBLE);
                if (freeCoupensNo == 1) {
                    freeCoupens.setText("free " + freeCoupensNo + " Coupen");
                } else {
                    freeCoupens.setText("free " + freeCoupensNo + " Coupen");
                }
            } else {
                freeCoupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }
            productPrice.setText(productPriceText);
            cuttedPrice.setText(cuttedPriceText);
            if (offersAppliedNo > 0) {
                offerApplied.setVisibility(View.VISIBLE);
                offerApplied.setText(offersAppliedNo + " offers Applied");
            } else {
                offerApplied.setVisibility(View.INVISIBLE);
            }
            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog quantityDailog=new Dialog(itemView.getContext());
                    quantityDailog.setContentView(R.layout.quantity_dailog);
                    quantityDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDailog.setCancelable(false);
                    EditText quantityno=quantityDailog.findViewById(R.id.quantity_no);
                    Button cancelBtn=quantityDailog.findViewById(R.id.cancel_btn);
                    Button okBtn=quantityDailog.findViewById(R.id.ok_btn);

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantityDailog.dismiss();
                        }
                    });
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productQuantity.setText("Qty: "+quantityno.getText());
                            quantityDailog.dismiss();
                        }
                    });
                    quantityDailog.show();
                }
            });
        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView totalItem;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView saveAmount;

        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItem = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            saveAmount = itemView.findViewById(R.id.saved_amount);
        }

        private void setTotalAmount(String totalItemText, String totalItemPriceText, String deliveryPriceText, String totalAmountText, String saveAmountText) {
            totalItem.setText(totalItemText);
            totalItemPrice.setText(totalItemPriceText);
            deliveryPrice.setText(deliveryPriceText);
            totalAmount.setText(totalAmountText);
            saveAmount.setText(saveAmountText);
        }
    }
}
