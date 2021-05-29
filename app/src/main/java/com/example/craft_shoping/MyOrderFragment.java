package com.example.craft_shoping;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView myOrderRecyclerview;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrderFragment newInstance(String param1, String param2) {
        MyOrderFragment fragment = new MyOrderFragment();
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
        View view= inflater.inflate(R.layout.fragment_my_order2, container, false);
        myOrderRecyclerview=view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderRecyclerview.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList=new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.men,2,"Dress Shirt(BLACK) XL Men","Delivered on Mon,15th May,2021"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.women,1,"Dress (BLACK) XL Women","Delivered on Mon,20th May,2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile6,0,"Vivo S1","Cancelled"));
        MyOrderAdapter myOrderAdapter=new MyOrderAdapter(myOrderItemModelList);
        myOrderRecyclerview.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return view;

    }
}