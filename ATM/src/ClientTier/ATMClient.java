package ClientTier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ATMClient {

	private static final String HOST = "localhost";
	private static final int PORT = 1234;
	
	private static final String EXIT = "Exit";
	
	public static void main(String[] args) {
	   
        try
        (
           Socket dataSocket = new Socket(HOST, PORT);
           BufferedReader in = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
           PrintWriter out = new PrintWriter(dataSocket.getOutputStream(), true);
           BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        		
        ) {
        	ClientProtocol protocol = new ClientProtocol();
        	
        	String msg;
        	String outmsg;
        	String inmsg;
        	int choice;
        	
        	while(true)
        	{
        		
        		/* We checking user input
        		 * and then we process it via protocol method
        		 */
        		do
            	{
            		showMenu();
            		msg = keyboard.readLine();
            		choice = protocol.processChoice(msg);
            		
            	} while(choice == -1);
            	
     
            	outmsg = protocol.prepareRequest(choice);
        		
        		if(outmsg.equals(EXIT)) {
        			
        			out.println(outmsg);
        			break;
        		}
        		
        		out.println(outmsg);
        		inmsg = in.readLine();
	   			protocol.processReply(inmsg, choice);
	   	
        	}
        	
        	
        	
        } catch (UnknownHostException e) {
        	
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        
        System.out.println("Data Socket closed");
	}
	
	
	private static void showMenu()
	{
	    ClientProtocol protocol = new ClientProtocol();
	    
	    
	    if(!protocol.isLogged()) {
	    	
	    	System.out.println("*** WELCOME TO ATM ***");
	    	System.out.println("OPTIONS AVAILABLE");
	    	System.out.println("1.Login");
	    	
	    } else {
	    	
	    	System.out.println("OPTIONS AVAILABLE");
	    	System.out.println("2.Withdraw");
	    	System.out.println("3.Deposit");
	    	System.out.println("4.Balance");
	    }
	    
	    System.out.println("5.Exit");
	    
	    System.out.println("Enter your choice: ");
	}

}
