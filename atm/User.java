package atm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class User {
    private static final String AES_ALGORITHM = "AES";
    private static final byte[] AES_KEY = {
            0x74, 0x68, 0x69, 0x73, 0x2d, 0x69, 0x73, 0x2d,
            0x61, 0x2d, 0x73, 0x74, 0x72, 0x6f, 0x6e, 0x67,
            0x2d, 0x32, 0x35, 0x36, 0x2d, 0x62, 0x79, 0x74,
            0x65, 0x73, 0x2d, 0x6b, 0x65, 0x79, 0x31, 0x32
    }; // 32-byte AES key

// Rest of the code remains the same
// Change this to your own secret key

    private String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY, AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY, AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, "UTF-8");
    }


    private String firstName;
    private String lastName;
    private String uuid;
    private byte[] passwordHash;
    private byte[] pinHash;
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String password, String pin, Bank theBank) {
        try {
            this.firstName = encrypt(firstName);
            this.lastName = encrypt(lastName);
        } catch (Exception e) {
            System.out.println("Error encrypting user data");
            e.printStackTrace();
            System.exit(1);
        }


        if (!isValidPassword(password)) {
            System.out.println("Error: Password does not meet complexity requirements.");
            System.exit(1);
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            this.passwordHash = md.digest(password.getBytes());
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: SHA-256 algorithm not found");
            System.exit(1);
        }

        this.uuid = theBank.getNewUserUUID();
        this.accounts = new ArrayList<Account>();

        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid);
    }


    public boolean validatePin(String apin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] inputHash = md.digest(apin.getBytes());
            return MessageDigest.isEqual(inputHash, this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: SHA-256 algorithm not found");
            System.exit(1);
        }

        return false;
    }





    String getUUID() {
            return this.uuid ;

    }

    public void addAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    public String getFirstName(){
        return this.firstName;
    }
    public void printAccountsSummary(){
       System.out.printf("\n\n%s's accounts summary\n", this.firstName);

        for (int a =0 ; a <this.accounts.size() ; a++){
            String line = this.accounts.get(a).getSummaryLine();
            System.out.println(line);
        }
    }
    public int numAccounts() {
		return this.accounts.size();
    }
    public double getAcctBalance(int acctIdx) {
		return this.accounts.get(acctIdx).getBalance();
	}
    public String getAcctUUID(int acctIdx) {
		return this.accounts.get(acctIdx).getUUID();
	}

    public void addAcctTransaction(int acctIdx, double amount, String memo) {
		this.accounts.get(acctIdx).addTransaction(amount, memo);
	}

    public void printAcctTransHistory(int acc){
        this.accounts.get(acc).showTransactions();

    }

    private boolean isValidPassword(String password) {
        // Define password complexity rules here
        int minLength = 8;
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        if (password.length() < minLength) {
            return false;
        }

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }


}