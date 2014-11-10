package com;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import util.Timer;


public class DatabaseCom extends Thread {

		
	boolean running;    // Is the thread running?  
    boolean available;  // Are there new things available?
	  
    String 	  	lastActiveCardID = "";			// controls that users dont swipe too much 
	int 		activeDeviceID=1; 						// the device which we are currently working with 
	int 		activeCategoryID=0; 					// the currently active category   
	
    boolean   	newCategory; 					// is there a new category set in the DB 
    boolean   	categoryPublished; 				// is the new category published (registered with the viz) 


	//-----------------------declare a connection by using Connection interface 
	Connection connection = null; 
	
	//-----------------------create string of connection url within specified format with machine name, port number and database name.
	
//	String host 		= "db486390501.db.1and1.com";
//	String port 		= "3306";
//	String dbName		= "db486390501";
//	String username 	= "dbo486390501";
//	String pw			= "SCSD2013";
//	
	String host 		= "23karat.de";
	String port 		= "3306";
	String dbName		= "karat_SCSD";
	String username 	= "karat_49";
	String pw			= "ypbDgAWwQgtVjX41";
	
//	String host 		= "127.0.0.1";
//	String port 		= "3306";
//	String dbName		= "scsd";
//	String username 	= "root";
//	String pw			= "";
	
	String connectionURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	
	boolean   dbNewResults;						// are there new participants in the DB
	boolean   dbResultsPublished;				// are the new results published (registered with the viz) 
    long 	  dbLastTimestamp;					// last time results were published     
    
    int       dbNumNewParticipants;				// num of newcoming participants (from DB) 
    ArrayList<DatabaseParticipant> dbParticipants;	// contains the current new results from the DB 
    
    
	public void reset() {
		dbLastTimestamp		= 0; 
		dbResultsPublished 	= true;
		dbParticipants.clear();		

//		categoryPublished 	= true;
//		activeCategoryID  = -1; 			
//	    lastActiveCardID = "";			// controls that users dont swipe too much 

	}
    
	
	public DatabaseCom(){
		try { 
			dbLastTimestamp			= 0; 
			dbResultsPublished 		= true; 
			dbParticipants 			= new ArrayList<DatabaseParticipant>(); 
			  
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 

			connection = DriverManager.getConnection(connectionURL, username, pw); 
			System.out.println(connection.toString());
			
			//System.out.println("DBCommunicator created!");
		} catch (Exception ex) { 
			System.out.println("DatabaseCom(): " + ex); 
			ex.printStackTrace(); 
		} 	
		
	}
	
	@Override
	public void start() {
	    running = true;
	    super.start();
	}
	  	
		
	/** To be implemented in subclasses */
	public void run() {
		while (running) {

		}
	}
	
	 
	public void writeDB (DatabaseParticipant participant) throws Exception {
		
		
		//---------------declare prepare statement. 
		PreparedStatement psmnt = null; 
		
		try { 			
			psmnt = connection.prepareStatement 
			("insert into participants(cardID, deviceID, categoryID, preferenceID, timestamp) " + "values(?,?,?,?,?)"); 
			
			if (participant != null) participant.print();
			//if (true) return; 
			
			if (psmnt != null){
				psmnt.clearParameters();
				
				psmnt.setString(1,	participant.getCardID()); 
				psmnt.setInt(2, participant.getDevID());
				psmnt.setInt(3,	participant.getCatID());
				psmnt.setInt(4, participant.getPrefID()); 
				psmnt.setLong(5,participant.getTstamp());
				
				int s = 0; 
				
				//s = psmnt.execute();
				//s = psmnt.executeUpdate(); 
				
				System.out.println("s = psmnt.execute() " + s);
				
				if(s>0) { 
					System.out.println("Participant uploaded successfully to DB!"); 
				} 
				else { 
					System.out.println("Unsucessfull upload to DB."); 
				} 		
			} else {
				System.out.println("writeDB(): PreparedStatement is null"); 
			}
				
		} catch (Exception ex) { 
			System.out.println("Found some error : "+ex); 
		} finally { 
			if (psmnt != null)
				psmnt.close(); 
		}	
	}
	
	public void close(){	
	}
	
