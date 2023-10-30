package TEST3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CBCModeEncryptionGUI extends JFrame implements ActionListener {
    
	private static final long serialVersionUID = -5017148371220740793L;
	private JTextField plaintextField, ciphertextField, keyField, ivField;
    private JButton encryptButton, decryptButton;

    public CBCModeEncryptionGUI() {
        setTitle("CBC Mode Encryption with S-AES");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        JLabel plaintextLabel = new JLabel("Plaintext:");
        plaintextField = new JTextField();
        JLabel ciphertextLabel = new JLabel("Ciphertext:");
        ciphertextField = new JTextField();
        JLabel keyLabel = new JLabel("Key (16 bits):");
        keyField = new JTextField();
        JLabel ivLabel = new JLabel("Initialization Vector (16 bits):");
        ivField = new JTextField();

        encryptButton = new JButton("Encrypt");
        encryptButton.addActionListener(this);
        decryptButton = new JButton("Decrypt");
        decryptButton.addActionListener(this);

        add(plaintextLabel);
        add(plaintextField);
        add(ciphertextLabel);
        add(ciphertextField);
        add(keyLabel);
        add(keyField);
        add(ivLabel);
        add(ivField);
        add(encryptButton);
        add(decryptButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CBCModeEncryptionGUI::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == encryptButton) {
            String plaintext = plaintextField.getText();
            String key = keyField.getText();
            String iv = ivField.getText();
            String ciphertext = performCBCEncryption(plaintext, key, iv);
            ciphertextField.setText(ciphertext);
        } else if (e.getSource() == decryptButton) {
            String ciphertext = ciphertextField.getText();
            String key = keyField.getText();
            String iv = ivField.getText();
            String plaintext = performCBCDecryption(ciphertext, key, iv);
            plaintextField.setText(plaintext);
        }
    }

    private String performCBCEncryption(String plaintext, String key, String iv) {
        String ciphertext = "";
        String previousCiphertext = "";

        for (int i = 0; i < plaintext.length() / 2; i++) {
            String currentBlock = plaintext.substring(i * 2, (i + 1) * 2);
            String xoredBlock = xorHex(currentBlock, previousCiphertext);
            String encryptedBlock = DoubleSAESGUI.encrypt(xoredBlock, key);
            ciphertext += encryptedBlock;
            previousCiphertext = encryptedBlock;
        }

        return ciphertext;
    }

    private String performCBCDecryption(String ciphertext, String key, String iv) {
        String plaintext = "";
        String previousCiphertext = iv;

        for (int i = 0; i < ciphertext.length() / 2; i++) {
            String currentBlock = ciphertext.substring(i * 2, (i + 1) * 2);
            String decryptedBlock = DoubleSAESGUI.decrypt(currentBlock, key);
            String xoredBlock = xorHex(decryptedBlock, previousCiphertext);
            plaintext += xoredBlock;
            previousCiphertext = currentBlock;
        }

        return plaintext;
    }

    private String xorHex(String a, String b) {
        int intA = Integer.parseInt(a, 16);
        int intB = Integer.parseInt(b, 16);
        int result = intA ^ intB;
        String hexResult = Integer.toHexString(result);
        if (hexResult.length() % 2 != 0) {
            hexResult = "0" + hexResult;
        }
        return hexResult;
    }
}

