package com.healthy_lifestyle.aip_lifestyle.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.healthy_lifestyle.aip_lifestyle.Constants;
import com.healthy_lifestyle.aip_lifestyle.RecipeModel;
import com.healthy_lifestyle.aip_lifestyle.navigation.NavigationActivity;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.edit_recipe.EditActivity;
import com.healthy_lifestyle.aip_lifestyle.main.MainActivity;
import java.util.Arrays;

public class RecipeActivity extends AppCompatActivity implements IngredientsAdapter.ItemClickListener, RecipeView {
    IngredientsAdapter ingredientsAdapter;
    ToggleButton btnFavourite;
    AppCompatButton btnEdit, btnDelete;
    TextView tvDescription, tvTitle, tvRecipe, tvPrep;
    ImageView ivFoodImage;
    String recipeName, imageUrl, recipePrep, recipeDescription, recipeItem, recipeIngredients, recipeKey, favoriteKey, fragment;
    FirebaseAuth firebaseAuth;
    Boolean triggeredFrom;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        showActionBar();
        findById();
        loadData();
        handleButtonVisibility();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        final DocumentReference favoriteDocumentReference = firebaseFirestore.
                collection(Constants.USERS_COLLECTION).document(userId);
        favoriteDocumentReference.collection("Favorites").get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RecipeModel foodData = documentSnapshot.toObject(RecipeModel.class);
                    if (recipeName.equals(foodData.getTitle())) {
                        btnFavourite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.unfavourite_button));
                        btnFavourite.setChecked(true);
                        favoriteKey = documentSnapshot.getId();
                    }
                }
            }
        });

        btnFavourite.setChecked(false);
        btnFavourite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.favourite_button));
        btnFavourite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (btnFavourite.isChecked()){
                   btnFavourite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                           R.drawable.unfavourite_button));
                   favoriteKey = firebaseFirestore.collection(Constants.USERS_COLLECTION).
                           document(userId).collection(Constants.FAVORITES_COLLECTION).document().getId();
                   RecipeModel fireStoreFoodData = new RecipeModel(recipeName,recipeDescription,
                           imageUrl,recipeItem, recipePrep,Arrays.asList(recipeIngredients.split(",")));
                   DocumentReference documentReference = firebaseFirestore.
                           collection(Constants.USERS_COLLECTION).document(userId).collection(Constants.FAVORITES_COLLECTION).document(favoriteKey);
                   documentReference.set(fireStoreFoodData).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Snackbar.make(view,"Failed to add recipe to favorites: " +
                                   e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                       }
                   });

               } else
               {   btnFavourite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),
                       R.drawable.favourite_button));
                   DocumentReference documentReference = firebaseFirestore.
                           collection(Constants.USERS_COLLECTION).document(userId).
                           collection("Favorites").document(favoriteKey);
                   documentReference.delete().addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Snackbar.make(findViewById(R.id.ll_recipe),"Failed to remove recipe from favorites: "
                                   + e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                       }
                   });
               }
           }
       });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view,"Failed to delete recipe image: " +
                                e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
                DocumentReference documentReference = firebaseFirestore.
                        collection(Constants.USERS_COLLECTION).document(userId).
                        collection("Recipes").document(recipeKey);
                documentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar. make(view,"Recipe Deleted",
                                        BaseTransientBottomBar.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),
                                        NavigationActivity.class);
                                intent.putExtra("fragment", "my_recipes");
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view,"Failed to delete recipe" +
                                e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, EditActivity.class);
                intent.putExtra("recipeKey",recipeKey);
                intent.putExtra("image", imageUrl);
                intent.putExtra("title", tvTitle.getText().toString());
                intent.putExtra("description", tvDescription.getText().toString());
                intent.putExtra("recipe", tvRecipe.getText().toString());
                intent.putExtra("prep", tvPrep.getText().toString());
                intent.putExtra("ingredients", recipeIngredients);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView_recipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsAdapter = new IngredientsAdapter(this, Arrays.asList(recipeIngredients.split(",")));
        ingredientsAdapter.setClickListener(this);
        recyclerView.setAdapter(ingredientsAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=new Intent(this, NavigationActivity.class);
        if (this.fragment ==  null) {
            this.fragment = "my_recipes";
        }
        intent.putExtra("fragment", this.fragment);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#C9EEDD"));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    private void findById() {
        tvTitle = findViewById(R.id.txt_recipe_title);
        tvDescription = findViewById(R.id.text_description);
        tvPrep = findViewById(R.id.prep);
        ivFoodImage = findViewById(R.id.food_image);
        tvRecipe = findViewById(R.id.recipe);
        btnEdit = findViewById(R.id.edit_button);
        btnDelete = findViewById(R.id.delete_button);
        btnFavourite = (ToggleButton) findViewById(R.id.btn_favorites);
    }

    private void loadData() {
        Bundle mBundle = getIntent().getExtras();

        if (mBundle != null) {
            recipeName = mBundle.getString("title");
            tvTitle.setText(recipeName);
            recipePrep = mBundle.getString("prep");
            tvPrep.setText(recipePrep);
            recipeDescription = mBundle.getString("description");
            tvDescription.setText(recipeDescription);
            recipeItem = mBundle.getString("recipe");
            tvRecipe.setText(recipeItem);
            recipeKey = mBundle.getString("recipeKey");
            imageUrl = mBundle.getString("image");
            Glide.with(this).load(mBundle.getString("image")).into(ivFoodImage);
            recipeIngredients =  mBundle.getString("ingredients");
            triggeredFrom = mBundle.getBoolean("triggeredFrom");
            fragment = mBundle.getString("fragment");
        }
    }

    private void handleButtonVisibility() {
        if (triggeredFrom) {
            btnFavourite.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }else {
            btnFavourite.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }
    }
}