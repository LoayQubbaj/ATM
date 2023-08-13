package atm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String uuid;
    private byte[] passwordHash;
    private byte[] pinHash;
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String password, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;

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

    // ... rest of the class



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