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
 * Use the {@link MyRewardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRewardsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rewardRecyclerview;

    public MyRewardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRewardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRewardsFragment newInstance(String param1, String param2) {
        MyRewardsFragment fragment = new MyRewardsFragment();
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
        View view= inflater.inflate(R.layout.fragment_my_rewards, container, false);
        rewardRecyclerview=view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardRecyclerview.setLayoutManager(layoutManager);
        List<RewardModel> rewardModelList=new ArrayList<>();
        rewardModelList.add(new RewardModel("Cash Back","Valid before 2th May 2021","Using this Coupen You can Get 20% OFF at any product above at Rs.500/- and below at Rs.2000/-"));
        rewardModelList.add(new RewardModel("Combo Coupen","Valid before 13th Jan 2021","Using this Coupen You can Get 30% OFF at any product above at Rs.3000/- "));
        rewardModelList.add(new RewardModel("Buy 1 get 1 free","Till 22th May 2021","Using this Coupen You can Get 50% OFF at any product above at Rs.500/- and below at Rs.2000/-"));
        rewardModelList.add(new RewardModel("Combo Coupen","Valid before 5th April 2021","70% Discount"));
        rewardModelList.add(new RewardModel("Cash Back","Valid before 2th May 2021","Using this Coupen You can Get 20% OFF at any product above at Rs.500/- and below at Rs.2000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 3 Free","Only Till Ramzan","Large Deal in Small price"));
        MyRewardAdapter myRewardAdapter=new MyRewardAdapter(rewardModelList,false);
        rewardRecyclerview.setAdapter(myRewardAdapter);
        myRewardAdapter.notifyDataSetChanged();
        return view;

    }
}