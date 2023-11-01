package TEST1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> intermediateResultTable1 = new HashMap<>();
        Map<String, String> intermediateResultTable2 = new HashMap<>();
         
        //encrypt table
        for (int key1 = 0; key1 <= 0xFFFF; key1++) {
            String keyHex1 = Integer.toBinaryString(key1);
            while(keyHex1.length()<16){
                keyHex1 = "0" + keyHex1;
            }
            String intermediateResult1 = SAES.encrypt(plaintext1, keyHex1);
            intermediateResultTable1.put(intermediateResult1, keyHex1);
        }

        //decrypt table
        for (int key2 = 0; key2 <= 0xFFFF; key2++){
            String keyHex2 = Integer.toBinaryString(key2);
            while(keyHex2.length()<16){
                keyHex2 = "0" + keyHex2;
            }
            String intermediateResult2 = SAES.decrypt(ciphertext1, keyHex2);
            intermediateResultTable2.put(intermediateResult2, keyHex2);
        }
        
        //compare and select the common keys and check with ciphertext2 and plaintext2
        for(Map.Entry<String,String> entry: intermediateResultTable1.entrySet()) {
        	String intermediateResult = entry.getKey();
        	String key1 = entry.getValue();
        	
        	if(intermediateResultTable2.containsKey(intermediateResult)){
        		String key = intermediateResultTable2.get(intermediateResult);
        		if (SAES.encrypt(plaintext2, key1) == SAES.decrypt(ciphertext2, key)) {
        			return key1 + " and " + key +"/n";
                }
        	}
        }           
        return null;
    }
}
   