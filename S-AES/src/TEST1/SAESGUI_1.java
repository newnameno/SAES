package TEST1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SAESGUI_1 extends JFrame {
   
	private static final long serialVersionUID = 893772817890896981L;
	private JTextField dataField;
    private JTextField keyField;
    private JTextField resultField;

    public SAESGUI_1() {
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
                String result = SAES.encrypt(data, key);
                resultField.setText(result);
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = dataField.getText();
                String key = keyField.getText();
                String result = SAES.decrypt(data, key);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SAESGUI_1();
            }
        });
    }
}

