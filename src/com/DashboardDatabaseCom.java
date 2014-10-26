package com;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import util.Timer;

class TimedString{
	String str; 
	Timer  timer; 
	
	TimedString(String str){
		this.str = str; 
		timer = new Timer(1 * 3 * 1000);
		timer.start();
	}
		
	boolean isLocked(){
		if (!timer.isFinished()) { 
			//System.out.println(str + ": locked");
			return true; 
		} else {
			//System.out.println(str + ": released");
			timer.reset(); 
			return false;
		}
	}
}

class TimedStringList {
	ArrayList<TimedString> timedStrings; 
	
	public TimedStringList() {
		timedStrings = new ArrayList<TimedString>();
	}
	
	public void reset(){ timedStrings.clear(); }
	
	public TimedString contains(String str){
		Iterator<TimedString> it = timedStrings.iterator();
		while(it.hasNext())
		{
			TimedString obj = it.next();
			//System.out.println("Check-Str: " + str + " Obj: " + obj.str);
			if (obj.str.equals(str)) {
				//System.out.println("Str " + str + " exists in List!!!!");
				return obj; 
			}
		}
		return null; 
	}
	
	public void add(String str){
		timedStrings.add(new TimedString(str));
	}

	public int size() {
		return timedStrings.size();
	}
}

public class DashboardDatabaseCom extends DatabaseCom implements DashboardListener{

	TimedStringList cardIDList = null; 
	boolean 		useCardIDList = true; 
	Timer 			heartTimer = null;
	boolean 		heartState = false; 
	int 			heartTimerSeconds = 20;
    Timer 			dbValidTimer = null;

	public DashboardDatabaseCom(){			
		super();
		
		cardIDList = new TimedStringList(); 
		System.out.println("usedCardIDs: " + cardIDList.size());
		heartTimer = new Timer(1 * heartTimerSeconds * 1000);
		dbValidTimer = new Timer(60 * 1000);
		dbValidTimer.start();
	}
	
