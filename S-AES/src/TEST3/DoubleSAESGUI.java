package TEST3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoubleSAESGUI extends JFrame {
   
	private static final long serialVersionUID = -999178731707888341L;
	private JTextArea dataArea, keyArea, resultArea;

    public DoubleSAESGUI() {
        setTitle("Double S-AES Encryption");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel dataPanel = new JPanel(new BorderLayout());
        JLabel dataLabel = new JLabel("Data:");
        dataArea = new JTextArea(5, 20);
        JScrollPane dataScrollPane = new JScrollPane(dataArea);
        dataPanel.add(dataLabel, BorderLayout.NORTH);
        dataPanel.add(dataScrollPane, BorderLayout.CENTER);

        JPanel keyPanel = new JPanel(new BorderLayout());
        JLabel keyLabel = new JLabel("Key:");
        keyArea = new JTextArea(2, 20);
        JScrollPane keyScrollPane = new JScrollPane(keyArea);
        keyPanel.add(keyLabel, BorderLayout.NORTH);
        keyPanel.add(keyScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        JPanel resultPanel = new JPanel(new BorderLayout());
        JLabel resultLabel = new JLabel("Result:");
        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        mainPanel.add(dataPanel);
        mainPanel.add(keyPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(resultPanel);

        add(mainPanel);

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = dataArea.getText();
                String key = keyArea.getText();
                String encryptedData = doubleEncrypt(data, key);
                resultArea.setText(encryptedData);
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encryptedData = resultArea.getText();
                String key = keyArea.getText();
                String decryptedData = doubleDecrypt(encryptedData, key);
                resultArea.setText(decryptedData);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DoubleSAESGUI().setVisible(true);
            }
        });
    }

    public static String doubleEncrypt(String data, String key) {
    	//Divide the key into two parts
    	String key1 = key.substring(0,16);
    	String key2 = key.substring(16);
        // First round of S-AES encryption
        String firstRoundEncryptedData = encrypt(data, key1);

        // Second round of S-AES encryption
        String secondRoundEncryptedData = encrypt(firstRoundEncryptedData, key2);

        return secondRoundEncryptedData;
    }

    public static String doubleDecrypt(String encryptedData, String key) {
    	//Divide the key into two parts
    	String key1 = key.substring(0,16);
    	String key2 = key.substring(16);
        // First round of S-AES decryption
        String firstRoundDecryptedData = decrypt(encryptedData, key2);

        // Second round of S-AES decryption
        String secondRoundDecryptedData = decrypt(firstRoundDecryptedData, key1);

        return secondRoundDecryptedData;
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
    public static String decrypt(String encryptedData, String key) {
        // Convert encrypted data and key from ASCII string to binary string
        String binaryEncryptedData = convertStringToBinary(encryptedData);
        String binaryKey = convertStringToBinary(key);

        // S-AES decryption implementation
        // Replace with your own logic

        // Convert decrypted binary data to ASCII string
        String decryptedData = convertBinaryToString(binaryEncryptedData);
        return decryptedData;
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

class SAES {
    public static int encrypt(int data, int key) {
        // TODO: Implement S-AES encryption
        // Use the provided data and key to perform encryption
        // Return the encrypted result as an integer
        return 0;
    }

    public static int decrypt(int data, int key) {
        // TODO: Implement S-AES decryption
        // Use the provided data and key to perform decryption
        // Return the decrypted result as an integer
        return 0;
    }

}

