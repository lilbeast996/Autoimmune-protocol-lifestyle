package com.healthy_lifestyle.aip_lifestyle.login;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.healthy_lifestyle.aip_lifestyle.Constants;

public class LoginControllerImpl implements LoginController{
    LoginView loginView;
    private FirebaseAuth auth;

    public LoginControllerImpl(LoginView loginView) {
        this.loginView = loginView;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void OnLogin(String email, String password) {
        LoginModelImpl userCredentials = new LoginModelImpl(email,password);
        int loginCode = userCredentials.isValid();
        switch (loginCode) {
            case Constants.EMPTY_EMAIL:
                loginView.OnLoginError("Please enter Email");
                break;
            case Constants.INVALID_EMAIL:
                loginView.OnLoginError("Please enter a valid Email");
                break;
            case Constants.EMPTY_PASSWORD:
                loginView.OnLoginError("Please enter Password");
                break;
            default:
                loginUser(email, password);
        }
    }

    private void loginUser(String user, String pass) {
        auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginView.OnLoginSuccess("Login successful!");
                } else {
                    loginView.OnLoginError("Login failed");
                }
            }
        });
    }
}
