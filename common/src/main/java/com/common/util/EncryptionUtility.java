package com.common.util;

import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtility {

    private final ObjectMapper objectMapper;
    private final String base64PrivateKey;


    public EncryptionUtility(ObjectMapper objectMapper, @Value("${encryption.private-key}") String base64PrivateKey) {
        this.objectMapper = objectMapper;
        // Remove the first and last lines (the "BEGIN" and "END" markers), and remove all line breaks
        this.base64PrivateKey = base64PrivateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }

    public byte[] encrypt(String text, PublicKey key) throws Exception {
        // Get an instance of the Cipher for RSA encryption
        Cipher cipher = Cipher.getInstance("RSA");

        // Initialize the Cipher with the public key for encryption
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the plaintext
        return cipher.doFinal(text.getBytes());
    }

    public String decrypt(byte[] encryptedText, PrivateKey key) throws Exception {
        // Get an instance of the Cipher for RSA decryption
        Cipher cipher = Cipher.getInstance("RSA");

        // Initialize the Cipher with the private key for decryption
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decrypt the ciphertext
        byte[] decryptedBytes = cipher.doFinal(encryptedText);

        // Convert the decrypted bytes to string
        return new String(decryptedBytes);
    }

    public String encodeToString(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] decodeFromString(String encodedData) {
        return Base64.getDecoder().decode(encodedData);
    }

//    public PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//
//        ClassPathResource classPathResource = new ClassPathResource("private.pem");
//
//        // Read the Base64-encoded key from file
//        StringBuilder sb = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new FileReader(classPathResource.getFile()))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                if (!line.startsWith("-----") && !line.isEmpty())
//                    sb.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Decode the Base64-encoded key bytes
//        byte[] encodedPrivateKey = Base64.getDecoder().decode(sb.toString());
//
//        KeyFactory keyFactory = null;
//        try {
//            keyFactory = KeyFactory.getInstance("RSA");
//            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
//            return keyFactory.generatePrivate(privateKeySpec);
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }



    public DecryptedDatasource decryptDataSource(Tenant tenant) throws Exception {

        byte[] encryptedText = decodeFromString(tenant.getEncryptedDataSource());
        String decrypted = decrypt(encryptedText, loadPrivateKey());
        DecryptedDatasource decryptedDatasource = objectMapper.readValue(decrypted, DecryptedDatasource.class);
        decryptedDatasource.setTenantUuid(tenant.getTenantUuid());
        return decryptedDatasource;
    }

    public PrivateKey loadPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println(base64PrivateKey);
        byte[] encodedPrivateKey = Base64.getDecoder().decode(base64PrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        return keyFactory.generatePrivate(privateKeySpec);
    }
}


