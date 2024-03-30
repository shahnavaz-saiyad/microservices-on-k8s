package com.common.util;

import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class EncryptionUtility {

    private static ObjectMapper objectMapper = new ObjectMapper();

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

    public static PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        ClassPathResource classPathResource = new ClassPathResource("private.pem");

        // Read the Base64-encoded key from file
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(classPathResource.getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("-----") && !line.isEmpty())
                    sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Decode the Base64-encoded key bytes
        byte[] encodedPrivateKey = Base64.getDecoder().decode(sb.toString());

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static DecryptedDatasource decryptDataSource(Tenant tenant) throws Exception {

        byte[] encryptedText = EncryptionUtility.decodeFromString(tenant.getEncryptedDataSource());
        String decrypted = EncryptionUtility.decrypt(encryptedText, EncryptionUtility.loadPrivateKey());
        DecryptedDatasource decryptedDatasource = objectMapper.readValue(decrypted, DecryptedDatasource.class);
        decryptedDatasource.setTenantUuid(tenant.getTenantUuid());
        return decryptedDatasource;
    }
}


