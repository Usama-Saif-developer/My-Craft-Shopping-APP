package com.example.craft_shoping;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ///////Cart Adapter and model links with eachother

    private RecyclerView cartItemsRecyclerview;
    private Button Continuebtn;

    public MyCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCartFragment newInstance(String param1, String param2) {
        MyCartFragment fragment = new MyCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartItemsRecyclerview=view.findViewById(R.id.cart_item_recyclerview);
        Continuebtn=view.findViewById(R.id.cart_continue_btn);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerview.setLayoutManager(layoutManager);

        List<CartitemModel> cartitemModelList=new ArrayList<>();
        cartitemModelList.add(new CartitemModel(0,R.drawable.mobile3,"Google Pixel XL",2,"Rs.42000/-","Rs.49000/-",1,0,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.mobile6,"Sumsung(Blue,64GB)",1,"Rs.39000/-","Rs.43000/-",2,1,0));
        cartitemModelList.add(new CartitemModel(0,R.drawable.mobile9,"Vivo S1(8GB,128GB)",0,"Rs.53000/-","Rs.64000/-",1,0,0));
        cartitemModelList.add(new CartitemModel(1,"Price (4 items)","Rs.173000/-","560","Rs.173560/-","Rs.5994/-"));

        CartAdapter cartAdapter=new CartAdapter(cartitemModelList);
        cartItemsRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        Continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent=new Intent(getContext(),AddressActivity.class);
                getContext().startActivity(deliveryIntent);
            }
        });
        return view;
    }
}