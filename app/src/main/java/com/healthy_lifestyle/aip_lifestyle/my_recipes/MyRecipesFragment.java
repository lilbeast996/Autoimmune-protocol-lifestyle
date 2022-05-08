package com.healthy_lifestyle.aip_lifestyle.my_recipes;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthy_lifestyle.aip_lifestyle.Constants;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.RecipeAdapter;
import com.healthy_lifestyle.aip_lifestyle.RecipeModel;
import com.healthy_lifestyle.aip_lifestyle.add_recipe.AddRecipeActivity;
import com.healthy_lifestyle.aip_lifestyle.databinding.FragmentMyRecipesBinding;
import com.healthy_lifestyle.aip_lifestyle.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;


public class MyRecipesFragment extends Fragment implements MyRecipesView {

    private FragmentMyRecipesBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;
    RecyclerView mRecyclerView;
    EditText etSearch;
    ProgressDialog progressDialog;
    List<RecipeModel> recipeList;
    RecipeAdapter recipeAdapter;
    FloatingActionButton addButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        binding = FragmentMyRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findById(root);
        setLayoutManager(root);
        onLoadingStarted();

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddRecipeActivity.class);
                startActivity(intent);            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            Toast.makeText(getContext(), "You need to login first!" , Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else {
           loadingRecipeList(root);
           onSearch();
       }
        return root;
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

    private void findById(View root) {
        mRecyclerView = root.findViewById(R.id.rv_myrecipes);
        etSearch = root.findViewById(R.id.et_myrecipes_search);
        addButton = root.findViewById(R.id.btn_add);
    }

    private void setLayoutManager(View root) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(),1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onLoadingStarted() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Recipes...");
        progressDialog.show();
    }

    @Override
    public void onLoadingFinished() {
        progressDialog.dismiss();
    }

    private void loadingRecipeList(View root) {
        userId = firebaseAuth.getCurrentUser().getUid();

        recipeList = new ArrayList<>();

        recipeAdapter = new RecipeAdapter(root.getContext(), recipeList, false);
        mRecyclerView.setAdapter(recipeAdapter);

        final DocumentReference documentReference = firebaseFirestore.collection(Constants.USERS_COLLECTION).document(userId);
        documentReference.collection(Constants.RECIPES_REFERENCE).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                recipeList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RecipeModel foodData = documentSnapshot.toObject(RecipeModel.class);
                    foodData.setRecipeKey(foodData.getRecipeKey());
                    recipeList.add(foodData);
                }
                recipeAdapter = new RecipeAdapter(root.getContext(), recipeList, false);
                mRecyclerView.setAdapter(recipeAdapter);

                recipeAdapter.notifyDataSetChanged();
                onLoadingFinished();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onLoadingFinished();
            }
        });
    }

    private void onSearch() {
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
        });
    }
}
