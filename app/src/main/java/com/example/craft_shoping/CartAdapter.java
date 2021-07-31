package com.example.craft_shoping;

import android.app.Dialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartitemModel> cartitemModelList;
    private int lastposition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartitemModel> cartitemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartitemModelList = cartitemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
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
                String productID = cartitemModelList.get(position).getProductID();
                String resource = cartitemModelList.get(position).getProductimage();
                String title = cartitemModelList.get(position).getProductTitle();
                Long freeCoupens = cartitemModelList.get(position).getFreeCoupens();
                String productPrice = cartitemModelList.get(position).getProductPrice();
                String cuttedPrice = cartitemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartitemModelList.get(position).getOffersApplied();
                Long productQuantity = cartitemModelList.get(position).getProductQuantity();
                Long coupenApplied = cartitemModelList.get(position).getCoupedApplied();
                boolean inStock = cartitemModelList.get(position).isInStock();
                Long maxQuantity = cartitemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartitemModelList.get(position).isQtyError();
                List<String> qtyIds = cartitemModelList.get(position).getQtyIDs();
                long stockQty = cartitemModelList.get(position).getStockQuantity();
                boolean COD=cartitemModelList.get(position).isCOD();
                ((CartItemViewHolder) holder).setItemDetails(productID, resource, title, freeCoupens, productPrice, cuttedPrice, offersApplied, position, inStock, String.valueOf(productQuantity), maxQuantity, qtyError, qtyIds, stockQty,COD);
                break;
            case CartitemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int saveAmount = 0;

                for (int x = 0; x < cartitemModelList.size(); x++) {
                    if (cartitemModelList.get(x).getType() == CartitemModel.CART_ITEM && cartitemModelList.get(x).isInStock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartitemModelList.get(x).getProductQuantity()));
                        totalItems = totalItems + quantity;
                        if (TextUtils.isEmpty(cartitemModelList.get(x).getSelectedCoupenId())) {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartitemModelList.get(x).getProductPrice()) * quantity;
                        } else {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartitemModelList.get(x).getDiscountedPrice()) * quantity;
                        }
                        if (!TextUtils.isEmpty(cartitemModelList.get(x).getCuttedPrice())) {
                            saveAmount = saveAmount + (Integer.parseInt(cartitemModelList.get(x).getCuttedPrice()) - Integer.parseInt(cartitemModelList.get(x).getProductPrice())) * quantity;
                            if (!TextUtils.isEmpty(cartitemModelList.get(x).getSelectedCoupenId())) {
                                saveAmount = saveAmount + (Integer.parseInt(cartitemModelList.get(x).getProductPrice()) - Integer.parseInt(cartitemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        } else {
                            if (!TextUtils.isEmpty(cartitemModelList.get(x).getSelectedCoupenId())) {
                                saveAmount = saveAmount + (Integer.parseInt(cartitemModelList.get(x).getProductPrice()) - Integer.parseInt(cartitemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }
                    }
                }
                if (totalItemPrice > 500) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                } else {
                    deliveryPrice = "99";
                    totalAmount = totalItemPrice + 99;
                }
                cartitemModelList.get(position).setTotalItems(totalItems);
                cartitemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartitemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartitemModelList.get(position).setTotalAmount(totalAmount);
                cartitemModelList.get(position).setSavedAmount(saveAmount);

                ((CartTotalAmountViewHolder) holder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, saveAmount);
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
        private LinearLayout deleteBtn;
        private LinearLayout coupenRedemptionLayout;
        private TextView coupenRedemptionBody;
        private Button redeemBtn;
        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;
        private TextView orignalPrice;
        private TextView discountedPrice;
        private Button removeCoupenBtn, applyCoupenBtn;
        private String productOrignalPrice;
        private LinearLayout applyORremoveBtnConntainer;
        private TextView footerText;
        private RecyclerView coupensRecyclerview;
        private LinearLayout selectedCoupen;
        private ImageView codIndicator;
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
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
            redeemBtn = itemView.findViewById(R.id.coupen_redemption_btn);
            coupenRedemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            coupenRedemptionBody = itemView.findViewById(R.id.tv_coupen_redemption);
            codIndicator=itemView.findViewById(R.id.cod_indicator);
        }


        private void setItemDetails(String productID, String resource, String title, Long freeCoupensNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, int position, boolean inStock, String quantity, Long maxQuantity, boolean qtyError, List<String> qtyIds, long stockQty,boolean COD) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.categoryicon)).into(productImage);
            productTitle.setText(title);

            Dialog checkCoupenPriceDailog = new Dialog(itemView.getContext());
            checkCoupenPriceDailog.setContentView(R.layout.coupen_redeem_dailog);
            checkCoupenPriceDailog.setCancelable(false);
            checkCoupenPriceDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (COD){
                codIndicator.setVisibility(View.VISIBLE);
            }else{
                codIndicator.setVisibility(View.INVISIBLE);
            }

            if (inStock) {
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
                productPrice.setText("Rs." + productPriceText + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs." + cuttedPriceText + "/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);


                //////Coupen Dailog Code start from here

                ImageView toggleRecyclerview = checkCoupenPriceDailog.findViewById(R.id.toggle_recyclerview);
                coupensRecyclerview = checkCoupenPriceDailog.findViewById(R.id.coupen_recyclerView);
                selectedCoupen = checkCoupenPriceDailog.findViewById(R.id.selected_coupen_container);
                coupenTitle = checkCoupenPriceDailog.findViewById(R.id.coupen_title_at_reward);
                coupenExpiryDate = checkCoupenPriceDailog.findViewById(R.id.coupen_validity);
                coupenBody = checkCoupenPriceDailog.findViewById(R.id.coupen_body_at_reward);
                footerText = checkCoupenPriceDailog.findViewById(R.id.footer_text);
                applyORremoveBtnConntainer = checkCoupenPriceDailog.findViewById(R.id.apply_or_remove_btns_container);
                removeCoupenBtn = checkCoupenPriceDailog.findViewById(R.id.remove_btn);
                applyCoupenBtn = checkCoupenPriceDailog.findViewById(R.id.apply_btn);

                footerText.setVisibility(View.GONE);
                applyORremoveBtnConntainer.setVisibility(View.VISIBLE);

                orignalPrice = checkCoupenPriceDailog.findViewById(R.id.orignal_price);
                discountedPrice = checkCoupenPriceDailog.findViewById(R.id.discounted_price);

                LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                coupensRecyclerview.setLayoutManager(layoutManager);

                orignalPrice.setText(productPrice.getText());
                productOrignalPrice = productPriceText;
                MyRewardAdapter myRewardAdapter = new MyRewardAdapter(position, DBqueries.rewardModelList, true, coupensRecyclerview, selectedCoupen, productOrignalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice, cartitemModelList);
                coupensRecyclerview.setAdapter(myRewardAdapter);
                myRewardAdapter.notifyDataSetChanged();
                applyCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(cartitemModelList.get(position).getSelectedCoupenId())) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                                    rewardModel.setAlreadyUsed(true);
                                    coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewards_gradient_background));
                                    coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                                    redeemBtn.setText("Coupen");
                                }
                            }
                            coupenApplied.setVisibility(View.VISIBLE);
                            cartitemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2));
                            productPrice.setText(discountedPrice.getText());
                            String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2)));
                            coupenApplied.setText("Coupen Applied -Rs." + offerDiscountedAmt + "/-");
                            notifyItemChanged(cartitemModelList.size() - 1);
                            checkCoupenPriceDailog.dismiss();
                        }
                    }
                });

                removeCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                        coupenTitle.setText("Coupen");
                        coupenExpiryDate.setText("Coupen Validity");
                        coupenBody.setText("Tap the icon on the right corner to select your coupen.");
                        coupenApplied.setVisibility(View.INVISIBLE);
                        coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.purple_700));
                        coupenRedemptionBody.setText("Apply your coupen Here.");
                        redeemBtn.setText("Redeem");
                        cartitemModelList.get(position).setSelectedCoupenId(null);
                        productPrice.setText("Rs." + productPriceText + "/-");
                        notifyItemChanged(cartitemModelList.size() - 1);
                        checkCoupenPriceDailog.dismiss();
                    }
                });

                toggleRecyclerview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowDailogRecyclerview();
                    }
                });

                if (!TextUtils.isEmpty(cartitemModelList.get(position).getSelectedCoupenId())) {
                    for (RewardModel rewardModel : DBqueries.rewardModelList) {
                        if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                            coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewards_gradient_background));
                            coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                            redeemBtn.setText("Coupen");
                            coupenBody.setText(rewardModel.getCoupenBody());
                            if (rewardModel.getType().equals("Discount")) {
                                coupenTitle.setText(rewardModel.getType());
                            } else {
                                coupenTitle.setText("FLAT Rs." + rewardModel.getDiscORamt() + " OFF");
                            }
                            ////validity code

                            Long eMills = Long.parseLong(rewardModel.getTimestamp());
                            MyRewardAdapter.simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            MyRewardAdapter.simpleTimeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                            String date = MyRewardAdapter.simpleDateFormat.format(eMills);
                            String time = MyRewardAdapter.simpleTimeFormat.format(eMills);
                            MyRewardAdapter.datentime = date + " " + time;
                            coupenExpiryDate.setText(MyRewardAdapter.datentime);
                        }
                    }
                    discountedPrice.setText("Rs." + cartitemModelList.get(position).getDiscountedPrice() + "/-");
                    coupenApplied.setVisibility(View.VISIBLE);
                    productPrice.setText("Rs." + cartitemModelList.get(position).getDiscountedPrice() + "/-");
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(cartitemModelList.get(position).getDiscountedPrice()));
                    coupenApplied.setText("Coupen Applied -Rs." + offerDiscountedAmt + "/-");
                } else {
                    coupenApplied.setVisibility(View.INVISIBLE);
                    coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.purple_700));
                    coupenRedemptionBody.setText("Apply your coupen Here.");
                    redeemBtn.setText("Redeem");
                }

                /////Coupen dailog code end

                productQuantity.setText("Qty: " + quantity);
                if (!showDeleteBtn) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.teal_700));
