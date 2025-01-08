package com.rossypotential.todo_assignment.utils;

public class UserUtils {
    public static final String LOGIN_FAILURE_CODE = "400";
    public static final String EMAIL_SENDING_ERROR_CODE = "400" ;
    public static final String GENERAL_ERROR_CODE = "400";
    public static final String INVALID_PASSWORD_FORMAT_CODE = "400";
    public static String ACCOUNT_EXISTS_CODE = "001";
    public static String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created!";

    public static String ACCOUNT_CREATION_SUCCESS_CODE = "200";

    public static String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Your account has been created successfully";

    public final static String LOGIN_SUCCESS_CODE = "200";
    public final static String LOGIN_SUCCESS_MESSAGE = "You have logged in successfully";

    public final static String INVALID_REQUEST_CODE = "004";
    public final static String INVALID_REQUEST_MESSAGE = "Fields must be filled";
    public final static String INVALID_EMAIL_FORMAT_CODE = "400";
    public final static String INVALID_EMAIL_FORMAT_MESSAGE = "Invalid email format";

    public final static String INVALID_EMAIL_DOMAIN_CODE = "400";
    public final static String INVALID_EMAIL_DOMAIN_MESSAGE = "Invalid email domain";
    public static String USER_UPDATE_MESSAGE = "This user have been Updated Successfully!";
    public static String USER_DETAILS_MESSAGE = "USER DETAILS";

    public static boolean isValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

}
