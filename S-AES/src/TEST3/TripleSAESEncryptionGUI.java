package TEST3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TripleSAESEncryptionGUI extends JFrame implements ActionListener {
    
	private static final long serialVersionUID = -3010717311067619417L;
	private JTextField plaintextField, ciphertextField, keyField;
    private JButton encryptButton, decryptButton;

    public TripleSAESEncryptionGUI() {
        setTitle("Triple Encryption with S-AES");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel plaintextLabel = new JLabel("Plaintext:");
        plaintextField = new JTextField();
        JLabel ciphertextLabel = new JLabel("Ciphertext:");
        ciphertextField = new JTextField();
        JLabel keyLabel = new JLabel("Key (48 bits):");
        keyField = new JTextField();

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
        add(encryptButton);
        add(decryptButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TripleSAESEncryptionGUI::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == encryptButton) {
            String plaintext = plaintextField.getText();
            String key = keyField.getText();
            String ciphertext = performTripleEncryption(plaintext, key);
            ciphertextField.setText(ciphertext);
        } else if(e.getSource() == decryptButton) {
            String ciphertext = ciphertextField.getText();
            String key = keyField.getText();
            String plaintext = performTripleDecryption(ciphertext, key);
            plaintextField.setText(plaintext);
        }
    }

    private String performTripleEncryption(String plaintext, String key) {
    	String key1 = key.substring(0,16);
    	String key2 = key.substring(16,32);
    	String key3 = key.substring(32);
        String intermediateCiphertext1 = MeetInTheMiddleAttack.encrypt(plaintext, key1);
        String intermediateCiphertext2 = MeetInTheMiddleAttack.encrypt(intermediateCiphertext1, key2);
        String ciphertext = MeetInTheMiddleAttack.encrypt(intermediateCiphertext2, key3);
        return ciphertext;
    }

    private String performTripleDecryption(String ciphertext, String key) {
    	String key1 = key.substring(0,16);
    	String key2 = key.substring(16,32);
    	String key3 = key.substring(32);
        String intermediatePlaintext2 = DoubleSAESGUI.decrypt(ciphertext, key3);
        String intermediatePlaintext1 = DoubleSAESGUI.decrypt(intermediatePlaintext2, key2);
        String plaintext = DoubleSAESGUI.decrypt(intermediatePlaintext1, key1);
        return plaintext;
    }
}

