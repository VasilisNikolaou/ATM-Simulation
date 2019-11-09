package ClientTier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientProtocol {
 
	//We have 2 instances of clientProtocol at ATMClient so we make this static.
	private static boolean isLogged = false;
	
	private BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	
	//Integers are preferable instead of Strings.
	private static final int LOGIN = 1;
	private static final int WITHDRAW = 2;
	private static final int DEPOSIT = 3;
	private static final int BALANCE = 4;
	private static final int EXIT = 5;
	
	private static final int ERROR = -1;

	public int processChoice(String choice) 
	{
		int c;
		
		try {
			
			c = Integer.parseInt(choice);
			
		} catch(NumberFormatException e) {
			
			System.out.println("You must enter an integer.");
			return ERROR;
		}
		
		if(c <= 0 || c >= 6)
		{
			System.out.println("Out of bounds input.");
			return ERROR;
		}
		
		return c;
		
	}
	
	/*
	 * In case of withdraw and deposit we are checking the amount 
	 * should not be a negative amount.
	 */
	public String prepareRequest(int choice) throws IOException {
		
		switch(choice) 
		 {
		     case LOGIN:
		    	 System.out.println("Enter your ID: ");
		    	 return "Id " + keyboard.readLine();
		     
		     case WITHDRAW:
		    	 System.out.println("Enter money to withdraw: ");
		    	 return "Withdraw " + check(keyboard.readLine());
		    		 
		     case DEPOSIT:
		    	 System.out.println("Enter money to deposit: ");
		    	 return "Deposit " + check(keyboard.readLine());
		    	 
		     case BALANCE:
		    	 return "Balance";
		    	 
		     case EXIT:
		    	 System.out.println("Exiting...");
		    	 return "Exit";
		 
		     default:
		    	 System.out.println("Something went wrong...");
		    	 System.exit(1);
		 }
		
		 return "Exit";
		
		
	}
	
	private String check(String amount) throws IOException {
		
		if(Integer.parseInt(amount) <= 0)
		{
			System.out.println("Not valid amount.");
			System.out.println("Enter a positive amount: ");
			return keyboard.readLine();
		}
		
		return amount;
	}
	
	public void processReply(String inmsg, int choice)
	{
		switch(choice) 
		{
		     case LOGIN:
		    	 System.out.println(inmsg);
		    	 isLogged = Boolean.valueOf(inmsg);
		    	 break;
		     
		     case WITHDRAW:
		    	 System.out.println(inmsg);
		    	 break;
		    		 
		     case DEPOSIT:
		    	 System.out.println(inmsg);
		    	 break;
		    	 
		     case BALANCE:
		    	 System.out.println(inmsg);
		    	 break;
		    	 
		     default:
		    	 System.out.println("Something went wrong...");
		    	 System.exit(1);
		 }
	}
	
	public boolean isLogged() {
		
		return isLogged;
	}
	
}
