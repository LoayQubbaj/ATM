
package atm;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name ;
    private ArrayList<User> users ;
    private ArrayList<Account > accounts ;
    
    public Bank(String name ){
        this.name = name ;
        accounts = new ArrayList<Account>();
        users = new ArrayList<User> ();        
    }
    
   
    public void addAccount(Account acc){
        this.accounts.add(acc);
    }
    
    public String getNewUserUUID(){
        Random rng = new Random();
        boolean nonUnique  ;
        String uuid  ;
        int len = 6 ;
        

        do {
             uuid = "" ;
            for (int c = 0 ; c<len ; c++){
                uuid +=( (Integer)rng.nextInt(10)).toString() ;
                
            }
            nonUnique = false ;
            for (User u : this.users){
                if (uuid.compareTo(u.getUUID()) == 0){
                    nonUnique = true ;
                    break ;
                }
            }
            
        }while (nonUnique);
        return uuid ;
    }
     public String getNewAccountUUID(){
          Random rng = new Random();
        boolean nonUnique  ;
        String uuid  ;
        int len = 10 ;
        

        do {
             uuid = "" ;
            for (int c = 0 ; c<len ; c++){
                uuid +=( (Integer)rng.nextInt(10)).toString() ;
                
            }
            nonUnique = false ;
            for (Account a : this.accounts){
                if (uuid.compareTo(a.getUUID()) == 0){
                    nonUnique = true ;
                    break ;
                }
            }
            
        }while (nonUnique);
        return uuid ;
        
    }

    public User addUser(String firstName, String lastName, String password, String pin) {
        User newUser = new User(firstName, lastName, password, pin, this);
        this.users.add(newUser);

        Account newAccount = new Account("savings", newUser, this);
        this.accounts.add(newAccount);
        newUser.addAccount(newAccount);
        return newUser;
    }
     public User userLogin (String userID , String pin){
         for (User u :this.users){
             if (u.getUUID().compareTo(userID) ==0 &&u.validatePin(pin) ) 
                 return u ;
                 
             
         
         }
        
         return null ;
     } 
     public String getName(){
         return this.name;
     }
    
    
    
}
