package com;
import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAEncryption {
    public static byte[] encrypt(String text, PublicKey key) throws Exception {
        // Get an instance of the Cipher for RSA encryption
        Cipher cipher = Cipher.getInstance("RSA");

        // Initialize the Cipher with the public key for encryption
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the plaintext
        return cipher.doFinal(text.getBytes());
    }

    public static String decrypt(byte[] encryptedText, PrivateKey key) throws Exception {
        // Get an instance of the Cipher for RSA decryption
        Cipher cipher = Cipher.getInstance("RSA");

        // Initialize the Cipher with the private key for decryption
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decrypt the ciphertext
        byte[] decryptedBytes = cipher.doFinal(encryptedText);

        // Convert the decrypted bytes to string
        return new String(decryptedBytes);
    }

    public static String encodeToString(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeFromString(String encodedData) {
        return Base64.getDecoder().decode(encodedData);
    }
}

