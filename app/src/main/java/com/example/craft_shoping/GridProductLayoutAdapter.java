package com.example.craft_shoping;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
    List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView==null)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent=new Intent(parent.getContext(),ProductDetailActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(position).getProductID());
                    parent.getContext().startActivity(productDetailsIntent);

                }
            });

            ImageView productImage=view.findViewById(R.id.h_s_product_image);
            TextView productTitle=view.findViewById(R.id.h_s_product_title);
            TextView productDescription=view.findViewById(R.id.h_s_product_description);
            TextView productPrice=view.findViewById(R.id.h_s_product_price);

            Glide.with(parent.getContext()).load(horizontalProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.categoryicon)).into(productImage);
            productTitle.setText(horizontalProductScrollModelList.get(position).getProducttitle());
            productDescription.setText(horizontalProductScrollModelList.get(position).getProductdescription());
            productPrice.setText("Rs."+horizontalProductScrollModelList.get(position).getProductprice()+"/-");

        }else
        {
            view=convertView;
        }
        return view;
    }
}
