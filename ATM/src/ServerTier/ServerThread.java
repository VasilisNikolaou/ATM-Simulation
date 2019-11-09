package ServerTier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread
{
    private Socket dataSocket;
    private BufferedReader in;
    private PrintWriter out;
    
    private static final String EXIT = "Exit";
    private static final int ERROR = -1;
    
    private static final int LOGIN = 1;
	private static final int WITHDRAW = 2;
	private static final int DEPOSIT = 3;
	private static final int BALANCE = 4;
	
	public ServerThread(Socket socket) throws IOException
	{
		this.dataSocket = socket;
		
		in = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
		out = new PrintWriter(dataSocket.getOutputStream(), true);
		
	}
	
	@Override
	public void run()
	{
		
		 String inmsg;
		 String outmsg = null;
		 int action;
		 
		 try {
			 
			 inmsg = in.readLine();
			 ServerProtocol protocol = new ServerProtocol();

             while(!inmsg.equals(EXIT))
             {
            	 
            	  action = protocol.getAction(inmsg);
            	  
            	  if(action == ERROR)
            	  {
            		   System.out.println("Something went wrong...");
            		   out.println(EXIT);
            		   break;
            	  }
            	  
            	  String s[] = inmsg.split("\\s+");
 
            	  
            	  if(action == LOGIN)
            	  {
            		  outmsg = protocol.findById(Integer.parseInt(s[1]));
            	  }
            	  
            	  if(action == WITHDRAW)
            	  {
            		  outmsg = protocol.withDrawAmount(Integer.parseInt(s[1]));
            	  }
            	  
            	  if(action == DEPOSIT)
            	  {
            		  outmsg = protocol.depositAmount(Integer.parseInt(s[1]));
            	  }
            	  
            	  if(action == BALANCE)
            	  {
            		  outmsg = protocol.showBalance();
            	  }
            	  
            	  out.println(outmsg);
            	  inmsg = in.readLine();
             }
             
             System.out.println("Closing connection to client");
             protocol.closeConnectionWithDB();
 			 dataSocket.close();
             
		 } catch(IOException ioe) 
		 {
			 ioe.printStackTrace();
		 }
		 
		 
	}
}
