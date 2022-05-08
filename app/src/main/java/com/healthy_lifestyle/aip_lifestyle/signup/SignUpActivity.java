package com.healthy_lifestyle.aip_lifestyle.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.healthy_lifestyle.aip_lifestyle.navigation.NavigationActivity;
import com.healthy_lifestyle.aip_lifestyle.R;


public class SignUpActivity extends AppCompatActivity implements SignUpView {
    private EditText etEmail;
    private EditText etFullName;
    private AppCompatEditText etRePassword;
    private AppCompatEditText etPassword;
    private Button btnSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etRePassword = findViewById(R.id.re_password);
        btnSignup = findViewById(R.id.signup);
        etFullName = findViewById(R.id.full_name);
        SignupController signupController = new SignupControllerImpl(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupController.OnSignup(etEmail.getText().toString(),
                        etPassword.getText().toString(),
                        etRePassword.getText().toString(),
                        etFullName.getText().toString());
            }
        });
    }



    @Override
    public void OnLoginSuccess(String message) {
        Toast.makeText(SignUpActivity.this,message , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, NavigationActivity.class);
        intent.putExtra("email",etEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void OnLoginError(String message) {
        Toast.makeText(SignUpActivity.this, message , Toast.LENGTH_LONG).show();
    }
}