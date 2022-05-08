package com.healthy_lifestyle.aip_lifestyle.login;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.healthy_lifestyle.aip_lifestyle.Constants;


class LoginModelImpl implements LoginModel {
    private String email, password, eventCode;

    public LoginModelImpl(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int isValid() {
        if (TextUtils.isEmpty(getEmail()))
            return Constants.EMPTY_EMAIL;
        else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches())
            return Constants.INVALID_EMAIL;
        else if (TextUtils.isEmpty(getPassword()))
            return Constants.EMPTY_PASSWORD;
        else
            return Constants.UNKNOWN_ERROR;
    }
}

