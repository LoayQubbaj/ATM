package atm;

import java.util.Scanner;

public class ATM {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Bank theBank = new Bank("bank masr");

		System.out.println("Welcome to " + theBank.getName());

		// Ask the user for their first name, last name, password, and PIN
		System.out.print("Enter your first name: ");
		String firstName = sc.nextLine();

		System.out.print("Enter your last name: ");
		String lastName = sc.nextLine();

		System.out.print("Enter your password: ");
		String password = sc.nextLine();

		System.out.print("Enter your PIN: ");
		String pin = sc.nextLine();

		User newUser = theBank.addUser(firstName, lastName, password, pin);
		Account newAccount = new Account("savings", newUser, theBank);
		newUser.addAccount(newAccount);
		theBank.addAccount(newAccount);

		User curUser;
		while (true) {
			curUser = ATM.menuPrompt(theBank, sc);
			ATM.printUserMenu(curUser, sc);
		}
	}



	public static  User menuPrompt(Bank theBank , Scanner sc ){
        String userID ;
        String pin ; 
        User authUser;
        do{
         System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());		

        System.out.println("enter your ID : ");
        userID = sc.nextLine();
        
        System.out.println("enter your pin : ");
        pin = sc.nextLine();
        authUser = theBank.userLogin(userID, pin);
        if (authUser == null){
            System.out.println("the combination of id and pin not correct please try again");
        }
        }while (authUser == null);
        
           return authUser ;     
        
        
        
    }
    
    public static void printUserMenu(User theUser , Scanner sc ){
                int choice ;

        do{
        System.out.println("welcome mr " +theUser.getFirstName() );
        theUser.printAccountsSummary();
        
        System.out.println("What would you like to do?");
			System.out.println("  1) Show account transaction history");
			System.out.println("  2) Withdraw");
			System.out.println("  3) Deposit");
			System.out.println("  4) Transfer");
			System.out.println("  5) Quit");
			System.out.println();
			System.out.print("Enter choice: ");
			choice = sc.nextInt();
                        
                        if (choice <0 || choice > 5 ){
                            System.out.println("invalid input try again ");
                        }
        } while (choice <0 || choice > 5 );
        
        switch (choice){
            case 1 :
                ATM.showAccountTransactionHistory(theUser , sc);
                break ;
            
            case 2 :
                ATM.withdrawFunds(theUser , sc);
                break ;
                
            case 3 :
                ATM.depositeFunds(theUser , sc);
                break ;
            case 4 :
                ATM.transferFunds( theUser , sc);
                break;
        }
       if (choice != 5)
           printUserMenu(theUser , sc);
         
    }
    
    public static void transferFunds(User theUser ,Scanner sc){
       int fromAcct;
		int toAcct;
		double amount;
		double acctBal;
		
		// get account to transfer from
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"transfer from: ", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		// get account to transfer to
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"transfer to: ", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcct < 0 || toAcct >= theUser.numAccounts());
		
		// get amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $", 
					acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than balance " +
						"of $.02f.\n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);
		
		// finally, do the transfer 
		theUser.addAcctTransaction(fromAcct, -1*amount, String.format(
				"Transfer to account %s", theUser.getAcctUUID(toAcct)));
		theUser.addAcctTransaction(toAcct, amount, String.format(
				"Transfer from account %s", theUser.getAcctUUID(fromAcct)));
		
	}
    
    public static void withdrawFunds(User theUser , Scanner sc){
                int fromAcct;
		double amount;
		double acctBal;
		String memo;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"withdraw from: ", theUser.numAccounts());
			fromAcct = sc.nextInt()-1;
			if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(fromAcct);
		
		do {
			System.out.printf("Enter the amount to withdraw (max $%.02f): $", 
					acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than balance " +
						"of $%.02f.\n", acctBal);
			}
		} while (amount < 0 || amount > acctBal);
		
		sc.nextLine();
		
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
                
                
                theUser.addAcctTransaction(fromAcct, -amount, memo);
                
        
        
        
    }
    public static void depositeFunds(User theUser , Scanner sc){
        int toAcct;
		double amount;
		double acctBal;
		String memo;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account to " + 
					"deposite from: ", theUser.numAccounts());
			toAcct = sc.nextInt()-1;
			if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcct < 0 || toAcct >= theUser.numAccounts());
		acctBal = theUser.getAcctBalance(toAcct);
		
		do {
			System.out.printf("Enter the amount to deposite (max $%.02f): $", 
					acctBal);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			
			}
		} while (amount < 0);
		
		sc.nextLine();
		
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
                
                
                theUser.addAcctTransaction(toAcct, amount, memo);
        
    }
    public static void showAccountTransactionHistory(User theUser , Scanner sc){
        int theAcc ;
        do {
			System.out.printf("Enter the number (1-%d) of the account  " + 
					": ", theUser.numAccounts());
			theAcc = sc.nextInt()-1;
			if (theAcc < 0 || theAcc >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
        
        
     
        }while (theAcc < 0 || theAcc >= theUser.numAccounts());
        theUser.printAcctTransHistory(theAcc);

        
    }
}

