package com.healthy_lifestyle.aip_lifestyle.main;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.add_recipe.AddRecipeActivity;
import com.healthy_lifestyle.aip_lifestyle.add_recipe.AddRecipeController;
import com.healthy_lifestyle.aip_lifestyle.login.LoginActivity;
import com.healthy_lifestyle.aip_lifestyle.navigation.NavigationActivity;
import com.healthy_lifestyle.aip_lifestyle.splash_screen.SplashActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}