package com.healthy_lifestyle.aip_lifestyle.add_recipe;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.healthy_lifestyle.aip_lifestyle.Constants;
import com.healthy_lifestyle.aip_lifestyle.RecipeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddRecipeControllerImpl implements AddRecipeController {

    AddRecipeView addRecipeView;
    RecipeModel recipeModel;

    private String userId;
    private FirebaseFirestore firebaseFirestore;

    public AddRecipeControllerImpl(AddRecipeView addRecipeView) {
        this.addRecipeView = addRecipeView;
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void OnUpload(String title, String description, Uri uri, String prep, String recipe, String ingredients) {
        List<String> ingredientsList = new ArrayList<String>(Arrays.asList(ingredients.split(",")));
        if (TextUtils.isEmpty(title))
            addRecipeView.onAddedFailed("Please enter a title");
        else if (TextUtils.isEmpty(description))
            addRecipeView.onAddedFailed("Please enter a description");
        else if (TextUtils.isEmpty(recipe))
            addRecipeView.onAddedFailed("Please enter a recipe");
        else if (TextUtils.isEmpty(prep))
            addRecipeView.onAddedFailed("Please enter a cook time");
        else if (TextUtils.isEmpty(ingredients))
            addRecipeView.onAddedFailed("Please enter ingredients");
        else if (uri == null)
            addRecipeView.onAddedFailed("Please upload image");
        else  {
            recipeModel = new RecipeModel(title, description, recipe, prep, ingredientsList);
            uploadRecipe(uri);
        }
    }

    public void uploadImage(Uri uri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImage").child(uri.getLastPathSegment());
        addRecipeView.onLoadingStarted();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                String imageUrl = urlImage.toString();
                recipeModel.setImage(imageUrl);
                uploadRecipeDatabase();
                addRecipeView.onLoadingFinished();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                addRecipeView.onAddedFailed("Failed to Upload Recipe" + e.getMessage());
                addRecipeView.onLoadingFinished();
            }
        });
    }

    public void uploadRecipe(Uri uri) {
        if(uri == null){
            addRecipeView.onAddedFailed("Please Upload Image");
        } else {
            uploadImage(uri);
        }
    }

    public void uploadRecipeDatabase() {
        String recipeKey = firebaseFirestore.collection(Constants.USERS_COLLECTION).document(userId).collection(Constants.RECIPES_REFERENCE).document().getId();
        recipeModel.setRecipeKey(recipeKey);
        firebaseFirestore.collection(Constants.USERS_COLLECTION).document(userId).collection(Constants.RECIPES_REFERENCE).document(recipeKey).set(recipeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addRecipeView.onAddedSuccess("Recipe Uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                addRecipeView.onAddedFailed("Failed to Upload Recipe" + e.getMessage());
            }
        });
    }
}
