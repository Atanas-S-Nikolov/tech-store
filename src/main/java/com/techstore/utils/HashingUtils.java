package com.techstore.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class HashingUtils {
    public static String hashSha256(String string) {
        return Hashing.sha256().hashString(string, StandardCharsets.UTF_8).toString();
    }
}
