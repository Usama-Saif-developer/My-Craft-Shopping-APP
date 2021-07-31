package com.example.craft_shoping;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyRewardAdapter extends RecyclerView.Adapter<MyRewardAdapter.ViewHolder> {
    public static String datentime;
    public static SimpleDateFormat simpleDateFormat, simpleTimeFormat;
    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView coupensRecyclerview;
    private LinearLayout selectedCoupen;
    private String productOrignalPrice;
    private TextView selectedCoupenTitle;
    private TextView selectedCoupenExpiryDate;
    private TextView selectedCoupenBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List<CartitemModel> cartitemModelList;

    public MyRewardAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    public MyRewardAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerview, LinearLayout selectedCoupen, String productOrignalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerview = coupensRecyclerview;
        this.selectedCoupen = selectedCoupen;
        this.productOrignalPrice = productOrignalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
    }

    public MyRewardAdapter(int cartItemPosition, List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerview, LinearLayout selectedCoupen, String productOrignalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice,List<CartitemModel> cartitemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerview = coupensRecyclerview;
        this.selectedCoupen = selectedCoupen;
        this.productOrignalPrice = productOrignalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartitemModelList=cartitemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useMiniLayout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String coupenId = rewardModelList.get(position).getCoupenId();
        String type = rewardModelList.get(position).getType();
        String validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCoupenBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String disORamt = rewardModelList.get(position).getDiscORamt();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();
        holder.setDataAtReward(coupenId, type, validity, body, upperLimit, lowerLimit, disORamt, alreadyUsed);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView expiryDate;
        private TextView coupenBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title_at_reward);
            expiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body_at_reward);
        }

        private void setDataAtReward(String coupenId, String type, String validity, String body, String upperLimit, String lowerLimit, String disORamt, boolean alreadyUsed) {
            if (type.equals("Discount")) {
                coupenTitle.setText(type);
            } else {
                coupenTitle.setText("FLAT Rs." + disORamt + " OFF");
            }

            Long eMills = Long.parseLong(validity);
            simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            simpleTimeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String date = simpleDateFormat.format(eMills);
            String time = simpleTimeFormat.format(eMills);
            datentime = date + " " + time;

            if (alreadyUsed) {
                expiryDate.setText("Already used");
                expiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.teal_700));
                coupenBody.setTextColor(Color.parseColor("#50ffffff"));
                coupenTitle.setTextColor(Color.parseColor("#50ffffff"));
            } else {
                /////Rewards Code
                coupenBody.setTextColor(Color.parseColor("#ffffff"));
                coupenTitle.setTextColor(Color.parseColor("#ffffff"));
                expiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.purple_500));
                expiryDate.setText(datentime);
            }
            coupenBody.setText(body);

            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!alreadyUsed) {
                            selectedCoupenTitle.setText(type);
                            selectedCoupenExpiryDate.setText(datentime);
                            selectedCoupenBody.setText(body);
                            if (Long.valueOf(productOrignalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOrignalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = Long.valueOf(productOrignalPrice) * Long.valueOf(disORamt) / 100;
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOrignalPrice) - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOrignalPrice) - Long.valueOf(disORamt)) + "/-");
                                }
                                if (cartItemPosition != -1) {
                                    cartitemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                                }
                            } else {
                                if (cartItemPosition != -1) {
                                    cartitemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                                }
                                discountedPrice.setText("Invalid");
                                Toast.makeText(itemView.getContext(), "Sorry !Product does not match coupen terms.", Toast.LENGTH_SHORT).show();
                            }

                            if (coupensRecyclerview.getVisibility() == View.GONE) {
                                coupensRecyclerview.setVisibility(View.VISIBLE);
                                selectedCoupen.setVisibility(View.GONE);
                            } else {
                                coupensRecyclerview.setVisibility(View.GONE);
                                selectedCoupen.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
                /////Rewards Code
            }
        }
    }
}
