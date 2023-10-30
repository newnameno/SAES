package TEST3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MeetInTheMiddleAttack extends JFrame implements ActionListener {
    
	private static final long serialVersionUID = 4702211221527598371L;
	private JTextField plaintext1Field, ciphertext1Field, plaintext2Field, ciphertext2Field, keyField;
    private JButton attackButton;

    public MeetInTheMiddleAttack() {
        setTitle("Double Encryption S-AES - Meet-in-the-Middle Attack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel plaintext1Label = new JLabel("Plaintext 1:");
        plaintext1Field = new JTextField();
        JLabel ciphertext1Label = new JLabel("Ciphertext 1:");
        ciphertext1Field = new JTextField();
        JLabel plaintext2Label = new JLabel("Plaintext 2:");
        plaintext2Field = new JTextField();
        JLabel ciphertext2Label = new JLabel("Ciphertext 2:");
        ciphertext2Field = new JTextField();
        JLabel keyLabel = new JLabel("Recovered Key:");
        keyField = new JTextField();
        keyField.setEditable(false);

        attackButton = new JButton("Perform Attack");
        attackButton.addActionListener(this);

        add(plaintext1Label);
        add(plaintext1Field);
        add(ciphertext1Label);
        add(ciphertext1Field);
        add(plaintext2Label);
        add(plaintext2Field);
        add(ciphertext2Label);
        add(ciphertext2Field);
        add(keyLabel);
        add(keyField);
        add(attackButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MeetInTheMiddleAttack::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == attackButton) {
            String plaintext1 = plaintext1Field.getText();
            String ciphertext1 = ciphertext1Field.getText();
            String plaintext2 = plaintext2Field.getText();
            String ciphertext2 = ciphertext2Field.getText();
            String recoveredKey = performAttack(plaintext1, ciphertext1, plaintext2, ciphertext2);
            keyField.setText(recoveredKey);
        }
    }

    private String performAttack(String plaintext1, String ciphertext1, String plaintext2, String ciphertext2) {
        // Brute-force attack to find the common key
        for (int i = 0; i <= 0xFFFF; i++) {
            // Convert the integer to a 16-bit binary string
            String key = String.format("%16s", Integer.toBinaryString(i)).replace(' ', '0');

            // Encrypt the known plaintexts using the current key
            String encrypted1 = MeetInTheMiddleAttack.encrypt(plaintext1, key);
            String encrypted2 = MeetInTheMiddleAttack.encrypt(plaintext2, key);

            // Check if the encrypted values match the known ciphertexts
            if (encrypted1.equals(ciphertext1) && encrypted2.equals(ciphertext2)) {
                return key;
            }
        }

        return "Key not found";
    }
    public static String encrypt(String data, String key) {
    	// Convert data and key from ASCII string to binary string
        String binaryData = convertStringToBinary(data);
        String binaryKey = convertStringToBinary(key);

        // S-AES encryption implementation
        // Replace with your own logic

        // Convert encrypted binary data to ASCII string
        String encryptedData = convertBinaryToString(binaryData);
        return encryptedData;
}

	private static String convertStringToBinary(String input) {
	    StringBuilder binaryBuilder = new StringBuilder();
	
	    for (char c : input.toCharArray()) {
	        String binary = Integer.toBinaryString(c);
	        while (binary.length() < 8) {
	            binary = "0" + binary;
	        }
	        binaryBuilder.append(binary);
	    }
	
	    return binaryBuilder.toString();
	}
	private static String convertBinaryToString(String binary) {
	    StringBuilder stringBuilder = new StringBuilder();
	
	    for (int i = 0; i < binary.length(); i += 8) {
	        String binaryChar = binary.substring(i, i + 8);
	        int decimal = Integer.parseInt(binaryChar, 2);
	        char character = (char) decimal;
	        stringBuilder.append(character);
	    }
	
	    return stringBuilder.toString();
	}
}

