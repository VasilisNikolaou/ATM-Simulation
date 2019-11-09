package DataTier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
 *  I have already created a database called atm
 *  Database already have a table called atm with 4 columns (balance integer, total integer, id primary key integer, date date)
 *  We assume that user can withdraw a total amount that can not pass 'totalMoneyPerDay'
 *  Every new day we set it to zero.
 *  atm table already have some registers for simplicity.
 */



public class Database {
	
	 private Connection con;
	 private String dbName;
	 
	 //Maximum amount every user can make per day.
	 private final int totalMoneyPerDay = 400;
	 
	 private static final String USERNAME = "root";
	 private static final String PASSWORD = "vasilisPaok1230";
	 private int id;
	 
	 //The date 'NOW'
	 private Date date;
	 private SimpleDateFormat formatter;
	
	 public Database()
	 {
		  try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", USERNAME, PASSWORD);
			dbName = "atm";
			date = new Date();
			formatter = new SimpleDateFormat("yyy-MM-dd");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 
	 private void checkIfDayPassed() {
		 
		 String preparedQuery = "UPDATE " + dbName + " SET date = ?" + ", total = 0" + " WHERE id = ?";
		 String query = "SELECT date FROM " + dbName;
		 
		 try
		 (
			  PreparedStatement stmt = con.prepareStatement(preparedQuery);
			  Statement readStmt =  con.createStatement();
		      ResultSet rs = readStmt.executeQuery(query);
				 
		 ) {
				
			  rs.absolute(id);
			  Date dbDate = rs.getDate("date");
			  
			 
			  String s1 = formatter.format(date);
			  String s2 = formatter.format(dbDate);
				
			  if(s1.compareTo(s2) > 0)
			  {
				  System.out.println("in");
				  java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				  stmt.setDate(1, sqlDate);
				  stmt.setInt(2, id);
				  stmt.executeUpdate();
			  }
				
			} catch (SQLException e) {
		
				e.printStackTrace();
			}
	 }
	 
	 /* LIMITATIONS
	  * 1. new balance must not be negative.
	  * 2. We have a maximum amount every user can make per day. (we can't exceed it)
	  * 3. Amount given by the user must be able to break it down into multiples of 50 and 20.
	  */
	 public String withdrawDatabase(int amount)
	 {
		  String query = "SELECT balance, total FROM " + dbName;
		  String answer = null;
				          
		  try(
				
			 Statement readStmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			 Statement updateStmt = con.createStatement();
			 ResultSet rs = readStmt.executeQuery(query);
				  
		  ) {
			  
			  con.setAutoCommit(false);
			  
			  rs.absolute(id);
			  
			  //Modify balance
			  int oldBalance = rs.getInt("balance");
			  int newBalance = oldBalance - amount;
			  
			  System.out.println("oldBalance is: " + oldBalance);
			  System.out.println("newBalance is: " + newBalance);
			  
			  //Modify total
			  int oldTotal = rs.getInt("total");
			  int newTotal = oldTotal + amount;
			  
			  System.out.println("oldTotal is: " + oldTotal);
			  System.out.println("newTotal is: " + newTotal);
			  
			  //Updating
			  updateStmt.executeUpdate("UPDATE " + dbName + " SET balance = " + newBalance + " , total = " + newTotal + " WHERE id = " + id);
			  
			  int money50 = amount / 50;
			  int newAmount = amount - money50 * 50;
			  int money20 = newAmount / 20;
			  
			  if((money50 * 50 + money20 * 20) != amount)
			  {
				  System.out.println("Not valid amount to withdraw");
				  System.out.println("Rolling back the transaction...");
				  con.rollback();
				  return "Not valid amount to withdraw";
			  }
			  
			  
			  if(newBalance < 0)
			  {
				  System.out.println("Not enough money");
				  System.out.println("Rolling back the transaction...");
				  answer = "Not enough money";
				  con.rollback();
				  return answer;
			  }
			  
			   
			  if(newTotal > totalMoneyPerDay)
			  {
				  System.out.println("Exceeded today's limit");
				  System.out.println("Rolling back the transaction...");
				  answer = "Exceeded today's limit";
				  con.rollback();
				  return answer;
			  }
			  
			  answer = "Successful withdraw";
			  con.commit();
			  con.setAutoCommit(true);
			  
		  } catch (SQLException e) {
			e.printStackTrace();
		  }
		  
		  return answer;
	 }
	 
	 /* LIMITATIONS
	  * Amount given by the user must be able to break it down into multiples of 5.
	  */
	 public String depositDatabase(int amount)
	 {
		 String query  = "UPDATE " + dbName + " SET balance = balance + ?" + " WHERE id = ?";
		 String answer = null;
		 if(amount % 5 == 0)
		 {
			 try(PreparedStatement stmt = con.prepareStatement(query))
			 {
				 stmt.setInt(1, amount);
				 stmt.setInt(2, id);
				 stmt.executeUpdate();
				 System.out.println("Deposit completed.");
				 return answer = "Deposit completed.";
				 
			 } catch (SQLException e) {
				e.printStackTrace();
			 }
			 
		 } else
		 {
			 System.out.println("Wrong amount");
			 answer = "Wrong amount";
		 }
		 
		 return answer;
		 
	 }
	 
	 public String printBalanceDatabase()
	 {
		 String query = "SELECT balance FROM " + dbName + " WHERE id = " + id;
		 String answer = null;
		 
		 try
		 (
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
		 ) {
			 
			 rs.next();
			 System.out.println("Your balance is: " + rs.getInt("balance"));
			 answer = "Your balance is: " + rs.getInt("balance");
			 
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
		 
		 return answer;
	 }
	 
	 public void closeConnection()
	 {
		 try {
			con.close();
			System.out.println("Connection closed ...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 public boolean chechIfExists(int id) {
		 
		 String query = "SELECT id FROM " + dbName  
				        + " WHERE id = " + id;
		 
		 try(
			  Statement stmt = con.createStatement();
			  ResultSet rs = stmt.executeQuery(query);
			)
		 {
			 if(!rs.next())
			 {
				 System.out.println("User with id = " + id + " does not exist.");
				 return false;
			 }
			 
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
		 
		 this.id = id;
		 checkIfDayPassed();
		 return true;
	 } 	 

}