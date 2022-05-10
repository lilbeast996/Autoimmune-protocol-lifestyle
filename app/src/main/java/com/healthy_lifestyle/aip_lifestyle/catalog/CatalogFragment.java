package com.healthy_lifestyle.aip_lifestyle.catalog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.healthy_lifestyle.aip_lifestyle.Constants;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.RecipeModel;
import com.healthy_lifestyle.aip_lifestyle.RecipeAdapter;
import com.healthy_lifestyle.aip_lifestyle.databinding.FragmentCatalogBinding;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment implements CatalogView{

    private FragmentCatalogBinding binding;
    RecipeAdapter recipeAdapter;
    RecyclerView rvRecipes;
    List<RecipeModel> recipeList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    EditText etSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findById(root);
        setLayoutManager(root);
        onLoadingStarted();

        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(root.getContext(), recipeList, true);
        rvRecipes.setAdapter(recipeAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.RECIPES_REFERENCE);
        progressDialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    RecipeModel recipeModel = itemSnapshot.getValue(RecipeModel.class);
                    recipeModel.setRecipeKey(itemSnapshot.getKey());
                    recipeList.add(recipeModel);
                }
                recipeAdapter = new RecipeAdapter(root.getContext(), recipeList, true);
                rvRecipes.setAdapter(recipeAdapter);
                recipeAdapter.notifyDataSetChanged();
                onLoadingFinished();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });        return root;
    }

    private void filter(String text) {
        ArrayList<RecipeModel> filterList = new ArrayList<>();
        for (RecipeModel item: recipeList){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        recipeAdapter.filteredList(filterList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void findById(View root) {
        rvRecipes = root.findViewById(R.id.rv_catalog);
        etSearch = root.findViewById(R.id.et_catalog_search);
    }

     private void setLayoutManager(View root) {
         GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(),1);
         rvRecipes.setLayoutManager(gridLayoutManager);
     }

    @Override
    public void onLoadingStarted() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Recipe Recipes...");
        progressDialog.show();
    }

    @Override
    public void onLoadingFinished() {
        progressDialog.dismiss();
    }
}