package com.healthy_lifestyle.aip_lifestyle.foodList;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.databinding.FragmentFoodlistBinding;
import java.util.ArrayList;
import java.util.List;

public class FoodListFragment extends Fragment {

    private FragmentFoodlistBinding binding;
    FoodListAdapter foodListAdapter;
    RecyclerView rvFoodList;
    List<FoodListModel> myFoodList;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    public static FoodListFragment newInstance() {
        return new FoodListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        rvFoodList = root.findViewById(R.id.rv_food_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(),1);
        rvFoodList.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setMessage("Loading Food List...");

        myFoodList = new ArrayList<>();

        foodListAdapter = new FoodListAdapter(root.getContext(), myFoodList);
        rvFoodList.setAdapter(foodListAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Ingredients");
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myFoodList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                    FoodListModel foodData = itemSnapshot.getValue(FoodListModel.class);
                    myFoodList.add(foodData);
                }
                foodListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
         return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}