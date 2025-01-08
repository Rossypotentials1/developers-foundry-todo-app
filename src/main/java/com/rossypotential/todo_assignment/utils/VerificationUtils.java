package com.rossypotential.todo_assignment.utils;

import java.security.SecureRandom;

public class VerificationUtils {
    // Characters allowed in the verification code
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int DEFAULT_CODE_LENGTH = 8;

    // SecureRandom for better security over Random
    private static final SecureRandom secureRandom = new SecureRandom();

    // Method to generate a verification code of default length
    public static String generateVerificationCode() {
        return generateVerificationCode(DEFAULT_CODE_LENGTH);
    }

    // Method to generate a verification code of custom length
    public static String generateVerificationCode(int length) {
        StringBuilder verificationCode = new StringBuilder(length);

        // Generate random characters from the allowed set
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            verificationCode.append(CHARACTERS.charAt(index));
        }

        return verificationCode.toString();
    }
}