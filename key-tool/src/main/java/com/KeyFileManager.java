package com;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyFileManager {
    public static void savePublicKey(PublicKey publicKey, String filename) throws IOException {
        byte[] encodedPublicKey = publicKey.getEncoded();
        String base64EncodedKey = Base64.getEncoder().encodeToString(encodedPublicKey);

        // Construct the content with BEGIN/END lines and line breaks
        String content = "-----BEGIN PUBLIC KEY-----\n" +
                insertLineBreaks(base64EncodedKey, 64) + // Break lines every 64 characters
                "\n-----END PUBLIC KEY-----";

        // Write content to file
        File file = new File("key-tool/encryption-keys", filename);
        System.out.println(file.getAbsoluteFile());
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePrivateKey(PrivateKey privateKey, String filename) throws IOException {
        byte[] encodedPrivateKey = privateKey.getEncoded();
        String base64EncodedKey = Base64.getEncoder().encodeToString(encodedPrivateKey);

        // Construct the content with BEGIN/END lines and line breaks
        String content = "-----BEGIN PRIVATE KEY-----\n" +
                insertLineBreaks(base64EncodedKey, 64) + // Break lines every 64 characters
                "\n-----END PRIVATE KEY-----";

        // Write content to file
        try (FileWriter writer = new FileWriter(new File("key-tool/encryption-keys", filename))) {
            writer.write(content);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String insertLineBreaks(String input, int lineLength) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        while (index < input.length()) {
            builder.append(input.substring(index, Math.min(index + lineLength, input.length())));
            builder.append("\n");
            index += lineLength;
        }
        return builder.toString();
    }

    public static PublicKey loadPublicKey(String filename)  {
        // Read the Base64-encoded key from file
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("key-tool/encryption-keys", filename)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("-----") && !line.isEmpty())
                    sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Decode the Base64-encoded key bytes
        byte[] encodedPublicKey = Base64.getDecoder().decode(sb.toString());

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey loadPrivateKey(String filename) {
        // Read the Base64-encoded key from file
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("key-tool/encryption-keys", filename)))) {
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
}

