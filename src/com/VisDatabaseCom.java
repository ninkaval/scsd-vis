package com;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class VisDatabaseCom extends DatabaseCom {

	//----------------------------------------------------------------
	DashboardListener listener; 

	public DashboardListener getListener() {
		return listener;
	}

	public void setDashboardListener(DashboardListener listener) {
		this.listener = listener;
	}
	
	//----------------------------------------------------------------
	public VisDatabaseCom(){			
		super();
	}
	
	/** Downloads from the DB all participants who have submitted feedback for the specified category */
	public void run() {
		while (running) {
			Statement stmt 	= null;
	        ResultSet rs 	= null;

			try {
				
				//--------------------first of all query the active category for which we have to pull results 
				boolean isCatNew = queryCategory();
				if (isCatNew) {
					listener.categorySelected(activeCategoryID);
					//--------reset DB settings, so we can pull the stuff for the new category from the DB @see run()
		       		reset(); 
				}
				
				//--------------------pull results for new active category 
				if (dbResultsPublished) {
					stmt = connection.createStatement();
				
					String sqlString = "select * from participants where categoryID=" 
					+ activeCategoryID 
					+ " AND timestamp>" 
					+ dbLastTimestamp;
	
					//----------------save current last timestamp for comparison 
					long tmpStamp 	 = dbLastTimestamp; 
					
					if (stmt.execute(sqlString)) {
		                rs = stmt.getResultSet();
		            } else {
		                System.err.println("select failed");
		            }
					
					//------------------------------reset DB communication variables
					int resultCounter = 0; 		
					dbParticipants.clear(); 
					
					//read new results from DB 
					while (rs.next()) {
						String cardID 	= rs.getString(1);
						int devID 		= rs.getInt(2);
						int catID		= rs.getInt(3);
						int prefID		= rs.getInt(4);
						//dbLastTimestamp = rs.getLong(5);
						tmpStamp 		= rs.getLong(5);
						//--------------------------store new last timestamp value only if larger
						//--------------------------(if DB entries are stored in FILO fashion, the newest entrie (with largers timestamp) is first
						if (tmpStamp > dbLastTimestamp) {
							dbLastTimestamp = tmpStamp; 
							System.out.println("dbLastTimestamp : " + dbLastTimestamp + " category : " + catID);							
						}
						System.out.println("tmpStamp : " + resultCounter + " " + tmpStamp + " category : " + catID);
		                dbParticipants.add(new DatabaseParticipant(cardID, devID, catID, prefID, tmpStamp));
		                resultCounter++; 
		            }
					dbNumNewParticipants = resultCounter; 	
					
					if (dbNumNewParticipants > 0) {
						dbNewResults       	 = true;			//set flag to notify new results 
						dbResultsPublished   = false; 			//set flag to false so main app can publish new results
					}
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
		}
	}

	
	
	
	
}
