package com.security.authentication.util;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private PasswordUtils() {}

    @NonNull
    public static String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
