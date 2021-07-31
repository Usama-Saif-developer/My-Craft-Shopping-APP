package com.example.craft_shoping;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
    public static CartAdapter cartAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;

    ///////Cart Adapter and model links with eachother
    private String mParam2;
    private RecyclerView cartItemsRecyclerview;
    private Button Continuebtn;
    private Dialog loadingDialog;
    private TextView totalAmont;

    public MyCartFragment() {
        // Required empty public constructor
    }

    /*
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
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        ///loading Dailog//////
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dailog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ///loading Dailog//////

        cartItemsRecyclerview = view.findViewById(R.id.cart_item_recyclerview);
        Continuebtn = view.findViewById(R.id.cart_continue_btn);
        totalAmont = view.findViewById(R.id.total_cart_amount);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerview.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(DBqueries.cartitemModelList, totalAmont, true);
        cartItemsRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        Continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartitemModelList = new ArrayList<>();
                DeliveryActivity.fromCart = true;
                for (int x = 0; x < DBqueries.cartitemModelList.size(); x++) {
                    CartitemModel cartitemModel = DBqueries.cartitemModelList.get(x);
                    if (cartitemModel.isInStock()) {
                        DeliveryActivity.cartitemModelList.add(cartitemModel);
                    }
                }
                DeliveryActivity.cartitemModelList.add(new CartitemModel(CartitemModel.TOTAL_AMOUNT));
                loadingDialog.show();
                if (DBqueries.addressesModelList.size() == 0) {
                    DBqueries.loadAddresses(getContext(), loadingDialog,true);
                } else {
                    loadingDialog.dismiss();
                    Intent deliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();
        if (DBqueries.rewardModelList.size() == 0) {
            loadingDialog.show();
            DBqueries.loadRewards(getContext(), loadingDialog, false);
        }

        if (DBqueries.cartitemModelList.size() == 0) {
            DBqueries.cartList.clear();
            DBqueries.loadCartList(getContext(), loadingDialog, true, new TextView(getContext()), totalAmont);
        } else {
            if (DBqueries.cartitemModelList.get(DBqueries.cartitemModelList.size() - 1).getType() == CartitemModel.TOTAL_AMOUNT) {
                LinearLayout parent = (LinearLayout) totalAmont.getParent().getParent();
                parent.setVisibility(View.INVISIBLE);
            }
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (CartitemModel cartitemModel : DBqueries.cartitemModelList) {
            if (!TextUtils.isEmpty(cartitemModel.getSelectedCoupenId())) {
                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                    if (rewardModel.getCoupenId().equals(cartitemModel.getSelectedCoupenId())) {
                        rewardModel.setAlreadyUsed(false);
                    }
                }
                cartitemModel.setSelectedCoupenId(null);
                if (MyRewardsFragment.myRewardAdapter != null) {
                    MyRewardsFragment.myRewardAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}