package com.healthy_lifestyle.aip_lifestyle.edit_recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.healthy_lifestyle.aip_lifestyle.Constants;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.RecipeModel;
import com.healthy_lifestyle.aip_lifestyle.recipe.RecipeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditActivity extends AppCompatActivity implements EditView {
    String recipeKey = "";
    ImageView recipeImage;
    String recipeTitle, recipePrice, recipeDescription, recipeItem, recipeIngredients;
    Uri uri;
    ImageView foodImage;
    EditText etTitle, etDescription, etPrep, etRecipe, etIngredients;
    String imageUrl, userID;
    String  oldImageUrl;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        showActionBar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findById();
        loadData();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
    }

    public void uploadRecipe() {
        List<String> ingredients = new ArrayList<String>(Arrays.asList(etIngredients.getText().toString().split(",")));
        RecipeModel foodData = new RecipeModel(
                etTitle.getText().toString(),
                etDescription.getText().toString(),
                imageUrl,
                etRecipe.getText().toString(),
                etPrep.getText().toString(),
                ingredients, recipeKey);

        if(imageUrl != oldImageUrl ){
            StorageReference storageReferenceNew = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
            storageReferenceNew.delete();
        }

        firebaseFirestore.collection(Constants.USERS_COLLECTION).document(userID).collection(Constants.RECIPES_REFERENCE).document(recipeKey).set(foodData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                        intent.putExtra("title", etTitle.getText().toString());
                        intent.putExtra("prep", etPrep.getText().toString());
                        intent.putExtra("image",imageUrl);
                        intent.putExtra("description",   etDescription.getText().toString());
                        intent.putExtra("recipe", etRecipe.getText().toString());
                        intent.putExtra("ingredients", etIngredients.getText().toString());
                        intent.putExtra("triggeredFrom", false);
                        intent.putExtra("recipeKey", recipeKey);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void selectImage(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            uri = data.getData();
            recipeImage.setImageURI(uri);
        }
        else {
            Snackbar.make(findViewById(R.id.et_edit_name),"You haven't picked image", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnUpdateRecipe(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading...");
        progressDialog.show();


        if(uri == null){
            imageUrl = oldImageUrl;
            uploadRecipe();
            progressDialog.dismiss();
        }
        else{
            storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImage").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    uploadRecipe();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void findById() {
        recipeImage = findViewById(R.id.iv_edit_image);
        etTitle = findViewById(R.id.et_edit_name);
        etDescription = findViewById(R.id.et_edit_description);
        etPrep = findViewById(R.id.et_edit_time);
        etRecipe = findViewById(R.id.et_edit_recipe);
        etIngredients = findViewById(R.id.et_edit_ingredients);
        foodImage = findViewById(R.id.iv_edit_image);
    }

    @Override
    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#C9EEDD"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("");
    }

    private void loadData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            recipeKey = mBundle.getString("recipeKey");
            oldImageUrl = mBundle.getString("image");
            recipeTitle = mBundle.getString("title");
            etTitle.setText(recipeTitle);
            recipePrice = mBundle.getString("prep");
            etPrep.setText(recipePrice);
            recipeDescription = mBundle.getString("description");
            etDescription.setText(recipeDescription);
            recipeItem = mBundle.getString("recipe");
            etRecipe.setText(recipeItem);
            imageUrl = mBundle.getString("image");
            Glide.with(this).load(mBundle.getString("image")).into(foodImage);
            recipeIngredients =  mBundle.getString("ingredients");
            etIngredients.setText(recipeIngredients);
        }
    }
}