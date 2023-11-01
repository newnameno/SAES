package TEST1;

class SAES {
    private static final int[][] S_BOX = {
    		{0x09, 0x04, 0x0A, 0x0B},
            {0x0D, 0x01, 0x08, 0x05},
            {0x06, 0x02, 0x00, 0x03},
            {0x0C, 0x0E, 0x0F, 0x07}
            };
    
    private static final int[][] INVS_BOX = {
            {0x0A, 0x05, 0x09, 0x0B},
            {0x01, 0x07, 0x08, 0x0F},
            {0x06, 0x00, 0x02, 0x03},
            {0x0C, 0x04, 0x0D, 0x0E}
        };
    
    private static final int[][] G_Mult = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
            {0, 2, 4, 6, 8, 10, 12, 14, 3, 1, 7, 5, 11, 9, 15, 13},
            {0, 3, 6, 5, 12, 15, 10, 9, 11, 8, 13, 14, 7, 4, 1, 2},
            {0, 4, 8, 12, 3, 7, 11, 15, 6, 2, 14, 10, 5, 1, 13, 9},
            {0, 5, 10, 15, 7, 2, 13, 8, 14, 11, 4, 1, 9, 12, 3, 6},
            {0, 6, 12, 10, 11, 13, 7, 1, 5, 3, 9, 15, 14, 8, 2, 4},
            {0, 7, 14, 9, 15, 8, 1, 6, 13, 10, 3, 4, 2, 5, 12, 11},
            {0, 8, 3, 11, 6, 14, 5, 13, 12, 4, 15, 7, 10, 2, 9, 1},
            {0, 9, 1, 8, 2, 11, 3, 10, 4, 13, 5, 12, 6, 15, 7, 14},
            {0, 10, 7, 13, 14, 4, 9, 3, 15, 5, 8, 2, 1, 11, 6, 12},
            {0, 11, 5, 14, 10, 1, 15, 4, 7, 12, 2, 9, 13, 6, 8, 3},
            {0, 12, 11, 7, 5, 9, 14, 2, 10, 6, 1, 13, 15, 3, 4, 8},
            {0, 13, 9, 4, 1, 12, 8, 5, 2, 15, 11, 6, 3, 14, 10, 7},
            {0, 14, 15, 1, 13, 3, 2, 12, 9, 7, 6, 8, 4, 10, 11, 5},
            {0, 15, 13, 2, 9, 6, 4, 11, 1, 14, 12, 3, 8, 7, 5, 10}
        };
    
    public static String encrypt(String data, String key) {
        String Data = data;
        String [] Keys = SAES.keyExpansion(key);
        
        //first round
        Data = SAES.addRoundKey(Data,Keys[0]);
        
        //second round
        Data = SAES.subBytes(Data);
        Data = SAES.shiftRows(Data);
        Data = SAES.mixColumn(Data);
        Data = SAES.addRoundKey(Data, Keys[1]);
        
        //last round
        Data = SAES.subBytes(Data);
        Data = SAES.shiftRows(Data);
        Data = SAES.addRoundKey(Data, Keys[2]);

        return Data;
    }

    public static String decrypt(String data, String key) {
        String Data = data;
        String[] Keys = SAES.keyExpansion(key);
        
        //first round
        Data = SAES.addRoundKey(Data, Keys[2]);
        
        //second round
        Data = SAES.invShiftRows(Data);
        Data = SAES.invSubBytes(Data);
        Data = SAES.addRoundKey(Data, Keys[1]);
        Data = SAES.invMixCoiumns(Data);
        
        //last round
        Data = SAES.invShiftRows(Data);
        Data = SAES.invSubBytes(Data);
        Data = SAES.addRoundKey(Data, Keys[0]);

        return Data;
    }

    public static String[] keyExpansion(String key) {
    	String[] Keys = new String[3];
        Keys[0] = key;

        for (int round = 1; round < 3; round++) {
            String oldKey = Keys[round - 1];
            String oldKey1 = oldKey.substring(0,8);
            String oldKey2 = oldKey.substring(8);
            String Key0 = xorStrings(roundConstant(round), 
            						reBox(oldKey2.substring(4))+reBox(oldKey2.substring(0,4)));
            String newKey1 = xorStrings(oldKey1, Key0);
            String newKey2 = xorStrings(oldKey2, newKey1);
            String newKey = Key0 + newKey2;
            Keys[round] = newKey;
        }

        return Keys;
    }

    //replace the input data(4bits)through S Box
    private static String reBox(String a) {
		String result = "";
        int row = Integer.parseInt(a.substring(0, 2), 2);
        int col = Integer.parseInt(a.substring(2, 4), 2);
        String s = Integer.toBinaryString(S_BOX[row][col]);
        while(s.length()<4){
            s = "0"+s;
        }
        result += s;
        return result;
	}
	
    //produce round constant
	private static String roundConstant(int round) {
		int[] rcon = {
	            0x80, 0x30
	        };
	        return String.format("%08d", Integer.parseInt(Integer.toBinaryString(rcon[round - 1])));
	    
	}
	
	private static String xorStrings(String a, String b) {
		int intA = Integer.parseInt(a, 2);
        int intB = Integer.parseInt(b, 2);
        int result = intA ^ intB;
        return String.format("%8s", Integer.toBinaryString(result)).replace(' ', '0');
    
	}
	
	private static String addRoundKey(String data, String key) {
		int dataInt = Integer.parseInt(data, 2);
        int KeysInt = Integer.parseInt(key, 2);
        int result = dataInt ^ KeysInt;
        return String.format("%16s", Integer.toBinaryString(result)).replace(' ', '0');
    
	}

	private static String subBytes(String data) {
		String result = "";
        for (int i = 0; i < 16; i +=4) {
            String byteSub = data.substring(i, i + 4);
            int row = Integer.parseInt(byteSub.substring(0, 2), 2);
            int col = Integer.parseInt(byteSub.substring(2, 4), 2);
            String s = Integer.toBinaryString(S_BOX[row][col]);
            while(s.length()<4){
                s = "0"+s;
            }
            result += s;
        }
        return result;
	}

	private static String shiftRows(String data) {
		String row1 = data.substring(0, 4);
        String row2 = data.substring(4, 8);
        String row3 = data.substring(8, 12);
        String row4 = data.substring(12);

        return row1 + row4 + row3 + row2;
	}

	private static String mixColumn(String data) {
		String[] cols = new String[4];
        String[] Data = new String[4];
        int[] num1 = new int[4];
        int[] num2 = new int[4];
        for (int i = 0; i < 4; i++) {
            cols[i] = data.substring(4*i, 4*i + 4);
            num1[i] = Integer.parseInt(cols[i],2);
        }

        num2[0]= num1[0] ^ (G_Mult[num1[1]] [4]);
        num2[1]= num1[1] ^ (G_Mult[num1[0]] [4]);
        num2[2]= num1[2] ^ (G_Mult[num1[3]] [4]);
        num2[3]= num1[3] ^ (G_Mult[num1[2]] [4]);

        Data[0] = Integer.toBinaryString(num2[0]);
        Data[1] = Integer.toBinaryString(num2[1]);
        Data[2] = Integer.toBinaryString(num2[2]);
        Data[3] = Integer.toBinaryString(num2[3]);

        for (int i = 0; i < 4; i++) {
            while(Data[i].length()<4){
                Data[i] = "0"+Data[i];
            }
        }
        data = Data[0]+Data[1]+Data[2]+Data[3];
        return data;
	}

	private static String invShiftRows(String data) {
		String row1 = data.substring(0, 4);
        String row2 = data.substring(4, 8);
        String row3 = data.substring(8, 12);
        String row4 = data.substring(12);
        return row1 + row4 + row3 + row2;
        }

	private static String invSubBytes(String data) {
		String result = "";
        for (int j = 0; j < 4; j++) {
            String byteSub = data.substring(4*j, 4*j + 4);
            int row = Integer.parseInt(byteSub.substring(0, 2), 2);
            int col = Integer.parseInt(byteSub.substring(2, 4), 2);
            String s = Integer.toBinaryString(INVS_BOX[row][col]);
            while(s.length()<4){
                s = "0"+s;
            }
            result += s;
        }
        return result;
	}

	private static String invMixCoiumns(String data) {
		String[] cols = new String[4];
        String[] Data = new String[4];
        int[] num1 = new int[4];
        int[] num2 = new int[4];
        for (int i = 0; i < 4; i++) {
            cols[i] = data.substring(4*i, 4*i + 4);
            num1[i] = Integer.parseInt(cols[i],2);
        }

        num2[0]= (G_Mult[num1[0]] [9]) ^ (G_Mult[num1[1]] [2]);
        num2[1]= (G_Mult[num1[1]] [9]) ^ (G_Mult[num1[0]] [2]);
        num2[2]= (G_Mult[num1[2]] [9]) ^ (G_Mult[num1[3]] [2]);
        num2[3]= (G_Mult[num1[3]] [9]) ^ (G_Mult[num1[2]] [2]);

        Data[0] = Integer.toBinaryString(num2[0]);
        Data[1] = Integer.toBinaryString(num2[1]);
        Data[2] = Integer.toBinaryString(num2[2]);
        Data[3] = Integer.toBinaryString(num2[3]);

        for (int i = 0; i < 4; i++) {
            while(Data[i].length()<4){
                Data[i] = "0"+Data[i];
            }
        }

        data = Data[0]+Data[1]+Data[2]+Data[3];
        return data;
	}

	
	public static String hexToBinary(String hex) {	 
        int inte = Integer.parseInt(hex, 16);
        String binaryString = Integer.toBinaryString(inte);
        while(binaryString.length()<16){
            binaryString = "0" + binaryString;
        }
        return binaryString;
	}

}

