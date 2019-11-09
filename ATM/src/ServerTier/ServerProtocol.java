package ServerTier;

import DataTier.Database;

public class ServerProtocol {
	
	private static final int LOGIN = 1;
	private static final int WITHDRAW = 2;
	private static final int DEPOSIT = 3;
	private static final int BALANCE = 4;
	
	private static final int ERROR = -1;
	
	private Database db = new Database();
	
	public int getAction(String inmsg)
	{
		char action = inmsg.charAt(0);
		
		switch(action)
		{
		    
		    case 'I' :
		    	return LOGIN;
		    
		    case 'W' :
		    	return WITHDRAW;
		    	
		    case 'D' :
		    	return DEPOSIT;
		    	
		    case 'B' :
		    	return BALANCE;
		    	
		    default:
		    	return ERROR;
		}
		
	}
	
	public String findById(int id)
	{
		return String.valueOf(db.chechIfExists(id));
	}
	
	public String withDrawAmount(int amount)
	{
		return db.withdrawDatabase(amount);
	}
	
	public String depositAmount(int amount)
	{
		return db.depositDatabase(amount);
	}
	
	public String showBalance() {
		
		return db.printBalanceDatabase();
	}
	
	public void closeConnectionWithDB() {
		
		db.closeConnection();
	}

}
