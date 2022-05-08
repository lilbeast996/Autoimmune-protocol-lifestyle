package com.healthy_lifestyle.aip_lifestyle;

public class Constants {
    // The following constants are used to ensure proper login and signup validation.
    public static final int INVALID_EMAIL = 1;
    public static final int EMPTY_EMAIL = 0;
    public static final int EMPTY_PASSWORD = 2;
    public static final int INVALID_PASSWORD = 3;
    public static final int MISMATCHED_PASSWORD = 4;
    public static final int EMPTY_NAME = 5;
    public static final int EMPTY_REPASSWORD = 6;
    public static final int UNKNOWN_ERROR = -1;

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    public static final String USERS_COLLECTION = "users";
    public static final String FAVORITES_COLLECTION = "Favorites";
    public static final String RECIPES_REFERENCE = "Recipes";


    public static final String LEVEL_BEGINNER = "beginner";
    public static final String LEVEL_INTERMEDIATE = "intermediate";
    public static final String LEVEL_ADVANCED = "advanced";
}
