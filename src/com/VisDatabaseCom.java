package com;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.Assets;
import util.Timer;


public class VisDatabaseCom extends DatabaseCom {

	boolean   dbNewCategory;						// are there new participants in the DB
	boolean   dbCategoryPublished;				// are the new results published (registered with the viz) 
	
	boolean   dbNewHeart;						// are there new participants in the DB
	boolean   dbHeartPublished;				// are the new results published (registered with the viz) 
	
	int 	  dbHeartLast = 1;
	
	Timer dbValidTimer = null;
	String activeSQLString; 
	
	public void setActiveSQLString(int val){
		switch (val) {
		case 1: activeSQLString = "select * from participants where timestamp>" + dbLastTimestamp; 
		break;
		
		case 2: activeSQLString = "select * from participants where categoryID=" 
								+ activeCategoryID 
								+ " AND timestamp>" 
								+ dbLastTimestamp; 
		break;
		
		}
	}
	
	
	
	
//	//----------------------------------------------------------------
//	DashboardListener listener; 
//
//	public DashboardListener getListener() {
//		return listener;
//	}
//
//	public void setDashboardListener(DashboardListener listener) {
//		this.listener = listener;
//	}
	
	//----------------------------------------------------------------
	public VisDatabaseCom(){			
		super();
		
		dbCategoryPublished = true;
		dbHeartPublished	= true;
		setActiveSQLString(1);
		dbValidTimer = new Timer(60*1000);
		dbValidTimer.start();
	}
	
	
	/** just see every once in a while of the DB connection is still valid */
	public void validateDBConnection(){
		
		if (dbValidTimer.isFinished()){
			try {
				boolean isClosed = connection.isClosed();
				System.out.println("VisDatabaseCom::validateDBConnection() isClosed=" + isClosed);

				//recreate connection of closed 
				if (isClosed) {
						System.out.println("VisDatabaseCom::validateDBConnection() recreating... the connection!");
						connection = null;
						Class.forName("com.mysql.jdbc.Driver").newInstance();
						connection = DriverManager.getConnection(connectionURL, username, pw);
						System.out.println(connection.toString());
				}
				dbValidTimer.reset();

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("VisDatabaseCom::validateDBConnection() NullPointerException");
				e.printStackTrace();
			}
		}
	}
	
