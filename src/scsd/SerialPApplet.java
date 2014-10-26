package scsd;

import processing.core.PApplet;
import processing.serial.Serial;

import com.DashboardDatabaseCom;
import com.DashboardSerialCom;
import com.DatabaseParticipant;


public class SerialPApplet extends PApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	////////////////////////////////////////////////////////////////////////////////
	// Pseudo Singleton
	////////////////////////////////////////////////////////////////////////////////
	private static SerialPApplet instance;
	
	public static SerialPApplet getInstance() {
		return instance;
	}
	
	public SerialPApplet() {
		super();
		instance = this;
	}

	DashboardDatabaseCom dbCom; 
	DashboardSerialCom	serialCom; 
	
//	public int mainActiveCategoryID; 					// the currently active category   
//    public int mainActiveDeviceID=0; 						// the device which we are currently working with 
//	
	public void setup(){
		size(200, 200, P3D);
		background(255, 100, 35);
		
		dbCom 		= new DashboardDatabaseCom(); 
		serialCom 	= new DashboardSerialCom(this); 
		serialCom.setDashboardListener(dbCom);
		
		// init DB communication
		if (!dbCom.isRunning()){ 
			println("Activate DB Communication Thread...!");
			dbCom.start();
			dbCom.resetCategories();
			dbCom.setHeart(1);//init state of heart btn is 1
		}		
		
	}

	public void draw(){
		//readDB();
		dbCom.validateDBConnection();
		serialCom.readSerialMsg();
		dbCom.updateHeart();
		
	}
	
	/** Query DB for new participants */
	public void readDB(){
		if(dbCom.areNewResults()) {
		    
			//println("readDB(): NEW PARTICIPANTS IN DB!!!");
		    int num = dbCom.getParticipants().size(); 
		    DatabaseParticipant tmpParticipant; 
		    
		    for (int j=0; j < num; j++) {
		    	tmpParticipant = dbCom.getParticipants().get(j);
		      
		      if (tmpParticipant != null) {
		    	String cardID 	= tmpParticipant.getCardID();
				int devID 		= tmpParticipant.getDevID();
				int catID		= tmpParticipant.getCatID();
				int prefID		= tmpParticipant.getPrefID();
				long time		= tmpParticipant.getTstamp();
				//System.out.println("Participant: " + cardID + ":" + devID + ":" + catID + ":" + prefID + ":" + time);
				
//			    int index = -1; 
//			    for (int i=0; i<main_numOptions; i++){
//			    	  if (optId == main_args_options[i]) {
//			    		  index = i; 
//			    		  break;
//			    	  }
//			    }
//			      
//			    if (index >= 0 /*&& condition == SessionManager.getInstance().getCondition().ordinal()*/){
//			    	//select the area that corresponds to that participant 
//				    OptionArea tmpArea = main_optionAreas[index];
//				    tmpArea.addConfirmed(img);	  
//			    }
			      	  
		      }
		    }
		    
		    dbCom.setNewResults(false);
		    dbCom.setResultsPublished(true);
		    
//		    for (int i=0; i<main_optionAreas.length; i++) {
//		    	main_optionAreas[i].updateData(); 
//		    }
		  }
	}

	
	

	/**
	 SerialEvent occurs whenever a new data comes in the
	 hardware serial RX. This event can only be called the main PApplet class. Thats why it 
	 calls the self-made serialEvent() func of the DashboardSerialCom object which does the real work 
	 */
	public void serialEvent(Serial p) {
		//System.out.println("MainApp::serialEvent"); 
		serialCom.serialEvent(p);
	}

	int catCounter = 0; 
	public void keyPressed() {
		switch (key) {
		case 'w':
			System.out.println("Simulate a participant and write it to DB...");
			try {
				dbCom.sentimentSubmitted((int)random(0, 3), ""+(int)random(20000, 40000));
				
//				dbCom.writeDB(new DatabaseParticipant(""+(int)random(20000, 40000),
//														 dbCom.getActiveCategoryID(), 
//														 (int)random(0, 5),
//														 (int)random(0, 3), 
//														 System.currentTimeMillis()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break; 
		
		case 'c': 
			catCounter = (catCounter + 1)%6;
			System.out.println("Simulate a category and write it to DB... " + catCounter);
			dbCom.categorySelected(catCounter) ;
			break; 
		case 'r': dbCom.resetCategories(); break;
		case 'h': dbCom.specialCategorySelected(0); break;
		case 'j': dbCom.setHeart(0); break;
		case 'k': dbCom.setHeart(1); break;
		}
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[]{"scsd.SerialPApplet"});
	}
	
	
}
