package com.example.craft_shoping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView deliveryRecyclerview;
    private Button changeoraddnewaddressbtn;
    public static final int SELECT_ADDRESS=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        changeoraddnewaddressbtn=findViewById(R.id.change_or_add_address_btn);

        //Toolbar CODE IS hERE
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        //recyclerview code here
        deliveryRecyclerview=findViewById(R.id.delivery_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerview.setLayoutManager(layoutManager);

        List<CartitemModel> cartitemModelList=new ArrayList<>();
        cartitemModelList.add(new CartitemModel(0,R.drawable.mobile3,"Google Pixel XL",2,"Rs.42000/-","Rs.49000/-",1,0,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.mobile6,"Sumsung(Blue,64GB)",1,"Rs.39000/-","Rs.43000/-",2,1,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.mobile9,"Vivo S1(8GB,128GB)",0,"Rs.53000/-","Rs.64000/-",1,0,0));
        cartitemModelList.add(new CartitemModel(1,"Price (4 items)","Rs.173000/-","560","Rs.173560/-","Rs.5994/-"));

        CartAdapter cartAdapter=new CartAdapter(cartitemModelList);
        deliveryRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        changeoraddnewaddressbtn.setVisibility(View.VISIBLE);
        changeoraddnewaddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myaddressesIntent=new Intent(DeliveryActivity.this,MyAddressesActivity.class);
                myaddressesIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myaddressesIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}