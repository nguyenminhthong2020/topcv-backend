package com.example.Job.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.Job.models;

public class DecryptUtil {
    public static final String JWT_ALGORITHM = "HmacSHA512";  // Thuật toán HS512

    public static Result decryptString(String key, String cipherText) {
        try {
            byte[] iv = new byte[16];
            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            String plainText = "";
            
            SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);
            plainText = new String(decryptedBytes, StandardCharsets.UTF_8);
            
            return new Result(true, plainText);
        } catch (Exception e) {
            return new Result(false, e.toString());
        }
    }

    public static String encryptString(String keyHex, String plainText) throws Exception {
        // Chuyển key từ hex thành mảng byte
        byte[] key = hexStringToByteArray(keyHex);
        byte[] iv = new byte[16]; // Khởi tạo vector khởi tạo (IV) 16 bytes (AES block size)

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        // Chuyển kết quả mã hóa sang Base64 để có thể gửi qua mạng hoặc lưu trữ
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
