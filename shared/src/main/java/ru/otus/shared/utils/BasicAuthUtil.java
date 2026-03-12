package ru.otus.shared.utils;

import java.util.Base64;

public class BasicAuthUtil {

    private BasicAuthUtil(){}

    public static String encodeToBase64(String credentials) {
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