//                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.teal_700)));
                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                    }
                }
                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog quantityDailog = new Dialog(itemView.getContext());
                        quantityDailog.setContentView(R.layout.quantity_dailog);
                        quantityDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDailog.setCancelable(false);
                        EditText quantityno = quantityDailog.findViewById(R.id.quantity_no);
                        Button cancelBtn = quantityDailog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDailog.findViewById(R.id.ok_btn);
                        quantityno.setHint("Max " + String.valueOf(maxQuantity));

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDailog.dismiss();
                            }
                        });
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantityno.getText())) {
                                    if (Long.valueOf(quantityno.getText().toString()) <= maxQuantity && Long.valueOf(quantityno.getText().toString()) != 0) {
                                        if (itemView.getContext() instanceof MainActivity) {
                                            cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityno.getText().toString()));
                                        } else {
                                            if (DeliveryActivity.fromCart) {
                                                cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityno.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityno.getText().toString()));
                                            }
                                        }
                                        productQuantity.setText("Qty: " + quantityno.getText());
                                        notifyItemChanged(cartitemModelList.size() - 1);
                                        if (!showDeleteBtn) {
                                            DeliveryActivity.loadingDialog.show();
                                            DeliveryActivity.cartitemModelList.get(position).setQtyError(false);
                                            final int initialQty = Integer.parseInt(quantity);
                                            final int finalQty = Integer.parseInt(quantityno.getText().toString());
                                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                            if (finalQty > initialQty) {
                                                for (int y = 0; y < finalQty - initialQty; y++) {
                                                    String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                    Map<String, Object> timestamp = new HashMap<>();
                                                    timestamp.put("time", FieldValue.serverTimestamp());
                                                    int finalY = y;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.add(quantityDocumentName);
                                                                    if (finalY + 1 == finalQty - initialQty) {
                                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            List<String> serverQuantity = new ArrayList<>();
                                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                                serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                            }
                                                                                            long availableQty = 0;
                                                                                            for (String qtyId : qtyIds) {
                                                                                                if (!serverQuantity.contains(qtyId)) {
                                                                                                    DeliveryActivity.cartitemModelList.get(position).setQtyError(true);
                                                                                                    DeliveryActivity.cartitemModelList.get(position).setMaxQuantity(availableQty);
                                                                                                    Toast.makeText(itemView.getContext(), "Sorry,All products may not be Available in required Quantity...!", Toast.LENGTH_SHORT).show();
                                                                                                } else {
                                                                                                    availableQty++;
                                                                                                }
                                                                                            }
                                                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                        } else {
                                                                                            String error = task.getException().getMessage();
                                                                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                        DeliveryActivity.loadingDialog.dismiss();
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            });
                                                }
                                            } else if (initialQty > finalQty) {
                                                for (int x = 0; x < initialQty - finalQty; x++) {
                                                    String qtyId = qtyIds.get(qtyIds.size() - 1 - x);
                                                    int finalX = x;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.remove(qtyId);
                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                    if (finalX + 1 == initialQty - finalQty) {
                                                                        DeliveryActivity.loadingDialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max Quantity :" + maxQuantity.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                quantityDailog.dismiss();
                            }
                        });
                        quantityDailog.show();
                    }
                });
                if (offersAppliedNo > 0) {
                    offerApplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(cuttedPriceText) - Long.valueOf(productPriceText));
                    offerApplied.setText("offers Applied - Rs." + offerDiscountedAmt + "/-");
                } else {
                    offerApplied.setVisibility(View.INVISIBLE);
                }
            } else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.purple_500));
                cuttedPrice.setText(" ");
                coupenRedemptionLayout.setVisibility(View.GONE);
                productQuantity.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
                coupenApplied.setVisibility(View.GONE);
                offerApplied.setVisibility(View.GONE);
                freeCoupenIcon.setVisibility(View.INVISIBLE);
            }

            if (showDeleteBtn) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }

            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RewardModel rewardModel : DBqueries.rewardModelList) {
                        if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                            rewardModel.setAlreadyUsed(false);
                        }
                    }
                    checkCoupenPriceDailog.show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(cartitemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                    }
                    if (!ProductDetailActivity.running_cart_query) {
                        ProductDetailActivity.running_cart_query = true;
                        DBqueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });
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

        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int saveAmountText) {
            totalItem.setText("Price(" + totalItemText + " items)");
            totalItemPrice.setText("Rs." + totalItemPriceText + "/-");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            } else {
                deliveryPrice.setText("Rs." + deliveryPriceText + "/-");
            }
            totalAmount.setText("Rs." + totalAmountText + "/-");
            cartTotalAmount.setText("Rs." + totalAmountText + "/-");
            saveAmount.setText("You saved Rs." + saveAmountText + "/- on this Order");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0) {
                if (DeliveryActivity.fromCart) {
                    cartitemModelList.remove(cartitemModelList.size() - 1);
                    DeliveryActivity.cartitemModelList.remove(DeliveryActivity.cartitemModelList.size() - 1);
                }
                if (showDeleteBtn) {
                    cartitemModelList.remove(cartitemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }

        }
    }
}
