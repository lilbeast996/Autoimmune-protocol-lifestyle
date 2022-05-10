package com.healthy_lifestyle.aip_lifestyle.favorite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.healthy_lifestyle.aip_lifestyle.Constants;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.RecipeModel;
import com.healthy_lifestyle.aip_lifestyle.RecipeAdapter;
import com.healthy_lifestyle.aip_lifestyle.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteView {

    RecyclerView rvFavorites;
    List<RecipeModel> recipeList;
    ProgressDialog progressDialog;
    EditText etSearch;
    RecipeAdapter recipeAdapter;
    String userId;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findById(root);
        setLayoutManager(root);
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(root.getContext(), recipeList, true);
        rvFavorites.setAdapter(recipeAdapter);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            Toast.makeText(getContext(), "You need to login first!" , Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else {
            loadingFavoriteList(root);
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

    private void  findById(View root) {
        etSearch = root.findViewById(R.id.et_favorites_search);
        rvFavorites = root.findViewById(R.id.rv_favorites);
    }

    private void setLayoutManager (View root) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(),1);
        rvFavorites.setLayoutManager(gridLayoutManager);
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

    private void loadingFavoriteList (View root) {
        userId = firebaseAuth.getCurrentUser().getUid();
        onLoadingStarted();
        recipeList = new ArrayList<>();
        final DocumentReference documentReference = firebaseFirestore.collection(Constants.USERS_COLLECTION).document(userId);
        documentReference.collection(Constants.FAVORITES_COLLECTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                recipeList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RecipeModel foodData = documentSnapshot.toObject(RecipeModel.class);
                    recipeList.add(foodData);
                }
                recipeAdapter = new RecipeAdapter(root.getContext(), recipeList, true);
                rvFavorites.setAdapter(recipeAdapter);
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
