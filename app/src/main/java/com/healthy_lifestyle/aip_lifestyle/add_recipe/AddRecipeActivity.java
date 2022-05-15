package com.healthy_lifestyle.aip_lifestyle.add_recipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.login.LoginControllerImpl;
import com.healthy_lifestyle.aip_lifestyle.main.MainActivity;
import com.healthy_lifestyle.aip_lifestyle.navigation.NavigationActivity;
import com.healthy_lifestyle.aip_lifestyle.splash_screen.SplashActivity;

public class AddRecipeActivity extends AppCompatActivity implements AddRecipeView {
    ImageView recipeImage;
    Uri uri;
    EditText etTitle, etDescription, etPrep, etRecipe, etIngredients;
    ProgressDialog progressDialog;
    AppCompatButton btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        showActionBar();
        findById();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        AddRecipeController addRecipeController = new AddRecipeControllerImpl(this);
        btnUpload.setOnClickListener(view -> addRecipeController.OnUpload(
                etTitle.getText().toString(),
                etDescription.getText().toString(),
                uri,
                etPrep.getText().toString(),
                etRecipe.getText().toString(),
                etIngredients.getText().toString()));

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

    public void selectImage(View view) {

        Intent photoPicker = new Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            uri = data.getData();
            recipeImage.setImageURI(uri);
        }
        else {
            Snackbar.make(findViewById(R.id.tv_add_title),"You haven't picked image", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoadingStarted() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading...");
        progressDialog.show();
    }

    @Override
    public void onLoadingFinished() {
        progressDialog.dismiss();
    }

    @Override
    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#C9EEDD"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    public void onAddedSuccess(String message) {
        Snackbar.make(findViewById(R.id.ll_addrecipe), message, Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        intent.putExtra("fragment", "my_recipes");
        startActivity(intent);
        finish();
    }

    @Override
    public void onAddedFailed(String message) {
        Snackbar.make(findViewById(R.id.ll_addrecipe), message, Snackbar.LENGTH_LONG).show();
    }

    private void findById() {
        recipeImage = findViewById(R.id.iv_food_add);
        etTitle = findViewById(R.id.tv_add_title);
        etDescription = findViewById(R.id.tv_add_description);
        etPrep = findViewById(R.id.tv_add_prep);
        etRecipe = findViewById(R.id.tv_add_recipe);
        etIngredients = findViewById(R.id.tv_add_ingredients);
        btnUpload = findViewById(R.id.btn_upload);
    }
}