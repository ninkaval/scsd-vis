package scsd;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import toxi.physics2d.VerletPhysics2D;
import util.Assets;
import util.EventScheduler;
import vis.EnergyUserData;
import vis.SunburstCenter;
import vis.SunburstVis;

import com.DatabaseParticipant;
import com.VisDatabaseCom;




public class MainPApplet extends PApplet /* implements DashboardListener */{


	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////
	// Pseudo Singleton
	////////////////////////////////////////////////////////////////////////////////
	private static MainPApplet instance;
	
	public static MainPApplet getInstance() {
		return instance;
	}
	
	public MainPApplet() {
		super();
		instance = this;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Utility Objects 
	////////////////////////////////////////////////////////////////////////////////
	//WebCom 	mainCom; 
	EventScheduler scheduler;
	
	
	//////////////////////////////////////////////////////////////////////
	// Facade Mask Objects 
	//////////////////////////////////////////////////////////////////////
	PGraphics   pgTextureUnfold1 		= null; 
	PGraphics   pgTextureUnfold2 		= null; 
	PGraphics   pgFacadeUnfold			= null;
	PGraphics   pgTextureUnfoldRotated  = null;
	float 		pgScaleFact 			= 1.5f; 
	PImage 		testImage 				= null; 

	
	//-------------------------------------------------------------------
	boolean comOn 				= true    ;
	boolean comActive 			= false;
	
	
	
	//-------------------------------------------------------------------
	boolean moveLegend 			= false;
	float   scaleScreenFactor 	= 0.8f;
	boolean scaleScreen     	= false;
	boolean rotateScreen    = false; 
	boolean moveInfo       = false;
	boolean moveTitle      = false; 
	
	float rot = -HALF_PI; 
	
	
	
	//--------------------------------------------------------------------
	// Debugging
	boolean over 		= false;
	boolean move 		= false;  // If mouse down and over
	boolean recordPdf   = false;
	boolean debugScreen = false;
	int   	debugCol  = color(0,255,0);
	
	//--------------------------------------------------------------------
	
	//////////////////////////////////////////////////////////////////////
	// global variables 
	//////////////////////////////////////////////////////////////////////
	public static VerletPhysics2D mainPhysicsEngine;

	public static SunburstVis 	mainSunburstVisualization; 
	public static SunburstCenter 	mainCenterVis; 
	public static EnergyUserData 	mainUserData; 
	
	public static boolean g_bFirstLaunch = true; 

	//--------------------------------------------------------------------Database Com 
	public VisDatabaseCom 			dbCom; 

	public static boolean 	g_newResults;
	public static boolean 	g_resultsPublished;
	public static String  	g_lines[];
	public static int 		g_lastTimestamp;
	public static int 		g_num_new_participants;
	public static String[]  temp;//TODO check if this needs to be a global
	
	//--------------------------------------------------------------------
	PGraphics pgVisFullscreen; 
	PGraphics pgVisSmall; 
	PGraphics pgVisLegend;
	
	///*
	public void init() {
		if (frame != null) {
			frame.removeNotify();// make the frame not displayable
			frame.setResizable(false);
			frame.setUndecorated(true);
		     println("MainPApplet::init(): Frame is at "+frame.getLocation());
		    frame.addNotify();
		}
		super.init();
		
	}//*/
		
	@Override
	public void setup() {
		//size(1800, 768, PConstants.OPENGL);
		size(displayWidth, displayHeight, P3D);//FIXME: we use JAVA2D for the shapes (svg) to be rendered OK 
		
		//((PGraphicsOpenGL)g).textureSampling(3);
	    
		//hint(ENABLE_NATIVE_FONTS);
	    
		
	    frameRate(60);

	    Assets.load(this);
	    
		//--------------------------------------------------------------------
		scheduler 			= new EventScheduler(this);						//TODO check it  
	  	
		mainPhysicsEngine 					= new VerletPhysics2D();		//TODO: think about: pass as an argument to vis ???
		mainSunburstVisualization   		= new SunburstVis();
		mainCenterVis   					= new SunburstCenter(); 
	  
		//--------------------------------------------------------------------
		mainUserData 						= new EnergyUserData();
		mainUserData.registerVis(mainSunburstVisualization);
		
		//--------------------------------------------------------------------
//		mainCom   					= new WebCom();
//	  	g_lastTimestamp 		= 1;
//		g_resultsPublished 		= true;
		
		dbCom					= new VisDatabaseCom(); 
		//dbCom.setDashboardListener(this);
		
		//--------------------------------------------------------------------
//		facadeDims 				= Dimensions.getInstance();
//		facadeGeom  			= new Geometry(facadeDims.getPoints());

	//	testImage 				= loadImage("tex3.jpg");
	//	if (testImage !=null)   pgScaleFact	= testImage.width / facadeDims.totalWidth;
	
		pgScaleFact = 1.0f;
		
		//String mainRendererString = "P2D";
		//String mainRendererString = "P3D";
		
//		pgTextureUnfold1		= createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
//		pgTextureUnfold2 		= createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
//		pgFacadeUnfold 			= createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
//		pgTextureUnfoldRotated  = createGraphics((int)(facadeDims.totalWidth * pgScaleFact), (int)(facadeDims.totalHeight * pgScaleFact), P3D);
//		
		pgVisFullscreen 		= createGraphics(width, height, P3D);
//	    pgVisSmall				= createGraphics((int)facadeDims.totalWidth, (int)facadeDims.totalHeight, P3D);
		pgVisLegend				= createGraphics(width, height, JAVA2D);

		if (!Assets.mainDrawSmooth){
			noSmooth();
//			pgTextureUnfold1.noSmooth();
//			pgTextureUnfold2.noSmooth();
//			pgFacadeUnfold.noSmooth();
//			pgTextureUnfoldRotated.noSmooth();
			pgVisFullscreen.noSmooth();
			//pgVisSmall.smooth();
			//pgVisLegend.smooth();
		}
		//--------------------------------------------------------------------
//     	if (comOn && !mainCom.running){ 
//     		println();
//		    println("Activate DB Communication!");
//		    mainCom.start();
//     	}
     	if (comOn && !dbCom.isRunning()){ 
     		println();
		    println("MainPApplet::setup(): Activate DB Communication!");
		    dbCom.start();
     	}
	}
	
	float x 	= Assets.maskOffsetX; 
	float y		= Assets.maskOffsetY;
	float y2 	= (y - Assets.maskHeight) * 0.5f; 
	float dx 	= Assets.maskWidth + x;
	float dy2 	= Assets.maskHeight + y2;

	@Override
	public void draw(){
		//frame.setLocation(0, 0);

		background(Assets.appletBGColor);
		pushMatrix();
			renderVisSunburst(pgVisFullscreen, 1.0f);
			//translate(width/2, height/2);
			//imageMode(CENTER);
			image(pgVisFullscreen, 0, 0);
		popMatrix();
				
		//--------------------------------draw participation legend ontop of everything 
		if (Assets.mainDrawLegend) {
			renderVisLegend(pgVisLegend, 0.8f);
			pushMatrix();
				translate(x, y+dy2);
				image(pgVisLegend, 0, 0);
			popMatrix();
		}
		
		scheduler.update();
		readCategory();
		readDB();
		readHeart();
		dbCom.validateDBConnection();
		
		checkFirstLaunch();
	}
	
	
	
	public void renderVisLegend(PGraphics pg, float scaleFactor){
		
		pg.beginDraw();
		pg.hint(ENABLE_NATIVE_FONTS);
		
		//--------------------fill with transparent pixels 
		pg.loadPixels();
		for (int i = 0; i < pg.pixels.length; ++i) {
		  pg.pixels[i] = pg.pixels[i] & 0xff000000;
		}
		pg.updatePixels();
		
		if (Assets.mainDrawLegend){
			pg.pushMatrix();
				pg.scale(scaleFactor);
				mainSunburstVisualization.displayLegend(pg);
	    	pg.popMatrix();
		}
		
		pg.endDraw(); 
		
	}
	
	public void renderVisSunburst(PGraphics pg, float scaleFactor) {
		pg.beginDraw();
		pg.background(0);
		
		//pg.hint(ENABLE_NATIVE_FONTS);//-------------------not needed for low-res displays? 
	    //pg.smooth();//------------------------------------this slows down extremely 
	    
		//--------------------------------------------------FIXME: what is this about? 
		pg.pushMatrix();
			pg.rotate(frameCount/10);
			mainSunburstVisualization.update(); 
		pg.popMatrix();
	
		pg.pushMatrix();
			pg.translate(pg.width/2 + Assets.visZeroShiftX, pg.height/2 + Assets.visZeroShiftY);
			pg.scale(scaleFactor);
			mainSunburstVisualization.displaySunburst(pg);
			mainCenterVis.display(pg);
		pg.popMatrix();
		
//		pg.pushMatrix();
//			pg.translate(pg.width/2 + Assets.visZeroShiftX, pg.height/2 + Assets.visZeroShiftY);
//			pg.scale(scaleFactor);
//			
//		pg.popMatrix();
//		
		pg.endDraw();
	}
	
	
	@Override
	public void keyPressed(){    
		mainSunburstVisualization.keyPressed(key);
		mainUserData.keyPressed(key);
		switch(key){
			//--------------------------------------------------------flags for facade masks and rendering     
		    case 'f': Assets.mainDrawVisFullscreen = !Assets.mainDrawVisFullscreen; 	break; 
		    case 'm': Assets.mainDrawFacadeMask 	= !Assets.mainDrawFacadeMask; 		break; 
		    //case 'g': Assets.drawGUI	= !Assets.drawGUI; 				break; //FIXME not working
		    case 'l': Assets.mainDrawAuxLines		= !Assets.mainDrawAuxLines; 		break;
		    case 'g': Assets.mainDrawSmooth		= !Assets.mainDrawSmooth; 		break;
		    
		    //--------------------------------------------------------flags for drawing auxiliary visualization info    
			//case'1': mainDrawInfo 		= !mainDrawInfo; break;
		    case '2': Assets.mainDrawLegend 		= !Assets.mainDrawLegend; break;
		    case '3': Assets.mainDrawCategory 		= !Assets.mainDrawCategory; break; 
		    
		    case 'i': 
		    	//if (Assets.mainDrawCategory) {
		    	//	Assets.iconTestingID = ((Assets.iconTestingID + 1) % Assets.iconsCategory.size());
		    	//}
		    	//else if (Assets.iconTestingOn) {
		    		//Assets.iconTestingID = ((Assets.iconTestingID + 1) % Assets.iconsCategoryTesting.size());
		    		Assets.iconTestingID = ((Assets.iconTestingID + 1) % Assets.iconsCategory.size());
		    	//}
		    break;
		   
		    
		    //case'3': mainDrawTitle 		= !mainDrawTitle; break;
		    //case'4': mainDrawCityEnergy = !mainDrawCityEnergy; break;
		    
//		    case 'l': moveLegend 	= !moveLegend; /*scaleScreen = false;*/ break;
//		    case 'i': moveInfo   	= !moveInfo; moveTitle = false; break;
//		    case 't': moveTitle   	= !moveTitle; moveInfo = false;  break;
//		    
//		    case CODED: 
//		      if(moveLegend){
//		        if (keyCode == UP) 			Assets.projectLegendShiftY -= 10;
//		        else if (keyCode == DOWN) 	Assets.projectLegendShiftY += 10;
//		        else if (keyCode == LEFT) 	Assets.projectLegendShiftX -= 10;
//		        else if (keyCode == RIGHT) 	Assets.projectLegendShiftX += 10;
//		        break;
//		      }
//		      if(moveTitle){
//		        if (keyCode == UP) 			Assets.projectTitleShiftY -= 10;
//		        else if (keyCode == DOWN) 	Assets.projectTitleShiftY += 10;
//		        else if (keyCode == LEFT) 	Assets.projectTitleShiftX -= 10;
//		        else if (keyCode == RIGHT) 	Assets.projectTitleShiftX += 10;
//		        break;
//		      }
//		      else if (scaleScreen){
//		        if (keyCode == UP) scaleScreenFactor += 0.1;
//		        else if (keyCode == DOWN) scaleScreenFactor -= 0.1; 
//		        break;
//		      }
//		      else if (moveInfo){
//		        if (keyCode == UP)         	Assets.projectInfoShiftY += 10;
//		        else if (keyCode == DOWN)   Assets.projectInfoShiftY -= 10; 
//		        if (keyCode == LEFT)        Assets.projectInfoShiftX -= 10;
//		        else if (keyCode == RIGHT)  Assets.projectInfoShiftX += 10; 
//		        break;
//		      } 
//		   break;
		   case 'r':  mainSunburstVisualization.reset(); break;
		   case 'c': 
			   Assets.colorTestingID = (Assets.colorTestingID+1)%5; 
			   Assets.iconTestingID = (Assets.iconTestingID+1)%5;
			   break;
		   case 'n':
			   //int 	randId         = (int)(random(1,6));
			   int randId 		= Assets.colorTestingID + 1;
			   float userInput  = (int)(random(1,3));
			   //float userInput  = randId;
			   
			   float randGas = 0;
			   //float randGas      = random(10,30);
			   //float randEnergy   = randElectro / 0.15f + randGas / 0.05f; 
			   float randEnergy   = 0;

			   
			   String entryStamp = day() + "-" + hour() + ":" + minute() + ":" + second(); 
			   entryStamp = "user"+randId;
			   mainUserData.addEnergyUser(randId, entryStamp, userInput, randGas, randEnergy);
		   break;
		  
		   case 'p': 
		     if (moveInfo) {
		       println("yInfoShift = " + Assets.projectInfoShiftY);
		     } else if (moveLegend){
		         println("yLegendShift = " + Assets.projectLegendShiftY);
		         println("xLegendShift = " + Assets.projectLegendShiftX);
		     } else if(moveTitle){
		         println("yTitleShift = " + Assets.projectTitleShiftY);
		         println("xTitleShift = " + Assets.projectTitleShiftX);
		     } else if (scaleScreen){
		       println("scaleScreenFactor = " + scaleScreenFactor);
		     } else if (debugScreen) {
		       println ("visZeroX = " + Assets.visZeroShiftX);
		       println ("visZeroY = " + Assets.visZeroShiftY);
		     }
		   break;
		  }
		
		
	}

	@Override
	public void mousePressed() {
		if(over) {
			move = true;
		}
	}

	@Override
	public void mouseReleased(){
		move = false;
	}
	 
	
	/** Query DB for new participants */
	public void readDB(){
		if(dbCom.areNewResults()) { 
			//println("MainPApplet::readDB(): NEW PARTICIPANTS IN DB!!!");
		    int num = dbCom.getParticipants().size(); 
		    DatabaseParticipant tmpParticipant; 
		    
		    for (int j=0; j < num; j++) {
		    	tmpParticipant = dbCom.getParticipants().get(j);
		      
		      if (tmpParticipant != null) {
		    	String cardID 		= tmpParticipant.getCardID();
				//int devID 			= tmpParticipant.getDevID();
				int catID			= tmpParticipant.getCatID();
				int prefID			= tmpParticipant.getPrefID();
				long time			= tmpParticipant.getTstamp();
				
				int devID = catID;//FIXME: document better! due to lack of different locations to compare from we use the categories as locations

				//System.out.println("MainPApplet::readDB(): Participant: " + cardID + ":" + devID + ":" + catID + ":" + prefID + ":" + time);
				
				mainUserData.addEnergyUser(devID,cardID,prefID, 0, 0);
		      }
		    }
		    dbCom.setNewResults(false);
		    dbCom.setResultsPublished(true);
		  }
	}
	
	public void readHeart(){
		if (dbCom.isNewHeart()){
			System.out.println("MainPApplet::readHeart() newHeart=" + dbCom.getHeart());
			mainSunburstVisualization.reset();
			dbCom.reset();
			
			dbCom.setNewHeart(false);
			dbCom.setHeartPublished(true);
		}
	} 
	
	public void readCategory(){
		if (dbCom.isNewCategory()){
			System.out.println("MainPApplet::readCategory() newCategory=" + dbCom.getActiveCategoryID());
			//set category
			mainSunburstVisualization.reset();
			dbCom.reset();
			dbCom.setNewCategory(false);
			dbCom.setCategoryPublished(true);
			mainCenterVis.setContent(dbCom.getActiveCategoryString());
		}
	}
	
	public void checkFirstLaunch(){
		if (g_bFirstLaunch){
		    println("MainPApplet::checkFirstLaunch(): APP first launch... #frames " + frameCount); 
		    g_bFirstLaunch = false; 
		}
	}
	
	
	
	public static void main(String args[]) {
		//PApplet.main(new String[] { "-present", "App" });
		//PApplet.main(new String[]{"scsd.MainPApplet"});
		
		PApplet.main(new String[] { "--present", "scsd.MainPApplet" });

	}

	

	
		
	  
}