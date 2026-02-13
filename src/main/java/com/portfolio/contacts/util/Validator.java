package com.portfolio.contacts.util;

import java.util.regex.Pattern;

public final class Validator {
    private Validator() {} 

    private static final Pattern PHONE = Pattern.compile("^[0-9+()\\-\\s]{7,20}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío");
        }
    }

    public static void validatePhone(String phone) {
        requireNonBlank(phone, "Teléfono");
        if (!PHONE.matcher(phone).matches()) {
            throw new IllegalArgumentException("Teléfono inválido. Usa dígitos y símbolos comunes (+, -. espacios).");
        }
    }
    
    public static void validateEmail(String email) {
        requireNonBlank(email, "Email");
        if (!EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }
    }

    public static String sage(String s) {
        return s == null ? "" : s.trim();
    }
}