	 void quit() {
		System.out.println("Quitting DB Communication Thread..."); 
		try {
			running = false;  // Setting running to false ends the loop in run()
			
			connection.close();	
			// In case the thread is waiting. . .
			interrupt();
	     
		} catch (Exception ex) { 
			System.out.println("quit(): error : "+ex); 
		}
	}

	
	boolean available() {
		 return available;
    }
	
	
	//Queries DB and returns the total number of participants in the DB   
	public int queryNumParticipants(){
			int num = 0; 
			Statement stmt 	= null;
	        ResultSet rs 	= null;
			
	        try {
	        	 stmt 	= connection.createStatement();
	        	 rs 	= stmt.executeQuery("select count(*) from participants");

	        	 while (rs.next()) {
	        		 num = rs.getInt(1);
	        	 }
	        } catch (Exception e) {
	        	e.printStackTrace(); 
	        } finally {
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException ex) { /* ignore */ }
	                rs = null;
	            }
	            if (stmt != null) {
	                try {
	                    stmt.close();
	                } catch (SQLException ex) { /* ignore */ }
	                stmt = null;
	            }
	        }
			return num; 
		}
	
		
	//Queries DB and returns the number of participants to the specified deviceID  
	public int queryNumParticipants(int deviceID){
		int num = 0; 
		Statement stmt 	= null;
        ResultSet rs 	= null;
		
        try {
        	 stmt 	= connection.createStatement();
        	 rs 	= stmt.executeQuery("select count(*) from participants WHERE deviceID="+deviceID);

        	 while (rs.next()) {
        		 num = rs.getInt(1);
        	 }
        } catch (Exception e) {
        	e.printStackTrace(); 
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) { /* ignore */ }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) { /* ignore */ }
                stmt = null;
            }
        }
		return num; 
	}
	
	public boolean queryCategory() {
		boolean ret = false; 
		
		Statement stmt 	= null;
        ResultSet rs 	= null;
        
        String sqlString = "select * from categories where active=1";
        
        int dbID = -1;
        
        try {
       	 stmt 	= connection.createStatement();
       	 rs 	= stmt.executeQuery(sqlString);

       	 while (rs.next()) {
       		 dbID = rs.getInt(1);
       	 }
       	 
       	 //------------active category in DB has changed 
       	 if (dbID != activeCategoryID){
       		 System.out.println("DatabaseCom::queryCategory(): new category in DB: " + dbID);
       		 //--------set the new active category for the app
       		 activeCategoryID = dbID; 
       		 ret = true; 
       	 } 
       	 else ret = false; 
       		 
       } catch (Exception e) {
       	e.printStackTrace(); 
       } finally {
           if (rs != null) {
               try {
                   rs.close();
               } catch (SQLException ex) { /* ignore */ }
               rs = null;
           }
           if (stmt != null) {
               try {
                   stmt.close();
               } catch (SQLException ex) { /* ignore */ }
               stmt = null;
           }
       }
       return ret;        
	}

	public int queryDevice() {
		Statement stmt 	= null;
        ResultSet rs 	= null;
        
        String sqlString = "select * from interactionDevices where active=1";
        
        int dbID = -1;
        
        try {
       	 stmt 	= connection.createStatement();
       	 rs 	= stmt.executeQuery(sqlString);

       	 while (rs.next()) {
       		 dbID = rs.getInt(1);
       	 }
       	 
       	 //------------active category in DB has changed 
       	 if (dbID != activeDeviceID){
       		 System.out.println("queryDevice(): active device in DB: " + dbID);
       		 //--------set the new active category for the app
       		 activeDeviceID = dbID; 
       		 //--------reset DB settings, so we can pull the stuff for the new category from the DB @see run()
       		 reset();
       	 }
       		 
       } catch (Exception e) {
       	e.printStackTrace(); 
       } finally {
           if (rs != null) {
               try {
                   rs.close();
               } catch (SQLException ex) { /* ignore */ }
               rs = null;
           }
           if (stmt != null) {
               try {
                   stmt.close();
               } catch (SQLException ex) { /* ignore */ }
               stmt = null;
           }
       }
       return -1;        
	}
    public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean areNewResults() {
		return dbNewResults;
	}

	public void setNewResults(boolean dbNewResults) {
		this.dbNewResults = dbNewResults;
	}

	public boolean areResultsPublished() {
		return dbResultsPublished;
	}

	public void setResultsPublished(boolean dbResultsPublished) {
		this.dbResultsPublished = dbResultsPublished;
	}

	
	public int getNumNewParticipants() {
		return dbNumNewParticipants;
	}

	public void setNumNewParticipants(int dbNumNewParticipants) {
		this.dbNumNewParticipants = dbNumNewParticipants;
	}

	public ArrayList<DatabaseParticipant> getParticipants() {
		return dbParticipants;
	}

	public void setParticipants(ArrayList<DatabaseParticipant> dbParticipants) {
		this.dbParticipants = dbParticipants;
	}

	public int getActiveCategoryID() {
		return activeCategoryID;
	}

	public void setActiveCategoryID(int activeCategoryID) {
		this.activeCategoryID = activeCategoryID;
	}

	public int getActiveDeviceID() {
		return activeDeviceID;
	}

	public void setActiveDeviceID(int activeDeviceID) {
		this.activeDeviceID = activeDeviceID;
	}
	
	


	
}
