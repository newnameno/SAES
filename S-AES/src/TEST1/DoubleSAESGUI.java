package TEST1;
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
    	String k1 = SAES.hexToBinary(key1);
        String k2 = SAES.hexToBinary(key2);
        
        // First round of S-AES encryption
        String firstData = SAESGUI_2.encrypt(data, k1);

        // Second round of S-AES encryption
        String secondData = SAESGUI_2.encrypt(firstData, k2);

        return secondData;
    }

    public static String doubleDecrypt(String encryptedData, String key) {
    	//Divide the key into two parts
    	String key1 = key.substring(0,16);
    	String key2 = key.substring(16);
    	String k1 = SAES.hexToBinary(key1);
        String k2 = SAES.hexToBinary(key2);
        
        // First round of S-AES decryption
        String firstData = SAESGUI_2.decrypt(encryptedData, k2);

        // Second round of S-AES decryption
        String secondData = SAESGUI_2.decrypt(firstData, k1);

        return secondData;
    }
}
   