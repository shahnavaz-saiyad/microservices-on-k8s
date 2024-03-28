package com;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyFileManager {
    public static void savePublicKey(PublicKey publicKey, String filename) throws IOException {

        FileOutputStream fos = new FileOutputStream(new File("encryption-decryption/encryption-keys", filename));
        fos.write(publicKey.getEncoded());
        fos.close();
    }

    public static void savePrivateKey(PrivateKey privateKey, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File("encryption-decryption/encryption-keys", filename));
        fos.write(privateKey.getEncoded());
        fos.close();
    }

    public static PublicKey loadPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(new File("encryption-decryption/encryption-keys", filename));
        byte[] encodedPublicKey = new byte[fis.available()];
        fis.read(encodedPublicKey);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        return keyFactory.generatePublic(publicKeySpec);
    }

    public static PrivateKey loadPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(new File("encryption-decryption/encryption-keys", filename));
        byte[] encodedPrivateKey = new byte[fis.available()];
        fis.read(encodedPrivateKey);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        return keyFactory.generatePrivate(privateKeySpec);
    }
}

