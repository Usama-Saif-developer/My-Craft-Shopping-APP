package com.example.craft_shoping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.example.craft_shoping.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView MyaddressesRecyclerview;
    private static AddressesAdapter addressesAdapter;
    private Button deliverHereBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        //Toolbar CODE IS hERE
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");


        deliverHereBtn=findViewById(R.id.deliver_here_btn);
        //addressesRecyclerview
        MyaddressesRecyclerview = findViewById(R.id.addresses_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyaddressesRecyclerview.setLayoutManager(layoutManager);
        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Usama Saif", "Gujranwala,punjab,pakistan", "34043", true));
        addressesModelList.add(new AddressesModel("Ali Raza", "Multan,punjab,pakistan", "73722", false));
        addressesModelList.add(new AddressesModel("Tuseef", "Sheikhupura,punjab,pakistan", "636372", false));
       // addressesModelList.add(new AddressesModel("Umar", "Faislabad,punjab,pakistan", "477383", false));
       // addressesModelList.add(new AddressesModel("Malik", "Lahore,punjab,pakistan", "782915", false));
       // addressesModelList.add(new AddressesModel("Rana", "gujrat,punjab,pakistan", "524272", false));
        int mode=getIntent().getIntExtra("MODE",-1);
        if (mode==SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else{
            deliverHereBtn.setVisibility(View.GONE);
        }
        addressesAdapter = new AddressesAdapter(addressesModelList,mode);
        MyaddressesRecyclerview.setAdapter(addressesAdapter);
        ((SimpleItemAnimator) MyaddressesRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();
    }

    public static void refreshItem(int deSelect, int Select) {
        addressesAdapter.notifyItemChanged(deSelect);
        addressesAdapter.notifyItemChanged(Select);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}