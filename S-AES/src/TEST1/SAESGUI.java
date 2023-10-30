package TEST1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SAESGUI extends JFrame {
   
	private static final long serialVersionUID = 893772817890896981L;
	private JTextField dataField;
    private JTextField keyField;
    private JTextField resultField;

    public SAESGUI() {
        setTitle("S-AES Encryption/Decryption");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new GridLayout(4, 2));

        JLabel dataLabel = new JLabel("Data (16 bits):");
        dataField = new JTextField();
        JLabel keyLabel = new JLabel("Key (16 bits):");
        keyField = new JTextField();
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        JLabel resultLabel = new JLabel("Result:");
        resultField = new JTextField();

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = dataField.getText();
                String key = keyField.getText();
                String result = encrypt(data, key);
                resultField.setText(result);
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = dataField.getText();
                String key = keyField.getText();
                String result = decrypt(data, key);
                resultField.setText(result);
            }
        });

        add(dataLabel);
        add(dataField);
        add(keyLabel);
        add(keyField);
        add(encryptButton);
        add(decryptButton);
        add(resultLabel);
        add(resultField);

        setVisible(true);
    }

    private String encrypt(String data, String key) {
        int inputData = Integer.parseInt(data, 2);
        int inputKey = Integer.parseInt(key, 2);
        int encryptedData = SAES.encrypt(inputData, inputKey);
        String result = Integer.toBinaryString(encryptedData);
        while (result.length() < 16) {
            result = "0" + result;
        }
        return result;
    }

    private String decrypt(String data, String key) {
        int inputData = Integer.parseInt(data, 2);
        int inputKey = Integer.parseInt(key, 2);
        int decryptedData = SAES.decrypt(inputData, inputKey);
        String result = Integer.toBinaryString(decryptedData);
        while (result.length() < 16) {
            result = "0" + result;
        }
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SAESGUI();
            }
        });
    }
}

class SAES {
    private static final int[] S_BOX = {
        0x9, 0x4, 0xA, 0xB, 0xD, 0x1, 0x8, 0x5, 0x6, 0x2, 0x0, 0x3, 0xC, 0xE, 0xF, 0x7
    };

    private static final int[] P_BOX = {
        0x1, 0x0, 0x3, 0x2
    };

    public static int encrypt(int plaintext, int key) {
        int roundKey = key;

        // Initial round
        int ciphertext = plaintext ^ roundKey;

        // Encryption rounds
        for (int i = 0; i < 3; i++) {
            ciphertext = substitute(ciphertext);
            ciphertext = permute(ciphertext);
            roundKey = generateRoundKey(roundKey, i);
            ciphertext ^= roundKey;
        }

        // Final round
        ciphertext = substitute(ciphertext);
        roundKey = generateRoundKey(roundKey, 3);
        ciphertext ^= roundKey;

        return ciphertext;
    }
 
    public static int decrypt(int ciphertext, int key) {
        int roundKey = generateRoundKey(key, 3);

        // Final round
        ciphertext ^= roundKey;
        ciphertext = inverseSubstitute(ciphertext);

        // Encryption rounds
        for (int i = 2; i >= 0; i--) {
       ciphertext ^= roundKey;
            ciphertext = inversePermute(ciphertext);
            ciphertext = inverseSubstitute(ciphertext);
        }

        // Initial round
        roundKey = key;
        int plaintext = ciphertext ^ roundKey;

        return plaintext;
    }

    private static int substitute(int value) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int nibble = (value >> (4 * i)) & 0xF;
            nibble = S_BOX[nibble];
            result |= nibble << (4 * i);
        }
        return result;
    }

    private static int permute(int value) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int bit = (value >> i) & 0x1;
            int permutedBit = (bit << P_BOX[i]) & 0x1;
            result |= permutedBit << i;
        }
        return result;
    }

    private static int inverseSubstitute(int value) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int nibble = (value >> (4 * i)) & 0xF;
            nibble = inverseSBox(nibble);
            result |= nibble << (4 * i);
        }
        return result;
    }

    private static int inversePermute(int value) {
    	int result = 0;
        for (int i = 0; i < 4; i++) {
            int bit = (value >> i) & 0x1;
            int invertedBit = (bit << P_BOX[i]) & 0x1;
            result |= invertedBit << i;
        }
        return result;
    }

    private static int inverseSBox(int value) {
        int result = 0;
        for (int i = 0; i < 16; i++) {
            if (S_BOX[i] == value) {
                result = i;
                break;
            }
        }
        return result;
    }

    private static int generateRoundKey(int key, int round) {
        // Do some key scheduling logic here
        // This is a simplified example, actual SAES requires more complex key scheduling

        // In this example, we simply rotate the key by 4 bits for each round
        int rotatedKey = (key << 4) | (key >>> 12);
        return rotatedKey;
    }

    public static void main(String[] args) {
        int plaintext = 0x1234;
        int key = 0xABCD;

        int ciphertext = encrypt(plaintext, key);
        System.out.println("Plaintext: " + Integer.toHexString(plaintext));
        System.out.println("Key: " + Integer.toHexString(key));
        System.out.println("Ciphertext: " + Integer.toHexString(ciphertext));
    }
}

