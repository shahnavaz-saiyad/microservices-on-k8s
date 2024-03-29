package com;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Hello world!
 *
 */
//public class App
//{
//    public static void main( String[] args ) throws Exception {
//
//        // Load Public and Private Keys from Files
//        PublicKey publicKey = KeyFileManager.loadPublicKey("public.key");
//        PrivateKey privateKey = KeyFileManager.loadPrivateKey("private.key");
//
//        // Text to be encrypted
//        String plaintext = "Hello, World!";
//
//        // Encrypt the text using the public key
//        // Encrypt the text using the public key
//        byte[] encryptedText = RSAEncryption.encrypt(plaintext, publicKey);
//
//        // Convert the encrypted bytes to a printable string
//        String encryptedString = RSAEncryption.encodeToString(encryptedText);
//
//        // Decrypt the text using the private key
//        byte[] decodedEncryptedText = RSAEncryption.decodeFromString(encryptedString);
//        String decryptedText = RSAEncryption.decrypt(decodedEncryptedText, privateKey);
//
//        // Output the results
//        System.out.println("Original: " + plaintext);
//        System.out.println("Encrypted: " + encryptedString);
//        System.out.println("Decrypted: " + decryptedText);
//    }
//}


public class App extends JFrame implements ActionListener {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    public App() {
        setTitle("RSA Encryption App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        inputTextArea = new JTextArea(5, 20);
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(this);
        encryptButton.setPreferredSize(new Dimension(100, 30));

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(outputTextArea.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                JOptionPane.showMessageDialog(null, "Encrypted text copied to clipboard.");
            }
        });

        outputTextArea = new JTextArea(5, 20);
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(encryptButton);
        buttonPanel.add(copyButton);

        panel.add(inputScrollPane, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(outputScrollPane, BorderLayout.SOUTH);

        add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Encrypt")) {
            try {
                // Load Public Key from File
                PublicKey publicKey = KeyFileManager.loadPublicKey("public.pem");

                // Get text from input text area
                String plaintext = inputTextArea.getText();

                // Encrypt the text using the public key
                byte[] encryptedText = RSAEncryption.encrypt(plaintext, publicKey);

                // Convert the encrypted bytes to a printable string
                String encryptedString = RSAEncryption.encodeToString(encryptedText);

                // Display encrypted text in output text area
                outputTextArea.setText(encryptedString);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error encrypting text: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
//        generateKeyPair();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    private static void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);

            // Generate Key Pair
            KeyPair keyPair = keyGen.generateKeyPair();

            // Save Public and Private Keys
            KeyFileManager.savePublicKey(keyPair.getPublic(), "public.pem");
            KeyFileManager.savePrivateKey(keyPair.getPrivate(), "private.pem");

            PublicKey publicKey = KeyFileManager.loadPublicKey("public.pem");
            PrivateKey privateKey = KeyFileManager.loadPrivateKey("private.pem");

            System.out.println("Keys loaded successfully.");
            String plaintext = "Hello, World!";
            byte[] encryptedText = RSAEncryption.encrypt(plaintext, publicKey);
            String decryptedText = RSAEncryption.decrypt(encryptedText, privateKey);

            System.out.println("Original: " + plaintext);
            System.out.println("Encrypted: " + new String(encryptedText));
            System.out.println("Decrypted: " + decryptedText);


        } catch (Exception e) {
            System.err.println("Error generating and saving keys: " + e.getMessage());
        }

    }


}
