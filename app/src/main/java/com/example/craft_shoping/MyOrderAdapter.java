package com.example.craft_shoping;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position) {
        int resource=myOrderItemModelList.get(position).getProductImage();
        int rating=myOrderItemModelList.get(position).getRating();
        String title=myOrderItemModelList.get(position).getProductTitle();
        String delivedDate=myOrderItemModelList.get(position).getDeliveryStatus();
        holder.setData(resource,title,delivedDate,rating);
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateNowContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image_at_wishlist);
            orderIndicator=itemView.findViewById(R.id.order_indicator);
            productTitle=itemView.findViewById(R.id.product_title_at_wishlist);
            deliveryStatus=itemView.findViewById(R.id.order_delivery_date);
            rateNowContainer=itemView.findViewById(R.id.rate_now_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailIntent=new Intent(itemView.getContext(),OrderDetailsActivity.class);
                    itemView.getContext().startActivity(orderDetailIntent);

                }
            });
        }
        private void setData(int resource,String title,String deliveredDate,int rating){
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if(deliveredDate.equals("Cancelled")){
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.teal_700)));
            }else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.teal_200)));
            }
            deliveryStatus.setText(deliveredDate);



            ////Rating Layout
            for(int x=0; x<rateNowContainer.getChildCount();x++)
            {
                setRating(rating);
                final int startposition=x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRating(startposition);
                    }
                });
            }
            ////Rating Layout
        }
        private void setRating(int startposition) {
            for(int x=0;x<rateNowContainer.getChildCount();x++){
                ImageView startbtn=(ImageView)rateNowContainer.getChildAt(x);
                startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C9C8CA")));
                if (x<=startposition)
                {
                    startbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }

        }

    }
}
