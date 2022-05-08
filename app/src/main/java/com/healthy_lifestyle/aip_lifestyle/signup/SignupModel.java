package com.healthy_lifestyle.aip_lifestyle.signup;

public interface SignupModel {
    String getEmail();
    String getPassword();
    String getRePassword();
    int isValid();
}
