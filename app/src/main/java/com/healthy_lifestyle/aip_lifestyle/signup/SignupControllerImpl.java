package com.healthy_lifestyle.aip_lifestyle.signup;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthy_lifestyle.aip_lifestyle.Constants;

import java.util.HashMap;
import java.util.Map;


public class SignupControllerImpl implements SignupController {
    SignUpView signUpView;
    private FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    public SignupControllerImpl(SignUpView signUpView) {
        this.signUpView = signUpView;
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void OnSignup(String email, String password, String rePassword, String fullName) {
        SignupModel userCredentials = new SignupModelImpl(email, password, rePassword, fullName);
        int loginCode = userCredentials.isValid();
        switch (loginCode) {
            case Constants.EMPTY_EMAIL:
                signUpView.OnLoginError("Please enter Email");
                break;
            case Constants.INVALID_EMAIL:
                signUpView.OnLoginError("Please enter a valid Email");
                break;
            case Constants.EMPTY_PASSWORD:
                signUpView.OnLoginError("Please enter Password");
                break;
            case Constants.INVALID_PASSWORD:
                signUpView.OnLoginError("Password must contain at least one digit, lowercase, uppercase, special characters and at least 8 characters total");
                break;
            case Constants.MISMATCHED_PASSWORD:
                signUpView.OnLoginError("Passwords must match");
                break;
            case Constants.EMPTY_NAME:
                signUpView.OnLoginError("Please enter a Name");
                break;
            case Constants.EMPTY_REPASSWORD:
                signUpView.OnLoginError("Please enter a Re-Password");
                break;
            default:
                signUpUser(email, password, fullName);
       }
    }

    private void signUpUser(String email, String password, String fullName) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    addToFirestore(fullName, email);
                } else {
                    signUpView.OnLoginError("Signup failed!");
                }
            }
        });
    }

    private void addToFirestore (String fullName, String email) {
        userID = auth.getCurrentUser().getUid();
        Map<String,Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("email", email);
        firebaseFirestore.collection(Constants.USERS_COLLECTION).document(userID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    signUpView.OnLoginSuccess("Signup successful!");
                } else {
                    signUpView.OnLoginError("Signup failed!");
                }
            }
        });
    }
}