	public void reset() {
		dbLastTimestamp		= 0; 
		dbResultsPublished 	= true;
		dbParticipants.clear();		
		
//		dbCategoryPublished = true;
//		dbHeartPublished	= true;

	}
	
	
	/** Downloads from the DB all participants who have submitted feedback for the specified category */
	public void run() {
		if (connection == null) {
			System.out.println("VisDatabaseCom::run() connection is null, returning....");
			return;
		}
		
		while (running) {
			Statement stmtParticipants 		= null;
	        ResultSet resultParticipants 	= null;

	        Statement stmtCategory 			= null;
	        ResultSet resultCategory 		= null;
	        
	        Statement stmtHeart 			= null;
	        ResultSet resultHeart 			= null;
			try {
						
				if (dbCategoryPublished) {
					//System.out.println("dbCategoryPublished");
					
					int dbID 			= activeCategoryID;
			        String sqlString 	= "select * from categories where active=1";
			        
			       	stmtCategory 	= connection.createStatement();
			       	resultCategory 	= stmtCategory.executeQuery(sqlString);

			       	 while (resultCategory.next()) {
			       		 dbID = resultCategory.getInt(1);
			       	 }
			       	 
			       	 //------------active category in DB has changed & is not heart category 
			       	 if (dbID != activeCategoryID){
			       		 System.out.println("VisDatabaseCom::dbCategoryPublished: new category in DB: " + dbID);
			       		 
			       		 //--------set the new active category for the app
			       		 activeCategoryID 		 = dbID; 
			       		 
			       		 if (dbID > 0) //just to make sure we dont get a negative index access
			       			 activeCategoryString    = Assets.categoryNames[dbID-1];
			       		 else 
			       			activeCategoryString	 = Assets.categoryNames[5];
			       		 
			       		 dbNewCategory       	 = true;			//set flag to notify new results 
			       		 dbCategoryPublished   	 = false; 			//set flag to false so main app can publish new results
			       		 
			       	 }  else {
			       		 
			       	 }
				}
				
				//--------------------pull results for new active category 
				if (dbResultsPublished) {
					stmtParticipants = connection.createStatement();
				
					String sqlString = "select * from participants where categoryID=" 
										+ activeCategoryID 
										+ " AND timestamp>" 
										+ dbLastTimestamp;
					
					if (dbHeartLast == 0){
						sqlString = "select * from participants where timestamp>" 
								+ dbLastTimestamp;
					} 
					
					
//					sqlString = "select * from participants where categoryID=" 
//							+ activeCategoryID 
//							+ " AND timestamp>" 
//							+ dbLastTimestamp;
										
					//----------------save current last timestamp for comparison 
					long tmpStamp 	 = dbLastTimestamp; 
					
					if (stmtParticipants.execute(sqlString)) {
						resultParticipants = stmtParticipants.getResultSet();
		            } else {
		                System.err.println("select failed");
		            }
					
					//------------------------------reset DB communication variables
					int resultCounter = 0; 		
					dbParticipants.clear(); 
					
					//read new results from DB 
					while (resultParticipants.next()) {
						String cardID 	= resultParticipants.getString(1);
						int devID 		= resultParticipants.getInt(2);
						int catID		= resultParticipants.getInt(3);
						int prefID		= resultParticipants.getInt(4);
						//dbLastTimestamp = rs.getLong(5);
						tmpStamp 		= resultParticipants.getLong(5);
						//--------------------------store new last timestamp value only if larger
						//--------------------------(if DB entries are stored in FILO fashion, the newest entrie (with largers timestamp) is first
						if (tmpStamp > dbLastTimestamp) {
							dbLastTimestamp = tmpStamp; 
							//System.out.println("dbLastTimestamp : " + dbLastTimestamp + " category : " + catID);							
						}
						//System.out.println("tmpStamp : " + resultCounter + " " + tmpStamp + " category : " + catID);
		                dbParticipants.add(new DatabaseParticipant(cardID, devID, catID, prefID, tmpStamp));
		                resultCounter++; 
		            }
					dbNumNewParticipants = resultCounter; 	
					
					if (dbNumNewParticipants > 0) {
						dbNewResults       	 = true;			//set flag to notify new results 
						dbResultsPublished   = false; 			//set flag to false so main app can publish new results
					}
					

				}
				
				if (dbHeartPublished) {
					//System.out.println("dbHeartPublished");
	       			
			        String sqlString 	= "select * from heart";
			        
			       	stmtHeart 	= connection.createStatement();
			       	resultHeart	= stmtCategory.executeQuery(sqlString);

			       	int value 	= dbHeartLast;
			       	
			       	while (resultHeart.next()) {
			       		value = resultHeart.getInt(1);
			       	}
			       	 
			       	//------------active category in DB has changed & is not heart category 
			       	if (value != dbHeartLast){
			       		 System.out.println("VisDatabaseCom::dbHeartPublished: new heart in DB: " + value);
			       		 dbHeartLast = value;
			       		 //--------set the new active category for the app
			       		 dbNewHeart      	 = true;			//set flag to notify new results 
			       		 dbHeartPublished    = false; 			//set flag to false so main app can publish new results
			       		 
			       	}  else {
			       		 
			       	}
				}
				
			} catch (NullPointerException e) {
				System.out.println("VisDatabaseCom::run() NullPointerException");
				e.printStackTrace();
			 
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				//////////////////////////////////////////////
				//---------participants
	            if (resultParticipants != null) {
	                try {
	                    resultParticipants.close();
	                } catch (SQLException ex) { /* ignore */ }
	                resultParticipants = null;
	            }
	            if (stmtParticipants != null) {
	                try {
	                	stmtParticipants.close();
	                } catch (SQLException ex) { /* ignore */ }
	                stmtParticipants = null;
	            }
	            /////////////////////////////////////////////
	            //----------category 
	            if (resultCategory != null) {
	                try {
	                    resultCategory.close();
	                } catch (SQLException ex) { /* ignore */ }
	                resultCategory = null;
	            }
	            if (stmtCategory != null) {
	                try {
	                	stmtCategory.close();
	                } catch (SQLException ex) { /* ignore */ }
	                stmtCategory = null;
	            }
	            //////////////////////////////////////////////
	            //---------heart
	            if (resultHeart != null) {
	                try {
	                	resultHeart.close();
	                } catch (SQLException ex) { /* ignore */ }
	                resultHeart = null;
	            }
	            if (stmtHeart != null) {
	                try {
	                	stmtHeart.close();
	                } catch (SQLException ex) { /* ignore */ }
	                stmtHeart = null;
	            }
	            //////////////////////////////////////////////
	        }
		}
	}

	public boolean isNewCategory() {
		return dbNewCategory;
	}

	public void setNewCategory(boolean b) {
		dbNewCategory = b;
	}

	public void setCategoryPublished(boolean b) {
		dbCategoryPublished = b;
	}

	public boolean isNewHeart() {
		return dbNewHeart;
	}

	public void setNewHeart(boolean b) {
		dbNewHeart = b;
	}

	public void setHeartPublished(boolean b) {
		dbHeartPublished = b;
		
	}

	public int getHeart() {
		return dbHeartLast;
	}

	
	
	
	
	
}
