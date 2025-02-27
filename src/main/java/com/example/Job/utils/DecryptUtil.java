package com.example.Job.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import com.example.Job.models.Result;

public class DecryptUtil {
    public static final String JWT_ALGORITHM = "HmacSHA512"; // Thuật toán HS512

    public static Result decryptString(String keyHex, String cipherText) {
        try {
            byte[] iv = new byte[16]; // IV mặc định toàn 0
            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte[] key = hexStringToByteArray(keyHex); // Chuyển key từ hex sang byte

            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(cipherBytes);
            String plainText = new String(decryptedBytes, StandardCharsets.UTF_8);

            return new Result(true, plainText);
        } catch (Exception e) {
            return new Result(false, e.toString());
        }
    }

    public static String encryptString(String keyHex, String plainText) {
        try {
            byte[] key = hexStringToByteArray(keyHex);
            byte[] iv = new byte[16]; // IV mặc định toàn 0

            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi mã hóa AES: " + ex.getMessage(), ex);
        }
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
