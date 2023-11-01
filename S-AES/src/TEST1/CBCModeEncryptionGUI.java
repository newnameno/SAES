package TEST1;
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

    private String performCBCEncryption(String ptext, String key, String iv) {
    	String previousCipherBlock = iv; 
        String encryptedText = "";

        int blockSize = 16; 
        String plaintext = SAESGUI_2.StrToBin(ptext);
        String[] blocks = splitToBlocks(plaintext, blockSize);
                 
        for (String block : blocks) {
            //Each block XOR with the previous and then encrypt              
       	 String xorResult = xorHex(block, previousCipherBlock);
            String cipherBlock = SAESGUI_2.encrypt(xorResult, key);
            previousCipherBlock = cipherBlock; //update iv
            encryptedText += cipherBlock;
        }
        String enText = SAESGUI_2.BinToStr(encryptedText);
        return enText;
    }

    private String performCBCDecryption(String ctext, String key, String iv) {
    	String previousCipherBlock = iv;
        String decryptedText = "";
        
        int blockSize = 16; 
        String ciphertext = SAESGUI_2.StrToBin(ctext);
        String[] blocks = splitToBlocks(ciphertext, blockSize);
        
        //Each block decrypt and then XOR with the previous              
        for (String block : blocks) {
            String decryptedBlock = SAESGUI_2.decrypt(block, key);
            String xorResult = xorHex(decryptedBlock, previousCipherBlock);
            previousCipherBlock = block;//update iv
            decryptedText += xorResult;
        }
        String deText = SAESGUI_2.BinToStr(decryptedText);
        return deText;
    }

    private String[] splitToBlocks(String text, int blockSize) {
    	int len = text.length();
        int num = (int) Math.ceil((double) len / blockSize);
        String[] blocks = new String[num];

        for (int i = 0; i < num; i++) {
            int start = i * blockSize;
            int end = Math.min((i + 1) * blockSize, len);
            blocks[i] = text.substring(start, end);
        }

        return blocks;
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