	/** just see every once in a while of the DB connection is still valid */
	public void validateDBConnection(){
		if (dbValidTimer.isFinished()){
			try {
				boolean isClosed = connection.isClosed();
				System.out.println("DashboardDatabaseCom::validateDBConnection() isClosed=" + isClosed);
				
				//recreate connection of closed 
				if (isClosed) {
					System.out.println("DashboardDatabaseCom::validateDBConnection() recreating... the connection!");
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
				e.printStackTrace();
			}
		}
	}
	
	public void reset(){
		super.reset();
		resetCardList();
	}
	
	public void resetCardList(){
		cardIDList.reset();
	}
    
	@Override
	public void categorySelected(int _catID) {
		//TODO: remove if you want this function to work
		//if (true) return; 
		
		System.out.println();
		System.out.println("DashboardDatabaseCom::categorySelected(): " + _catID);
		
		resetCategories();
		
		String query1 				= "update categories set active = 1 where id = " + _catID;			 //activate new category
		PreparedStatement psmnt 	= null; 
				
		try { 
			//---------------activate new category		
			psmnt 			= connection.prepareStatement(query1); 
			System.out.println(psmnt.toString());
			
			int s 			= 0; 
			s = psmnt.executeUpdate(); 
			
			//---------------update category
			if(s>0) { 
				activeCategoryID 	= _catID; 
				System.out.println("Category <" + activeCategoryID + "> successfully activated in DB!"); 
			} 
			else { 
				System.out.println("Unsucessfull activation of category to DB."); 
			} 
			
			//---------------delete usedCardIDs list 
			cardIDList.reset();
			
		} catch (Exception ex) { 
			System.out.println("Found some error : "+ex); 
		} finally { 
			try {
				if (psmnt!=null)
					psmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}	
		
	}

	@Override
	public void sentimentSubmitted(int _prefID, String _cardID) {
		
		//Dont do anything if the active category is ZERO 
		if (activeCategoryID == 0) {
			return; 
		}
		
		//TODO: remove if you want this function to work
		//if (true) return;
			
		TimedString timedStr = null;
		timedStr = cardIDList.contains(_cardID); 
		
		PreparedStatement psmnt = null; 
		String query 			= "insert into participants(cardID, deviceID, categoryID, preferenceID, timestamp) " + "values(?,?,?,?,?)";

		try {
			
			
			//------------------add a TimedString to the list only if the string is new 
			if (useCardIDList){
				if (timedStr == null) {
					cardIDList.add(_cardID);
					System.out.println();
					System.out.println("DashboardDatabaseCom::sentimentSubmitted() : NEW cardID("+_cardID+")" + " for category <" + activeCategoryID +">" + " prefID <"+_prefID+">");
				} else if (timedStr != null && !timedStr.isLocked()){
					System.out.println();
					System.out.println("DashboardDatabaseCom::sentimentSubmitted() : UNLOCKED! cardID("+_cardID+")" + " for category <" + activeCategoryID +">" + " prefID <"+_prefID+">");
				} else if (timedStr != null && timedStr.isLocked()){
					//System.out.println("DashboardDatabaseCom::sentimentSubmitted() : SORRY! cardID("+_cardID+") is temporarily locked...!");
					return; 
				}
			}
			
			psmnt 			= connection.prepareStatement(query); 
			
			if (psmnt != null){
				psmnt.clearParameters();
				psmnt.setString(1,	_cardID); 
				psmnt.setInt(2, 	activeDeviceID);
				psmnt.setInt(3,		activeCategoryID);
				psmnt.setInt(4, 	_prefID); 
				psmnt.setLong(5,	System.currentTimeMillis());
				
				int s = psmnt.executeUpdate(); 
				
				if(s>0) { 
					System.out.println("Participant uploaded successfully to DB!"); 
				} 
				else { 
					System.out.println("Unsucessfull upload to DB."); 
				} 		
			} else {
				System.out.println("DashboardDatabaseCom::sentimentSubmitted(): PreparedStatement is null"); 
			}
			
			//------------------func arrives here only if new cardID or unlocked cardID 
//			writeDB(new DatabaseParticipant(_cardID, 
//											activeDeviceID, 
//											activeCategoryID, 
//											_prefID, 
//											System.currentTimeMillis()));
			
			//IMPORTANT: this is a test, where a district corresponds to a sentiment (preference) id 
//			writeDB(new DatabaseParticipant(_cardID, 
//					_prefID, 
//					activeCategoryID, 
//					_prefID, 
//					System.currentTimeMillis()));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void specialCategorySelected(int _val) {
		System.out.println("DashboardDatabaseCom::specialCategorySelected: " + _val);
		
		if (_val == 0 && !heartState) {
			 setHeart(0);
			 heartTimer.start();
			 heartState = true; 
			 System.out.println("DashboardDatabaseCom::specialCategorySelected(): ---------<3 heart state ON!");
		} else {
			System.out.println("DashboardDatabaseCom::specialCategorySelected: DONT DO ANYTHING YET");
		}
//		//TODO do something with it in the DB 
//		if (_val == 1) {
//			activeCategoryID = 5;
//		} else if (_val == 0) {
//			activeCategoryID = -1;
//		}
//		
	}
	
	public void updateHeart(){
		if (heartState && heartTimer.isFinished()){
			heartState = false; 
			setHeart(1);
			System.out.println("DashboardDatabaseCom::updateHeart(): ---------<3 heart state OVER!");
		}
	}
	
	public void setHeart(int val){
		String query1 				= "update heart set value = " + val;
		PreparedStatement psmnt 	= null; 
		try { 
			//---------------activate new category		
			psmnt 			= connection.prepareStatement(query1); 
			
			int s 			= 0; 
			
			if (psmnt!=null){
				System.out.println(psmnt.toString());
				s = psmnt.executeUpdate();
			}
			
			//---------------update category
			if(s>0) { 
				System.out.println("Heart successfully resetted in DB!"); 
			} 
			else { 
				System.out.println("Unsucessfull resetting of heart to DB."); 
			} 
			
		} catch (Exception ex) { 
			System.out.println("Found some error : "+ex); 
		} finally { 
			try {
				if (psmnt!=null)
					psmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public void resetCategories(){
		System.out.println();
		System.out.println("DashboardDatabaseCom::resetCategories():");
		
		String query1 				= "update categories set active = 0";			 //activate new category
		PreparedStatement psmnt 	= null; 
				
		try { 
			//---------------activate new category		
			psmnt 			= connection.prepareStatement(query1); 
			
			int s 			= 0; 
			
			if (psmnt!=null){
				System.out.println(psmnt.toString());
				s = psmnt.executeUpdate();
			}
			
			//---------------update category
			if(s>0) { 
				System.out.println("Categories successfully resetted in DB!"); 
			} 
			else { 
				System.out.println("Unsucessfull resetting of category’es to DB."); 
			} 
			
		} catch (Exception ex) { 
			System.out.println("Found some error : "+ex); 
		} finally { 
			try {
				if (psmnt!=null)
					psmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}		
	}

	


	
}
