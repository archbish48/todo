package com.example.todo.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


//SHA-256 으로 해싱 처리(가장 간단한 해싱 알고리즘)
public class PasswordUtil {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed =md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
