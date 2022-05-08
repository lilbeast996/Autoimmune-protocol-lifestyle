package com.healthy_lifestyle.aip_lifestyle.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.healthy_lifestyle.aip_lifestyle.navigation.NavigationActivity;
import com.healthy_lifestyle.aip_lifestyle.R;
import com.healthy_lifestyle.aip_lifestyle.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private EditText etEmail;
    private AppCompatEditText etPassword;
    private Button btnLogin;
    private TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignup = findViewById(R.id.tv_signup);
        LoginController loginController = new LoginControllerImpl(this);
        btnLogin.setOnClickListener(view -> loginController.OnLogin(etEmail.getText().toString(), etPassword.getText().toString()));

        tvSignup.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

    }

    @Override
    public void OnLoginSuccess(String message) {
        Toast.makeText(LoginActivity.this,message , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(new Intent(LoginActivity.this, NavigationActivity.class));
        intent.putExtra("email",etEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void OnLoginError(String message) {
        Toast.makeText(LoginActivity.this, message , Toast.LENGTH_LONG).show();
    }
}

