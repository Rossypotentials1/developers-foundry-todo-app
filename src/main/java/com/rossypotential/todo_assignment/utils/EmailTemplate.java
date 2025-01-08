package com.rossypotential.todo_assignment.utils;

public class EmailTemplate {

    public static String getEmailMessage(String name, String baseUrl, String verificationCode) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n" +
                getVerificationUrl(baseUrl, verificationCode) + "\n\nThe support Team";
    }

    public static String getVerificationUrl( String baseUrl, String verificationCode) {
        String verifyEndpoint = "/api/v1/auth/verify-email?verificationCode=";
        return String.format("%s%s%s", baseUrl, verifyEndpoint, verificationCode);
    }

    public static String getForgotPasswordVerificationUrl(String baseUrl, String resetToken) {
        return baseUrl + "/api/v1/auth/verify-forgot-password-email?resetToken=" + resetToken;
    }
}