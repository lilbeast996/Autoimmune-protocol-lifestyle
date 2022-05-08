package com.healthy_lifestyle.aip_lifestyle.signup;

import android.text.TextUtils;
import android.util.Patterns;

import com.healthy_lifestyle.aip_lifestyle.Constants;

public class SignupModelImpl implements SignupModel{
    private String email, password, rePassword, fullName;
    public SignupModelImpl(String email, String password, String re_password, String full_name) {
        this.email = email;
        this.password = password;
        this.rePassword = re_password;
        this.fullName = full_name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getRePassword() {
        return rePassword;
    }

    @Override
    public int isValid() {
        if (TextUtils.isEmpty(getEmail()))
            return Constants.EMPTY_EMAIL;
        else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches())
            return Constants.EMPTY_EMAIL;
        else if (TextUtils.isEmpty(getPassword()))
            return Constants.EMPTY_PASSWORD;
        else if (!getPassword().matches(Constants.PASSWORD_REGEX))
            return Constants.INVALID_PASSWORD;
        else  if (!getPassword().equals(getRePassword()))
            return Constants.MISMATCHED_PASSWORD;
        else if (TextUtils.isEmpty(getFullName()))
            return Constants.EMPTY_NAME;
        else if (TextUtils.isEmpty(getRePassword()))
            return Constants.EMPTY_REPASSWORD;
        else
            return Constants.UNKNOWN_ERROR;
    }
}
