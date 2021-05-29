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
 * Use the {@link ProductSpecificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductSpecificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView productSpecificationrrecyclerview;

    public List<ProductSpecificationModel> productSpecificationModelList;


    public ProductSpecificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductSpecificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductSpecificationFragment newInstance(String param1, String param2) {
        ProductSpecificationFragment fragment = new ProductSpecificationFragment();
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
        View view= inflater.inflate(R.layout.fragment_product_specification, container, false);
        productSpecificationrrecyclerview=view.findViewById(R.id.product_specification_recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productSpecificationrrecyclerview.setLayoutManager(linearLayoutManager);

//        productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","8GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Camera","32mp"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Battery","4000mh"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Finger_Print","Enable"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Brand","Huawei"));
//       productSpecificationModelList.add(new ProductSpecificationModel(1,"Hard","128GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Display"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","8GB"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Camera","32mp"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Battery","4000mh"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Finger_Print","Enable"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Brand","Huawei"));
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"Hard","128GB"));

        ProductSpecificationAdatpter productSpecificationAdatpter=new ProductSpecificationAdatpter(productSpecificationModelList);
        productSpecificationrrecyclerview.setAdapter(productSpecificationAdatpter);
        productSpecificationAdatpter.notifyDataSetChanged();
        return view;
    }
}